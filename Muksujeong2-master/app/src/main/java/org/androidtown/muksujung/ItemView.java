package org.androidtown.muksujung;

import android.content.Context;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by hyon1001 on 2018-04-02.
 */

public class ItemView extends FrameLayout {
    public ItemView(Context context) {
        super(context);
        init();
    }

    URLImageView iconView;
    TextView titleView, telephoneView, addressView;
    private void init(){
        inflate(getContext(), R.layout.view_item_store, this);
        iconView = findViewById(R.id.image_icon);
        titleView = (TextView)findViewById(R.id.itemstore_text_title);
        telephoneView = (TextView)findViewById(R.id.itemstore_text_telephone);
        addressView = (TextView)findViewById(R.id.itemstore_text_address);
    }

    StoreItem item;
    //    ImageRequest mRequest;
    //
    public void setStoreItem(StoreItem item) {
        this.item = item;
        titleView.setText(Html.fromHtml(item.title));
        addressView.setText(item.description);
        telephoneView.setText(item.telephone);

        // iconView item.image....
        iconView.setImageURL(item.image);

        //수동으로 itemView 마다 이미지를 가져오는 경우
    }
}