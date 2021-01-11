package com.dimitri.remoiville.go4lunch;

import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.UUID;

public class SingletonUnitTest {

    private final String mLastName = "Eslinger";
    private final String mMail = "lisabeth.eslinger@gmail.com";
    private final String mFirstName = "Lisabeth";

    @Test
    public void singletonReturnGoodUser() {

        String uId = UUID.randomUUID().toString();
        User user = new User(UUID.randomUUID().toString(), mFirstName, mLastName, mMail, null);

        SingletonCurrentUser.getInstance().setCurrentUser(user);

        User getUser = SingletonCurrentUser.getInstance().getCurrentUser();

        assertEquals(user, getUser);

    }
}
