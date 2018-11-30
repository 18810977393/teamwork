package com.scwang.refreshlayout.activity.style;

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
import android.widget.TextView;

import com.scwang.refreshlayout.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AwardActivity extends AppCompatActivity {
    private String selectedItem;
    private Toolbar mToolbar;
    private RefreshLayout mRefreshLayout;
    private static boolean isFirstEnter = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AwardActivity.this, addAwardActivity.class));
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteNote();
            }
        });

        ListView listView = (ListView) findViewById(
                R.id.listView1);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int position, long id) {
                        readNote(position);
                    }
                });
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout = findViewById(R.id.refreshLayout);
        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
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
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, titles);
        listView.setAdapter(arrayAdapter);
    }


    private void readNote(int position) {
        String[] titles = fileList();
        if (titles.length > position) {
            selectedItem = titles[position];
            File dir = getFilesDir();
            File file = new File(dir, selectedItem);
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                StringBuilder sb = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    sb.append(line);
                    line = bufferedReader.readLine();
                }
                ((TextView) findViewById(R.id.textView1)).
                        setText(sb.toString());
            } catch (IOException e) {

            } finally {
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
    }

    private void deleteNote() {
        if (selectedItem != null) {
            deleteFile(selectedItem);
            selectedItem = null;
            ((TextView) findViewById(R.id.textView1)).setText("");
            refreshList();
        }
    }
}
