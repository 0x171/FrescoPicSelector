package com.xperfect.tt.lib.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xperfect.tt.lib.R;

import java.util.ArrayList;

/**
 * Created by Kyle on 2016/10/13.
 */
public abstract class  RecyclerViewAdapterBase< T,V extends View >extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_ITEM = 0;//一般item
    public static final int TYPE_FOOTER = 1;//底部item
    public static final int TYPE_HEADER = 2;//底部item

    /**
     * 存储需要绑定的数据
     */
    protected ArrayList<T> items = new ArrayList<>();

    protected int headerHolderCount = 0;

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> items) {
        if( this.items == null ){
            this.items = new ArrayList<>();
        }
        this.items.clear();
        this.items.addAll( items );
    }

    public void setItemsAddAll(ArrayList<T> items) {
        if( this.items == null ){
            this.items = new ArrayList<>();
        }
        this.items.addAll( items );
    }

    public void addHeader(){
        headerHolderCount++;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return headerHolderCount + items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if( position < headerHolderCount ){
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * 进行创建视图承载类
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if( viewType == TYPE_ITEM ){
            return new ViewWrapper<V>( onCreateCustomItemView(parent, viewType) );
        } else {
            return onCreateHeaderItemView( parent, viewType );
        }
    }

    /**
     * 创建普通视图Item，交给具体实现类完成
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract V onCreateCustomItemView(ViewGroup parent, int viewType);

    /**
     * 创建头部视图Item，交给具体实现类完成
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract RecyclerView.ViewHolder onCreateHeaderItemView(ViewGroup parent, int viewType);

    /**
     * 底部的view holder
     * */
    public static class HeadViewHolder extends  RecyclerView.ViewHolder{

        public LinearLayout ll_header_view_item;
        public SimpleDraweeView iv_header_view_item;

        public HeadViewHolder( View view ) {
            super(view);
            iv_header_view_item = (SimpleDraweeView) view.findViewById(R.id.iv_header_view_item);
            ll_header_view_item = (LinearLayout) view.findViewById(R.id.ll_header_view_item);
        }

    }

}