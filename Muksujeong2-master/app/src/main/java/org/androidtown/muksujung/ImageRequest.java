package org.androidtown.muksujung;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hyon1001 on 2018-04-02.
 */

public class ImageRequest extends NetworkRequest<Bitmap>{
    String url;
    public ImageRequest(String url) {
        this.url = url;
    }
    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(url);
    }

    @Override
    protected Bitmap parse(InputStream is) throws ParseException {
        Bitmap bm = BitmapFactory.decodeStream(is);
        return bm;
    }
}
