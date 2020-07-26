package com.example.kindom;

import androidx.test.filters.SmallTest;

import com.example.kindom.utils.CalendarHandler;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@SmallTest
public class CalendarTest {

    @Test
    public void testGetDateString1() {
        String result = CalendarHandler.getDateString(2020, 0, 1);
        Assert.assertEquals("01/01/2020", result);
    }

    @Test
    public void testGetDateString2() {
        String result = CalendarHandler.getDateString(2020, 11, 12);
        Assert.assertEquals("12/12/2020", result);
    }

    @Test
    public void testGetTimeString1() {
        String result = CalendarHandler.getTimeString(0, 0);
        Assert.assertEquals("12:00 AM", result);
    }

    @Test
    public void testGetTimeString2() {
        String result = CalendarHandler.getTimeString(13, 0);
        Assert.assertEquals("01:00 PM", result);
    }

    @Test
    public void testCheckIfExpired1() {
        boolean result = CalendarHandler.checkIfExpired(
                CalendarHandler.getDateString(2020, 1, 1),
                CalendarHandler.getTimeString(0, 0));
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckIfExpired2() {
        boolean result = CalendarHandler.checkIfExpired(
                CalendarHandler.getDateString(2030, 1, 1),
                CalendarHandler.getTimeString(0, 0));
        Assert.assertFalse(result);
    }
}