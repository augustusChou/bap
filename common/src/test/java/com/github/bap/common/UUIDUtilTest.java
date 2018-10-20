package com.github.bap.common;

import org.junit.Assert;

/**
 * @author 周广
 **/
public class UUIDUtilTest {

    @org.junit.Test
    public void getUUID() {
        String uuid = UUIDUtil.getUUID();
        Assert.assertEquals(32, uuid.length());
        Assert.assertTrue(!uuid.contains("-"));
    }
}