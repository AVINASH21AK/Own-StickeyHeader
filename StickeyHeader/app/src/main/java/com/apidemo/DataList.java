package com.apidemo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataList {

    @SerializedName("articles")
    public ArrayList<ArticlesModel> articles;
}
