package com.journey.service.database;

import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class DebugHelperTest {
    private static ArrayList<String> emailList = Lists.newArrayList("iris@123.com",
            "liu@tcd.com",
            "mao@tcd.com",
            "yan@tcd.com");

    @Test
    public void testGetOneRandomEmail() { Assert.assertEquals(1,1);
    }


    @Test
    public void getTwoRandomEmail() {
        Assert.assertEquals(1,1);
    }

    @Test
    public void getNRandomEmail() {
        Assert.assertEquals(1,1);
    }
}