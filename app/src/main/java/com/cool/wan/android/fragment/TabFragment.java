package com.cool.wan.android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cool.wan.CoolAndroidApplication;
import com.cool.wan.android.R;
import com.cool.wan.android.activity.WebViewActivity;
import com.cool.wan.android.bean.ProjectBean;
import com.cool.wan.android.bean.ProjectListBean;
import com.cool.wan.android.utils.OKGO;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import static com.cool.wan.android.utils.fonts.TypefaceHelper.typeface;

/**
 * 作者    cpf
 * 时间    2019/5/16
 * 文件    coolwanandroid
 * 描述
 */
@SuppressLint("ValidFragment")
public class TabFragment extends BaseFragment {

    private static final String TAG = TabFragment.class.getSimpleName();
    private ProjectBean.DataBean mDatas;
    private TextView content;
    private View parentView;
    private SmartRefreshLayout smartrefreshlayout;
    private RecyclerView recyclerview;
    private Context mContext;
    private TabAdapter adapter;
    private LinearLayout vierGroup;

    public TabFragment(ProjectBean.DataBean dataBean) {
        this.mDatas = dataBean;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (parentView == null) {
            parentView = inflater.inflate(R.layout.fragment_tab_fragment_new, container, false);
            //在这里做一些初始化处理
            initChoiceLayout();
            Log.d(TAG, "onCreateView: 1");
        } else {
            ViewGroup viewGroup = (ViewGroup) parentView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(parentView);
            Log.d(TAG, "onCreateView: 2");
        }

        CoolAndroidApplication coolAndroidApplication = (CoolAndroidApplication) getActivity().getApplication();
        typeface(smartrefreshlayout, coolAndroidApplication.getSystemItalicTypeface());
        return parentView;
    }

    private void initChoiceLayout() {
        vierGroup = parentView.findViewById(R.id.rootview);
        smartrefreshlayout = parentView.findViewById(R.id.smartrefreshlayout);
        recyclerview = parentView.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(linearLayoutManager);

        adapter = new TabAdapter(R.layout.home_adapter_item);
        recyclerview.setAdapter(adapter);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayouId() {
        return R.layout.fragment_tab_fragment;
    }


    @Override
    protected void loadData() {
        getDataList(mDatas.getId());
    }

    private void getDataList(int id) {

        String url = "http://www.wanandroid.com/project/list/1/json?cid=" + id;

        OKGO.get(url, "123", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                ProjectListBean projectListBean = JSONObject.parseObject(body, ProjectListBean.class);
                if (projectListBean.getErrorCode() == 0) {
                    adapter.addData(projectListBean.getData().getDatas());
                } else {
                    Log.i(TAG, "onSuccess: failed");
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);

            }
        });
    }

    private class TabAdapter extends BaseQuickAdapter<ProjectListBean.DataBean.DatasBean, BaseViewHolder> {

        public TabAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder holder, ProjectListBean.DataBean.DatasBean item) {
            holder.setText(R.id.tv_item_title, item.getTitle());
            holder.setText(R.id.tv_itemt_desc, item.getDesc());
            holder.setText(R.id.tv_item_author_name, item.getAuthor());
            holder.setText(R.id.tv_item_time_, item.getNiceDate());
            holder.getView(R.id.rl_rootview).setOnClickListener(v -> {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("link", item.getLink());
                mContext.startActivity(intent);
            });
        }
    }
}
