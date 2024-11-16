package zjc.devicemanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zjc.devicemanage.R;
import zjc.devicemanage.model.Device;
import zjc.devicemanage.model.DeviceClass;
import zjc.devicemanage.model.DeviceClassList;
import zjc.devicemanage.model.DeviceList;

public class EmbedDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DEVICE_CLASS = 1;
    private static final int TYPE_DEVICE = 2;
    
    private Context context;
    private DeviceClassList deviceClassList;
    private DeviceList deviceList;
    
    public EmbedDeviceAdapter(Context context, DeviceClassList deviceClassList, DeviceList deviceList) {
        this.context = context;
        this.deviceClassList = deviceClassList;
        this.deviceList = deviceList;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_DEVICE_CLASS : TYPE_DEVICE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_DEVICE_CLASS) {
            View view = LayoutInflater.from(context).inflate(R.layout.device_class_grid_item, parent, false);
            return new DeviceClassViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.device_viewholder, parent, false);
            return new DeviceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DeviceClassViewHolder) {
            DeviceClassViewHolder deviceClassHolder = (DeviceClassViewHolder) holder;
            setupDeviceClassGrid(deviceClassHolder);
        } else if (holder instanceof DeviceViewHolder) {
            DeviceViewHolder deviceHolder = (DeviceViewHolder) holder;
            Device device = deviceList.getResult().get(position - 1);
            bindDeviceData(deviceHolder, device);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + (deviceList != null && deviceList.getResult() != null ? deviceList.getResult().size() : 0);
    }

    private void setupDeviceClassGrid(DeviceClassViewHolder holder) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        
        DeviceClassAdapter adapter = new DeviceClassAdapter(context, deviceClassList.getResult());
        adapter.setOnItemClickListener((position, deviceClassId) -> {
            if (onDeviceClassClickListener != null) {
                onDeviceClassClickListener.onDeviceClassClick(deviceClassId);
            }
        });
        holder.recyclerView.setAdapter(adapter);
    }

    private void bindDeviceData(DeviceViewHolder holder, Device device) {
        holder.deviceNameTv.setText(device.getDeviceName());
        holder.devicePriceTv.setText(device.getDevicePrice());
        
        // Set device image based on device name
        switch (device.getDeviceName()) {
            case "打印机":
                holder.deviceIv.setImageResource(R.drawable.printer);
                break;
            case "耳机":
                holder.deviceIv.setImageResource(R.drawable.earphone);
                break;
            case "鼠标":
                holder.deviceIv.setImageResource(R.drawable.mouse);
                break;
            case "笔记本电脑":
                holder.deviceIv.setImageResource(R.drawable.computer);
                break;
            case "U盘":
                holder.deviceIv.setImageResource(R.drawable.udisk);
                break;
            case "头盔":
                holder.deviceIv.setImageResource(R.drawable.helmet);
                break;
            default:
                holder.deviceIv.setImageResource(R.drawable.china);
                break;
        }
    }

    // ViewHolder classes
    static class DeviceClassViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        DeviceClassViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.device_class_grid_recycler);
        }
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        ImageView deviceIv;
        TextView deviceNameTv;
        TextView devicePriceTv;
        LinearLayout deviceLl;

        DeviceViewHolder(View itemView) {
            super(itemView);
            deviceIv = itemView.findViewById(R.id.device_viewholder_device_iv);
            deviceNameTv = itemView.findViewById(R.id.device_viewholder_devicename_tv);
            devicePriceTv = itemView.findViewById(R.id.device_viewholder_deviceprice_tv);
            deviceLl = itemView.findViewById(R.id.device_viewholder_ll);
        }
    }

    // Click listener interface
    public interface OnDeviceClassClickListener {
        void onDeviceClassClick(String deviceClassId);
    }

    private OnDeviceClassClickListener onDeviceClassClickListener;

    public void setOnDeviceClassClickListener(OnDeviceClassClickListener listener) {
        this.onDeviceClassClickListener = listener;
    }
} 