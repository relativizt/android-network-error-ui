package com.example.user.networkcontentapp.stackoverflow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.user.networkcontentapp.basenetworkcontent.BaseNetContentActivity;


/**
 * Created by user on 08.10.2018.
 */

public class StackOverflowActivity extends BaseNetContentActivity<StackOverflowFragment> {
    @NonNull
    @Override
    protected StackOverflowFragment createNetworkContentFragment() {
        return new StackOverflowFragment();
    }

    @Override
    protected void setPresenter(@NonNull StackOverflowFragment fragment, @Nullable Bundle savedInstanceState) {
        String url = "https://stackoverflow.com/questions/36054212/" +
                "best-approach-to-show-network-error-in-android-in-android-with-tap-to-retry-opti";
        StackOverflowContract.Presenter presenter = new StackOverflowPresenter(url, fragment);
    }

}
