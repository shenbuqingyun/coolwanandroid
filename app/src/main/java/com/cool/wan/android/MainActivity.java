package com.cool.wan.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cool.wan.CoolAndroidApplication;
import com.cool.wan.android.fragment.GuideFragment;
import com.cool.wan.android.fragment.HomeFragment;
import com.cool.wan.android.fragment.KnowedgeFragment;
import com.cool.wan.android.fragment.ProjectFragment;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.particle.ParticleView;

import static com.cool.wan.android.utils.fonts.TypefaceHelper.typeface;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.particleView)
    ParticleView particleView;
    @BindView(R.id.vp_home)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initNavigationBar();
        initViewPager();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);

        CoolAndroidApplication coolAndroidApplication = (CoolAndroidApplication)getApplication();
        typeface(this);
    }

    private void initNavigationBar() {
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.activity_home, R.string.activity_main_home))
                .addItem(new BottomNavigationItem(R.drawable.activity_know, R.string.activity_main_know))
                .addItem(new BottomNavigationItem(R.drawable.activity_guide, R.string.activity_main_guide))
                .addItem(new BottomNavigationItem(R.drawable.activity_project, R.string.activity_main_project))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position); //导航动 页面跟着动
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void initViewPager() {
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new KnowedgeFragment());
        mFragmentList.add(new GuideFragment());
        mFragmentList.add(new ProjectFragment());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);//页面移动 导航跟着动
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        particleView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        particleView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
