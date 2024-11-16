package zjc.devicemanage.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import zjc.devicemanage.R;
import zjc.devicemanage.activity.MainActivity;
import zjc.devicemanage.adapter.DeviceAdapter;
import zjc.devicemanage.adapter.DeviceClassAdapter;
import zjc.devicemanage.adapter.EmbedDeviceAdapter;
import zjc.devicemanage.adapter.SampleAdapter;
import zjc.devicemanage.model.DeviceClassList;
import zjc.devicemanage.model.DeviceList;
import zjc.devicemanage.service.DeviceClassService;
import zjc.devicemanage.service.DeviceService;
import zjc.devicemanage.service.imp.DeviceClassServiceImp;
import zjc.devicemanage.service.imp.DeviceServiceImp;

public class HomeFragment extends Fragment {
    private DeviceAdapter deviceAdapter;
    private DeviceClassAdapter deviceClassAdapter;
    private RecyclerView fragment_home_recyclerView;
    private View fragment_homeView;
    private Banner banner;
    private DeviceClassList deviceClassList;
    private DeviceList deviceList;
    private DeviceClassService deviceClassService;
    private DeviceService deviceService;
    private SampleAdapter sampleAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    private void initContinentsAndCountries(){
        // ... 你的现有代码 ...
    }

    // 初始化轮播图
    private void initBanner() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.bannerimg1);
        images.add(R.drawable.bannerimg2);
        images.add(R.drawable.bannerimg3);
        images.add(R.drawable.bannerimg4);
        images.add(R.drawable.bannerimg5);

        banner = fragment_homeView.findViewById(R.id.home_banner);
        banner.setImageLoader(new ImageLoader(){
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        banner.setImages(images);
        banner.start();
    }

    // 初始化设备分类数据
    public void initDeviceClassData() {
        deviceClassService = new DeviceClassServiceImp(this);
        deviceService = new DeviceServiceImp(this);  // 初始化 deviceService
        deviceClassService.findAllDeviceClass();
    }


    // 初始化设备数据
    private void initDeviceData() {
        deviceService = new DeviceServiceImp(this);
        deviceService.findAllDevice();
    }

    public void showAllDeviceClassCallback(DeviceClassList deviceClassListFromJson) {
        this.deviceClassList = deviceClassListFromJson;

        getActivity().runOnUiThread(() -> {
            if (deviceList != null) {
                setupEmbedDeviceAdapter();
            }
        });
    }

    public void showAllDeviceCallback(DeviceList deviceListFromJson) {
        this.deviceList = deviceListFromJson;

        getActivity().runOnUiThread(() -> {
            if (deviceClassList != null) {
                setupEmbedDeviceAdapter();
            }
        });
    }

    private void setupEmbedDeviceAdapter() {
        EmbedDeviceAdapter adapter = new EmbedDeviceAdapter(getContext(), deviceClassList, deviceList);
        adapter.setOnDeviceClassClickListener(deviceClassId -> {
            // Handle device class click
            deviceService.findDeviceByDeviceClassId(Integer.parseInt(deviceClassId));
        });

        fragment_home_recyclerView.setAdapter(adapter);
    }

    // 已存在的回调方法：处理指定设备分类的设备数据
    public void showAddShopingcartCallback(final String addDeviceID) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),
                        "设备编号" + addDeviceID + "加入购物车成功！",
                        Toast.LENGTH_LONG).show();

                // 通知购物车Fragment刷新数据
                if (getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    ShopingcartFragment shopingcartFragment = activity.getShopingcartFragment();
                    if (shopingcartFragment != null) {
                        shopingcartFragment.refreshData();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment_homeView = inflater.inflate(R.layout.fragment_home, container, false);
        fragment_home_recyclerView = fragment_homeView.findViewById(R.id.fragment_home_recyclerView);

        // 使用 LinearLayoutManager 替代 GridLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        fragment_home_recyclerView.setLayoutManager(linearLayoutManager);

        // 添加分割线
        fragment_home_recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // 初始化设备分类数据
        initDeviceClassData();

        // 初始化设备数据
        initDeviceData();

        // 初始化轮播图
        initBanner();

        return fragment_homeView;
    }
}