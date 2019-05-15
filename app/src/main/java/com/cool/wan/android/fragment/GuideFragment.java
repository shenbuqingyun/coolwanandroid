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
 *         ② 使用双列表联动展示数据。并且完善逻辑：
 *         右边列表上下滑动，带动左边列表被选中状态改变；
 *         左边列表触发点击事件，右边列表自动跳到对应位置
 *         右边列表点击，要能进行跳转，展示数据
 */
public class GuideFragment extends Fragment {

    @BindView(R.id.recyclervie_left)
    RecyclerView rv1;
    @BindView(R.id.recyclervie_right)
    RecyclerView rv2;
    private Context mContext;
    private View inflate;
    private LeftAdapter leftAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate  = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.bind(this,inflate);
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

                    List<GuideBean.DataBean> data = resultBean.getData();
                    ArrayList<Boolean> booleans = new ArrayList<>();
                    booleans.add(true);
                    for (int i = 1; i < data.size(); i++) {
                        booleans.add(false);
                    }
                    leftAdapter = new LeftAdapter(R.layout.item_text);
                    rv1.setAdapter(leftAdapter);
                    leftAdapter.setNewData(data);
                    leftAdapter.setSelect(booleans);
                    leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            setSelected(adapter, position);
                            RightAdapter rightAdapter = new RightAdapter(R.layout.item_tixi);
                            rv2.setAdapter(rightAdapter);
                            if (data != null) {
                                rightAdapter.setNewData(data);
                            }
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