package com.qunli.demo;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.adapter.RcyAdapter;
import com.base.adapter.RcyHolder;
import com.base.adapter.RcySAdapter;
import com.base.refresh.XRecyclerView;
import com.base.refresh.progress.IndicatorView;
import com.base.refresh.progress.Style;
import com.base.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class IndicatorActivity extends BaseActivity {

    RcyAdapter<Integer> adapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_view;
    }

    @Override
    public void init() {
        XRecyclerView refresh = getView(R.id.refresh);
        final GridLayoutManager layoutmanager = new GridLayoutManager(this, 4);
        refresh.setLayoutManager(layoutmanager);
        refresh.enableRefresh(false);
        refresh.enableLoad(false);
        adapter = new RcySAdapter<Integer>(this, R.layout.item_indicator) {
            @Override
            public void convert(RcyHolder iHolder, Integer o, int position) {
                IndicatorView view = (IndicatorView) iHolder.getView(R.id.indicator);
                view.setStyle(Style.valueOf(o));
                iHolder.setText(R.id.info, "index = " + o);
            }
        };
        adapter.setRefreshView(refresh);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i);
        }
        adapter.setData(list, false);
    }

}
