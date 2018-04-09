package org.androidtown.muksujung;

import com.begentgroup.xmlparser.SerializedName;

import java.util.ArrayList;

/**
 * Created by hyon1001 on 2018-04-04.
 */

public class NaverStores {
    String title;
    String description;
    int total;
    int start;
    int display;
    @SerializedName("item")
    ArrayList<StoreItem> items;
}