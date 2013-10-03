package com.edinubuntu.petlove.object;

import com.edinubuntu.petlove.R;

/**
 * Created by edward_chiang on 13/10/3.
 */
public class DrawerAction {

    private String title;

    private ActionType actionType;

    public DrawerAction(String title, ActionType actionType) {
        this.title = title;
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResource() {
        int imageResource = 0;
        if (getActionType() == ActionType.HOME) {
            imageResource = R.drawable.ic_action_tiles_large;
        } else if (getActionType() == ActionType.USER_PROFILE) {
            imageResource = R.drawable.ic_action_user;
        } else if (getActionType() == ActionType.PET_ALL_RECORDS) {
            imageResource = R.drawable.ic_action_search;
        } else if (getActionType() == ActionType.FRIENDS) {
            imageResource = R.drawable.ic_action_star_10;
        } else if (getActionType() == ActionType.TODO_LIST) {
            imageResource = R.drawable.ic_action_bookmark;
        }
        return imageResource;
    }

    public enum ActionType {
        HOME, USER_PROFILE, PET_ALL_RECORDS, FRIENDS, TODO_LIST
    }
}
