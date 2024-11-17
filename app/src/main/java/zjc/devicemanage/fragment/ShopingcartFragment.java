package zjc.devicemanage.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;

import zjc.devicemanage.R;
import zjc.devicemanage.adapter.ShopingcartAdapter;
import zjc.devicemanage.model.Shopingcart;
import zjc.devicemanage.model.ShopingcartList;
import zjc.devicemanage.service.ShopingcartService;
import zjc.devicemanage.service.imp.ShopingcartServiceImp;
import zjc.devicemanage.util.MyApplication;

public class ShopingcartFragment extends Fragment {
    // 视图相关
    private View fragment_shopingcartView;
    private SwipeRefreshLayout fragment_shopingcart_SRL;
    private RecyclerView fragment_shopingcart_rv;
    private TextView fragment_shopingcart_sum_tv;
    private ImageView fragment_shopingcart_choiceall_iv;

    // 数据相关
    private ShopingcartAdapter shopingcartAdapter;
    private ShopingcartList shopingcartList = new ShopingcartList();
    private HashMap<Integer, Boolean> choiceMap = new HashMap<>();
    private String userId;
    private Boolean isChoiceAll = false;

    // 服务相关
    private ShopingcartService shopingcartService;

    // Handler处理UI更新
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: // 刷新完成
                    if (getActivity() != null && isAdded()) {
                        shopingcartAdapter.notifyDataSetChanged();
                        fragment_shopingcart_SRL.setRefreshing(false);
                    }
                    break;
                // 可以添加其他消息类型的处理
            }
        }
    };

    public ShopingcartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 初始化视图
        fragment_shopingcartView = inflater.inflate(R.layout.fragment_shopingcart, container, false);
        
        // 获取当前用户ID
        userId = MyApplication.getUser_id();

        // 初始化控件
        initViews();
        
        // 设置布局管理器
        setupRecyclerView();
        
        // 设置适配器
        setupAdapter();
        
        // 设置监听器
        setupListeners();
        
        // 加载当前用户的购物车数据
        initShopingcartData(userId);

        return fragment_shopingcartView;
    }

    private void initViews() {
        fragment_shopingcart_SRL = fragment_shopingcartView.findViewById(R.id.fragment_shoppingcart_SRL);
        fragment_shopingcart_rv = fragment_shopingcartView.findViewById(R.id.fragment_shoppingcart_rv);
        fragment_shopingcart_sum_tv = fragment_shopingcartView.findViewById(R.id.fragment_shoppingcart_sum_tv);
        fragment_shopingcart_choiceall_iv = fragment_shopingcartView.findViewById(R.id.fragment_shoppingcart_choiceall_iv);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        fragment_shopingcart_rv.setLayoutManager(linearLayoutManager);
        fragment_shopingcart_rv.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void setupAdapter() {
        shopingcartAdapter = new ShopingcartAdapter(shopingcartList, choiceMap);
        fragment_shopingcart_rv.setAdapter(shopingcartAdapter);
        
        // 设置选中按钮的点击监听器
        shopingcartAdapter.setOnItemChoiceListener(new ShopingcartAdapter.OnItemChoiceListener() {
            @Override
            public void onItemChoice(int position) {
                // 更新总金额
                updateMoneySum();
                
                // 检查是否需要更新全选状态
                checkAllSelected();
            }
        });

        // 设置增加按钮点击监听器
        shopingcartAdapter.setOnItemAddListener(new ShopingcartAdapter.OnItemAddListener() {
            @Override
            public void onItemAdd(int position) {
                // 检查当前项是否被选中
                Boolean isEnabled = choiceMap.get(position);
                
                // 如果未选中当前购物车项，则返回，不能操作增加按钮
                if (isEnabled == null || !isEnabled) {
                    return;
                }
                
                // 获取当前购物车项
                Shopingcart sc = shopingcartList.getResult().get(position);
                
                // 更新购买数量
                int curBuynum = Integer.parseInt(sc.getBuyNum());
                curBuynum++;
                sc.setBuyNum(String.valueOf(curBuynum));
                
                // 修改界面上的数量显示
                ShopingcartAdapter.ShopingcartViewHolder scHolder = 
                    shopingcartAdapter.getShopingcartViewHolder();
                scHolder.shopingcart_buynum_tv.setText(String.valueOf(curBuynum));
                
                // 更新总金额
                updateMoneySum();
                
                // 强制刷新界面
                shopingcartAdapter.notifyDataSetChanged();
            }
        });

        // 设置减少按钮点击监听器
        shopingcartAdapter.setOnItemSubtractionListener(new ShopingcartAdapter.OnItemSubtractionListener() {
            @Override
            public void onItemSubtraction(int position) {
                // 检查当前项是否被选中
                Boolean isEnabled = choiceMap.get(position);
                
                // 如果未选中当前购物车项，则返回，不能操作减少按钮
                if (isEnabled == null || !isEnabled) {
                    return;
                }
                
                // 获取当前购物车项
                Shopingcart sc = shopingcartList.getResult().get(position);
                
                // 更新购买数量
                int curBuynum = Integer.parseInt(sc.getBuyNum());
                curBuynum--;
                
                // 如果购买数量小于1，则返回，不能继续操作减少按钮
                if (curBuynum < 1) {
                    return;
                }
                
                // 更新购买数量
                sc.setBuyNum(String.valueOf(curBuynum));
                
                // 修改界面上的数量显示
                ShopingcartAdapter.ShopingcartViewHolder scHolder = 
                    shopingcartAdapter.getShopingcartViewHolder();
                scHolder.shopingcart_buynum_tv.setText(String.valueOf(curBuynum));
                
                // 更新总金额
                updateMoneySum();
                
                // 强制刷新界面
                shopingcartAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupListeners() {
        // 设置下拉刷新
        fragment_shopingcart_SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 清空当前数据
                shopingcartList.getResult().clear();
                
                // 检查列表是否在滚动
                if (fragment_shopingcart_rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE 
                        || !fragment_shopingcart_rv.isComputingLayout()) {
                    shopingcartAdapter.notifyDataSetChanged();
                }

                // 重新加载当前用户的购物车数据
                initShopingcartData(userId);

                // 创建新线程处理刷新动画
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 延时1秒，让用户看到刷新动画
                            Thread.sleep(1000);
                            
                            // 在主线程中更新UI
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (getActivity() != null && isAdded()) {
                                        // 刷新列表
                                        shopingcartAdapter.notifyDataSetChanged();
                                        // 停止刷新动画
                                        fragment_shopingcart_SRL.setRefreshing(false);
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            // 确保刷新动画停止
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    fragment_shopingcart_SRL.setRefreshing(false);
                                });
                            }
                        }
                    }
                }).start();
            }
        });

        // 设置全选按钮点击事件
        fragment_shopingcart_choiceall_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换全选状态
                isChoiceAll = !isChoiceAll;
                
                // 更新选择状态
                updateBatchChoiceMap(isChoiceAll);
                
                // 更新全选按钮图标
                fragment_shopingcart_choiceall_iv.setImageResource(
                    isChoiceAll ? R.drawable.choiced : R.drawable.choice
                );
            }
        });

        // 设置适配器的选择监听器
        shopingcartAdapter.setOnItemSelectListener((position, isSelected) -> {
            updateMoneySum();
        });

        // 设置适配器的数量变化监听器
        shopingcartAdapter.setOnQuantityChangeListener(shopingcart -> {
            updateMoneySum();
        });
    }

    public void initShopingcartData(String userId) {
        shopingcartService = new ShopingcartServiceImp(this);
        shopingcartService.findAllShopingcartByUserId(userId, new ShopingcartService.CheckCallback() {
            @Override
            public void onResult(ShopingcartList cartList) {
                if (cartList != null) {
                    showAllShopingcartCallback(cartList);
                }
            }
        });
    }

    public void updateMoneySum() {
        int moneySum = 0;
        List<Shopingcart> shopingcarts = shopingcartList.getResult();
        for (int i = 0; i < shopingcarts.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (choiceMap.getOrDefault(i, false)) {
                    Shopingcart sc = shopingcarts.get(i);
                    int buyNum = Integer.parseInt(sc.getBuyNum());
                    int price = Integer.parseInt(sc.getDevice().getDevicePrice());
                    moneySum += buyNum * price;
                }
            }
        }
        fragment_shopingcart_sum_tv.setText(String.valueOf(moneySum));
    }

    public void updateBatchChoiceMap(Boolean value) {
        // 更新所有购物车项的选择状态
        for (int i = 0; i < shopingcartList.getResult().size(); i++) {
            choiceMap.put(i, value);
        }
        
        // 通知适配器数据已更新
        shopingcartAdapter.notifyDataSetChanged();
        
        // 更新总金额
        updateMoneySum();
    }

    public void showAllShopingcartCallback(final ShopingcartList shopingcartListFromJson) {
        if (getActivity() == null) return;
        
        getActivity().runOnUiThread(() -> {
            shopingcartList = shopingcartListFromJson;
            shopingcartAdapter.setShopingcartList(shopingcartList);
            updateBatchChoiceMap(false);
            updateMoneySum();
        });
    }

    // 检查是否所有项都被选中
    private void checkAllSelected() {
        boolean allSelected = true;
        for (int i = 0; i < shopingcartList.getResult().size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!choiceMap.getOrDefault(i, false)) {
                    allSelected = false;
                    break;
                }
            }
        }
        
        // 更新全选按钮状态
        isChoiceAll = allSelected;
        fragment_shopingcart_choiceall_iv.setImageResource(
            allSelected ? R.drawable.choiced : R.drawable.choice
        );
    }

    public void refreshData() {
        if (userId != null) {
            initShopingcartData(userId);
        }
    }

    public void showAddShopingcartCallback(final String addDeviceID) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            // Show success message
            Toast.makeText(getContext(), 
                "设备编号" + addDeviceID + "加入购物车成功！", 
                Toast.LENGTH_LONG).show();
                
            // Refresh the shopping cart data
            refreshData();
        });
    }
}