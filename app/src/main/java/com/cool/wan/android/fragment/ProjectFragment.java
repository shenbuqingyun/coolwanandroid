package com.cool.wan.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.cool.wan.android.R;
import com.cool.wan.android.bean.ProjectBean;
import com.cool.wan.android.utils.Constants;
import com.cool.wan.android.utils.OKGO;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cool.wan.android.utils.fonts.TypefaceHelper.typeface;

/**
 * 作者    cpf
 * 时间    2019/3/20
 * 文件    coolwanandroid
 * 描述    ProjectFragment = Tablayout + ViewPager
 *         ViewPager = 多个TabFragment
 *         实际内容展示使用的是TabFragment，非常经典的架构
 */
public class ProjectFragment extends Fragment {
    private static final String TAG = "ProjectFragment";
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private VPAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 在使用官方BottomNavigationBar时，由于刷新机制的存在，需要加入以下代码，保证每次都能展示布局
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_project, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null)
                viewGroup.removeView(view);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        typeface(tablayout);
    }

    private void initData() {
        OKGO.get(Constants.PROJECT_TREE, "project", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                ProjectBean projectBean = JSONObject.parseObject(body, ProjectBean.class);
                if (projectBean.getErrorCode() == 0) {
                    List<ProjectBean.DataBean> data = projectBean.getData();
                    ArrayList<TabFragment> tabFragments = new ArrayList<>();
                    ArrayList<String> titles = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        tabFragments.add(new TabFragment(data.get(i)));
                        titles.add(data.get(i).getName());
                    }
                    if (adapter == null) {
                        adapter = new VPAdapter(getChildFragmentManager(), tabFragments, titles);
                        viewPager.setAdapter(adapter);
                        tablayout.setupWithViewPager(viewPager, false);
                    }

                } else {
                    Log.d(TAG, "onSuccess: " + "failed");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);

            }
        });
    }

    private class VPAdapter extends FragmentPagerAdapter {
        ArrayList<TabFragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitles = new ArrayList<>();

        VPAdapter(FragmentManager supportFragmentManager,
                  ArrayList<TabFragment> tabFragments, ArrayList<String> data) {
            super(supportFragmentManager);
            this.mFragments = tabFragments;
            this.mTitles = data;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}