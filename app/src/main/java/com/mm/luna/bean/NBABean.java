package com.mm.luna.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ZMM on 2018/10/11 9:42.
 */
public class NBABean implements MultiItemEntity {
    public static final int DATE = 1;
    public static final int NORMAL = 2;
    private int itemType;

    private String homeTem;
    private String visitingTeam;
    private String time;
    private String date;
    private String title;
    private String url;

    public NBABean(String date, int itemType) {
        this.date = date;
        this.itemType = itemType;
    }

    public NBABean(String[] split, int itemType) {
        time = split[0];
        homeTem = split[1];
        visitingTeam = split[3];
        this.itemType = itemType;
    }

    public NBABean(String time, String title, String homeTem, String visitingTeam, String url, int itemType) {
        this.time = time;
        this.title = title;
        this.homeTem = homeTem;
        this.visitingTeam = visitingTeam;
        this.url = url;
        this.itemType = itemType;
    }

    public String getHomeTem() {
        return homeTem == null ? "" : homeTem;
    }

    public String getVisitingTeam() {
        return visitingTeam == null ? "" : visitingTeam;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public String getDate() {
        return date == null ? "" : date;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
