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
    private int[] bgp = {R.drawable.raw_5_ys,R.drawable.raw_6_ys,R.drawable.raw_7_ys,R.drawable.raw_8_ys};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,null);
        Button skip_index1 = (Button)view.findViewById(R.id.skip_index1);
        Button skip_index2 = (Button)view.findViewById(R.id.skip_index2);
        Button skip_index3 = (Button)view.findViewById(R.id.skip_index3);
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
        skip_index1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IndexMainActivity.class));
            }
        });
        skip_index2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IndexMainActivity.class));
            }
        });
        skip_index3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IndexMainActivity.class));
            }
        });
        skip_index1.setVisibility(index == 0 ? View.VISIBLE:View.GONE);
        skip_index2.setVisibility(index == 1 ? View.VISIBLE:View.GONE);
        skip_index3.setVisibility(index == 2 ? View.VISIBLE:View.GONE);
        btn.setVisibility(index == 3? View.VISIBLE:View.GONE);

        return view;
    }
}
