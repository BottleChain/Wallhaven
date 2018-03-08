package com.njp.android.wallhaven.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.presenter.SplashPresenter;
import com.njp.android.wallhaven.utils.glide.GlideUtil;
import com.njp.android.wallhaven.view.SplashView;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private static final int SECONDS = 5;


    private Button mBtnSkip;
    private ImageView mIvBackground;
    private ImageView mIvLogo;

    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

        initEvent();

    }

    private void initView() {
        mBtnSkip = findViewById(R.id.btn_skip);
        mIvBackground = findViewById(R.id.iv_background);
        mIvLogo = findViewById(R.id.iv_logo);
    }


    private void initEvent() {
        getPresenter().getImage();
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainActivity();
            }
        });
        getPresenter().startTimer(SECONDS);
    }


    @Override
    public void onImageResponse(String url) {
        GlideUtil.simpleLoad(this,url,mIvBackground);
        GlideUtil.simpleLoad(this,R.drawable.logo,mIvLogo);
        getPresenter().startTimer(SECONDS);
    }

    @Override
    public void onImageError() {
        GlideUtil.simpleLoad(this,R.drawable.btn_bg_skip,mIvBackground);
        GlideUtil.simpleLoad(this,R.drawable.logo,mIvLogo);
    }

    @Override
    public void onTimer(int seconds) {
        mBtnSkip.setText("跳过 "+seconds);
    }

    @Override
    public void onTimerFinish() {
        toMainActivity();
    }

    private void toMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
