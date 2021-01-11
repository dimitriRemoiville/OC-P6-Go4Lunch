package com.dimitri.remoiville.go4lunch;

import com.dimitri.remoiville.go4lunch.utils.UtilsSettings;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilsSettingsUnitTest {

    @Test
    public void checkEmail() {
        assertTrue(UtilsSettings.validateEmail("dimr313@gmail.com"));
        assertFalse(UtilsSettings.validateEmail("dimr313gmail.com"));
        assertFalse(UtilsSettings.validateEmail("dimr313@_gmail.com"));
        assertFalse(UtilsSettings.validateEmail("dimr313@_gmail.c-om"));
    }

    @Test
    public void checkName() {
        assertFalse(UtilsSettings.validateName("Dimitri"));
        assertTrue(UtilsSettings.validateName(""));
    }
}
