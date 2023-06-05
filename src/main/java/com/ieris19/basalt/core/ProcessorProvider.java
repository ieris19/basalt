package com.ieris19.basalt.core;

/**
 * ProcessorProvider is a class that provides a Processor implementation.
 * <p>
 * ProcessorProvider are used to provide custom implementations to the Basalt.
 * Although by default, Basalt requires a mode to be specified from the
 * {@link ProcessorMode} enum, it is possible to provide a custom ProcessorProvider
 * to the BasaltFactory to instantiate custom implementation from wrapper applications.
 * </p>
 */
public interface ProcessorProvider
{
    Class<? extends AbstractProcessor> getImplementation();
}
