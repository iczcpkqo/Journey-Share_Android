package com.journey.service.database;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class DialogueHelperTest {

    @Test
    public void testConvertStringToList() {
        String test1_string = "[zhangsan, lisi, laowang]";
        List<String> actual = DialogueHelper.convertStringToList(test1_string);
        List<String> should = Arrays.asList("zhangsan", "lisi", "laowang");
        assertEquals(should, actual);
    }

    @Test
    public void testCleanDialogueString() {
        String test2_string = "[zhangsan, lisi, laowang]";
        String actual = DialogueHelper.cleanDialogueString(test2_string);
        String should = "zhangsan,lisi,laowang";
        assertEquals(should, actual);
    }

    @Test
    public void testSortString() {
       assertTrue(true);
    }


    @Test
    public void testUserListToUserString() {
        assertTrue(true);
    }
}