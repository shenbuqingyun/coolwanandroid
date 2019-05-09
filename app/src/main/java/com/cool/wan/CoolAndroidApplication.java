package com.cool.wan;

import android.app.Application;
import android.graphics.Typeface;

import com.cool.wan.android.utils.fonts.TypefaceCollection;
import com.cool.wan.android.utils.fonts.TypefaceHelper;

/**
 * 作者    cpf
 * 时间    2019/5/9
 * 文件    coolwanandroid
 * 描述    初始化字体
 */
public class CoolAndroidApplication extends Application {

    private static CoolAndroidApplication calcApplication ;

    public static CoolAndroidApplication getInstance() {
        return calcApplication ;
    }

    private TypefaceCollection mSystemItalicTypeface;

    @Override
    public void onCreate() {
        super.onCreate();
        calcApplication = this;

        TypefaceHelper.init(new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf"))
                .create());

        mSystemItalicTypeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/Roboto-MediumItalic.ttf"))
                .create();

    }

    public TypefaceCollection getSystemItalicTypeface() {
        return mSystemItalicTypeface;
    }
}
