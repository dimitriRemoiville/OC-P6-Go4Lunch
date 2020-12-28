package com.dimitri.remoiville.go4lunch;

import com.dimitri.remoiville.go4lunch.model.User;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.UUID;

public class UserUnitTest {

    private final String mLastName = "Eslinger";
    private final String mMail = "lisabeth.eslinger@gmail.com";
    private final String mFirstName = "Lisabeth";

    @Test
    public void addUser() {
        String uId = UUID.randomUUID().toString();
        User user = new User(UUID.randomUUID().toString(), mFirstName, mLastName, mMail, null);

        assertFalse(user.hasChosenNotification());
        assertNull(user.getRestaurantID());
        assertNull(user.getRestaurantName());
    }

}