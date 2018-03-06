package com.njp.android.wallhaven.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.chrisbanes.photoview.PhotoView;
import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.bean.DetailImageInfo;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.presenter.DetailPresenter;
import com.njp.android.wallhaven.utils.SPUtil;
import com.njp.android.wallhaven.utils.SnakeBarUtil;
import com.njp.android.wallhaven.utils.ToastUtil;
import com.njp.android.wallhaven.utils.glide.GlideUtil;
import com.njp.android.wallhaven.utils.glide.LoadImageListener;
import com.njp.android.wallhaven.view.DetailView;

import java.util.List;


public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailView {

    private static final String IMAGE_INFO = "imageInfo";
    public static final int REQUEST_CODE = 1001;
    private ImageInfo mImageInfo;

    private DetailImageInfo mDetailImageInfo;

    private Toolbar mToolbar;
    private PhotoView mPhotoView;
    private SwipeRefreshLayout mRefreshLayout;
    private NumberProgressBar mProgressBar;

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
        mProgressBar = findViewById(R.id.progress_bar);

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
                int colorBlue = getResources().getColor(R.color.holo_blue);
                mToolbar.setBackgroundColor(colorBlue);
                mRefreshLayout.setColorSchemeColors(colorBlue);
                mProgressBar.setProgressTextColor(colorBlue);
                mProgressBar.setReachedBarColor(colorBlue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorBlue);
                }
                break;
            case "red":
                int colorRed = getResources().getColor(R.color.holo_red);
                mToolbar.setBackgroundColor(colorRed);
                mRefreshLayout.setColorSchemeColors(colorRed);
                mProgressBar.setProgressTextColor(colorRed);
                mProgressBar.setReachedBarColor(colorRed);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorRed);
                }
                break;
            case "green":
                int colorGreen = getResources().getColor(R.color.holo_green);
                mToolbar.setBackgroundColor(colorGreen);
                mRefreshLayout.setColorSchemeColors(colorGreen);
                mProgressBar.setProgressTextColor(colorGreen);
                mProgressBar.setReachedBarColor(colorGreen);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorGreen);
                }
                break;
            case "orange":
                int colorOrange = getResources().getColor(R.color.holo_orange);
                mToolbar.setBackgroundColor(colorOrange);
                mRefreshLayout.setColorSchemeColors(colorOrange);
                mProgressBar.setProgressTextColor(colorOrange);
                mProgressBar.setReachedBarColor(colorOrange);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorOrange);
                }
                break;
            case "purple":
                int colorPurple = getResources().getColor(R.color.holo_purple);
                mToolbar.setBackgroundColor(colorPurple);
                mRefreshLayout.setColorSchemeColors(colorPurple);
                mProgressBar.setProgressTextColor(colorPurple);
                mProgressBar.setReachedBarColor(colorPurple);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorPurple);
                }
                break;
            case "gray":
                int colorGray = getResources().getColor(R.color.holo_gray);
                mToolbar.setBackgroundColor(colorGray);
                mRefreshLayout.setColorSchemeColors(colorGray);
                mProgressBar.setProgressTextColor(colorGray);
                mProgressBar.setReachedBarColor(colorGray);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorGray);
                }
                break;
            case "brown":
                int colorBrown = getResources().getColor(R.color.holo_brown);
                mToolbar.setBackgroundColor(colorBrown);
                mRefreshLayout.setColorSchemeColors(colorBrown);
                mProgressBar.setProgressTextColor(colorBrown);
                mProgressBar.setReachedBarColor(colorBrown);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorBrown);
                }
                break;
            case "yellow":
                int colorYellow = getResources().getColor(R.color.holo_yellow);
                mToolbar.setBackgroundColor(colorYellow);
                mRefreshLayout.setColorSchemeColors(colorYellow);
                mProgressBar.setProgressTextColor(colorYellow);
                mProgressBar.setReachedBarColor(colorYellow);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorYellow);
                }
                break;
            case "cyan":
                int colorCyan = getResources().getColor(R.color.holo_cyan);
                mToolbar.setBackgroundColor(colorCyan);
                mRefreshLayout.setColorSchemeColors(colorCyan);
                mProgressBar.setProgressTextColor(colorCyan);
                mProgressBar.setReachedBarColor(colorCyan);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(colorCyan);
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
    public void onSuccess(final DetailImageInfo imageInfo) {

        mDetailImageInfo = imageInfo;

        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setEnabled(false);

        GlideUtil.progressLoad(this, imageInfo.getUrl(), mPhotoView, new LoadImageListener() {
            @Override
            public void onStart() {
                mProgressBar.setProgress(0);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(int progress) {
                mProgressBar.setProgress(progress);
            }

            @Override
            public void onSuccess(@Nullable String path) {
                mProgressBar.setVisibility(View.INVISIBLE);
                ToastUtil.show("图片" + mImageInfo.getId() + "加载完成");
            }

            @Override
            public void onFailure() {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRefreshLayout.setEnabled(true);
                ToastUtil.show("图片" + mImageInfo.getId() + "加载失败");
            }
        });
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
                if (mDetailImageInfo != null && mDetailImageInfo.getTags().size() > 0) {
                    showDialog();
                }
                break;
        }
        return false;
    }

    private void showDialog() {
        final List<DetailImageInfo.Tag> tags = mDetailImageInfo.getTags();
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
