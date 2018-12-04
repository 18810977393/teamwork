package com.scwang.refreshlayout.activity.style;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.Sorting;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class AwardActivity extends AppCompatActivity {
    private String selectedItem;

    private RefreshLayout mRefreshLayout;
    private static boolean isFirstEnter = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        Button button1 = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AwardActivity.this, addAwardActivity.class));
            }
        });
        ListView listView = (ListView) findViewById(
                R.id.listView1);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, final int position, long id) {
                        final int index = position;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AwardActivity.this);
                        dialog.setTitle("满足奖励");
                        dialog.setMessage("花费成就点数来满足奖励");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] titles = fileList();
                                if (titles.length >index) {
                                    selectedItem = titles[index];
                                    deleteNote();
                                }
                            }
                        });
                           dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                               }
                           });
                           dialog.show();
                    }
                });

        mRefreshLayout = findViewById(R.id.refreshLayout);
        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.award_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(AwardActivity.this, addAwardActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        ListView listView = (ListView) findViewById(
                R.id.listView1);
        String[] titles = fileList();
        Award[] awards = new Award[titles.length];

        for (int i=0;i<titles.length;i++)
        {
            File dir = getFilesDir();
            File file = new File(dir,titles[i]);
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                StringBuilder sb = new StringBuilder();
                String line = bufferedReader.readLine()+" ";
                while (line != null) {
                    sb.append(line);
                    line = bufferedReader.readLine();
                }
               titles[i] +=" "+sb.toString();//将待显示的文字改为Award中的标题+成就点数+次数

            } catch (IOException e) {
                }
                finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        // 对其排序
        for (int j=0;j<titles.length;j++)
        {
            awards[j] = transferAward(titles[j]);
        }
        Sorting.shellSort(awards);
        for (int i=0;i<awards.length;i++)
        {
            titles[i] = awards[i].toString();//awards[i].getName()+"             "+"-"+awards[i].getScore()+"/次（"+awards[i].getTimes()+"次)";
        }
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, titles);
        listView.setAdapter(arrayAdapter);
    }

    private void deleteNote() {
        if (selectedItem != null) {
            deleteFile(selectedItem);
            selectedItem = null;
            refreshList();
        }
    }

    private Award transferAward(String x)
    {

        StringTokenizer stringTokenizer = new StringTokenizer(x);
        if (stringTokenizer.countTokens()==3)
        {
            Award award = new Award(stringTokenizer.nextToken(),Integer.parseInt(stringTokenizer.nextToken()),Integer.parseInt(stringTokenizer.nextToken()));
            return  award;
        }
        else
            return null;

    }
}

