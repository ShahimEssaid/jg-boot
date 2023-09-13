package com.essaid.jg.boot;

import com.essaid.jg.boot.tests.AddVertext;
import com.essaid.jg.boot.tests.PrintSchema;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainTests extends TestBase
        implements  AddVertext, PrintSchema {



    @Autowired
    MainTests(JGServer server) {
        super(server);
    }
}
