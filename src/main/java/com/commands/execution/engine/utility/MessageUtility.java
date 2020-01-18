package com.commands.execution.engine.utility;

import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@UtilityClass
public class MessageUtility {

    private static final ResourceBundle messagesResource = ResourceBundle.getBundle("messages.core");

    public static final String MISSING_ARGUMENT = "MISSING_ARGUMENT";


    public static String getMessage(final String key, final Object... args) {
        final String msgString = messagesResource.getString(key);
        return MessageFormat.format(msgString, args);
    }
}
