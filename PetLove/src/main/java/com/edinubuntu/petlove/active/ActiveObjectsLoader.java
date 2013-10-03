package com.edinubuntu.petlove.active;

import com.activeandroid.Model;
import com.edinubuntu.petlove.model.AsyncModel;

/**
 * Created by edward_chiang on 13/10/3.
 */
public interface ActiveObjectsLoader<T extends Model> {

    public java.util.List<T> selectObjects(boolean more);

    public void loadObjects(AsyncModel asyncModel, boolean more);

    public void refreshObjectsToViews(java.util.List<T> object);
}
