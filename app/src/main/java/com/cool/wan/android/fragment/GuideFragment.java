package com.cool.wan.android.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cool.wan.android.R;
import com.cool.wan.android.adapter.LeftAdapter;
import com.cool.wan.android.adapter.RightAdapter;
import com.cool.wan.android.bean.GuideBean;
import com.cool.wan.android.utils.Constants;
import com.cool.wan.android.utils.OKGO;
import com.cool.wan.android.utils.RecyclerViewHelper;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者    cpf
 * 时间    2019/3/20
 * 文件    coolwanandroid
 * 描述    ① 使用BRVAHRecyclerView列表开源项目装载数据；
 * ② 使用双列表联动展示数据。并且完善逻辑：
 * 右边列表上下滑动，带动左边列表被选中状态改变；
 * 左边列表触发点击事件，右边列表自动跳到对应位置
 * 右边列表点击，要能进行跳转，展示数据
 */
public class GuideFragment extends Fragment {
    private static final String TAG = "GuideFragment";
    @BindView(R.id.recyclervie_left)
    RecyclerView rv1;
    @BindView(R.id.recyclervie_right)
    RecyclerView rv2;
    private Context mContext;
    private View inflate;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private List<GuideBean.DataBean> dataBeanList;
    private List<GuideBean.DataBean.ArticlesBean> articlesBeanList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.bind(this, inflate);
        this.mContext = getActivity();
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        OKGO.get(Constants.NAVIURL, "navifragment", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                GuideBean resultBean = JSONObject.parseObject(response.body(), GuideBean.class);
                int errorCode = resultBean.getErrorCode();
                if (errorCode == 0) {
                    articlesBeanList = new ArrayList<>();
                    dataBeanList = resultBean.getData();

                    for (int i = 0; i < dataBeanList.size(); i++) {
                        articlesBeanList.addAll(dataBeanList.get(i).getArticles());
                    }

                    leftAdapter = new LeftAdapter(R.layout.item_text);
                    rv1.setAdapter(leftAdapter);
                    leftAdapter.setNewData(dataBeanList);

                    rightAdapter = new RightAdapter(R.layout.item_tixi);
                    rv2.setAdapter(rightAdapter);
                    if (dataBeanList != null) {
                        rightAdapter.setNewData(dataBeanList);
                    }

                    // 每次进入导航页 定位到上次浏览的地方，包括从WebViewActivity中返回时
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    int last_position = sharedPreferences.getInt("last_position", 2);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rv2.getLayoutManager();
                    layoutManager.scrollToPositionWithOffset(last_position, 0); // 右侧列表定位
                    leftAdapter.setSelection(last_position); // 左侧列表定位

                    leftAdapter.setOnItemClickListener((adapter, view, position) -> {
                        leftAdapter.setSelection(position);
                        RecyclerViewHelper.autoOffsetPosition(rv1,position);
                        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
                        rv1.setOnFlingListener(null); // 纠错java.lang.IllegalStateException: An instance of OnFlingListener already set.
                        mLinearSnapHelper.attachToRecyclerView(rv1);
                        //根据左侧，定位右侧的展示数据
                        layoutManager.scrollToPositionWithOffset(position, 0); // 定位到某个item,并将其置顶显示

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("last_position", position).apply();
                    });


                    rv2.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            //获取滚动时的第一条展示的position
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                            int position = 0;
                            for (int i = 0; i < firstVisibleItemPosition; i++) {
                                position += dataBeanList.get(i).getArticles().size();
                            }

                            //获取右侧数据的关联id
                            GuideBean.DataBean.ArticlesBean articlesBean = articlesBeanList.get(position);
                            int outId = articlesBean.getChapterId();
                            //记录外部id， 更新左侧状态栏状态
                            int pos = 0;
                            for (int i = 0; i < dataBeanList.size(); i++) {
                                int id = dataBeanList.get(i).getCid();
                                if ((outId == id)) {
                                    pos = i;
                                }
                            }
                            leftAdapter.setSelection(pos);
                            rv1.smoothScrollToPosition(pos);
                        }
                    });
                } else {
                    showToast(resultBean.getErrorMsg());
                }
            }

            private void setSelected(BaseQuickAdapter adapter, int position) {
                for (int i = 0; i < adapter.getData().size(); i++) {
                    if (i == position) {
                        leftAdapter.getSelected().set(i, true);
                    } else {
                        leftAdapter.getSelected().set(i, false);
                    }
                    leftAdapter.notifyDataSetChanged();
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

    private void showToast(String body) {
        Toast.makeText(getActivity(), body + "", Toast.LENGTH_SHORT).show();
    }
}