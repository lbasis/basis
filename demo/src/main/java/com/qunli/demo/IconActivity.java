package com.qunli.demo;

import android.widget.ImageView;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.base.adapter.RcyHolder;
import com.base.adapter.RcySAdapter;
import com.base.kit.UIKit;
import com.base.refresh.XRecyclerView;
import com.base.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class IconActivity extends BaseActivity {

    private final static int[] icons = new int[]{
            R.drawable.icon_add_circle,
            R.drawable.icon_add,
            R.drawable.icon_back,
            R.drawable.icon_add_rect,
            R.drawable.icon_check_box,
            R.drawable.icon_clear,
            R.drawable.icon_clear_circle,
            R.drawable.icon_left,
            R.drawable.icon_right,
            R.drawable.icon_right_ok,
            R.drawable.icon_star,
            R.drawable.icon_uncheck_box,
            R.drawable.icon_unstar
    };
    private final static List<Integer> iconAs = new ArrayList<>();

    static {
        int len = icons.length;
        for (int i = 0; i < len; i++) {
            iconAs.add(icons[i]);
        }
    }

    RcySAdapter<Integer> adapter;


    @Override
    public int setLayoutId() {
        return R.layout.activity_icon;
    }

    @Override
    public void init() {
//        XRecyclerView refresh = getView(R.id.refresh);
        XRecyclerView refresh = UIKit.getFirstViewByClass(getLayout(),XRecyclerView.class,-1);
//        final GridLayoutManager layoutmanager = new GridLayoutManager(this, 4);
        StaggeredGridLayoutManager layoutmanager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        refresh.setLayoutManager(layoutmanager);
        adapter = new RcySAdapter<Integer>(this, R.layout.item_icon) {
            @Override
            public void convert(RcyHolder holder, Integer integer, int position) {

                ImageView view = holder.getView(R.id.image);
                view.setImageResource(integer);
            }
        };
        adapter.setRefreshView(refresh);
        adapter.setData(iconAs, true);
    }
}
