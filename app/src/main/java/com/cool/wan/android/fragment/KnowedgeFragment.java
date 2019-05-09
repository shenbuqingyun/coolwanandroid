package com.cool.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cool.wan.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者    cpf
 * 时间    2019/3/20
 * 文件    coolwanandroid
 * 描述
 */
public class KnowedgeFragment extends Fragment {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_knowedge, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}