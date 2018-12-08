package com.scwang.refreshlayout.activity.style;

import android.app.AlertDialog;
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

import java.io.File;
import java.io.PrintWriter;

public class addAwardActivity extends AppCompatActivity {
    private Spinner  sp;
    private String str;
    private Toolbar mToolbar;
    private final int type = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_award);

        String[] ctype = new String[]{"单次", "无限"};
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
        String times ;
       if (str.equals("无限"))
       {
           times = "∞";
       }
       else
           {
               times = String.valueOf(1);
           }


        File parent = getFilesDir();

        String path= parent.getAbsolutePath();
        String name="Award";//你要新建的文件夹名或者文件名
        String pathx=path+name;
        File file=new File(pathx);
        boolean is=file.exists();//判断文件（夹）是否存在
        if(!is) {
            file.mkdir();//创建文件夹
        }
        File file0 = new File(file,fileName);

        PrintWriter writer = null;
        if (scores.equals("")||scores.equals(null))
            showAlertDialog("添加失败", "请输入耗费成就点数");
        else
            {
                if (!isNum(scores))
                {
                    showAlertDialog("添加失败", "耗费成就点数为整数");
                }
                else
                    try {
                        writer = new PrintWriter(file0);
                        writer.write(fileName + "|"+ scores + "|" + times);

                        //Award award = new Award(fileName,scores,times,0,type)
                        AVObject testObject = new AVObject(AVUser.getCurrentUser().getUsername());
                        testObject.put("Title",fileName);
                        testObject.put("Type",type);
                        testObject.put("Scores",scores);
                        testObject.put("Totaltime",times);
                        testObject.put("times",0);
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
                    showAlertDialog("添加失败", "请输入奖励名称");
                }
                finally {
                    if (writer != null) {
                        try { writer.close();
                        }
                        catch (Exception e) { }
                    }
                }
            }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new
                AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();

    }
    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
}
