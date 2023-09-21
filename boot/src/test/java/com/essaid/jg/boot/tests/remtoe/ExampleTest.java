package com.essaid.jg.boot.tests.remtoe;

import com.essaid.jg.boot.tests.TestBase;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.jupiter.api.Test;

public class ExampleTest extends TestBase  {

    @Test
    void createVertex(){
        GraphTraversalSource source = source();
        GraphTraversal<Vertex, Vertex> iterate = source.addV().iterate();
        Vertex next = source.V().next();
        System.out.println(next);
    }



}
