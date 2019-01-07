package com.example.user.networkcontentapp.stackoverflow;

import android.support.annotation.NonNull;

/**
 * Created by user on 08.10.2018.
 */

public class StackOverflowPresenter implements StackOverflowContract.Presenter {

    private String mPageUrl;

    private StackOverflowContract.View mView;

    public StackOverflowPresenter(@NonNull String pageUrl,
                                  @NonNull StackOverflowContract.View view) {
        mPageUrl = pageUrl;
        mView = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        mView.showPageLoading();
        mView.loadPage(mPageUrl);
    }

    @Override
    public void connectionError(@NonNull String description){
        mView.hideWebView();
        mView.hidePageLoading();
        mView.showNetworkContentError();
    }

    @Override
    public void pageLoaded() {
        mView.showWebView();
        mView.hidePageLoading();
    }

}
