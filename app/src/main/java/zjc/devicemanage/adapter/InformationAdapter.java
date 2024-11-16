// InformationAdapter.java
package zjc.devicemanage.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import zjc.devicemanage.R;
import zjc.devicemanage.model.Information;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {
    // 数据源
    private List<Information> informationList;

    // 构造函数
    public InformationAdapter(List<Information> informationList) {
        this.informationList = informationList;
    }

    // setter方法
    public void setInformationList(List<Information> informationList) {
        this.informationList = informationList;
        notifyDataSetChanged(); // 通知数据已更新
    }

    // ViewHolder内部类
    public static class InformationViewHolder extends RecyclerView.ViewHolder {
        ImageView information_viewholder_information_iv;
        TextView information_viewholder_infocontent_tv;
        TextView information_viewholder_infocreatedtime_tv;

        public InformationViewHolder(View itemView) {
            super(itemView);
            // 绑定视图组件
            information_viewholder_information_iv = itemView.findViewById(
                    R.id.information_viewholder_information_iv);
            information_viewholder_infocontent_tv = itemView.findViewById(
                    R.id.information_viewholder_infocontent_tv);
            information_viewholder_infocreatedtime_tv = itemView.findViewById(
                    R.id.information_viewholder_infocreatedtime_tv);
        }
    }

    @Override
    public InformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.information_viewholder, parent, false);
        return new InformationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InformationViewHolder holder, int position) {
        // 获取当前位置的数据
        Information information = informationList.get(position);

        // 打印 imageUrl 以调试
        Log.d("InformationAdapter", "Loading image URL: " + information.getImageUrl());

        // 使用 Glide 加载图片
        Glide.with(holder.itemView.getContext())
                .load(information.getImageUrl())
                .placeholder(R.drawable.infobackground)
                .error(R.drawable.live)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            e.printStackTrace();
                            Log.e("Glide", "Image load failed for URL: " + information.getImageUrl(), e);
                        }
                        return false; // 返回 false 让 Glide 处理错误图
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.information_viewholder_information_iv);

        // 设置文本内容
        holder.information_viewholder_infocontent_tv.setText(information.getContent());
        holder.information_viewholder_infocreatedtime_tv.setText(information.getDate());

        // 设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(information);
                }
            }
        });

        holder.information_viewholder_infocontent_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onContentClick(information);
                }
            }
        });

        holder.information_viewholder_infocreatedtime_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDateClick(information);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return informationList == null ? 0 : informationList.size();
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(Information information);
        void onContentClick(Information information);
        void onDateClick(Information information);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}