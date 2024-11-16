package zjc.devicemanage.util;

import android.app.DownloadManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;

public class MyHttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static String host ="http://192.168.1.216:8080";
    public static void sendOkhttpPostRequest(String address, RequestBody requestBody, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().post(requestBody).build();
               client.newCall(request).enqueue(callback);
    }
    public static void sendOkhttpGetRequest(String address,  Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
                 client.newCall(request).enqueue(callback);
    }

    /**
     * 异步获取数据
     *
     * @param url      请求的 URL
     * @param callback 回调对象
     */
    public static void getDataAsync(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
