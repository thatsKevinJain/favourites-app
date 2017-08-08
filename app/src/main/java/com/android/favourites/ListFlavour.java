package com.android.favourites;

public class ListFlavour {

    String title;
    String desc;
    String type;
    String view_count;
    String imgUrl;

    public ListFlavour(String ti, String d, String ty, String v, String imgUrl) {
        title = ti;
        desc = d;
        type = ty;
        view_count = v;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getView_count() {
        return view_count;
    }

    public String getImgUrl() {
        return imgUrl;
    }

}
