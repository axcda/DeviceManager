package zjc.devicemanage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zjc.devicemanage.R;
import zjc.devicemanage.model.Device;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private Context context;
    private List<Device> deviceList;

    // 构造函数
    public DeviceAdapter(Context context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_viewholder, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device currentDevice = deviceList.get(position);

        String deviceName = currentDevice.getDeviceName();
        holder.device_viewholder_devicename_tv.setText(deviceName);

        String devicePrice = currentDevice.getDevicePrice();
        holder.device_viewholder_deviceprice_tv.setText(devicePrice);

        switch (deviceName) {
            case "打印机":
                holder.device_viewholder_device_iv.setImageResource(R.drawable.printer);
                break;
            case "耳机":
                holder.device_viewholder_device_iv.setImageResource(R.drawable.earphone);
                break;
            case "鼠标":
                holder.device_viewholder_device_iv.setImageResource(R.drawable.mouse);
                break;
            case "笔记本电脑":
                holder.device_viewholder_device_iv.setImageResource(R.drawable.computer);
                break;
            case "U盘":
                holder.device_viewholder_device_iv.setImageResource(R.drawable.udisk);
                break;
            case "头盔":
                holder.device_viewholder_device_iv.setImageResource(R.drawable.helmet);
                break;
            default:
                holder.device_viewholder_device_iv.setImageResource(R.drawable.china);
                break;
        }

//        holder.device_viewholder_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClick(currentDevice.getDeviceID());
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return deviceList != null ? deviceList.size() : 0;
    }

    // ViewHolder 内部类
    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout device_viewholder_ll;
        private ImageView device_viewholder_device_iv;
        private TextView device_viewholder_devicename_tv;
        private TextView device_viewholder_deviceprice_tv;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            device_viewholder_ll = itemView.findViewById(R.id.device_viewholder_ll);
            device_viewholder_device_iv = itemView.findViewById(R.id.device_viewholder_device_iv);
            device_viewholder_devicename_tv = itemView.findViewById(R.id.device_viewholder_devicename_tv);
            device_viewholder_deviceprice_tv = itemView.findViewById(R.id.device_viewholder_deviceprice_tv);
        }
    }

    // 点击事件监听器接口
    public interface OnItemClickListener {
        void onItemClick(int deviceID);
    }

    private OnItemClickListener onItemClickListener;

    // 设置点击事件监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}