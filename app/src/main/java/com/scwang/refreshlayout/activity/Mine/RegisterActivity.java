package com.scwang.refreshlayout.activity.Mine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.scwang.refreshlayout.R;

public class RegisterActivity extends AppCompatActivity {
  private EditText mUsernameView;
  private EditText mPasswordView;
  private View mRegisterFormView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register2);

    //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Set up the register form.
    mUsernameView = (EditText) findViewById(R.id.username);

    mPasswordView = (EditText) findViewById(R.id.password);
    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.register || id == EditorInfo.IME_NULL) {
          attemptRegister();
          return true;
        }
        return false;
      }
    });

    Button musernameSignInButton = (Button) findViewById(R.id.username_register_button);
    musernameSignInButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptRegister();
      }
    });

    mRegisterFormView = findViewById(R.id.register_form);

  }

  private void attemptRegister() {
    mUsernameView.setError(null);
    mPasswordView.setError(null);

    String username = mUsernameView.getText().toString();
    String password = mPasswordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
      mPasswordView.setError("密码不能少于4位");
      focusView = mPasswordView;
      cancel = true;
    }

    if (TextUtils.isEmpty(username)) {
      mUsernameView.setError("这个是必填项");
      focusView = mUsernameView;
      cancel = true;
    }

    if (cancel) {
      focusView.requestFocus();
    } else {
      showProgress(true);

      AVUser user = new AVUser();// 新建 AVUser 对象实例
      user.setUsername(username);// 设置用户名
      user.setPassword(password);// 设置密码
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(AVException e) {
          if (e == null) {
            // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
            //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            RegisterActivity.this.finish();
          } else {
            // 失败的原因可能有多种，常见的是用户名已经存在。
            showProgress(false);
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  private boolean isusernameValid(String username) {
    //TODO: Replace this with your own logic
    return username.contains("@");
  }

  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() > 4;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
              show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

    }
  }
}

