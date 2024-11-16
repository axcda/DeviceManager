package zjc.devicemanage.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import zjc.devicemanage.R;
import zjc.devicemanage.model.Device;
import zjc.devicemanage.model.ShopingcartList;
import zjc.devicemanage.model.Shopingcart;

public class ShopingcartAdapter extends RecyclerView.Adapter<ShopingcartAdapter.ShopingcartViewHolder> {
    // 数据源
    private ShopingcartList shopingcartList;
    // 选择状态Map
    private HashMap<Integer, Boolean> choiceMap;
    // 当前ViewHolder引用
    private ShopingcartViewHolder shopingcartViewHolder;

    // 构造函数
    public ShopingcartAdapter(ShopingcartList shopingcartList, HashMap<Integer, Boolean> choiceMap) {
        this.shopingcartList = shopingcartList;
        this.choiceMap = choiceMap;
    }

    // Setter方法
    public void setShopingcartList(ShopingcartList shopingcartList) {
        this.shopingcartList = shopingcartList;
        notifyDataSetChanged();
    }

    public void setChoiceMap(HashMap<Integer, Boolean> choiceMap) {
        this.choiceMap = choiceMap;
        notifyDataSetChanged();
    }

    // ViewHolder的getter和setter
    public ShopingcartViewHolder getShopingcartViewHolder() {
        return shopingcartViewHolder;
    }

    public void setShopingcartViewHolder(ShopingcartViewHolder shopingcartViewHolder) {
        this.shopingcartViewHolder = shopingcartViewHolder;
    }

    @NonNull
    @Override
    public ShopingcartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopingcart_viewholder, parent, false);
        shopingcartViewHolder = new ShopingcartViewHolder(itemView);
        return shopingcartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopingcartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 获取当前购物车项的数据
        Shopingcart shopingcart = shopingcartList.getResult().get(position);
        Device device = shopingcart.getDevice();

        // 设置设备名称、数量和价格
        holder.shopingcart_devicename_tv.setText(device.getDeviceName());
        holder.shopingcart_buynum_tv.setText(shopingcart.getBuyNum());
        holder.shopingcart_deviceprice_tv.setText("￥" + device.getDevicePrice());

        // 设置选择状态图片
        boolean isSelected = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            isSelected = choiceMap.getOrDefault(position, false);
        }
        holder.shopingcart_choice_iv.setImageResource(
                isSelected ? R.drawable.choiced : R.drawable.choice
        );

        // 设置设备图片
        switch (device.getDeviceName()) {
            case "打印机":
                holder.shopingcart_deviceimage_iv.setImageResource(R.drawable.printer);
                break;
            case "耳机":
                holder.shopingcart_deviceimage_iv.setImageResource(R.drawable.earphone);
                break;
            case "鼠标":
                holder.shopingcart_deviceimage_iv.setImageResource(R.drawable.mouse);
                break;
            case "笔记本电脑":
                holder.shopingcart_deviceimage_iv.setImageResource(R.drawable.computer);
                break;
            case "U盘":
                holder.shopingcart_deviceimage_iv.setImageResource(R.drawable.udisk);
                break;
            case "头盔":
                holder.shopingcart_deviceimage_iv.setImageResource(R.drawable.helmet);
                break;
            default:
                holder.shopingcart_deviceimage_iv.setImageResource(R.drawable.office);
                break;
        }

        // 设置加减按钮的点击事件
        holder.shopingcart_subtraction_iv.setOnClickListener(v -> {
            int currentNum = Integer.parseInt(shopingcart.getBuyNum());
            if (currentNum > 1) {
                shopingcart.setBuyNum(String.valueOf(currentNum - 1));
                holder.shopingcart_buynum_tv.setText(shopingcart.getBuyNum());
                if (onQuantityChangeListener != null) {
                    onQuantityChangeListener.onQuantityChanged(shopingcart);
                }
            }
        });

        // 修改增加按钮的点击事件处理
        holder.shopingcart_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首先调用增加监听器
                if (onItemAddListener != null) {
                    onItemAddListener.onItemAdd(position);
                }
                
                // 更新数量（这部分逻辑可以移到 Fragment 中处理）
                int currentNum = Integer.parseInt(shopingcart.getBuyNum());
                shopingcart.setBuyNum(String.valueOf(currentNum + 1));
                holder.shopingcart_buynum_tv.setText(shopingcart.getBuyNum());
            }
        });

        // 修改选择图片按钮的点击事件处理
        holder.shopingcart_choice_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取当前项的选中状态
                boolean currentState = choiceMap.containsKey(position) ? choiceMap.get(position) : false;
                
                // 切换选中状态
                boolean newState = !currentState;
                choiceMap.put(position, newState);
                
                // 更新UI
                holder.shopingcart_choice_iv.setImageResource(
                    newState ? R.drawable.choiced : R.drawable.choice
                );
                
                // 通知选中状态变化
                if (onItemChoiceListener != null) {
                    onItemChoiceListener.onItemChoice(position);
                }
                
                // 通知选择状态变化
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelected(position, newState);
                }
            }
        });

        // 修改减少按钮的点击事件处理
        holder.shopingcart_subtraction_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 只调用减少监听器，将具体逻辑移到 Fragment 中处理
                if (onItemSubtractionListener != null) {
                    onItemSubtractionListener.onItemSubtraction(position);
                }
            }
        });

        // 移除原来在 shopingcart_choice_ll 上的点击事件
        holder.shopingcart_choice_ll.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return shopingcartList == null ? 0 : shopingcartList.getResult().size();
    }

    // 数量变化监听器接口
    public interface OnQuantityChangeListener {
        void onQuantityChanged(Shopingcart shopingcart);
    }

    // 选择状态变化监听器接口
    public interface OnItemSelectListener {
        void onItemSelected(int position, boolean isSelected);
    }

    // 选中按钮点击监听器接口
    public interface OnItemChoiceListener {
        void onItemChoice(int position);
    }

    // 增加按钮点击监听器接口
    public interface OnItemAddListener {
        void onItemAdd(int position);
    }

    // 减少按钮点击监听器接口
    public interface OnItemSubtractionListener {
        void onItemSubtraction(int position);
    }

    private OnQuantityChangeListener onQuantityChangeListener;
    private OnItemSelectListener onItemSelectListener;
    private OnItemChoiceListener onItemChoiceListener;
    private OnItemAddListener onItemAddListener;
    private OnItemSubtractionListener onItemSubtractionListener;

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.onQuantityChangeListener = listener;
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.onItemSelectListener = listener;
    }

    public void setOnItemChoiceListener(OnItemChoiceListener listener) {
        this.onItemChoiceListener = listener;
    }

    public void setOnItemAddListener(OnItemAddListener listener) {
        this.onItemAddListener = listener;
    }

    public void setOnItemSubtractionListener(OnItemSubtractionListener listener) {
        this.onItemSubtractionListener = listener;
    }

    // ViewHolder内部类
    public static class ShopingcartViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout shopingcart_viewholder_ll;
        private LinearLayout shopingcart_choice_ll;
        public ImageView shopingcart_choice_iv;
        private ImageView shopingcart_deviceimage_iv;
        private TextView shopingcart_devicename_tv;
        private TextView shopingcart_deviceprice_tv;
        private ImageView shopingcart_subtraction_iv;
        public TextView shopingcart_buynum_tv;
        private ImageView shopingcart_add_iv;

        public ShopingcartViewHolder(@NonNull View itemView) {
            super(itemView);
            shopingcart_viewholder_ll = itemView.findViewById(R.id.shopingcart_viewholder_ll);
            shopingcart_choice_ll = itemView.findViewById(R.id.shopingcart_choice_ll);
            shopingcart_choice_iv = itemView.findViewById(R.id.shopingcart_choice_iv);
            shopingcart_deviceimage_iv = itemView.findViewById(R.id.shopingcart_viewholder_device_iv);
            shopingcart_devicename_tv = itemView.findViewById(R.id.shopingcart_viewholder_devicename_tv);
            shopingcart_deviceprice_tv = itemView.findViewById(R.id.shopingcart_viewholder_deviceprice_tv);
            shopingcart_subtraction_iv = itemView.findViewById(R.id.shopingcart_subtraction_iv);
            shopingcart_buynum_tv = itemView.findViewById(R.id.shopingcart_viewholder_buynum_tv);
            shopingcart_add_iv = itemView.findViewById(R.id.shopingcart_add_iv);
        }
    }
} 