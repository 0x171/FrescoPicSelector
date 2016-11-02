package com.xperfect.tt.lib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Kyle on 2016/10/13.
 */

public class FrescoRecyclerView extends RecyclerView {

    public FrescoRecyclerView(Context context) {
        super(context);
        initListener();
    }

    public FrescoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initListener();
    }

    public FrescoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initListener();
    }

    private void initListener(){
        setOnScrollListener(
                new OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        switch (newState) {
                            case RecyclerView.SCROLL_STATE_IDLE:
                                Fresco.getImagePipeline().resume();
                                break;
                            case RecyclerView.SCROLL_STATE_DRAGGING:
                                Fresco.getImagePipeline().pause();
                                break;
                            case RecyclerView.SCROLL_STATE_SETTLING:
                                Fresco.getImagePipeline().pause();
                                break;
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled (RecyclerView recyclerView, int dx, int dy){
                        super.onScrolled(recyclerView, dx, dy);
                    }
                }
        );
    }

}