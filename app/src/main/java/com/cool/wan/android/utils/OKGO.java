package com.cool.wan.android.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

public class OKGO {

    public static void get(String url, String tag, StringCallback callback) {
        OkGo.<String>get(url)
                .tag(tag)
                .execute(callback);
    }

}
