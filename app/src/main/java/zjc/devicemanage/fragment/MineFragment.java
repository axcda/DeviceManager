package zjc.devicemanage.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import zjc.devicemanage.R;
import zjc.devicemanage.activity.LoginActivity;

public class MineFragment extends Fragment implements View.OnClickListener {
    private View fragment_mineView;
    private LinearLayout my_balance_ll;
    private LinearLayout my_orders_ll;
    private LinearLayout my_shipping_info_ll;
    private LinearLayout my_password_ll;
    private LinearLayout my_switch_account_ll;
    private LinearLayout my_feedback_ll;
    private LinearLayout my_exit_ll;

    public MineFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        fragment_mineView = inflater.inflate(R.layout.fragment_mine, container, false);
        initViews();
        return fragment_mineView;
    }

    private void initViews() {
        my_balance_ll = fragment_mineView.findViewById(R.id.my_balance_ll);
        my_orders_ll = fragment_mineView.findViewById(R.id.my_orders_ll);
        my_shipping_info_ll = fragment_mineView.findViewById(R.id.my_shipping_info_ll);
        my_password_ll = fragment_mineView.findViewById(R.id.my_password_ll);
        my_switch_account_ll = fragment_mineView.findViewById(R.id.my_switch_account_ll);
        my_feedback_ll = fragment_mineView.findViewById(R.id.my_feedback_ll);
        my_exit_ll = fragment_mineView.findViewById(R.id.my_exit_ll);

        // Set click listeners
        my_balance_ll.setOnClickListener(this);
        my_orders_ll.setOnClickListener(this);
        my_shipping_info_ll.setOnClickListener(this);
        my_password_ll.setOnClickListener(this);
        my_switch_account_ll.setOnClickListener(this);
        my_feedback_ll.setOnClickListener(this);
        my_exit_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.my_exit_ll) {
            showLogoutDialog();
        } else if (id == R.id.my_balance_ll) {
            // TODO: Implement account balance functionality
            showToast("账户余额功能待实现");
        } else if (id == R.id.my_orders_ll) {
            // TODO: Implement orders functionality
            showToast("全部订单功能待实现");
        } else if (id == R.id.my_shipping_info_ll) {
            // TODO: Implement shipping info modification
            showToast("修改收货信息功能待实现");
        } else if (id == R.id.my_password_ll) {
            // TODO: Implement password change
            showToast("修改密码功能待实现");
        } else if (id == R.id.my_switch_account_ll) {
            // TODO: Implement account switching
            showToast("切换账户功能待实现");
        } else if (id == R.id.my_feedback_ll) {
            // TODO: Implement feedback
            showToast("意见反馈功能待实现");
        }
    }

    private void showLogoutDialog() {
        TextView title = new TextView(getActivity());
        title.setPadding(10, 10, 10, 10);
        title.setText("设备管理");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#000000"));
        title.setGravity(Gravity.CENTER);

        new AlertDialog.Builder(getActivity())
                .setCustomTitle(title)
                .setMessage("是否要注销当前用户")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showToast(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}