package com.scwang.refreshlayout.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.IndexMainActivity;
import com.scwang.refreshlayout.activity.Mine.LoginActivity;

public class ContentFragment extends Fragment {
    private int[] bgp = {R.drawable.begin1,R.drawable.begin2,R.drawable.begin3};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,null);
        Button btn = (Button)view.findViewById(R.id.bt_joinBtn);
        RelativeLayout relativeLayout =(RelativeLayout) view.findViewById(R.id.rl_fgLayout);

        int index = getArguments().getInt("index");
        relativeLayout.setBackgroundResource(bgp[index]);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IndexMainActivity.class));
            }
        });

        btn.setVisibility(index == 2? View.VISIBLE:View.GONE);

        return view;
    }
}
