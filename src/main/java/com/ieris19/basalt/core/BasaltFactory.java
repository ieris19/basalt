package com.ieris19.basalt.core;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class BasaltFactory
{
private BasaltFactory() {}

public static AbstractProcessor create(ProcessorProvider provider, File sources, File output)
{
    AbstractProcessor processor;
    Class<? extends AbstractProcessor> implementation = provider.getImplementation();
    log.debug("Creating processor of type {}", implementation.getName());
    try {
        processor = implementation.getConstructor().newInstance();
    } catch (NoSuchMethodException e) {
        String str = "The processor implementation " + implementation.getName()
                + " does not have a default constructor.";
        throw new IllegalArgumentException(str, e);
    } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
        } else {
            throw new RuntimeException(cause);
        }
    } catch (InstantiationException e) {
        throw new RuntimeException("The provided implementation cannot be instantiated", e);
    } catch (IllegalAccessException e) {
        throw new RuntimeException("Basalt Core has no reflective access to"
                                           + " the provided implementation", e);
    }
    log.trace("Initializing processor with sources: {} | output: {}",
              sources, output);
    processor.init(sources, output);
    return processor;
}
}
