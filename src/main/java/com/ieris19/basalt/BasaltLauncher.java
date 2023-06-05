package com.ieris19.basalt;

import com.ieris19.basalt.core.AbstractProcessor;
import com.ieris19.basalt.core.BasaltFactory;
import com.ieris19.basalt.core.ProcessorMode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * BasaltLauncher is the entry point for any AbstractProcessor application.
 * It is responsible for parsing the command line arguments and creating
 * the corresponding AbstractProcessor instance.
 * <p>
 * The first argument is the mode, which needs to match one of the values in
 * {@link ProcessorMode}. The second argument is the path to the project directory.
 * </p>
 */
@Slf4j
public class BasaltLauncher
{
/**
 * Run a {@link AbstractProcessor} application. This method will create the
 * appropriate instance based on the previously validated arguments. If the
 * arguments are not validated beforehand, the application is likely to crash
 * or behave unexpectedly.
 *
 * @param mode The mode that the application is expected to run in.
 *             This needs to match one of the values in {@link ProcessorMode}.
 * @param path The path to the project directory.
 *             This is the root directory that contains the project The
 *             path will be scanned recursively for markdown files. Make
 *             sure that the directory contains ONLY those that you want
 *             to be included in the project.
 */
public static void run(ProcessorMode mode, File input, File output)
{
    log.info("Running Basalt in {} mode", mode);
    log.info("Parsing: {} -> {}", input.getAbsolutePath(), output.getAbsolutePath());
    AbstractProcessor processor = BasaltFactory.create(mode, input, output);
    processor.compose();
}

public static void main(String... args)
{
    log.info("BasaltLauncher called from cli with args: {}", (Object) args);
    try {
        assert args.length >= 2 : "BasaltLauncher requires at least two arguments: mode and path";
        BasaltLauncher launcher = new BasaltLauncher();
        ProcessorMode executionMode = launcher.parseMode(args[0]);
        log.trace("Parsed mode: {}", executionMode);
        File source = launcher.parsePath(args[1]);
        log.trace("Parsed source: {}", source);
        File target = args.length >= 3 ? launcher.parsePath(args[2]) : source;
        log.trace("Parsed target: {}", target);
        run(executionMode, source, target);
    } catch (Throwable t) {
        log.error("An error occurred while running Basalt", t);
        throw t;
    }
}

public ProcessorMode parseMode(String arg)
{
    try {
        return ProcessorMode.of(arg);
    } catch (IllegalArgumentException ex) {
        System.err.println(arg + " is not a valid mode for BasaltLauncher, defaulting to BASE mode.");
        throw ex;
    }
}

public File parsePath(String path)
{
    File target = new File(path);
    if (!validPath(target)) {
        throw new IllegalArgumentException(
                "The path provided is not valid: " + target.getAbsolutePath() + " must be an existing readable directory.");
    }
    return target;
}

private boolean validPath(File path)
{
    if (!path.exists()) {
        System.err.println("The path provided does not exist");
        return false;
    }
    if (!path.isDirectory()) {
        System.err.println("The path provided is not a directory");
        return false;
    }
    if (!path.canRead()) {
        System.err.println("The path provided is not readable");
        return false;
    }
    return true;
}
}
