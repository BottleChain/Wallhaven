package com.njp.android.wallhaven.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.njp.android.wallhaven.presenter.BasePresenter;
import com.njp.android.wallhaven.view.BaseView;

/**
 * Activity抽象父类
 */

public abstract class BaseActivity<Presenter extends BasePresenter> extends AppCompatActivity implements BaseView {

    private Presenter mPresenter;

    public abstract Presenter createPresenter();

    public Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
