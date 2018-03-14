package com.njp.android.wallhaven.presenter;

import android.util.Log;

import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.model.GalleryModel;
import com.njp.android.wallhaven.view.GalleryView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 图片展示页面中间媒介层
 */

public class GalleryPresenter extends BasePresenter<GalleryView> {

    private int tag;

    private int page = 1;

    private GalleryModel mModel;

    public GalleryPresenter(int tag) {
        mModel = new GalleryModel(tag);
    }

    public void refresh() {
        mModel.getImages(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            List<ImageInfo> imageInfoList = new ArrayList<>();
                            String s = responseBody.string();
                            Document document = Jsoup.parse(s);
                            Elements elements = document.select("figure");
                            for (Element element : elements) {
                                ImageInfo imageInfo = new ImageInfo();
                                String id = element.attr("data-wallpaper-id");
                                Element img = element.getElementsByTag("img").get(0);
                                String smallImg = img.attr("data-src");
                                imageInfo.setId(id);
                                imageInfo.setSmallImgUrl(smallImg);
                                imageInfoList.add(imageInfo);
                            }
                            if (getView() != null) {
                                getView().onRefreshSuccess(imageInfoList);
                                page = 1;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (getView() != null) {
                                getView().onRefreshError();
                            }
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onRefreshError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadMore() {
        mModel.getImages(++page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            List<ImageInfo> imageInfoList = new ArrayList<>();
                            String s = responseBody.string();
                            Document document = Jsoup.parse(s);
                            Elements elements = document.select("figure");
                            for (Element element : elements) {
                                ImageInfo imageInfo = new ImageInfo();
                                String id = element.attr("data-wallpaper-id");
                                Element img = element.getElementsByTag("img").get(0);
                                String smallImg = img.attr("data-src");
                                imageInfo.setId(id);
                                imageInfo.setSmallImgUrl(smallImg);
                                imageInfoList.add(imageInfo);
                            }
                            if (getView() != null) {
                                if (imageInfoList.size() > 0) {
                                    getView().onLoadMoreSuccess(imageInfoList);
                                } else {
                                    getView().onNoMore();
                                    page--;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (getView() != null) {
                                getView().onLoadMoreError();
                                page--;
                            }
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onLoadMoreError();
                            page--;
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
