package com.cool.wan.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cool.wan.CoolAndroidApplication;
import com.cool.wan.android.R;
import com.cool.wan.android.adapter.KnowAdapter;
import com.cool.wan.android.bean.KnowBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cool.wan.android.utils.fonts.TypefaceHelper.typeface;

/**
 * 作者    cpf
 * 时间    2019/3/20
 * 文件    coolwanandroid
 * 描述
 */
public class KnowedgeFragment extends Fragment {

    private Context mContext;
    private View inflate;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.recyclerview_know)
    RecyclerView recyclerview;
    private KnowAdapter adapter;
    private View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_knowedge, container, false);
        ButterKnife.bind(this, inflate);
        this.mContext = getActivity();
        initView(inflate);
        CoolAndroidApplication coolAndroidApplication = (CoolAndroidApplication) getActivity().getApplication();
        typeface(smartrefreshlayout, coolAndroidApplication.getSystemItalicTypeface());
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View inflate) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new KnowAdapter(R.layout.item_know, null);
        recyclerview.setAdapter(adapter);
        footer = View.inflate(getActivity(), R.layout.know_footer, null);

        smartrefreshlayout.autoRefresh();
        smartrefreshlayout.setEnableLoadMore(false);
        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refresh(refreshLayout);
            }
        });
    }

    private void refresh(RefreshLayout refreshLayout) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                getKnowData();
            }
        }, 2000);
    }

    private void getKnowData() {
        String url = "http://www.wanandroid.com/tree/json";
        OkGo.<String>get(url)
                .tag("tixi")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        KnowBean tiXiBean = com.alibaba.fastjson.JSONObject.parseObject(response.body(), KnowBean.class);
                        List<KnowBean.DataBean> data = tiXiBean.getData();
                        adapter.setNewData(data);
                        smartrefreshlayout.finishRefresh();

                        LinearLayout footerLayout = adapter.getFooterLayout();
                        if (footerLayout == null) {
                            adapter.setFooterView(footer);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();
    }
}