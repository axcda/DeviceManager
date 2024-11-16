package zjc.devicemanage.service;

import zjc.devicemanage.model.ShopingcartList;

public interface ShopingcartService {
    void findAllShopingcart();
    void findAllShopingcartByUserId(String userId, CheckCallback callback);
    void addToCart(String userId, String deviceId, String buyNum);
    void addShopingcart(String addDeviceID, String addBuyNum, String addUserID);

    interface CheckCallback {
        void onResult(ShopingcartList cartList);
    }
}
