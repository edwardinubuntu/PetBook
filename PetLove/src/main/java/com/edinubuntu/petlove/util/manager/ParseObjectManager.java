package com.edinubuntu.petlove.util.manager;

import android.app.Activity;
import android.provider.Settings;
import com.edinubuntu.petlove.object.User;
import com.parse.*;

import java.util.Calendar;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/6.
 */
public class ParseObjectManager {

    private static ParseObjectManager instance;

    private Activity activity;

    public ParseObjectManager(Activity activity) {
        this.activity = activity;
    }

    public static synchronized ParseObjectManager getInstance(Activity activity) {
        if (instance == null) {
            instance = new ParseObjectManager(activity);
        }
        return instance;
    }

    public Activity getActivity() {
        return activity;
    }

    public void initialize() {
        Parse.initialize(getActivity(), "aTeXcePV40lqj0fy4mqPa2ba6ILqMZ9gOl6ADKOi", "PRTRJG5SpRl9G8wf9mZURyfaATBXShhmqjlChEQQ");
        ParseAnalytics.trackAppOpened(getActivity().getIntent());
    }

    public String getUserUniqueId() {
        return Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void saveAndUpdateUser(User currentPlayer) {
        final String uniqueId = getUserUniqueId();

        // Update record to the server
        ParseQuery<ParseObject> findUserQuery = ParseQuery.getQuery("UserObject");
        findUserQuery.whereEqualTo("AndroidID", uniqueId);

        final User finalCurrentPlayer = currentPlayer;
        findUserQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null && !parseObjects.isEmpty()) {
                    ParseObject updatedObject = parseObjects.get(0);

                    Calendar beforeTimeMinute = Calendar.getInstance(); // creates calendar
                    beforeTimeMinute.add(Calendar.MINUTE, -10); // adds one minute

                    // Only update every 10 minutes
                    if (!(updatedObject.getDate("LastPlayAt") != null && updatedObject.getDate("LastPlayAt").after(beforeTimeMinute.getTime()))) {
                        updatedObject.put("PetsCount", finalCurrentPlayer.getActivePets().size());
                        updatedObject.put("PlayCount", updatedObject.getInt("PlayCount") + 1);
                        updatedObject.put("LastPlayAt", Calendar.getInstance().getTime());
                        updatedObject.saveInBackground();
                    }
                } else {
                    ParseObject parseObject = new ParseObject("UserObject");
                    parseObject.put("AndroidID", uniqueId);
                    parseObject.put("UserCreatedAt", finalCurrentPlayer.getCreatedDate());
                    parseObject.put("LastPlayAt", Calendar.getInstance().getTime());
                    parseObject.put("PlayCount", 1);
                    parseObject.put("PetsCount", finalCurrentPlayer.getActivePets().size());
                    parseObject.saveInBackground();
                }
            }
        });
    }

}
