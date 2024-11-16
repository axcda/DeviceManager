package zjc.devicemanage.service.imp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjc.devicemanage.fragment.HomeFragment;
import zjc.devicemanage.model.DeviceClassList;
import zjc.devicemanage.service.DeviceClassService;
import zjc.devicemanage.util.MyHttpUtil;

public class DeviceClassServiceImp implements DeviceClassService {
    private DeviceClassList deviceClassListFromJson;
    private HomeFragment homeFragment;

    public DeviceClassServiceImp(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    private void parseJSONtoDeviceClassList(String responseData) {
        Gson gson = new Gson();
        deviceClassListFromJson = gson.fromJson(responseData, new TypeToken<DeviceClassList>() {}.getType());
        Log.i("zjc", deviceClassListFromJson.toString());
    }

    @Override
    public void findAllDeviceClass() {
        // 构造请求URL
        String findAllDeviceClassURL = MyHttpUtil.host + "/DeviceManage/findAllDeviceClass";
        Log.i("zjc", findAllDeviceClassURL);
        MyHttpUtil.sendOkhttpGetRequest(findAllDeviceClassURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjc", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                // 打印原始 JSON 数据
                Log.d("DeviceClassServiceImp", "Raw JSON: " + jsonData);
                parseJSONtoDeviceClassList(jsonData);
                homeFragment.showAllDeviceClassCallback(deviceClassListFromJson); // 正确调用
            }
        });
    }
}