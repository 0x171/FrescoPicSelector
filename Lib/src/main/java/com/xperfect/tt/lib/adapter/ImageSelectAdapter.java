package com.xperfect.tt.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xperfect.tt.lib.R;
import com.xperfect.tt.lib.item.ImageItem;
import com.xperfect.tt.lib.item.ImageItem_;
import com.xperfect.tt.lib.model.ImageBean;
import com.xperfect.tt.lib.widget.RecyclerViewAdapterBase;
import com.xperfect.tt.lib.widget.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by Kyle on 2016/10/13.
 */
@EBean
public class ImageSelectAdapter extends RecyclerViewAdapterBase<ImageBean, ImageItem> implements ImageItem.OnItemSelectListener {

    public static final int TAKE_PHOTO_REQUEST_CODE = 0;
    public static final int SELECT_PHOTO_REQUEST_CODE = 1;

    @RootContext
    Context context;

    protected ArrayList<ImageBean> selectedList = new ArrayList<>();

    ImageItem.OnItemSelectListener onItemSelectListener;

    public ImageItem.OnItemSelectListener getOnItemSelectListener() {
        return onItemSelectListener;
    }

    public void setOnItemSelectListener(ImageItem.OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @Override
    protected ImageItem onCreateCustomItemView(ViewGroup parent, int viewType) {
        return ImageItem_.build(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderItemView(ViewGroup parent, int viewType) {
        View foot_view= LayoutInflater.from(context).inflate( R.layout.i_album_take_photo, parent, false );
        return new RecyclerViewAdapterBase.HeadViewHolder(foot_view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position ) {
        if( position > 0 ){
            ImageItem view = ((ViewWrapper<ImageItem>)holder).getView();
            ImageBean selectedImage = items.get(position-1);
            view.setOnItemSelectListener( this );
            view.bind( selectedImage, position, selectedList );
        } else {
            if( holder instanceof HeadViewHolder ){
                HeadViewHolder headViewHolder = (HeadViewHolder)holder;
                headViewHolder.iv_header_view_item.setAspectRatio(1.0f);
                headViewHolder.ll_header_view_item.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if( onItemSelectListener != null ){
                                    onItemSelectListener.itemSelect( true, 0 );
                                }
                            }
                        }
                );
            }
        }
    }

    @Override
    public void itemSelect(boolean isSelected, int position) {
        Log.e("itemSelect","itemSelect  position    " + position);
        if( isSelected ){
            if( !selectedList.contains( items.get(position-1) ) ){
                selectedList.add( items.get(position-1) );
            }
        } else {
            if( selectedList.contains( items.get(position-1) ) ){
                selectedList.remove( items.get(position-1) );
            }
        }
        items.get(position-1).isSelected = isSelected;
        notifyDataSetChanged();
    }

}