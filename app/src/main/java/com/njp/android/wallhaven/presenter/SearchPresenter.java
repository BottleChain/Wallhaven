package com.njp.android.wallhaven.presenter;

import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.model.SearchModel;
import com.njp.android.wallhaven.view.SearchView;

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
 * 搜索界面中间媒介层
 */

public class SearchPresenter extends BasePresenter<SearchView> {

    private int page = 1;

    private String query;

    private SearchModel mModel;

    public SearchPresenter() {
        mModel = new SearchModel();
    }

    public void refresh(String sorting) {
        searchImage(query, sorting);
    }

    public void searchImage(String q, String sorting) {
        if (q == null) {
            if (getView() != null) {
                getView().onSearchError();
                return;
            }
        }
        query = q;
        mModel.search(q, sorting, 1)
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
                                Element a = element.getElementsByTag("a").get(0);
                                String smallImg = img.attr("data-src");
                                String largeImg = a.attr("href");
                                imageInfo.setId(id);
                                imageInfo.setSmallImgUrl(smallImg);
                                imageInfo.setLargeImgUrl(largeImg);
                                imageInfoList.add(imageInfo);
                            }
                            if (getView() != null) {
                                if (imageInfoList.size() > 0) {
                                    getView().onSearchSuccess(imageInfoList);
                                    page = 1;
                                } else {
                                    getView().onSearchNone();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (getView() != null) {
                                getView().onSearchError();
                            }
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onSearchError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadMore(String sorting) {
        if (query == null) {
            if (getView() != null) {
                getView().onLoadMoreError();
            }
            return;
        }
        mModel.search(query, sorting, ++page)
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
                                Element a = element.getElementsByTag("a").get(0);
                                String smallImg = img.attr("data-src");
                                String largeImg = a.attr("href");
                                imageInfo.setId(id);
                                imageInfo.setSmallImgUrl(smallImg);
                                imageInfo.setLargeImgUrl(largeImg);
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
                                getView().onSearchError();
                            }
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onLoadMoreError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
