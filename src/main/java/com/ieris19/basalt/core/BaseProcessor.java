package com.ieris19.basalt.core;

import lombok.extern.slf4j.Slf4j;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class BaseProcessor implements AbstractProcessor
{
private File    source;
private File    output;
private Parser parser;
private HtmlRenderer  renderer;

@Override
public void init(File source, File output)
{
    this.source = source;
    this.output = output;
    this.parser = Parser.builder().build();
    this.renderer = HtmlRenderer.builder().build();
}

@Override
public void process(File target)
{
    Node document = parser.parse(readFile(target));
    String html = renderer.render(document);
    outputToFile(html, target.getName());
}

@Override
public void compose()
{
    log.debug("Forge process started!");
    List<File> sources = exploreDirectory(source);
    for (File markdown : sources) {
        process(markdown);
    }
}

public List<File> exploreDirectory(File folder)
{
    log.trace("Exploring directory: {}", folder);
    File[] contents = folder.listFiles();
    ArrayList<File> list = new ArrayList<>();
    if (contents == null) {
        return list;
    }
    for (File file : contents) {
        if (file.isDirectory()) {
            list.addAll(exploreDirectory(file));
        } else if (file.isFile() && file.getName().endsWith(".md") || file.getName().endsWith(".markdown")) {
            list.add(file);
        }
    }
    log.trace("Found {} markdown files", list.size());
    return list;
}

public String readFile(File file)
{
    StringBuilder targetContents = new StringBuilder();
    try (Scanner reader = new Scanner(file)) {
        while (reader.hasNext()) {
            targetContents.append(reader.nextLine()).append('\n');
        }
    } catch (IOException e) {
        throw new RuntimeException("File could not found", e);
    }
    return targetContents.toString();
}

public void outputToFile(String html, String name)
{
    String outputName = name.substring(0, name.lastIndexOf('.')) + ".html";
    File outputFile = new File(output, outputName);
    try {
        if (outputFile.createNewFile()) {
            log.trace("Created new file: {}", outputFile);
            writeToFile(html, outputFile);
        } else {
            if (outputFile.delete()) {
                log.trace("Deleted existing file: {}", outputFile);
                outputToFile(html, name);
            } else {
                log.error("Could not overwrite output file for {}", name);
                throw new IOException("Could not overwrite output file for " + name);
            }
        }
    } catch (IOException e) {
        throw new RuntimeException("Unexpected IO error", e);
    }
}

public void writeToFile(String html, File file) throws IOException
{
    try (FileWriter writer = new FileWriter(file)) {
        writer.write(html);
    }
}
}
