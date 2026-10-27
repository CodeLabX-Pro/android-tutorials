package com.codingtutorials.recyclerfirebase;

import com.google.firebase.database.Exclude;

/**
 * One user in the database. Firebase fills these fields by name, so it needs
 * public fields (or getters) and an empty constructor.
 */
public class User {
    public String name;
    public String email;
    public String age;

    /** The database key, used to delete this user. @Exclude keeps it out of the saved data. */
    @Exclude
    public String key;

    public User() {
    }

    public User(String name, String email, String age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
