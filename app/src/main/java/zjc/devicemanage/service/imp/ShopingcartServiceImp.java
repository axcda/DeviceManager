package zjc.devicemanage.service.imp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjc.devicemanage.fragment.HomeFragment;
import zjc.devicemanage.fragment.ShopingcartFragment;
import zjc.devicemanage.model.ShopingcartList;
import zjc.devicemanage.service.ShopingcartService;
import zjc.devicemanage.util.MyApplication;
import zjc.devicemanage.util.MyHttpUtil;

public class ShopingcartServiceImp implements ShopingcartService {
    //从web服务器获取的“购物车列表”属性
    private ShopingcartList shopingcartListFromJson;
    private HomeFragment homeFragment;
    private ShopingcartFragment shopingcartFragment;
// 注入委托类的构造函数
    public ShopingcartServiceImp(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public ShopingcartServiceImp(ShopingcartFragment shopingcartFragment) {
        this.shopingcartFragment = shopingcartFragment;
    }

    private void parseJSONtoShopingcartList(String responseData) {
        Gson gson=new Gson();
        shopingcartListFromJson= gson.fromJson(responseData,
                new TypeToken<ShopingcartList>(){}.getType());
        Log.i("zjc",shopingcartListFromJson.toString());
    }
    @Override
    public void findAllShopingcart() {
        //构造findAllShopingcart的tomcat服务请求URL
        String findAllShopingcartURL = MyHttpUtil.host + "/DeviceManage/findAllShopingcart";
        Log.i("zjc",findAllShopingcartURL);
        sendRequest(findAllShopingcartURL);
    }
    @Override
    public void findAllShopingcartByUserId(String userId, CheckCallback callback) {
        String findAllShopingcartByUserIdURL = MyHttpUtil.host + 
                "/DeviceManage/findAllShopingcartByUserId?userId=" + userId;
        Log.i("zjc", findAllShopingcartByUserIdURL);
        
        MyHttpUtil.sendOkhttpGetRequest(findAllShopingcartByUserIdURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("zjc", "Web接口服务连接失败: " + e.getMessage());
                MyApplication.subThreadToast(
                    "Web接口服务连接失败，请确保主机IP地址正确且Tomcat服务器已启动");
                if (callback != null) {
                    callback.onResult(null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseJSONtoShopingcartList(response.body().string());
                if (callback != null) {
                    callback.onResult(shopingcartListFromJson);
                }
                if (shopingcartFragment != null) {
                    shopingcartFragment.showAllShopingcartCallback(shopingcartListFromJson);
                }
            }
        });
    }
    // 抽取共同的请求处理逻辑
    private void sendRequest(String url) {
        MyHttpUtil.sendOkhttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("zjc", "Web接口服务连接失败: " + e.getMessage());
                MyApplication.subThreadToast(
                    "Web接口服务连接失败，请确保主机IP地址正确且Tomcat服务器已启动");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseJSONtoShopingcartList(response.body().string());
                shopingcartFragment.showAllShopingcartCallback(
                        shopingcartListFromJson);
            }
        });
    }
    @Override
    public void addToCart(String userId, String deviceId, String buyNum) {
        String addToCartURL = MyHttpUtil.host + "/DeviceManage/addToCart" +
                "?userId=" + userId +
                "&deviceId=" + deviceId +
                "&buyNum=" + buyNum;
        
        MyHttpUtil.sendOkhttpGetRequest(addToCartURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("zjc", "添加购物车失败: " + e.getMessage());
                MyApplication.subThreadToast("添加购物车失败，请稍后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if ("success".equals(result)) {
                    MyApplication.subThreadToast("成功加入购物车");
                } else {
                    MyApplication.subThreadToast("加入购物车失败");
                }
            }
        });
    }

    @Override
    public void addShopingcart(final String addDeviceID, String addBuyNum, String addUserID) {
        // Construct the request URL
        String addShopingcartURL = MyHttpUtil.host + "/DeviceManage/addShopingcart" +
                "?addDeviceID=" + addDeviceID +
                "&addBuyNum=" + addBuyNum +
                "&addUserID=" + addUserID;

        MyHttpUtil.sendOkhttpGetRequest(addShopingcartURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("zjc", "添加购物车失败: " + e.getMessage());
                MyApplication.subThreadToast(
                        "Web接口服务连接失败，请确保主机IP地址正确且Tomcat服务器已启动");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if ("success".equals(result)) {
                    if (shopingcartFragment != null) {
                        shopingcartFragment.showAddShopingcartCallback(addDeviceID);
                    } else {
                        Log.e("zjc", "ShopingcartFragment is null.");
                        MyApplication.subThreadToast("添加购物车成功，但未能更新界面。");
                    }
                } else {
                    MyApplication.subThreadToast("添加购物车失败");
                }
            }
        });
    }
}
