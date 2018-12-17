package com.scwang.refreshlayout.activity.Task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.Mine.LoginActivity;
import com.scwang.refreshlayout.activity.Mine.RegisterActivity;
import com.scwang.refreshlayout.util.StatusBarUtil;

import org.w3c.dom.Text;

/**
 * 个人中心
 */
public class ProfilePracticeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_profile);

        final Toolbar toolbar = findViewById(R.id.toolbar);

        final TextView textView = (TextView)findViewById(R.id.user_names);

        final Button button = (Button) findViewById(R.id.fix_name);
        final Button button2 = (Button) findViewById(R.id.QianMing);

        button.setText(AVUser.getCurrentUser().getUsername());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.profile));
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.blurView));

        Button mLogOutButton = (Button) findViewById(R.id.logout);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();
                startActivity(new Intent(ProfilePracticeActivity.this, LoginActivity.class));
                ProfilePracticeActivity.this.finish();
            }
        });
    }

    public void alertDialog(){
        View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
        final EditText editText = (EditText) view.findViewById(R.id.usernames);
        final TextView textView = (TextView)findViewById(R.id.user_names);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.heart)//设置标题的图片
                .setTitle("修改内容")//设置对话框的标题
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editText.getText().toString();
                        Toast.makeText(ProfilePracticeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        textView.setText(content);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }
    public void alertDialog2(){
        View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
        final EditText editText = (EditText) view.findViewById(R.id.usernames);
        final TextView textView2 = (TextView)findViewById(R.id.SignName);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.heart)//设置标题的图片
                .setTitle("修改内容")//设置对话框的标题
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editText.getText().toString();
                        Toast.makeText(ProfilePracticeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        textView2.setText(content);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }

}
