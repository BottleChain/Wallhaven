package com.njp.android.wallhaven.presenter;

import com.njp.android.wallhaven.bean.BingImageInfo;
import com.njp.android.wallhaven.model.SplashModel;
import com.njp.android.wallhaven.view.SplashView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Splash页中间层
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    private SplashModel mModel;

    public SplashPresenter() {
        mModel = new SplashModel();
    }

    public void getImage() {
        mModel.getImageInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BingImageInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BingImageInfo bingImageInfo) {
                        if (getView() != null) {
                            String url = bingImageInfo.getImages().get(0).getUrl();
                            getView().onImageResponse("http://s.cn.bing.net" + url);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onImageError();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void startTimer(final int seconds) {
        if (getView() != null) {
            new Observable<Integer>() {
                @Override
                protected void subscribeActual(Observer<? super Integer> observer) {

                    for (int i = seconds; i > 0; i--) {
                        observer.onNext(i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    observer.onComplete();

                }
            }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Integer integer) {
                            if (getView() != null) {
                                getView().onTimer(integer);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            if (getView() != null) {
                                getView().onTimerFinish();
                            }
                        }
                    });
        }

    }

}
