package org.androidtown.muksujung;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyon1001 on 2018-04-02.
 */

public class StoreAdapter extends BaseAdapter {
    List<StoreItem> items = new ArrayList<StoreItem>();

    public void addOne(StoreItem item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<StoreItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public StoreItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView view;
        if(convertView == null){
            view = new ItemView(parent.getContext());
        }else {
            view = (ItemView)convertView;
        }
        view.setStoreItem(items.get(position));
        return view;
    }
}