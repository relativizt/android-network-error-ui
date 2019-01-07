package com.example.user.networkcontentapp.stackoverflow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.user.networkcontentapp.R;
import com.example.user.networkcontentapp.basenetworkcontent.BaseNetContentFragment;


/**
 * Created by user on 08.10.2018.
 */

public class StackOverflowFragment extends BaseNetContentFragment implements StackOverflowContract.View {
    private static final String mStackOverflowUrl = "https://stackoverflow.com";

    private StackOverflowContract.Presenter mPresenter;

    private WebView mWebView;
    private ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.stackoverflow_frag, container, false);

        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        // Set up web page view
        mWebView = (WebView) root.findViewById(R.id.webView);
        mWebView.setWebViewClient(new CustomWebViewClient());

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull StackOverflowContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPageLoading() {
        mWebView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePageLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNetworkContentError() {
        if (hasOnNetworkErrorListener()) {
            tryToShowNetworkError();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            Snackbar.make(mWebView, R.string.network_error_text, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.start();
                        }
                    }).show();
        }
    }

    @Override
    public void showWebView() {
        mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWebView() {
        mWebView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void loadPage(@NonNull String url) {
        mWebView.loadUrl(url);
    }

    /**
     * Custom WebViewClient to handle network error
     */
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPresenter.pageLoaded();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            super.shouldOverrideUrlLoading(view, url);
            // true - abort URL loading
            return true;

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            super.shouldOverrideUrlLoading(view, request);
            // true - abort URL loading
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {

            // There is no way to detect HTTP errors when embedding a webview
            // https://issuetracker.google.com/issues/36905665
            onReceivedError(view.getUrl(), failingUrl, description);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                        WebResourceResponse errorResponse) {

            // Always show http errors(first and second arguments are match)
            onReceivedError(view.getUrl(), view.getUrl(), errorResponse.getReasonPhrase());
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {

            // Handle errors only from main frame
            // https://stackoverflow.com/questions/44068123
            if (request.isForMainFrame()) {
                mPresenter.connectionError(error.getDescription().toString());
            }
        }

        private void onReceivedError(String webViewUrl, String failingUrl,
                                     CharSequence description) {
            // Skip errors for other page elements (not main url)
            if (webViewUrl.equals(failingUrl)) {
                mPresenter.connectionError(description.toString());
            }
        }
    }

}
