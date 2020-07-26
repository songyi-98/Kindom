package com.example.kindom;

import androidx.test.filters.SmallTest;

import com.example.kindom.utils.Validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@SmallTest
public class ValidatePasswordTest {

    @Test
    public void testIsValidPassword1() {
        boolean result = Validation.isValidPassword("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsValidPassword2() {
        boolean result = Validation.isValidPassword("password");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsValidPassword3() {
        boolean result = Validation.isValidPassword("password1");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsValidPassword4() {
        boolean result = Validation.isValidPassword("Pass1");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsValidPassword5() {
        boolean result = Validation.isValidPassword("Password1");
        Assert.assertTrue(result);
    }
}