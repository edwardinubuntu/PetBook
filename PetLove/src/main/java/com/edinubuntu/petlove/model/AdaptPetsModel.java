package com.edinubuntu.petlove.model;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by edward_chiang on 13/8/12.
 */
public class AdaptPetsModel {

    private AsyncHttpClient asyncHttpClient;

    private boolean loading;

    public AdaptPetsModel() {
        asyncHttpClient = new AsyncHttpClient();
    }

    public void load(AsyncHttpResponseHandler responseHandler) {

        String apiCall = "http://60.199.253.136/api/action/datastore_search?resource_id=c57f54e2-8ac3-4d30-bce0-637a8968796e&limit=500";
        asyncHttpClient.get(apiCall, new RequestParams(), responseHandler);

    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
