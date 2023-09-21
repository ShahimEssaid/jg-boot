package hold;

import com.essaid.jg.boot.JGServer;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.graphdb.grpc.JanusGraphManagerClient;
import org.janusgraph.graphdb.grpc.JanusGraphManagerServiceGrpc;
import org.janusgraph.graphdb.grpc.schema.SchemaManagerServiceGrpc;

public interface ITestBase {


    JGServer getServer();

    Cluster getCluster();

    Client getClient();

    GraphTraversalSource getTraversal();

    public JanusGraphManagerServiceGrpc.JanusGraphManagerServiceBlockingStub getManagerStub();

    public SchemaManagerServiceGrpc.SchemaManagerServiceBlockingStub getSchemaStub();

    public JanusGraphManagerClient getManagerClient();

}
