package com.github.bap.common;

import java.util.UUID;

/**
 * @author 周广
 **/
public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
