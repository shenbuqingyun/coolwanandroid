package com.cool.wan.android.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cool.wan.android.R;
import com.cool.wan.android.activity.TagDetailActivity;
import com.cool.wan.android.bean.KnowBean;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者    cpf
 * 时间    2019/5/10
 * 文件    coolwanandroid
 * 描述
 */
public class KnowAdapter extends BaseQuickAdapter<KnowBean.DataBean, BaseViewHolder> {


    public KnowAdapter(int layoutResId, @Nullable List<KnowBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, KnowBean.DataBean item) {
        holder.setText(R.id.tv_item_know, item.getName());
        List<KnowBean.DataBean.ChildrenBean> children = item.getChildren();
        TagFlowLayout tfl = holder.getView(R.id.tfl);
        ArrayList<String> mVals = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            mVals.add(children.get(i).getName());
        }

        tfl.setAdapter(new TagAdapter<String>(mVals) {
            @SuppressLint("ResourceType")
            @Override
            public View getView(com.zhy.view.flowlayout.FlowLayout parent, int position, String s) {
                Random random = new Random();
                int r = random.nextInt(150);
                int g = random.nextInt(150);
                int b = random.nextInt(150);

                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tfl, tfl, false);
                tv.setTextColor(Color.rgb(r, g, b));
                tv.setText(s);
                return tv;
            }
        });

        tfl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, com.zhy.view.flowlayout.FlowLayout parent) {
                Log.i(TAG, "onTagClick: id -->" + item.getChildren().get(position).getId());
                Intent intent = new Intent(mContext, TagDetailActivity.class);
                intent.putExtra("childbean", item.getChildren().get(position));
                mContext.startActivity(intent);
                return false;
            }
        });
    }
}
