package com.github.bap.event.handler.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 周广
 **/
@Component
public class EnvService {

    @Value("${run.env}")
    private String env;

    public EnvEnum getEnv() {
        return EnvEnum.getEnum(env);
    }

    public static enum EnvEnum {

        /**
         * 用于冗余
         */
        UNKNOWN("unknown", "未知环境"),
        DEV("dev", "开发环境"),
        TEST("test", "测试环境"),
        PROD("prod", "生产环境"),
        ;
        private String env;
        private String desc;

        EnvEnum(String env, String desc) {
            this.env = env;
            this.desc = desc;
        }

        public static EnvEnum getEnum(String env) {
            for (EnvEnum value : values()) {
                if (value.env.equalsIgnoreCase(env)) {
                    return value;
                }
            }
            return UNKNOWN;
        }

        public String getEnv() {
            return env;
        }

        public String getDesc() {
            return desc;
        }
    }

}
