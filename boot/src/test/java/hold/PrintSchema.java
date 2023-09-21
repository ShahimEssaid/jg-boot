package hold;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

public interface PrintSchema extends ITestBase {

    @Test
    default void printSchema(TestReporter reporter) {

//        JanusGraph g = null;
//        JanusGraphManagement janusGraphManagement = g.openManagement();

        Client client = getClient();
        ResultSet results = client.submit("mgmt = graph.openManagement();\n" + "mgmt.printSchema();");
        reporter.publishEntry(results.one().toString());

    }
}
