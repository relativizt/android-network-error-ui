package com.example.user.networkcontentapp.basenetworkcontent;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class BaseNetContentFragment extends Fragment implements BaseNetContentView {
    @Nullable
    private OnNetworkErrorListener mOnNetworkErrorListener;

    protected final void tryToShowNetworkError() {
        if (mOnNetworkErrorListener != null) {
            mOnNetworkErrorListener.onNetworkError();
        }
    }

    protected final boolean hasOnNetworkErrorListener() {
        return mOnNetworkErrorListener != null;
    }

    public final void setOnNetworkErrorListener(
            @Nullable OnNetworkErrorListener onNetworkErrorListener) {
        mOnNetworkErrorListener = onNetworkErrorListener;
    }

    public interface OnNetworkErrorListener {
        public void onNetworkError();
    }

}
