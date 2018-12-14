package com.scwang.refreshlayout.Initiate;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.IndexMainActivity;


public class InitiateActivity extends AppCompatActivity {
    private ImageView imageView;
    private AnimationDrawable animationDrawable = null;
    private Intent intent = new Intent("com.angel.Android.MUSIC");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initaite);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MediaPlayer player=MediaPlayer.create(getApplicationContext(), R.raw.background_music);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.animation_horse_splash);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        player.start();
        Handler handler = new Handler();
//        handler.postDelayed(begin0,0020);
//        handler.postDelayed(begin1,5020);
        handler.postDelayed(begin3,0020);
        handler.postDelayed(begin4,3020);

    }
//    private Runnable begin0 = new Runnable() {
//        @Override
//        public void run() {
//            try{
//                animationDrawable.start();
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    };
//    private Runnable begin1 = new Runnable() {
//        @Override
//        public void run() {
//            try{
//                animationDrawable.stop();
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    };
    private Runnable begin3 = new Runnable() {
        @Override
        public void run() {
            try{
                imageView.setImageResource(R.drawable.second);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                alphaAnimation.setDuration(3000);
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
                Intent intent = new Intent(InitiateActivity.this, IndexMainActivity.class);
                startActivity(intent);
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
