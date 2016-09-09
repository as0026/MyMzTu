package com.hjs.mymztu.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hjs.mymztu.R;
import com.hjs.mymztu.entity.MzituBean;
import com.hjs.mymztu.utils.VolleyHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainHoder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MzituBean> mData;
    private OnItemClickLitener mOnItemClickLitener;

    public MainRecyclerAdapter(Context context, List<MzituBean> data){
        mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MainHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainHoder holder = new MainHoder(this.mInflater.inflate(R.layout.main_recycler_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MainHoder holder, int position) {
        holder.itemTxt.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getFirstImgUrl()).crossFade(500).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fitCenter().into(holder.itemCoverImg);
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.mData != null ? this.mData.size():0;
    }

    class MainHoder extends RecyclerView.ViewHolder{
        TextView itemTxt;
        ImageView itemCoverImg;
        public MainHoder(View itemView) {
            super(itemView);
            itemTxt = (TextView) itemView.findViewById(R.id.item_title_txt);
            itemCoverImg = (ImageView) itemView.findViewById(R.id.item_cover_img);
        }
    }
}
