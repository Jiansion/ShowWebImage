package com.conch.showwebimage.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.conch.showwebimage.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Author : Jiansion
 * Create : 2017/7/10.
 * Description : 可以点击放大和保存网页图片的 WebView
 * Email : amazeconch@gmail.com
 */

public class ShowImageWebView extends WebView {

    private final static String TAG = ShowImageWebView.class.getName();
    // 用于匹配 img 标签的正则
    private final static String IMAGE_TAG = "<img.*src=(.*?)[^>]>";
    // 匹配 img 标签内的 src 路径
    private final static String IMAGE_URL = "http:\"?(.*?)(\"|>|\\s+)";

    //拥有存放该网页中的所有 图片路径
    private List<String> urlList = new ArrayList<>();

    private Context context;


    private Handler mHandler = new Handler();


    public ShowImageWebView(Context context) {
        this(context, null);
    }

    public ShowImageWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowImageWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 初始化动作
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void init(Context context) {
        this.context = context;
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDefaultTextEncodingName("UTF-8");

        //载入 JS
        addJavascriptInterface(new OnShowImageInterface(), "imageListener");

        addJavascriptInterface(new OnParseHtmlInterface(), "local_obj");

    }

    private class OnShowImageInterface {
//        private Context context;


//        public OnShowImageInterface(Context context) {
//            this.context = context;
//        }

        @JavascriptInterface
        public void onShowImageDialog(final String url) {
            //显示图片的 Dialog , Dialog 中加载 ViewPage 用于存放图片集合
            Log.e(TAG, "onShowImageDialog:---> " + url);
//            Toast.makeText(context, "onShowImageDialog", Toast.LENGTH_SHORT).show();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showDialog(url);
                }
            });
        }
    }

    private class OnParseHtmlInterface {
        @JavascriptInterface
        public void parseHtml(String html) {
            Matcher tagMatcher = Pattern.compile(IMAGE_TAG).matcher(html);
            List<String> tagList = new ArrayList<>();

            while (tagMatcher.find()) {
                String group = tagMatcher.group();
                if (!TextUtils.isEmpty(group)) {
                    tagList.add(group);
                }
            }

            for (String imageUrl : tagList) {
                Matcher urlMatcher = Pattern.compile(IMAGE_URL).matcher(imageUrl);
                while (urlMatcher.find()) {
                    String url = urlMatcher.group().substring(0, urlMatcher.group().length() - 1);
                    if (!TextUtils.isEmpty(url)) {
                        urlList.add(url);
                    }
                }

            }

        }
    }

    /**
     * 解析 HTML 该方法应在 setWebViewClient 的 onPageFinish 方法中进行调用
     *
     * @param view WebView
     */
    public void parseHTML(WebView view) {
        view.loadUrl("javascript:window.local_obj.parseHtml('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    /**
     * 注入 JS函数监听，这段 JS 函数 的功能就是遍历所有的图片，
     * 并添加 onClick 函数，实现点击事件
     * 即：在点击时调用本地的 java 接口并传递 url 过去
     */
    public void setImageClickListener() {
        this.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); " + "for(var i=0;i<objs.length;i++)  " + "{" + "    objs[i].onclick=function()  " + "    {  " + "        window.imageListener.onShowImageDialog(this.src);  " + "    }  " + "}" + "})()");
    }

    /**
     * 加载大图，使用 Dialog 作为 容器
     */
    private void showDialog(String url) {
        Log.e(TAG, "showDialog: " + urlList.toString());
        final Dialog fullDialog = new Dialog(context, R.style.DialogFullscreen);
        fullDialog.setContentView(R.layout.dialog_fullscreen);
        ImageView imBack = (ImageView) fullDialog.findViewById(R.id.imBack);
        final TextView tvOrder = (TextView) fullDialog.findViewById(R.id.tvOrder);
        ViewPager viewPager = (ViewPager) fullDialog.findViewById(R.id.viewPager);
        imBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fullDialog.dismiss();
            }
        });
        viewPager.setAdapter(new ImageAdapter(context, urlList));

        // 获取点中的图片的当前路径
        int position = urlList.indexOf(url);
        final int size = urlList.size();

        if (size > 1) {
            tvOrder.setVisibility(VISIBLE);
            tvOrder.setText(position + 1 + "/" + size);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    int index = position % size;
                    tvOrder.setText((index + 1) + "/" + size);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            tvOrder.setVisibility(GONE);
        }
        viewPager.setCurrentItem(position);


        fullDialog.show();

    }

}
