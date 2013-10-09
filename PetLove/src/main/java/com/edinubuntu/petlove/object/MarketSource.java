package com.edinubuntu.petlove.object;

import android.content.Context;
import com.edinubuntu.petlove.R;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class MarketSource {

    public static final int TYPE_SELF = 0;
    public static final int TYPE_TAIPEI_OPEN_DATA = 1;

    private int type;

    private Context context;

    public MarketSource(int type, Context context) {
        this.type = type;
        this.context = context;
    }

    public int getType() {
        return type;
    }

    public String getImageURL() {
        switch (getType()) {
            case TYPE_SELF:
                return "http://1.bp.blogspot.com/_l4t59F04HW4/S_2CeHFVoMI/AAAAAAAACQ4/q0TOMfnlwrI/s1600/IMG_9939.JPG";
            case TYPE_TAIPEI_OPEN_DATA:
                return "http://bigboat.typepad.com/.a/6a00d83451d96d69e20147e319a943970b-320wi";
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public String getTitle() {
        switch (getType()) {
            case TYPE_SELF:
                return getContext().getString(R.string.market_source_self_title);
            case TYPE_TAIPEI_OPEN_DATA:
                return getContext().getString(R.string.market_source_taipei_open_data_title);
        }
        return null;
    }
}
