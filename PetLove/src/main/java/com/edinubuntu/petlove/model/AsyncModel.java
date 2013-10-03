package com.edinubuntu.petlove.model;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by edward_chiang on 13/10/3.
 */
public abstract class AsyncModel {

    private boolean loading;

    public abstract void load(AsyncHttpResponseHandler responseHandler);

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
