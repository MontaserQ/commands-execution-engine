package com.commands.execution.engine.core.command.contract;

import com.commands.execution.engine.common.TypeUtility;

public interface CommandData<C extends Command<?>> {

    default Class<C> getCommandClass() {
        return TypeUtility.getGenericTypeParameter(this.getClass(), 0);
    }

}
