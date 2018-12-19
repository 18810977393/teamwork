package com.scwang.refreshlayout.countDown;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.refreshlayout.R;

public class CountdownActivity extends AppCompatActivity {
    private TextView tvtime1,tvtime2,tvtime3;
    private EditText editText;
    private ImageButton start_btn;
    private ImageView astronaut_iv;
    private  long  time=400;
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 3000;  // 快速点击间隔
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        init();
    }
    public abstract static class OnMultiClickListener implements View.OnClickListener{
        // 两次点击按钮之间的点击间隔不能少于1000毫秒
        private static final int MIN_CLICK_DELAY_TIME = 3000;
        private static long lastClickTime = 0;

        public abstract void onMultiClick(View v);

        @Override
        public void onClick(View v) {
            long curClickTime = System.currentTimeMillis();
            if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                // 超过点击间隔后再将lastClickTime重置为当前点击时间
                lastClickTime = curClickTime;
                onMultiClick(v);
            }
        }
    }
    private boolean processFlag = true; //默认可以点击
    /**
     * 设置按钮在短时间内被重复点击的有效标识（true表示点击有效，false表示点击无效）
     */
    private synchronized void setProcessFlag() {
        processFlag = false;
    }

    /**
     * 计时线程（防止在一定时间段内重复点击按钮）
     */
    private class TimeThread extends Thread {
        public void run() {
            try {
                sleep(3000);
                processFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





    private void init(){
        tvtime1=(TextView)findViewById(R.id.tvtime1);
        tvtime2=(TextView) findViewById(R.id.tvtime2);
        tvtime3=(TextView) findViewById(R.id.tvtime3);
        editText=(EditText)findViewById(R.id.ed);
        start_btn=(ImageButton)findViewById(R.id.start_btn);
        astronaut_iv=(ImageView)findViewById(R.id.astronaut_iv);



        start_btn.setOnClickListener(new OnMultiClickListener() {

            @Override
            public void onMultiClick(View v) {
                start_btn.setEnabled(false);
                String temp =  editText.getText().toString();
                if (processFlag) {
                    setProcessFlag();
                    if (!TextUtils.isEmpty(temp)){
                        int minute = Integer.valueOf(temp);
                        time = minute*60;

                        startAnimation();
                        handler.postDelayed(runnable, 1000);
                        editText.setText("");
                    }
                    else{
                        Toast.makeText(CountdownActivity.this,"输入框不能为空！",Toast.LENGTH_LONG).show();
                    }
                    new TimeThread().start();
                }
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        start_btn.setEnabled(true);
                    }
                }, 3000);
            }
        });


//        start_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String temp =  editText.getText().toString();
//                if (!TextUtils.isEmpty(temp)){
//                    int minute = Integer.valueOf(temp);
//                    time = minute*60;
//
//                    startAnimation();
//                    handler.postDelayed(runnable, 1000);
//                    editText.setText("");
//                }
//                else{
//                    Toast.makeText(CountdownActivity.this,"输入框不能为空！",Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
    }

    private void startAnimation() {
        Animation rotateAnimation = new RotateAnimation(0, 355, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 3.3f);
        rotateAnimation.setDuration(60000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);//设置动画插入器
        astronaut_iv.startAnimation(rotateAnimation);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            String formatLongToTimeStr = formatLongToTimeStr(time);
            String[] split = formatLongToTimeStr.split("：");
            for (int i = 0; i < split.length; i++) {
                if(i==0){
                    tvtime1.setText(split[0]+"时");
                }
                if(i==1){
                    tvtime2.setText(split[1]+"分");
                }
                if(i==2){
                    tvtime3.setText(split[2]+"秒");
                }

            }
            if(time>0){
                handler.postDelayed(this, 1000);
            }
        }
    };

    public  String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;         //取整
            second = second % 60;         //取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour+"："+minute+"："+second;
        return strtime;

    }
}
