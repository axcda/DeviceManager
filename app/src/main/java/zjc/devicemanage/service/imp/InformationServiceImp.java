package zjc.devicemanage.service.imp;

import android.widget.Toast;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjc.devicemanage.model.Information;
import zjc.devicemanage.model.InformationList;
import zjc.devicemanage.fragment.InformationFragment;
import zjc.devicemanage.service.InformationService;
import zjc.devicemanage.util.MyHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InformationServiceImp implements InformationService {
    private InformationFragment informationFragment;

    public InformationServiceImp(InformationFragment informationFragment) {
        this.informationFragment = informationFragment;
    }

    @Override
    public void findAllInformation() {
        String url = MyHttpUtil.host + "/DeviceManage/information/findAllInformation";

        MyHttpUtil.getDataAsync(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 在主线程中通知 Fragment 请求失败
                if (informationFragment.getActivity() != null) {
                    informationFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(informationFragment.getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                            // 停止刷新动画
                            informationFragment.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    // 解析为 InformationList 对象
                    InformationList informationList = gson.fromJson(json, InformationList.class);

                    // 获取实际的信息列表
                    List<Information> resultList;
                    if (informationList != null && informationList.getResult() != null) {
                        resultList = informationList.getResult();

                        // 拼接完整的图片 URL
                        for (Information info : resultList) {
                            String imageUrl = info.getImageUrl();
                            if (imageUrl != null && !imageUrl.startsWith("http")) {
                                // 如果 imageUrl 不以 "http" 开头，则拼接基 URL
                                imageUrl = MyHttpUtil.host + imageUrl;
                                info.setImageUrl(imageUrl);
                            }
                        }
                    } else {
                        resultList = new ArrayList<>();
                    }

                    // 在主线程更新 UI
                    if (informationFragment.getActivity() != null) {
                        List<Information> finalResultList = resultList;
                        informationFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                informationFragment.showAllInformationCallback(finalResultList);
                                // 停止刷新动画
                                informationFragment.setRefreshing(false);
                            }
                        });
                    }
                } else {
                    // 在主线程中通知 Fragment 响应失败
                    if (informationFragment.getActivity() != null) {
                        informationFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(informationFragment.getContext(), "响应失败", Toast.LENGTH_SHORT).show();
                                // 停止刷新动画
                                informationFragment.setRefreshing(false);
                            }
                        });
                    }
                }
            }
        });
    }
}