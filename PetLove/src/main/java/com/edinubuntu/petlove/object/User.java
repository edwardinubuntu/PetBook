package com.edinubuntu.petlove.object;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/5.
 */
@Table(name = "Users")
public class User extends Model {

    @Column(name = "Type")
    private Type type;

    @Column(name = "CreatedDate")
    private Date createdDate;

    public User() {
        super();
        this.createdDate = new Date();
    }

    public User(Type type) {
        this();
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Pet> getActivePets() {
        return getMany(Pet.class, "Owner");
    }

    @Override
    public String toString() {
        return "User{" +
                ", type=" + type +
                ", createdDate=" + createdDate +
                "} " + super.toString();
    }

    public enum Type {
        FRIEND, PLAYER
    }
}
