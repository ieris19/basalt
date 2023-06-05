module ieris.basalt {
    // Compile time dependencies
    requires static lombok;

    // Compile and Runtime dependencies
    requires org.slf4j;
    
    requires flexmark;
    requires flexmark.util.ast;
}