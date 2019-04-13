package com.groupr.groupr;

import java.util.ArrayList;

public class AllNews {

    private ArrayList<News> allNews;

    AllNews(){
        allNews = new ArrayList<>();
    }

    void addNews(News newNews){
        allNews.add(newNews);
    }

}
