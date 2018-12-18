package com.scwang.refreshlayout.Initiate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.ViewPager.ViewPagerActivity;
import com.scwang.refreshlayout.activity.IndexMainActivity;


public class InitiateActivity extends AppCompatActivity {
    private ImageView imageView;
    private MediaPlayer mediaPlayer;
    private boolean whether_First_use;
    private SharedPreferences sharedPreferences;
    private AnimationDrawable animationDrawable = null;
    private Intent intent = new Intent("com.angel.Android.MUSIC");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initaite);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imageView = (ImageView)findViewById(R.id.imageView);
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        sharedPreferences = getSharedPreferences("whether_First_use",MODE_PRIVATE);
        whether_First_use = sharedPreferences.getBoolean("whether_First_use",true);
        Handler handler = new Handler();
        handler.postDelayed(begin0,0000);
        handler.postDelayed(begin1,1000);
        handler.postDelayed(begin3,6000);
        if (whether_First_use){
            handler.postDelayed(begin4,8000);
        }
        else {
            handler.postDelayed(begin5,8000);
        }

    }
    private Runnable begin0 = new Runnable() {
        @Override
        public void run() {
            try{
                imageView.setImageResource(R.drawable.first_ys);
                mediaPlayer.prepare();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private Runnable begin1 = new Runnable() {
        @Override
        public void run() {
            try{
                mediaPlayer.start();
                FrameAnimationDrawable.MyAnimationDrawable.animateRawManually(R.drawable.animation_horse_splash,imageView,null,null);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private Runnable begin3 = new Runnable() {
        @Override
        public void run() {
            try{
                mediaPlayer.release();
                imageView.setImageResource(R.drawable.second_ys);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                alphaAnimation.setDuration(2000);
                alphaAnimation.setRepeatCount(0);
                alphaAnimation.setFillAfter(true);
                imageView.startAnimation(alphaAnimation);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private Runnable begin4 = new Runnable() {
        @Override
        public void run() {
            try{
                imageView.setImageResource(0);
                startActivity(new Intent(InitiateActivity.this, ViewPagerActivity.class));
                finish();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("whether_First_use",false);
                editor.commit();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private Runnable begin5 = new Runnable() {
        @Override
        public void run() {
            try{
                imageView.setImageResource(0);
                startActivity(new Intent(InitiateActivity.this, IndexMainActivity.class));
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}