package com.ieris19.basalt.core;

public enum ProcessorMode implements ProcessorProvider
{
    BASE(BaseProcessor.class);

private Class<? extends AbstractProcessor> implementation;

ProcessorMode(Class<? extends AbstractProcessor> implementation)
{
    this.implementation = implementation;
}

public static ProcessorMode of(String mode)
{
    return ProcessorMode.valueOf(mode.toUpperCase());
}

public Class<? extends AbstractProcessor> getImplementation()
{
    return this.implementation;
}
}
