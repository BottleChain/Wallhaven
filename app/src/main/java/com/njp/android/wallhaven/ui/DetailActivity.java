package com.njp.android.wallhaven.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.bean.DetailImageInfo;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.presenter.DetailPresenter;
import com.njp.android.wallhaven.utils.SPUtil;
import com.njp.android.wallhaven.utils.SnakeBarUtil;
import com.njp.android.wallhaven.utils.ToastUtil;
import com.njp.android.wallhaven.view.DetailView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailView {

    private static final String IMAGE_INFO = "imageInfo";
    public static final int REQUEST_CODE = 1001;
    private ImageInfo mImageInfo;

    private List<DetailImageInfo.Tag> tags;

    private Toolbar mToolbar;
    private PhotoView mPhotoView;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImageInfo = (ImageInfo) getIntent().getSerializableExtra(IMAGE_INFO);

        initView();

        initEvent();

    }

    private void initView() {
        mPhotoView = findViewById(R.id.photo_view);
        mToolbar = findViewById(R.id.toolbar);
        mRefreshLayout = findViewById(R.id.refresh_layout);

        mToolbar.setTitle(mImageInfo.getId());
        setSupportActionBar(mToolbar);


        changeSkin(SPUtil.getString("skin", "blue"));
    }

    private void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().getDetailImage(mImageInfo.getId());
            }
        });

        mRefreshLayout.setRefreshing(true);
        getPresenter().getDetailImage(mImageInfo.getId());
    }

    private void changeSkin(String skin) {
        switch (skin) {
            case "blue":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_blue));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_blue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_blue));
                }
                break;
            case "red":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_red));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_red));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_red));
                }
                break;
            case "green":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_green));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_green));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_green));
                }
                break;
            case "orange":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_orange));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_orange));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_orange));
                }
                break;
            case "purple":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_purple));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_purple));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_purple));
                }
                break;
            case "gray":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_gray));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_gray));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_gray));
                }
                break;
            case "brown":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_brown));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_brown));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_brown));
                }
                break;
            case "yellow":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_yellow));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_yellow));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_yellow));
                }
                break;
            case "cyan":
                mToolbar.setBackgroundColor(getResources().getColor(R.color.holo_cyan));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_cyan));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_cyan));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public DetailPresenter createPresenter() {
        return new DetailPresenter();
    }

    @Override
    public void onSuccess(DetailImageInfo imageInfo) {

        tags = imageInfo.getTags();

        Glide.with(this)
                .load(imageInfo.getUrl())
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        SnakeBarUtil.getInstance().show("图片加载失败", mRefreshLayout);
                        mRefreshLayout.setRefreshing(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.setEnabled(false);
                        ToastUtil.show("图片" + mImageInfo.getId() + "加载完成");
                        return false;
                    }
                })
                .into(mPhotoView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                ToastUtil.show("download");
                break;
            case R.id.collect:
                ToastUtil.show("collect");
                break;
            case R.id.tags:
                if (tags != null && tags.size() > 0) {
                    showDialog();
                }
                break;
        }
        return false;
    }

    private void showDialog() {
        String[] names = new String[tags.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = tags.get(i).getName();
        }
        new AlertDialog.Builder(this)
                .setTitle("Tags")
                .setIcon(R.drawable.tags_)
                .setItems(names,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra("query", "id:" + tags.get(which).getId());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }).setCancelable(true).show();
    }

    @Override
    public void onError() {
        SnakeBarUtil.getInstance().show("加载图片失败", mRefreshLayout);
        mRefreshLayout.setRefreshing(false);
    }

    public static void actionStart(Activity activity, ImageInfo imageInfo) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(IMAGE_INFO, imageInfo);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

}
