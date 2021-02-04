package com.example.finaltbf;

import java.util.Date;
import java.util.Map;

public class ReviewItem {
    //그 장소 부분에서 Stirng placeImg;로 하기. 왜냐하면 Firebase에서 URL형식으로 가져와서?
    //private String image; //url이라서 String임
    private String userName;
    private double rate;
    //private String date; //사용자가 리뷰 작성한 날짜
    private String review;
    //Firebase에서 날짜 저장하는 방식이래
    private Map date;
    //리뷰의 장소 id
    private String contentId;

    public ReviewItem(double rate, String review, Map date, String contentId) {
        this.userName = userName;
        this.rate = rate;
        this.review = review;
        this.date = date;
        this.contentId = contentId;
    }

    public ReviewItem() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getRate() {
        return rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Map getDate() {
        return date;
    }

    public void setDate(Map date) {
        this.date = date;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
