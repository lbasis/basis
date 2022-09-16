package com.qunli.demo;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.base.ui.BaseActivity;

public class SamepleFragmentActivity extends BaseActivity {

    @Override
    public int setLayoutId() {
        return R.layout.activity_sample_fragment;
    }

    @Override
    public void init() {
        getWrapBar().setTitle(R.string.str_fragment).work();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, new RcyListFragment());
        transaction.commitNow();
    }

}