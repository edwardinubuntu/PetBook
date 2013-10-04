package com.edinubuntu.petlove.object;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by edward_chiang on 13/10/4.
 */
@Table(name = "Events")
public class Event extends Model {

    @Column(name = "Action")
    private Action action;

    @Column(name = "Message")
    private String message;

    @Column(name = "CreatedDate")
    private Date createdDate;

    public Event() {
        super();
    }

    public Event(Action action) {
        super();
        this.action = action;
        this.createdDate = new Date();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        if (!super.equals(o)) return false;

        Event event = (Event) o;

        if (action != event.action) return false;
        if (createdDate != null ? !createdDate.equals(event.createdDate) : event.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    public enum Action {
        VISIT_MARKET_ALL,
        VISIT_MARKET_DOG,
        VISIT_MARKET_CAT
    }
}
