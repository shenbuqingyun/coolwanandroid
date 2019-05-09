package com.cool.wan.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cool.wan.CoolAndroidApplication;
import com.cool.wan.android.R;
import com.cool.wan.android.activity.WebViewActivity;
import com.cool.wan.android.adapter.HomeAdapter;
import com.cool.wan.android.bean.BannerBean;
import com.cool.wan.android.bean.HomeDetailBean;
import com.cool.wan.android.utils.GlideImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.particle.ParticleView;

import static com.cool.wan.android.utils.fonts.TypefaceHelper.typeface;

/**
 * 作者    cpf
 * 时间    2019/3/20
 * 文件    coolwanandroid
 * 描述
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private Context mContext;
    private List<String> titles;
    private int pageNum = 0;
    private Banner banner;
    private View inflate;
    private View headerBanner;
    private SmartRefreshLayout smartFreshLayout;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerviewHome;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, inflate);
        mContext = getActivity();
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(inflate);
        CoolAndroidApplication coolAndroidApplication = (CoolAndroidApplication) getActivity().getApplication();
        typeface(smartFreshLayout, coolAndroidApplication.getSystemItalicTypeface());
    }

    private void initView(View inflate) {
        ClassicsHeader header = inflate.findViewById(R.id.header_home);
        recyclerviewHome = inflate.findViewById(R.id.recyclerview_home);

        smartFreshLayout = inflate.findViewById(R.id.smartrefreshlayout);
        smartFreshLayout.autoRefresh();
        smartFreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                // 手指上滑 从下部加载更多
                refresh(refreshLayout, false);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                // 手指下滑 进行刷新
                refresh(refreshLayout, true);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewHome.setLayoutManager(linearLayoutManager);
        homeAdapter = new HomeAdapter(getActivity(), R.layout.home_adapter_item, null);
        headerBanner = LayoutInflater.from(mContext).inflate(R.layout.header_banner, smartFreshLayout, false);
        homeAdapter.addHeaderView(headerBanner);
        recyclerviewHome.setAdapter(homeAdapter);
    }

    //获取Banner数据
    private void getBanner() {
        titles = new ArrayList<>();
        final ArrayList<String> images = new ArrayList<>();
        String banner = "http://www.wanandroid.com/banner/json";
        OkGo.<String>get(banner)
                .tag("banner")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        BannerBean bean = JSONObject.parseObject(response.body(), BannerBean.class);
                        List<BannerBean.DataBean> data = bean.getData();
                        for (int i = 0; i < data.size(); i++) {
                            titles.add(data.get(i).getTitle());
                            images.add(data.get(i).getImagePath());
                        }
                        setBanner(headerBanner, images, titles, data);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });

    }

    private void setBanner(View view, ArrayList<String> images, List<String> data,
                           List<BannerBean.DataBean> dataBeans) {
        Banner bannerLayout = view.findViewById(R.id.banner);
        CoolAndroidApplication coolAndroidApplication = (CoolAndroidApplication) getActivity().getApplication();
        typeface(bannerLayout, coolAndroidApplication.getSystemItalicTypeface());

        banner = bannerLayout.setImages(images).setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(data);
        banner.start();

        banner.setOnBannerListener(position -> {
            switch (position) {

                default:
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("link", dataBeans.get(position).getUrl());
                    startActivity(intent);
                    break;
            }

        });

    }

    // 获取首页展示数据
    public void getHomeData() {
        String url = "http://www.wanandroid.com/article/list/" + 0 + "/json";
        OkGo.<String>get(url)
                .tag("home")
                .execute(new StringCallback() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeDetailBean homeDetailBean = JSONObject.parseObject(response.body(), HomeDetailBean.class);
                        if (homeDetailBean.getErrorCode() == 0) {
                            homeAdapter.setNewData(homeDetailBean.getData().getDatas()); //首页列表使用的适配器在此填充数据
                            smartFreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.i(TAG, "onError: " + response.toString());

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }


    private void refresh(RefreshLayout refreshlayout, final boolean isFresh) {

        refreshlayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFresh) {
                    getBanner();
                    getHomeData();
                } else {
                    getMoreHomeData();
                }
            }
        }, 200);
    }

    // 手指上滑 加载更多内容
    private void getMoreHomeData() {
        pageNum++;
        String url = "http://www.wanandroid.com/article/list/" + pageNum + "/json";
        OkGo.<String>get(url)
                .tag("home1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomeDetailBean homeDetailBean = JSONObject.parseObject(response.body(), HomeDetailBean.class);
                        homeAdapter.addData(homeDetailBean.getData().getDatas());
                        smartFreshLayout.finishLoadmore();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.i(TAG, "onError: " + response.toString());
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}