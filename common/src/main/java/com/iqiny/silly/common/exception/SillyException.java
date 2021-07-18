/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common.exception;

public class SillyException extends RuntimeException {

    public SillyException(String message) {
        super(message);
    }

    public SillyException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SillyException newInstance(String message) {
        return new SillyException(message);
    }

    public static SillyException newInstance(String message, Throwable cause) {
        return new SillyException(message, cause);
    }
}
