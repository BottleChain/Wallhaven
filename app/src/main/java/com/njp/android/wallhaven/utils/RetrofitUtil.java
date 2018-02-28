package com.njp.android.wallhaven.utils;

import com.njp.android.wallhaven.bean.BingImageInfo;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 网络请求工具类
 */

public class RetrofitUtil {

    private static OkHttpClient sClient = new OkHttpClient.Builder().build();

    private static Retrofit.Builder sBuilder = new Retrofit.Builder()
            .client(sClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    public interface BingImageService {

        @GET("HPImageArchive.aspx")
        Observable<BingImageInfo> getImage(
                @Query("format") String format,
                @Query("idx") int idx,
                @Query("n") int n
        );
    }

    public interface ImageService {

        @GET("{path}")
        Observable<ResponseBody> getImages(
                @Path("path") String path,
                @Query("page") int page
        );

        @GET("search")
        Observable<ResponseBody> searchImages(
                @Query("q") String q,
                @Query("sorting") String sorting,
                @Query("page") int page
        );

    }

    public static BingImageService getBingImageService() {
        sBuilder.baseUrl("http://cn.bing.com/");
        return sBuilder.build().create(BingImageService.class);
    }

    public static ImageService getGalleryImageService() {
        sBuilder.baseUrl("https://alpha.wallhaven.cc/");
        return sBuilder.build().create(ImageService.class);
    }


}
