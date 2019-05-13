package com.cool.wan.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cool.wan.CoolAndroidApplication;
import com.cool.wan.android.R;
import com.cool.wan.android.bean.KnowBean;
import com.cool.wan.android.bean.TagDetailBean;
import com.cool.wan.android.utils.OKGO;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import static com.cool.wan.android.utils.fonts.TypefaceHelper.typeface;

public class TagDetailActivity extends AppCompatActivity {

    private static final String TAG = "TagDetailActivity";
    /*帧动画实现 每个颜色作为一帧*/
    AnimationDrawable anim;
    private Context mContext;
    private SmartRefreshLayout smartrefreshlayout;
    private RecyclerView recyclerview;
    private KnowBean.DataBean.ChildrenBean childbean;
    private int pageNum = 0;
    private int cid = -1;
    private DetailAdapter adapter;
    private TextView title;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        childbean = (KnowBean.DataBean.ChildrenBean) getIntent().getSerializableExtra("childbean");
        cid = childbean.getId();
        Log.i(TAG, "onCreate:  " + childbean.getId() + ",titel" + childbean.getName());
        this.mContext = TagDetailActivity.this;
        
        LinearLayout container = findViewById(R.id.container);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(3000);
        anim.setExitFadeDuration(1000);
        
        initView();
        typeface(this, CoolAndroidApplication.getInstance().getSystemItalicTypeface());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
    }

    private void initView() {
        recyclerview = findViewById(R.id.recyclerview_tag_detail);
        smartrefreshlayout = findViewById(R.id.smartrefreshlayout);
        title = findViewById(R.id.tv_title);
        title.setText(childbean.getName());
        back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(linearLayoutManager);


        smartrefreshlayout.autoRefresh();
        smartrefreshlayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refresh(refreshLayout, false);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refresh(refreshLayout, true);
            }
        });

    }

    private void refresh(RefreshLayout refreshLayout, boolean b) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    getData();
                } else {
                    loadMore();
                }
            }
        }, 2000);
    }

    private void loadMore() {
        pageNum++;
        String url = "http://www.wanandroid.com/article/list/" + pageNum + "/json?cid=" + cid;
        OKGO.get(url, "tagdetail", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                TagDetailBean tagDetailBean = JSONObject.parseObject(response.body(), TagDetailBean.class);
                if (tagDetailBean != null) {
                    if (tagDetailBean.getData().getDatas().size() > 0) {
                        List<TagDetailBean.DataBean.DatasBean> datas = tagDetailBean.getData().getDatas();
                        adapter.addData(datas);
                    } else {
                        View footer = View.inflate(mContext, R.layout.know_footer, null);
                        if (adapter.getFooterLayout() == null) {
                            adapter.addFooterView(footer);
                            smartrefreshlayout.setEnableLoadMore(false);
                        }
                    }
                    smartrefreshlayout.finishLoadMore();
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

    private void getData() {
        pageNum = 0;
        String url = "http://www.wanandroid.com/article/list/" + pageNum + "/json?cid=" + cid;
        OKGO.get(url, "tagdetail", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                TagDetailBean tagDetailBean = JSONObject.parseObject(response.body(), TagDetailBean.class);
                List<TagDetailBean.DataBean.DatasBean> datas = tagDetailBean.getData().getDatas();
                adapter = new DetailAdapter(R.layout.home_adapter_item, datas);
                recyclerview.setAdapter(adapter);
                smartrefreshlayout.setEnableLoadMore(true);
                smartrefreshlayout.finishRefresh();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                showToast(response.body());
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG, "onFinish: ");
            }
        });
    }

    private class DetailAdapter extends BaseQuickAdapter<TagDetailBean.DataBean.DatasBean, BaseViewHolder> {

        public DetailAdapter(int layoutResId, @Nullable List<TagDetailBean.DataBean.DatasBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, TagDetailBean.DataBean.DatasBean item) {
            holder.setText(R.id.tv_item_title, item.getTitle().trim());
            holder.setText(R.id.tv_itemt_desc, item.getDesc());
            holder.setText(R.id.tv_item_author_name, item.getAuthor());
            holder.setText(R.id.tv_item_time_, item.getNiceDate());
            holder.setText(R.id.tv_chaptername, item.getChapterName());

            holder.getView(R.id.rl_rootview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("link", item.getLink());
                    startActivity(intent);
                }
            });


        }
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }
}
