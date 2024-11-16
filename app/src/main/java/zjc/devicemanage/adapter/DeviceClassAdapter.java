package zjc.devicemanage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zjc.devicemanage.R;
import zjc.devicemanage.model.DeviceClass;

public class DeviceClassAdapter extends RecyclerView.Adapter<DeviceClassAdapter.DeviceClassViewHolder> {
    private List<DeviceClass> deviceClasses;

    public DeviceClassAdapter(Context context, List<DeviceClass> deviceClasses) {
        this.deviceClasses = deviceClasses;
    }

    @NonNull
    @Override
    public DeviceClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deviceclass_viewholder, parent, false);
        return new DeviceClassViewHolder(itemView);
    }

    @Override
public void onBindViewHolder(@NonNull DeviceClassViewHolder holder, @SuppressLint("RecyclerView") int position) {
    DeviceClass deviceClass = deviceClasses.get(position);
    String itemText = deviceClass.getDeviceClassName();
    holder.deviceClassTv.setText(itemText);
    
    // 根据设备类型名称设置对应的图标
    switch (itemText) {
        case "办公设备":
            holder.deviceClassIv.setImageResource(R.drawable.office);
            break;
        case "生活设备":
            holder.deviceClassIv.setImageResource(R.drawable.live);
            break;
        case "学习设备":
            holder.deviceClassIv.setImageResource(R.drawable.study);
            break;
        case "户外设备":
            holder.deviceClassIv.setImageResource(R.drawable.outdoor);
            break;
        case "电子设备":
            holder.deviceClassIv.setImageResource(R.drawable.elecdevice);
            break;
        case "其他设备":
            holder.deviceClassIv.setImageResource(R.drawable.other);
            break;
        default:
            holder.deviceClassIv.setImageResource(R.drawable.other);
            break;
    }
    
    // 设置点击事件
    if (onItemClickListener != null) {
        holder.deviceClassLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position, deviceClass.getDeviceClassID());
            }
        });
    }
}

    private void setDeviceClassIcon(ImageView imageView, String deviceClassId) {
        // 根据设备分类ID设置不同的图标
        switch(deviceClassId) {
            case "1":
                imageView.setImageResource(R.drawable.office);
                break;
            // 可以添加更多的设备分类图标
            default:
                imageView.setImageResource(R.drawable.office);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return deviceClasses != null ? deviceClasses.size() : 0;
    }

    // ViewHolder内部类
    public static class DeviceClassViewHolder extends RecyclerView.ViewHolder {
        LinearLayout deviceClassLl;
        ImageView deviceClassIv;
        TextView deviceClassTv;

        public DeviceClassViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceClassLl = itemView.findViewById(R.id.deviceclass_viewholder_ll);
            deviceClassIv = itemView.findViewById(R.id.deviceclass_viewholder_iv);
            deviceClassTv = itemView.findViewById(R.id.deviceclass_viewholder_tv);
        }
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(int position, String deviceClassId);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}