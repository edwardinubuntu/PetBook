package com.edinubuntu.petlove.util.manager;

import android.content.Context;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.Event;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class DisplayTextManager {

    private Context context;

    private static DisplayTextManager instance;

    public DisplayTextManager(Context context) {
        this.context = context;
    }

    public static synchronized DisplayTextManager newInstance(Context context) {
        if (instance == null) {
            instance = new DisplayTextManager(context);
        }
        return instance;
    }

    public String getType(String typeName) {
        if (typeName.equals(context.getString(R.string.record_type_dog))) {
            return context.getString(R.string.dropdown_selection_dog);
        } else if (typeName.equals(context.getString(R.string.record_type_cat))) {
            return context.getString(R.string.dropdown_selection_cat);
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public String getMessageText(Event event) {
        String eventText = new String();
        switch (event.getAction()) {
            case VISIT_MARKET_ALL:
                eventText = getContext().getString(R.string.event_message_visit_market_all);
                break;
            case VISIT_MARKET_CAT:
                eventText = getContext().getString(R.string.event_message_visit_market_cat);
                break;
            case VISIT_MARKET_DOG:
                eventText = getContext().getString(R.string.event_message_visit_market_dog);
                break;
            case VISIT_MARKET_QUESTIONS_ANSWER:
                eventText = getContext().getString(R.string.event_message_visit_question_answer);
                break;
            case VISIT_MARKET_SUGGESTIONS:
                eventText = getContext().getString(R.string.event_message_visit_suggestions);
                break;
            case PET_ADAPT_SUCCESS:
                eventText = getContext().getString(R.string.event_message_pet_adapt_success);
                break;
            case USER_PROFILE_CREATE:
                eventText = context.getString(R.string.event_message_welcome);
                break;
        }
        return eventText;
    }
}
