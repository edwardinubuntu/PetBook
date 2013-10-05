package com.edinubuntu.petlove.object;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by edward_chiang on 13/10/5.
 */
@Table(name = "Pets")
public class Pet extends Model {

    @Column(name = "Profile")
    private Record profile;

    @Column(name = "Name")
    private String name;

    @Column(name = "Owner")
    private User owner;

    public Pet() {
        super();
    }

    public Record getProfile() {
        return profile;
    }

    public void setProfile(Record profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
