package com.njp.android.wallhaven.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.chrisbanes.photoview.PhotoView;
import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.bean.DetailImageInfo;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.presenter.DetailPresenter;
import com.njp.android.wallhaven.utils.FileUtil;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.ImageDao;
import com.njp.android.wallhaven.utils.SPUtil;
import com.njp.android.wallhaven.utils.SnakeBarUtil;
import com.njp.android.wallhaven.utils.ToastUtil;
import com.njp.android.wallhaven.utils.glide.GlideUtil;
import com.njp.android.wallhaven.utils.glide.LoadImageListener;
import com.njp.android.wallhaven.view.DetailView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailView {

    private static final String IMAGE_INFO = "imageInfo";
    public static final int REQUEST_CODE = 1001;
    public static final int REQUEST_CODE_PERMISSION = 1001;
    private boolean isFinish = false;
    private ImageInfo mImageInfo;

    private DetailImageInfo mDetailImageInfo;

    private Toolbar mToolbar;
    private PhotoView mPhotoView;
    private SwipeRefreshLayout mRefreshLayout;
    private NumberProgressBar mProgressBar;
    private ImageView mIvStar;
    private ImageView mIvDownload;
    private ImageView mIvTags;
    private ImageView mIvBg;
    private ImageView mIvFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImageInfo = (ImageInfo) getIntent().getSerializableExtra(IMAGE_INFO);

        initView();

        initEvent();

        initPermission();

    }

    private void initView() {
        mPhotoView = findViewById(R.id.photo_view);
        mToolbar = findViewById(R.id.toolbar);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mProgressBar = findViewById(R.id.progress_bar);
        mIvStar = findViewById(R.id.iv_star);
        mIvDownload = findViewById(R.id.iv_download);
        mIvTags = findViewById(R.id.iv_tags);
        mIvBg = findViewById(R.id.iv_bg);
        mIvFinish = findViewById(R.id.iv_finish);

        mToolbar.setTitle(mImageInfo.getId());
        setSupportActionBar(mToolbar);

        if (ImageDao.exists(mImageInfo)) {
            mIvStar.setImageResource(R.drawable.star_yellow);
        }

        Glide.with(this)
                .load(mImageInfo.getSmallImgUrl())
                .bitmapTransform(new BlurTransformation(this, 14, 3))
                .into(mIvBg);


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
                if (mDetailImageInfo == null) {
                    getPresenter().getDetailImage(mImageInfo.getId());
                } else {
                    loadImage(mDetailImageInfo.getUrl());
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });

        mIvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageInfo != null) {
                    if (!ImageDao.exists(mImageInfo)) {
                        ImageDao.insert(mImageInfo);
                        mIvStar.setImageResource(R.drawable.star_yellow);
                        ToastUtil.show("已收藏");
                    } else {
                        ImageDao.delete(mImageInfo);
                        mIvStar.setImageResource(R.drawable.star_white);
                        ToastUtil.show("已取消收藏");
                    }
                    EventBus.getDefault().post(EventBusUtil.ChangeStarEvent.CHANGE_STAR_EVENT);
                }
            }
        });

        mIvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFinish) {
                    ToastUtil.show("已下载");
                    return;
                }
                if (initPermission()) {
                    downLoad();
                }
            }
        });

        mIvTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailImageInfo != null && mDetailImageInfo.getTags().size() > 0) {
                    showDialog();
                }
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
    public DetailPresenter createPresenter() {
        return new DetailPresenter();
    }

    @Override
    public void onSuccess(final DetailImageInfo imageInfo) {

        mDetailImageInfo = imageInfo;

        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setEnabled(false);

        if (initPermission()) {
            initFinish();
        }

        loadImage(imageInfo.getUrl());
    }

    private void initFinish() {
        String targetPath = Environment.getExternalStorageDirectory().getPath() + "/Wallhaven";
        String[] strings = mDetailImageInfo.getUrl().split("/");
        String name = strings[strings.length - 1];
        isFinish = FileUtil.isFinish(targetPath, name);
        if (isFinish) {
            mIvFinish.setVisibility(View.VISIBLE);
        }
    }

    private void loadImage(String url) {
        GlideUtil.progressLoad(getApplicationContext(), url, mPhotoView, new LoadImageListener() {
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
                mIvBg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure() {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRefreshLayout.setEnabled(true);
                SnakeBarUtil.getInstance().show("图片" + mImageInfo.getId() + "加载失败", mRefreshLayout);
            }
        });
    }

    private void downLoad() {
        if (mDetailImageInfo == null || mProgressBar.getVisibility() == View.VISIBLE) {
            ToastUtil.show("图片正在加载中，请稍候");
            return;
        }
        GlideUtil.progressDownload(getApplicationContext(), mDetailImageInfo.getUrl(), new LoadImageListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onProgress(int progress) {
            }

            @Override
            public void onSuccess(@Nullable String path) {
                String[] strings = mDetailImageInfo.getUrl().split("/");
                String name = strings[strings.length - 1];
                String targetPath = Environment.getExternalStorageDirectory().getPath() + "/Wallhaven";
                if (FileUtil.CopyFile(path, targetPath, name)) {
                    String filePath = targetPath + "/" + name;
                    ToastUtil.show("图片保存到" + filePath);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath))));
                    isFinish = true;
                    mIvFinish.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.show("保存失败");
                }
            }

            @Override
            public void onFailure() {
                ToastUtil.show("下载失败");
            }
        });
    }

    private boolean initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            return false;
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initFinish();
                    downLoad();
                } else {
                    SnakeBarUtil.getInstance().show("无法下载", mRefreshLayout);
                }
                break;
        }
    }
}
