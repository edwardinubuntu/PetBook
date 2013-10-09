package com.edinubuntu.petlove.util.manager.badge;

import android.app.Activity;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.object.Badge;
import com.edinubuntu.petlove.object.Event;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class BadgeStudentChecker implements BadgeChecker {
    @Override
    public Badge check(Activity activity) {
        java.util.List<Event> eventList = new Select().from(Event.class)
                .where("Action = '" + Event.Action.READ_KNOW_CONTENT +"'")
                .limit(10)
                .execute();
        if  (eventList != null && eventList.size() >= 3) {
            return new Badge(Badge.Type.STUDENT);
        }

        return null;
    }

    @Override
    public int getResourceName() {
        return 0;
    }
}
