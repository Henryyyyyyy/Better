package me.henry.versatile.base.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import me.henry.versatile.R;
import me.henry.versatile.app.Canute;
import me.henry.versatile.base.VFragment;
import me.henry.versatile.view.titlebar.TitleBar;


/**
 * Created by henry on 2017/12/19.
 */

public class WebFragment extends VFragment {
    public static final String DATA_URL = "DATA_URL";
    String mUrl;
    @BindView(R.id.rl_webcontainer)
    RelativeLayout rl_webcontainer;
    WebView mWebView;
    @BindView(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mUrl = args.getString(DATA_URL);

        }
    }

    public static WebFragment newInstance(String url) {
        final Bundle args = new Bundle();
        args.putString(DATA_URL, url);

        final WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_web;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
       initTitlebar();
        mWebView = new WebView(Canute.getApplicationContext());
        mWebView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        WebViewInitializer.init(mWebView);
        mWebView.setWebViewClient(new WebViewClientImpl(this));
        mWebView.setWebChromeClient(new WebChromeClientImpl());
        rl_webcontainer.addView(mWebView);
        mWebView.loadUrl(mUrl);

    }
    private void initTitlebar() {
        mTitleBar.getCenterTxt().setText("Content");
        ImageView leftImg = mTitleBar.getLeftImg();
        mTitleBar.getRightImg().setVisibility(View.INVISIBLE);
        leftImg.setImageResource(R.mipmap.icon_back);
        leftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });

    }
    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }

    }

    @Override
    public boolean onBackPressedSupport() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onBackPressedSupport();
    }
}
