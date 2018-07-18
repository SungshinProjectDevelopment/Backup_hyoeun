package org.androidtown.muksujung;

import android.util.Log;

/**
 * Created by hyon1001 on 2018-04-02.
 * 검색 시 받아오는 아이템 하나.
 */

public class StoreItem {

    // 4. 출력결과
    String title;
    String image;
    String link;
    String description;
    String telephone;
    String address;
    String mapx, mapy;

    @Override
    public String toString() {
        Log.d("StoreItem.javaclass", title + description);

        return title;
    }

    public String getLink() {
        return link;
    }

}
