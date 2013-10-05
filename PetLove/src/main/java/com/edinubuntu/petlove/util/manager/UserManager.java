package com.edinubuntu.petlove.util.manager;

import com.activeandroid.query.Select;
import com.edinubuntu.petlove.object.User;

/**
 * Created by edward_chiang on 13/10/5.
 */
public class UserManager {

    public static User getCurrentPlayer() {
        return new Select().from(User.class).where("Type = '" +User.Type.PLAYER+ "'").executeSingle();
    }
}
