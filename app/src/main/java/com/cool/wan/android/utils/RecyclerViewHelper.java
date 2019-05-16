package com.cool.wan.android.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 作者    cpf
 * 时间    2019/5/16
 * 文件    coolwanandroid
 * 描述
 */
public class RecyclerViewHelper {

    public static void autoOffsetPosition(RecyclerView recyclerView, int position) {

        try {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            int itemCount = adapter.getItemCount();

            boolean isFirst = firstCompletelyVisibleItemPosition == 0;
            boolean isLast = lastCompletelyVisibleItemPosition == itemCount - 1;

            boolean isFirstVisible = firstVisibleItemPosition == position
                    || firstCompletelyVisibleItemPosition == position;//最前一个 或者完整显示的一个
            boolean isLastVisible = lastVisibleItemPosition == position
                    || lastCompletelyVisibleItemPosition == position;//最后一个 或者完整显示的一个

            if(!isFirst && isFirstVisible) {
                int target = position - 1;
                recyclerView.smoothScrollToPosition(target < 0 ? 0: target);
            }
            else if(!isLast && isLastVisible) {
                int target = position + 1;
                recyclerView.smoothScrollToPosition(target > itemCount-1 ? itemCount-1: target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

