package com.example.chatapp;

import com.google.firebase.database.Exclude;

import java.util.Locale;

/**
 * One person, saved at users/{uid} in the database.
 * Firebase needs public fields and an empty constructor to turn database data into a User.
 */
public class User {
    public String name;
    public String email;

    /** The database key. It isn't saved inside the user, so it's excluded. */
    @Exclude
    public String uid;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /** The first letter of the name, shown in the round avatar. */
    @Exclude
    public String initial() {
        return name == null || name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase(Locale.getDefault());
    }
}
