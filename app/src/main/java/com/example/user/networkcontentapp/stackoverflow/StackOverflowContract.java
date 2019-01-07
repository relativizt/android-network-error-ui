package com.example.user.networkcontentapp.stackoverflow;

import android.support.annotation.NonNull;
import com.example.user.networkcontentapp.BaseView;
import com.example.user.networkcontentapp.BasePresenter;
import com.example.user.networkcontentapp.basenetworkcontent.BaseNetContentView;

/**
 * Created by user on 08.10.2018.
 */

public interface StackOverflowContract {
    interface View extends BaseView<Presenter>, BaseNetContentView {

        void showPageLoading();
        void hidePageLoading();

        void showWebView();
        void hideWebView();

        void loadPage(@NonNull String url);

    }

    interface Presenter extends BasePresenter{

        void pageLoaded();

        void connectionError(@NonNull String description);

    }
}
