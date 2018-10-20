package com.github.bap.event.handler.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 周广
 **/
@Service
public class SessionService {
    public static Map<String, String> SESSION = new ConcurrentHashMap<>();


    public void saveSessionId(String sid, String loginName) {
        SESSION.put(sid, loginName);
    }

    public void removeSid(String sid) {
        SESSION.remove(sid);
    }


    public boolean verifySessionId(String sid) {
        if (SESSION.containsKey(sid)) {
            return true;
        }
        return false;
    }

    public String getLoginName(String sid) {
        return SESSION.get(sid);
    }
}
