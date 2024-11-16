package zjc.devicemanage.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zjc.devicemanage.R;
import zjc.devicemanage.adapter.InformationAdapter;
import zjc.devicemanage.model.Information;
import zjc.devicemanage.service.InformationService;
import zjc.devicemanage.service.imp.InformationServiceImp;

public class InformationFragment extends Fragment {
    // 视图相关属性
    private View fragment_informationView;
    private RecyclerView fragment_information_rv;
    public SwipeRefreshLayout fragment_information_SRL; // 声明 SwipeRefreshLayout
    private InformationAdapter informationAdapter;
    private List<Information> informationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 步骤1：加载布局
        fragment_informationView = inflater.inflate(
                R.layout.fragment_information, container, false);

        // 步骤2：初始化视图
        initViews();

        // 步骤3：加载数据
        initInformationData();

        return fragment_informationView;
    }

    private void initViews() {
        // 获取RecyclerView控件
        fragment_information_rv = fragment_informationView.findViewById(
                R.id.fragment_information_rv);

        // 获取SwipeRefreshLayout控件
        fragment_information_SRL = fragment_informationView.findViewById(
                R.id.fragment_information_SRL); // 确保ID匹配

        // 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        fragment_information_rv.setLayoutManager(linearLayoutManager);

        // 添加分割线
        fragment_information_rv.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // 初始化数据列表和适配器
        informationList = new ArrayList<>();
        informationAdapter = new InformationAdapter(informationList);

        // 设置适配器
        fragment_information_rv.setAdapter(informationAdapter);

        // 设置点击事件
        informationAdapter.setOnItemClickListener(new InformationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Information information) { // 修改方法签名
                Toast.makeText(getContext(),
                        "咨询编号" + information.getId() + " 被点击",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContentClick(Information information) { // 修改方法签名
                Toast.makeText(getContext(),
                        "内容被点击: " + information.getContent(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateClick(Information information) { // 修改方法签名
                Toast.makeText(getContext(),
                        "日期被点击: " + information.getDate(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 设置下拉刷新监听器
        fragment_information_SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                initInformationData();
            }
        });
    }

    private void initInformationData() {
        InformationService informationService = new InformationServiceImp(this);
        informationService.findAllInformation();
    }

    public void showAllInformationCallback(final List<Information> newInformationList) {
        if (getActivity() == null) return;

        // 打印所有信息对象的字段值
        for (Information info : newInformationList) {
            Log.d("InformationData", "ID: " + info.getId());
            Log.d("InformationData", "Content: " + info.getContent());
            Log.d("InformationData", "Date: " + info.getDate());
            Log.d("InformationData", "Image URL: " + info.getImageUrl());
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                informationAdapter.setInformationList(newInformationList);
                if (fragment_information_SRL != null) {
                    fragment_information_SRL.setRefreshing(false);
                }
            }
        });
    }

    // 可选：添加一个方法来设置 SwipeRefreshLayout 的刷新状态
    public void setRefreshing(boolean refreshing) {
        if (fragment_information_SRL != null) {
            fragment_information_SRL.setRefreshing(refreshing);
        }
    }
}