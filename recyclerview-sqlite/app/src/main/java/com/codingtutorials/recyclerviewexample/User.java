package com.codingtutorials.recyclerviewexample;

/** One row of the Userdetails table. Keeping the three values together makes the adapter simpler. */
public class User {
    public final String name;
    public final String email;
    public final String age;

    public User(String name, String email, String age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
