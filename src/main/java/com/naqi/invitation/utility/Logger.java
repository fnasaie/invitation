package com.naqi.invitation.utility;

import org.slf4j.LoggerFactory;

public class Logger {

    public static final org.slf4j.Logger application = LoggerFactory.getLogger("application");
    
     public static final org.slf4j.Logger cdr = LoggerFactory.getLogger("cdr");

    public static String pattern = "[v{}][{}] {}{}";
}
