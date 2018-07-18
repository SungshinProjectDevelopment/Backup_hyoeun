package org.androidtown.muksujung;

import android.util.Log;

import com.begentgroup.xmlparser.XMLParser;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;

/**
 * Created by hyon1001 on 2018-04-04.
 */

public class NaverStoreRequest extends NetworkRequest<NaverStores> {

    public String keyword;
    int start, display;

    public NaverStoreRequest(String keyword) {
        this(keyword, 1, 20);
    }

    public NaverStoreRequest(String keyword, int start) {
        this(keyword, start, 20);
    }

    public NaverStoreRequest(String keyword, int start, int display) {

        try {
            this.keyword = URLEncoder.encode(keyword,"utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.start =start;
        this.display = display;
        Log.i("NaverStoreRequest", keyword);

    }

    private static final String URL_FORMAT = "https://openapi.naver.com/v1/search/local.xml?query=%s&display=%s&start=%s";
    @Override
    public URL getURL() throws MalformedURLException {
        String url = String.format(URL_FORMAT, keyword, display, start);
        return new URL(url);
    }

    @Override
    public void setRequestHeader(HttpURLConnection conn) {
        super.setRequestHeader(conn);
        conn.setRequestProperty("X-Naver-Client-Id", "gmXhBBrREE97PFH2fRMc");
        conn.setRequestProperty("X-Naver-Client-Secret", "5jW5WNaCkh");
    }

    @Override
    protected NaverStores parse(InputStream is) throws ParseException {
        XMLParser parser = new XMLParser();
        NaverStores stores = parser.fromXml(is, "channel", NaverStores.class);
        return stores;
    }
}
