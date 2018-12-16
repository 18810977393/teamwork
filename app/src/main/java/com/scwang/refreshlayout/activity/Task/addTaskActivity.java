package com.scwang.refreshlayout.activity.Task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.scwang.refreshlayout.R;

public class addTaskActivity extends AppCompatActivity {
    private Spinner sp;
    private String str;
    private Toolbar mToolbar;
    private final int type = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        String[] ctype = new String[]{"1次", "2次","3次","4次","5次"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        Spinner spinner = (Spinner) super.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        sp = (Spinner) findViewById(R.id.spinner);
        str = (String) sp.getSelectedItem();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                str = (String) sp.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void cancel(View view) {
        finish();
    }

    public void addNote(View view) {
        String fileName = ((EditText)
                findViewById(R.id.noteTitle))
                .getText().toString();
        String scores = ((EditText) findViewById(R.id.pays))
                .getText().toString();
        String times ="0";
        switch (str)
        {
            case "1次":
                times = "1";
                break;
            case "2次":
                times = "2";
            case "3次":
                times = "3";
                break;
            case "4次":
                times = "4";
                break;
            case "5次":
                times = "5";
                break;
        }


        if (scores.equals("")||scores.equals(null))
            showAlertDialog("添加失败", "请输入成就点数");
        else
        {
            if (!isNum(scores))
            {
                showAlertDialog("添加失败", "成就点数为整数");
            }
            else
                try {
                    AVObject testObject = new AVObject(AVUser.getCurrentUser().getUsername());
                    testObject.put("Title",fileName);
                    testObject.put("Type",type);
                    testObject.put("Scores",scores);
                    testObject.put("Totaltime",times);
                    testObject.put("times",0);
                    testObject.put("status",true);
                    testObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null){
                                Log.d("saved","success!");
                            }
                        }
                    });
                    finish();
                }
                catch (Exception e) {
                    showAlertDialog("添加失败", "请输入任务名称");
                }
                finally {

                }
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new
                AlertDialog.Builder(addTaskActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialog.show();
    }
    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
}
