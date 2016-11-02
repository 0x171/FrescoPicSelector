package com.xperfect.tt.lib.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kyle on 2016/10/13.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    public V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }

}