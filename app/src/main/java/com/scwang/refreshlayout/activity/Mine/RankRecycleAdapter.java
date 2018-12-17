package com.scwang.refreshlayout.activity.Mine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.Award.MainRecyclerAdapter;

import java.util.List;

public class RankRecycleAdapter extends RecyclerView.Adapter<RankRecycleAdapter.MainViewHolder>{

    private Context mContext;
    private List<AVObject> mList;
    private MainRecyclerAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private MainRecyclerAdapter.OnRecyclerItemLongListener mOnItemLong = null;
    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);

    }
    public interface OnRecyclerItemLongListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(MainRecyclerAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(MainRecyclerAdapter.OnRecyclerItemLongListener listener){
        this.mOnItemLong =  listener;
    }
    public RankRecycleAdapter(List<AVObject> list, Context context) {
        this.mContext = context;
        this.mList = list;
    }


    @Override
    public RankRecycleAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rank, parent, false);
        RankRecycleAdapter.MainViewHolder holder = new RankRecycleAdapter.MainViewHolder(view, mOnItemClickListener,mOnItemLong);
        return holder;

    }

    @Override
    public void onBindViewHolder(RankRecycleAdapter.MainViewHolder holder, final int position) {
        holder.Title.setText((CharSequence) mList.get(position).getString("Name"));
        holder.scores.setText(String.valueOf( mList.get(position).getInt("Scores")));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private TextView Title;
        private TextView scores;


        private MainRecyclerAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
        private MainRecyclerAdapter.OnRecyclerItemLongListener mOnItemLong = null;
        public MainViewHolder(View itemView, MainRecyclerAdapter.OnRecyclerViewItemClickListener mListener, MainRecyclerAdapter.OnRecyclerItemLongListener longListener) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title);
            scores = (TextView) itemView.findViewById(R.id.scores);
            this.mOnItemClickListener = mListener;
            this.mOnItemLong = longListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v,getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mOnItemLong != null){
                mOnItemLong.onItemLongClick(v,getLayoutPosition());
            }
            return true;
        }
    }
}
