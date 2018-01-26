package me.henry.versatile.base.web;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import me.henry.versatile.R;
import me.henry.versatile.utils.CanuteLog;

public class WebViewClientImpl extends WebViewClient {

    private final WebFragment mFragment;
//    private IPageLoadListener mIPageLoadListener = null;
//    private static final Handler HANDLER = Latte.getHandler();
//
//    public void setPageLoadListener(IPageLoadListener listener) {
//        this.mIPageLoadListener = listener;
//    }
//
    public WebViewClientImpl(WebFragment fragment) {
        this.mFragment = fragment;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
       // mFragment.getSupportDelegate().start(WebFragment.newInstance(url));
        return false;

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        CanuteLog.d("miao","onPageStarted");
//        if (mIPageLoadListener != null) {
//            mIPageLoadListener.onLoadStart();
//        }
//        LatteLoader.showLoading(view.getContext());
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        CanuteLog.d("miao","onPageFinished");
//        if (mIPageLoadListener != null) {
//            mIPageLoadListener.onLoadEnd();
//        }
//        HANDLER.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LatteLoader.stopLoading();
//            }
//        }, 1000);
    }
}