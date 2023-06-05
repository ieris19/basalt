package com.ieris19.basalt.core;

import java.io.File;

/**
 * This is the abstract class that defines the public interface for all processors,
 * regardless of their type.
 */
public interface AbstractProcessor
{
void init(File source, File output);

void process(File target);

void compose();
}
