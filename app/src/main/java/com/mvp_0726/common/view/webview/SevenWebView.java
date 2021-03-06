package com.mvp_0726.common.view.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mvp_0726.common.base.MvpApplication;
import com.mvp_0726.common.utils.DensityUtil;
import com.project.wisdomfirecontrol.R;


/**
 * on 2018/5/27
 */

public class SevenWebView extends WebView {

    private H5JSInterface h5JSInterface;
    private ProgressBar progressBar;
    private boolean isStart = true;

    public SevenWebView(Context context) {
        super(context);
        initUi();
    }

    public SevenWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUi();
    }

    public SevenWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUi();
    }

    public H5JSInterface getH5JsInterface() {
        return h5JSInterface;
    }

    private void initUi() {
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.web_progress_bar_bg));
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 2)));
        addView(progressBar);

        initWebViewSetting();
    }

    private void initWebViewSetting() {
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
        h5JSInterface = new H5JSInterface(MvpApplication.getContext());
        addJavascriptInterface(h5JSInterface, "imagelistner");
        /*下面一顿copy就好了*/
        setClickable(true);
       /* setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCachePath(getContext().getCacheDir() + "/webviewCache");
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        /*
        * 继续请求
        * */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            isStart = false;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            isStart = false;
            super.onPageFinished(view, url);
            addImageviewListenter(view);
        }

        private void addImageviewListenter(WebView webView) {
            // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，
            // 函数的功能是在图片点击的时候调用本地java接口并传递url过去
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imagelistner.openImage(this.src);  " +
                    "    }  " +
                    "}" +
                    "})()");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("SevenWebView", "加载的url===" + url);
            if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")) {
                view.loadUrl(url);
                return true;
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
           /* return true ;会拦截shouldOverrideUrlLoading(WebView, String)方法
           但此方法内部实际还是调用的shouldOverrideUrlLoading(WebView, String)*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
                return true;
            } else {
                return false;
            }
        }

       /* *//*
        * 防止被劫持有效方法，拦截域名，除了我们内部的域名都不放行
        * *//*
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String url = request.getUrl().toString();
                *//*
                * 因为这边是别人开放的api，我就没办法去做拦截
                * *//*
                if (url.contains(NetworkUrl.ANDROID_TEST_SERVICE)) {
                    return super.shouldInterceptRequest(view, url);
                } else {
                    return new WebResourceResponse(null, null, null);
                }
            } else {
                return new WebResourceResponse(null, null, null);
            }
        }*/
    };
    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar != null && isStart && newProgress < 100) {
                progressBar.setVisibility(VISIBLE);
                progressBar.setProgress(newProgress);
            } else if (progressBar != null && newProgress == 100) {
                progressBar.setVisibility(GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    @Override
    public void loadUrl(String url) {
        /*if (NetUtils.isNetworkConnected(getContext())) {
            this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {*/
        this.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
        super.loadUrl(url);
    }
}
