package com.essaid.jg.boot.tests;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@SpringBootTest
public abstract class TestBase implements ITest {

    @Autowired
    GraphTraversalSource graphTraversalSource;


    @Override
    public GraphTraversalSource source() {
        return graphTraversalSource;
    }
}
