package com.basis.ui.mvvm;

import java.util.List;

public class RefPam<ND> {
    protected boolean refresh;
    protected List<ND> data;

    public RefPam(boolean refresh, List<ND> data) {
        this.refresh = refresh;
        this.data = data;
    }
}