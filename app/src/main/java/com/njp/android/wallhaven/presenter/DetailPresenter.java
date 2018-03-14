package com.njp.android.wallhaven.presenter;

import android.util.Log;

import com.njp.android.wallhaven.bean.DetailImageInfo;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.model.DetailModel;
import com.njp.android.wallhaven.view.DetailView;

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
 * 详情界面的中间媒介层
 */

public class DetailPresenter extends BasePresenter<DetailView> {
    private DetailModel mModel;

    public DetailPresenter() {
        mModel = new DetailModel();
    }

    public void getDetailImage(String id) {
        mModel.getDetailImage(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String s = responseBody.string();
                            Document document = Jsoup.parse(s);
                            DetailImageInfo imageInfo = new DetailImageInfo();
                            Elements url = document.select("#wallpaper");
                            imageInfo.setUrl("http:" + url.attr("src"));
                            Elements elements = document.select("li[data-tag-id]");
                            List<DetailImageInfo.Tag> tags = new ArrayList<>();
                            for (Element element : elements) {
                                DetailImageInfo.Tag tag = new DetailImageInfo.Tag();
                                String id = element.attr("data-tag-id");
                                Elements select = element.select(".tagname");
                                String name = select.text();
                                tag.setId(id);
                                tag.setName(name);
                                tags.add(tag);
                            }
                            imageInfo.setTags(tags);
                            if (getView() != null) {
                                getView().onSuccess(imageInfo);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            if (getView() != null) {
                                getView().onError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}
