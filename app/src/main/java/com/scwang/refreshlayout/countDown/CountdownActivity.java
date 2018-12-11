package com.scwang.refreshlayout.countDown;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
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


    private void init(){
        tvtime1=(TextView)findViewById(R.id.tvtime1);
        tvtime2=(TextView) findViewById(R.id.tvtime2);
        tvtime3=(TextView) findViewById(R.id.tvtime3);
        editText=(EditText)findViewById(R.id.ed);
        start_btn=(ImageButton)findViewById(R.id.start_btn);
        astronaut_iv=(ImageView)findViewById(R.id.astronaut_iv);



        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp =  editText.getText().toString();
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

            }
        });
    }

    private void startAnimation() {
        Animation translateAnimation = new TranslateAnimation(0, 100, 0, 0);
        translateAnimation.setDuration(1500);
        translateAnimation.setInterpolator(this, android.R.anim.cycle_interpolator);//设置动画插入器
        translateAnimation.setRepeatCount(1000000);
        translateAnimation.setFillAfter(true);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
        astronaut_iv.startAnimation(translateAnimation);
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
