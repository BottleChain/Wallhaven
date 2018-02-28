package com.njp.android.wallhaven.presenter;

import com.njp.android.wallhaven.view.BaseView;

/**
 * Presenter层的抽象父类
 * 实现View的绑定与解绑
 */

public abstract class BasePresenter<View extends BaseView> {

    private View mView;

    public View getView() {
        return mView;
    }

    public void attachView(View view) {
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
    }

}
