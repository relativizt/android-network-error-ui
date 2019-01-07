package com.example.user.networkcontentapp.basenetworkcontent;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.networkcontentapp.R;

/**
 * Base Activity for handling network error by changing fragment
 * Created by user on 01.08.2018.
 */

public abstract class BaseNetContentActivity<T extends BaseNetContentFragment>
        extends AppCompatActivity implements BaseNetContentFragment.OnNetworkErrorListener {
    private static final String TAG = "BaseNetContentActivity";

    @Override
    public void onNetworkError() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getCurrentContentFragment(fragmentManager);

        // Skip if already NetworkErrorFragment
        if (!(fragment instanceof NetworkErrorFragment)) {
            setFragmentToActivity(fragmentManager, new NetworkErrorFragment());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        Fragment fragment = getCurrentContentFragment(getSupportFragmentManager());

        // NetworkErrorFragment is self-sufficient
        if (fragment instanceof NetworkErrorFragment) {
            return;
        }

        setNetworkContentFragmentToActivity(savedInstanceState);

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // Set appropriate listener to fragment
        if (fragment instanceof NetworkErrorFragment) {
            ((NetworkErrorFragment) fragment)
                    .setOnReloadContentListener(new NetworkErrorFragment.OnReloadContentListener() {
                        @Override
                        public void onReloadContent() {
                                setNetworkContentFragmentToActivity(null);
                        }
                    });
        } else if (fragment instanceof BaseNetContentFragment) {
            ((BaseNetContentFragment) fragment).setOnNetworkErrorListener(this);
        }
        // Don't do anything with other fragment's type
    }

    @NonNull
    protected abstract T createNetworkContentFragment();

    protected abstract void setPresenter(@NonNull T fragment, @Nullable Bundle savedInstanceState);

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.basenetworkcontent_act;
    }

    @IdRes
    protected int getContentFrameId() {
        return R.id.network_content_frame;
    }

    private void setNetworkContentFragmentToActivity(@Nullable Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getCurrentContentFragment(fragmentManager);

        if (fragment == null || fragment instanceof NetworkErrorFragment) {
            fragment = createNetworkContentFragment();
        }

        try {
            setPresenter((T) fragment, savedInstanceState);
        } catch (ClassCastException e) {
            // Unexpected fragment type
            Log.d(TAG,"Can't set Presenter because of wrong View type (wrong fragment)" + e);

            // Casting to T type is safe, because createNetworkFragment() returns T type
            fragment = createNetworkContentFragment(); // returns type T
            setPresenter((T) fragment, savedInstanceState);
        }

        setFragmentToActivity(fragmentManager, fragment);

    }

    private Fragment getCurrentContentFragment(@NonNull FragmentManager fragmentManager) {
        return fragmentManager.findFragmentById(getContentFrameId());
    }

    private void setFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(getContentFrameId(), fragment)
                .commit();
    }

    public static class NetworkErrorFragment extends Fragment implements View.OnClickListener {
        @Nullable
        private OnReloadContentListener mOnReloadContentListener;
        private Button mReloadButton;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.networkerror_frag, container, false);
            mReloadButton = (Button) root.findViewById(R.id.reload_content_button);
            if (mOnReloadContentListener != null) {
                mReloadButton.setOnClickListener(this);
            } else {
                mReloadButton.setVisibility(View.INVISIBLE);
            }
            return root;
        }

        @Override
        public void onClick(View v) {
            if (mOnReloadContentListener != null) {
                mOnReloadContentListener.onReloadContent();
            }
        }

        public void setOnReloadContentListener(@Nullable OnReloadContentListener onReloadContentListener) {
            mOnReloadContentListener = onReloadContentListener;
        }

        public interface OnReloadContentListener {
            public void onReloadContent();
        }

    }

}
