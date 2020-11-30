package com.dimitri.remoiville.go4lunch.viewmodel;

import com.dimitri.remoiville.go4lunch.model.User;

public class SingletonCurrentUser {

    private static SingletonCurrentUser INSTANCE = null;
    private static User CURRENT_USER = null;

    private SingletonCurrentUser() {
    }

    public static SingletonCurrentUser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonCurrentUser();
        }
        return (INSTANCE);
    }

    public void setCurrentUser(User user) {
        if (CURRENT_USER == null) {
            CURRENT_USER = user;
        }
    }

    public User getCurrentUser() {
        return CURRENT_USER;
    }
}
