package com.edinubuntu.petlove.active;

import com.activeandroid.Model;
import com.edinubuntu.petlove.model.AsyncModel;

/**
 * Created by edward_chiang on 13/10/3.
 */
public interface ActiveObjectLoader<T extends Model> {

    public T selectObject();

    public void loadObject(AsyncModel asyncModel);

    public void refreshObjectToViews(T object);
}
