package com.example.finaltbf;

public class Place {

    //현재 테스트하느라 위도 경도값만 넣음 + 카테고리분류번호(관광지,숙박,...)

    //민진: mapx, mapy로 바꿔야 하나?
    private double mapx;
    private double mapy;
    private String categoryNo;
    private String contentId;

    private String title;
    private int order;

    public Place(){}

    public Place(double mapx, double mapy, String categoryNo, String contentId, String title) {
        this.mapx = mapx;
        this.mapy = mapy;
        this.categoryNo = categoryNo;
        this.contentId = contentId;
        this.title = title;
    }

    public Place(double mapx, double mapy, String title) {
        this.mapx = mapx;
        this.mapy = mapy;
        this.title = title;
    }

    public Place(double mapx, double mapy, String title, int order) {
        this.mapx = mapx;
        this.mapy = mapy;
        this.title = title;
        this.order = order;
    }

    public Place(int order) {
        this.order = order;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
