package zjc.devicemanage.model;

import com.google.gson.annotations.SerializedName;

public class Information {
    @SerializedName("InformationID")
    private int id;

    @SerializedName("InformationImage")
    private String imageUrl;

    @SerializedName("InformationContent")
    private String content;

    @SerializedName("InformationCreateTime")
    private String date;

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }
}