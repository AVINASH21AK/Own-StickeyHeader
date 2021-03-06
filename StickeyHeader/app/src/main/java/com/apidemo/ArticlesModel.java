package com.apidemo;

import com.google.gson.annotations.SerializedName;

public class ArticlesModel {

    public String id;

    @SerializedName("author")
    public String author;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("urlToImage")
    public String urlToImage;

    @SerializedName("publishedAt")
    public String publishedAt;

    public ArticlesModel() {}

    public ArticlesModel(String author, String title, String description, String urlToImage, String publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public ArticlesModel(String id, String author, String title, String description, String urlToImage, String publishedAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }
}