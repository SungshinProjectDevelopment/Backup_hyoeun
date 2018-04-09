package org.androidtown.muksujung;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by hyon1001 on 2018-04-02.
 */

public class URLImageView  extends android.support.v7.widget.AppCompatImageView{
    public URLImageView(Context context) {
        super(context);
    }
    public URLImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    ImageRequest mRequest;
    // 지우고 밑에것쓰기(180404)
    public void setImageURL(String url) {
        setImageResource(R.drawable.login_icon);

    }
//    public void setImageURL(String url) {
//        if (mRequest != null) {  //scrapedView 때문에 잘못된 이미지가 나오지 않게 하기 위해
//            mRequest.cancel();
//            mRequest = null;
//        }
//        if (!TextUtils.isEmpty(url)) {
//            setImageResource(R.drawable.login_icon);
//            ImageRequest request = new ImageRequest(url);
//            mRequest = request;
//            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<Bitmap>() {
//                @Override
//                public void onSuccess(NetworkRequest<Bitmap> request, Bitmap result) {
//                    setImageBitmap(result);
//                    mRequest = null;
//                }
//
//                @Override
//                public void onFailure(NetworkRequest<Bitmap> request, int errorCode, int responseCode, String message, Throwable excepton) {
//                    setImageResource(R.drawable.login_icon);
//                    mRequest = null;
//                }
//            });
//        } else {
//            setImageResource(R.drawable.login_icon);
//        }
//
//    }
}