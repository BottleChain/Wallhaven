package com.njp.android.wallhaven.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.njp.android.wallhaven.presenter.BasePresenter;
import com.njp.android.wallhaven.view.BaseView;

/**
 * Fragment的抽象父类
 * 实现了Presenter层的绑定解绑
 * 实现了懒加载
 */

public abstract class BaseFragment<Presenter extends BasePresenter> extends Fragment implements BaseView {

    private boolean isFirstLoad = false;

    private static final String TAG = "BaseFragment";

    private Presenter mPresenter;

    public Presenter getPresenter() {
        return mPresenter;
    }

    public abstract Presenter createPresenter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = initView(inflater, container);

        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        initEvent();

        isFirstLoad = true;

        if (getUserVisibleHint()) {
            onLazyLoad();
            isFirstLoad = false;
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        isFirstLoad = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
            if (isFirstLoad) {
                onLazyLoad();
                isFirstLoad = false;
            }
        }else {
            onDisVisible();
        }
    }

    public void onLazyLoad() {
    }

    public abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container);

    public abstract void initEvent();

    public void onVisible() {
    }

    public void onDisVisible() {
    }


}
