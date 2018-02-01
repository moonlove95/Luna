package com.mm.luna.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.mm.luna.R;
import com.mm.luna.util.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZMM on 2018/1/17.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    public P presenter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置透明状态栏
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        presenter = initPresenter();
        initView();
    }

    public abstract int getLayoutId();

    public abstract P initPresenter();

    public abstract void initView();

    @Override
    public void ShowLoadingDialog(String msg) {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        if (presenter != null) {
            presenter.detach();
            presenter = null;
        }
        super.onDestroy();
    }
}
