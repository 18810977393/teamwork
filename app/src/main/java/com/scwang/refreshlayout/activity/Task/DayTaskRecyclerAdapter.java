package com.scwang.refreshlayout.activity.Task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.scwang.refreshlayout.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BinaryHB on 11/24/15.
 */
public class DayTaskRecyclerAdapter extends RecyclerView.Adapter<DayTaskRecyclerAdapter.MainViewHolder> {
  private Context mContext;
  private List<AVObject> mList;
  private OnRecyclerViewItemClickListener mOnItemClickListener = null;
  private OnRecyclerItemLongListener mOnItemLong = null;
  private AVObject avObject;
  private int stars;
  private List<AVObject> List = new ArrayList<>();
  //define interface
  public interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, int data);

  }
  public interface OnRecyclerItemLongListener{
    void onItemLongClick(View view, int position);
  }

  public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }
  public void setOnItemLongClickListener(OnRecyclerItemLongListener listener){
    this.mOnItemLong =  listener;
  }
  public DayTaskRecyclerAdapter(List<AVObject> list, Context context) {
    this.mContext = context;
    this.mList = list;
  }


  @Override
  public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_task_list_main, parent, false);
    MainViewHolder holder = new MainViewHolder(view, mOnItemClickListener,mOnItemLong);
    return holder;

    //return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_main, parent, false));
  }

  @Override
  public void onBindViewHolder(final MainViewHolder holder, final int position) {
    holder.Title.setText((CharSequence) mList.get(position).getString("Title"));
    holder.scores.setText(mList.get(position).getString("Scores"));
    holder.times.setText(mList.get(position).getInt("times")+" / "+mList.get(position).getString("Totaltime"));
    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
        {
          try {
            updateStars(position);
          } catch (AVException e) {
            e.printStackTrace();
          }
          AVObject avObject = mList.get(position);
          if (avObject.getInt("times")==Integer.valueOf(avObject.getString("Totaltime"))-1)
          {
            String objectId = avObject.getObjectId();
            AVObject todo = AVObject.createWithoutData(AVUser.getCurrentUser().getUsername(), objectId);
            todo.put("status",false);
            // 保存到云端
            todo.saveInBackground();
          }
          else
          {
            String objectId = avObject.getObjectId();
            int a = mList.get(position).getInt("times")+1;
            AVObject todo = AVObject.createWithoutData(AVUser.getCurrentUser().getUsername(), objectId);
            todo.put("times",a);
            // 保存到云端
            todo.saveInBackground();
          }
          holder.checkBox.setChecked(false);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
    private TextView Title;
    private TextView scores;
    private TextView times;
    private CheckBox checkBox;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerItemLongListener mOnItemLong = null;
    public MainViewHolder(View itemView,OnRecyclerViewItemClickListener mListener,OnRecyclerItemLongListener longListener) {
      super(itemView);
      Title = (TextView) itemView.findViewById(R.id.Title);
      scores = (TextView) itemView.findViewById(R.id.scores);
      times = (TextView) itemView.findViewById(R.id.times);
      checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);
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
  private void updateStars(int position) throws AVException {
    List.clear();
    AVQuery<AVObject> avQuery1 =new AVQuery<>("Data_table");
    List = avQuery1.find();
    String name = AVUser.getCurrentUser().getUsername();
    for (int i=0;i<List.size();i++)
    {
      if (name.compareTo(List.get(i).getString("Name"))==0)
      {
        avObject = List.get(i);
        break;
      }
    }
    stars = avObject.getInt("Scores");
    int scores =Integer.parseInt(mList.get(position).getString("Scores"));
    String id = avObject.getObjectId();
    AVObject todo = AVObject.createWithoutData("Data_table",id);
    todo.put("Scores",stars+scores);
    todo.saveInBackground();
  }
}
