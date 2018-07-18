package org.androidtown.muksujung;

/**
 * Created by hyon1001 on 2018-06-21.
 */

public class MPostItem {
    String posttitle;
    String restaurantname;
    String rating;
    String hits;
    String writerid;
    String postbody;

    public MPostItem(String posttitle, String restaurantname, String rating, String hits, String writerid, String postbody) {
        this.posttitle = posttitle;
        this.restaurantname = restaurantname;
        this.rating = rating;
        this.hits = hits;
        this.writerid = writerid;
        this.postbody = postbody;
    }
}
