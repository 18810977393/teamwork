package com.scwang.refreshlayout.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.ViewPager.ViewPagerActivity;
import com.scwang.refreshlayout.activity.IndexMainActivity;

public class SplashActivity extends AppCompatActivity {

    private final static int DELAY_TIME = 3000 ;
    String SHARE_APP_TAG ="Judge_the_first";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //隐藏状态栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //判断用户是安装APP后第一次使用 ，是就出现功能引导页面
                SharedPreferences setting = getSharedPreferences(SHARE_APP_TAG, 0);
                Boolean user_first = setting.getBoolean("FIRST",true);
                if(user_first){
                    setting.edit().putBoolean("FIRST", false).commit();
                    startActivity(new Intent(SplashActivity.this ,
                            ViewPagerActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, IndexMainActivity.class));
                }
                finish();
            }
        },DELAY_TIME) ;
    }
}

