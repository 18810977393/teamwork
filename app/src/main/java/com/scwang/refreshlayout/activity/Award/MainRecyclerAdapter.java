package com.scwang.refreshlayout.activity.Award;

import android.content.Context;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.avos.avoscloud.AVObject;
import com.scwang.refreshlayout.R;

import java.util.List;

/**
 * Created by BinaryHB on 11/24/15.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
  private Context mContext;
  private List<AVObject> mList;
  private OnRecyclerViewItemClickListener mOnItemClickListener = null;
  private OnRecyclerItemLongListener mOnItemLong = null;
  //define interface
  public interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, int data);

  }
  public interface OnRecyclerItemLongListener{
    void onItemLongClick(View view,int position);
  }

  public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }
  public void setOnItemLongClickListener(OnRecyclerItemLongListener listener){
    this.mOnItemLong =  listener;
  }
  public MainRecyclerAdapter(List<AVObject> list, Context context) {
    this.mContext = context;
    this.mList = list;
  }


  @Override
  public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_main, parent, false);
    MainViewHolder holder = new MainViewHolder(view, mOnItemClickListener,mOnItemLong);
    return holder;

    //return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_main, parent, false));
  }

  @Override
  public void onBindViewHolder(MainViewHolder holder, final int position) {
    holder.Title.setText((CharSequence) mList.get(position).getString("Title"));
    holder.scores.setText("-"+mList.get(position).getString("Scores"));
    holder.times.setText(mList.get(position).getInt("times")+" / "+mList.get(position).getString("Totaltime"));

  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
    private TextView Title;
    private TextView scores;
    private TextView times;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerItemLongListener mOnItemLong = null;
    public MainViewHolder(View itemView,OnRecyclerViewItemClickListener mListener,OnRecyclerItemLongListener longListener) {
      super(itemView);
      Title = (TextView) itemView.findViewById(R.id.Title);
      scores = (TextView) itemView.findViewById(R.id.scores);
      times = (TextView) itemView.findViewById(R.id.times);
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
