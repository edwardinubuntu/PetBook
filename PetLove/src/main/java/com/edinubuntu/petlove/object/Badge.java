package com.edinubuntu.petlove.object;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by edward_chiang on 13/10/6.
 */
@Table(name = "Badges")
public class Badge extends Model{

    @Column(name = "Type")
    private Type type;

    public Badge() {
        super();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Badge{" +
                ", type=" + type +
                "} " + super.toString();
    }

    public Badge(Type type) {
        this();
        this.type = type;
    }

    public enum Type {
        MOST_PLAY_COUNT,
        TOP_10_OLD,
        NEWBIE,
        STUDENT;
    }
}
