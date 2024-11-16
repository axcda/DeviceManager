package zjc.devicemanage.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import zjc.devicemanage.R;
import zjc.devicemanage.fragment.HomeFragment;
import zjc.devicemanage.fragment.InformationFragment;
import zjc.devicemanage.fragment.MineFragment;
import zjc.devicemanage.fragment.ShopingcartFragment;

public class MainActivity extends AppCompatActivity {
    private TextView mainTitleTv;
    private FrameLayout mainFl;
    private ImageButton tabHomeIb;
    private TextView tabHomeTv;
    private LinearLayout tabHomeLl;
    private ImageButton tabInformationIb;
    private TextView tabInformationTv;
    private LinearLayout tabInformationLl;
    private ImageButton tabShopingcartIb;
    private TextView tabShopingcartTv;
    private LinearLayout tabShopingcartLl;
    private ImageButton tabMineIb;
    private TextView tabMineTv;
    private LinearLayout tabMineLl;
    // 4个Fragment控件
    private HomeFragment homeFragment;
    private InformationFragment informationFragment;
    private ShopingcartFragment shopingcartFragment;
    private MineFragment mineFragment;

    // 生成4个Fragment对象
    private void initControls() {
        mainTitleTv = findViewById(R.id.mainTitle_tv);
        mainFl = findViewById(R.id.main_fl);
        tabHomeIb = findViewById(R.id.tab_home_ib);
        tabHomeTv = findViewById(R.id.tab_home_tv);
        tabHomeLl = findViewById(R.id.tab_home_ll);
        tabInformationIb = findViewById(R.id.tab_information_ib);
        tabInformationTv = findViewById(R.id.tab_information_tv);
        tabInformationLl = findViewById(R.id.tab_information_ll);
        tabShopingcartIb = findViewById(R.id.tab_shopingcart_ib);
        tabShopingcartTv = findViewById(R.id.tab_shopingcart_tv);
        tabShopingcartLl = findViewById(R.id.tab_shopingcart_ll);
        tabMineIb = findViewById(R.id.tab_mine_ib);
        tabMineTv = findViewById(R.id.tab_mine_tv);
        tabMineLl = findViewById(R.id.tab_mine_ll);
        homeFragment = new HomeFragment();
        informationFragment = new InformationFragment();
        shopingcartFragment = new ShopingcartFragment();
        mineFragment = new MineFragment();
    }

    //载入不同的碎片窗口，根据不同Fragment显示不同的标题、点击图片和文字变色
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 用4种fragment视图控件替换activity_main.xml中的FrameLayout控件
        fragmentTransaction.replace(R.id.main_fl, fragment);
        fragmentTransaction.commit();
        // 根据不同的Fragment显示不同的标题、点击图片和文字变色
        if (fragment instanceof HomeFragment) {
            mainTitleTv.setText("首页");
            tabHomeIb.setImageResource(R.drawable.home_click);
            tabHomeTv.setTextColor(Color.parseColor("#00BFFF"));
        } else if (fragment instanceof InformationFragment) {
            mainTitleTv.setText("资讯");
            tabInformationIb.setImageResource(R.drawable.information_click);
            tabInformationTv.setTextColor(Color.parseColor("#00BFFF"));
        } else if (fragment instanceof ShopingcartFragment) {
            mainTitleTv.setText("购物车");
            tabShopingcartIb.setImageResource(R.drawable.shoppingcart_click);
            tabShopingcartTv.setTextColor(Color.parseColor("#00BFFF"));
        } else if (fragment instanceof MineFragment) {
            mainTitleTv.setText("我");
            tabMineIb.setImageResource(R.drawable.mine_click);
            tabMineTv.setTextColor(Color.parseColor("#00BFFF"));
        }
    }

    // 重置4组按钮图片和文本颜色
    private void resetImageAndTextColor() {
        // 重置首页按钮图片和文本颜色
        tabHomeIb.setImageResource(R.drawable.home);
        tabHomeTv.setTextColor(Color.parseColor("#272727"));
        // 重置资讯按钮图片和文本颜色
        tabInformationIb.setImageResource(R.drawable.information);
        tabInformationTv.setTextColor(Color.parseColor("#272727"));
        // 重置购物车按钮图片和文本颜色
        tabShopingcartIb.setImageResource(R.drawable.shoppingcart);
        tabShopingcartTv.setTextColor(Color.parseColor("#272727"));
        // 重置我按钮图片和文本颜色
        tabMineIb.setImageResource(R.drawable.mine);
        tabMineTv.setTextColor(Color.parseColor("#272727"));
    }

    public ShopingcartFragment getShopingcartFragment() {
        return shopingcartFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
        loadFragment(homeFragment);
        tabHomeLl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(homeFragment);
            }
        });
        tabInformationLl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(informationFragment);
            }
        });
        tabShopingcartLl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(shopingcartFragment);
            }
        });
        tabMineLl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadFragment(mineFragment);
            }
        });
    }
}