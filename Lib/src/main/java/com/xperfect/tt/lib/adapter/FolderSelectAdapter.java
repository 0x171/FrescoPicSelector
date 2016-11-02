package com.xperfect.tt.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xperfect.tt.lib.item.FolderItem;
import com.xperfect.tt.lib.item.FolderItem_;
import com.xperfect.tt.lib.model.FolderBean;
import com.xperfect.tt.lib.widget.RecyclerViewAdapterBase;
import com.xperfect.tt.lib.widget.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by Kyle on 2016/10/13.
 */
@EBean
public class FolderSelectAdapter extends RecyclerViewAdapterBase<FolderBean, FolderItem> implements FolderItem.OnFolderItemClickListener {

    @RootContext
    Context context;

    public ArrayList<FolderBean> selectedFolders = new ArrayList<>();
    OnFolderSelectListener onFolderSelectListener;

    public OnFolderSelectListener getOnFolderSelectListener() {
        return onFolderSelectListener;
    }

    public void setOnFolderSelectListener(OnFolderSelectListener onFolderSelectListener) {
        this.onFolderSelectListener = onFolderSelectListener;
    }

    @Override
    protected FolderItem onCreateCustomItemView(ViewGroup parent, int viewType) {
        return FolderItem_.build(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderItemView(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ////
        FolderItem view = ((ViewWrapper<FolderItem>)holder).getView();
        FolderBean folderBean = items.get(position);
        view.setOnFolderItemClickListener( this );
        view.bind( folderBean, position );
    }

    @Override
    public void onItemClick(boolean isSelected, int position) {
        if( isSelected ){
            if( !selectedFolders.contains( items.get(position) ) ){
                selectedFolders.add( items.get(position) );
            }
        } else {
            if( selectedFolders.contains( items.get(position) ) ){
                selectedFolders.remove( items.get(position) );
            }
        }
        if( onFolderSelectListener != null ){
            onFolderSelectListener.onFolderSelect( selectedFolders );
        }
        items.get(position).isSelected = isSelected;
        notifyDataSetChanged();
//        notifyItemChanged( position );
    }

    public interface OnFolderSelectListener {
        void onFolderSelect(ArrayList<FolderBean> selectedFolders);
    }

}