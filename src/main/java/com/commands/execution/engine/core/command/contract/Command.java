package com.commands.execution.engine.core.command.contract;

import com.commands.execution.engine.common.TypeUtility;
import com.commands.execution.engine.core.command.annotation.Required;
import com.commands.execution.engine.core.command.exception.CommandException;
import com.commands.execution.engine.utility.MessageUtility;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Command<D extends CommandData<?>> {

    public Class<D> getCommandDataClass() {
        return TypeUtility.getGenericTypeParameter(this.getClass(), 0);
    }

    public List<CommandData<? extends Command>> getCommandList(final D data) {
        return Collections.singletonList(data);
    }

    public abstract CommandResult execute(D data) throws CommandException;


    public void validate(final D data) throws CommandException {
        validateObject(data);
    }

    private void validateObject(final Object object) throws CommandException {
        for (final Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Required.class)) {
                validateField(object, field);
            }
        }
    }

    private void validateField(final Object object, final Field field) throws CommandException {
        try {
            final Required annotation = field.getAnnotation(Required.class);
            field.setAccessible(true);
            final Object fieldValue = field.get(object);

            if (fieldValue == null) {
                throw new CommandException(MessageUtility.getMessage(annotation.message(), (Object[]) annotation.messageArguments()));
            }

            if (isEmptyString(fieldValue) || isEmptyCollection(fieldValue)) {
                throw new CommandException(MessageUtility.getMessage(annotation.message(), (Object[]) annotation.messageArguments()));
            }
            if (annotation.innerValidation()) {
                validateObject(fieldValue);
            }

        } catch (final IllegalAccessException e) {
            throw new CommandException(e.getMessage(), e);
        }
    }

    private boolean isEmptyCollection(final Object fieldValue) {
        return fieldValue instanceof Collection && ((Collection) fieldValue).isEmpty();
    }

    private boolean isEmptyString(final Object fieldValue) {
        return fieldValue instanceof String && ((String) fieldValue).isEmpty();
    }
}
