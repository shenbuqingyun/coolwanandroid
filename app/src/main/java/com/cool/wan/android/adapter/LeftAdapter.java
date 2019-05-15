package com.cool.wan.android.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cool.wan.android.R;
import com.cool.wan.android.bean.GuideBean;

import java.util.ArrayList;

public class LeftAdapter extends BaseQuickAdapter<GuideBean.DataBean, BaseViewHolder> {
    private ArrayList<Boolean> selected = new ArrayList<>();

    public ArrayList<Boolean> getSelected() {
        return selected;
    }

    public LeftAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, GuideBean.DataBean item) {
        holder.setText(R.id.tv_title, item.getName());
        holder.getView(R.id.view).setSelected(selected.get(holder.getLayoutPosition()));
    }

    public void setSelect(ArrayList<Boolean> booleans) {
        this.selected = booleans;
        notifyDataSetChanged();
    }
}
