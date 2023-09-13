package com.essaid.jg.boot;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.graphdb.grpc.JanusGraphManagerClient;
import org.janusgraph.graphdb.grpc.JanusGraphManagerServiceGrpc;
import org.janusgraph.graphdb.grpc.schema.SchemaManagerServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

@SpringBootTest()
public class TestBase implements ITestBase {


    final JGServer server;
    protected GraphTraversalSource traversal;
    protected Cluster cluster;
    protected Client client;


    public JanusGraphManagerClient getManagerClient() {
        return managerClient;
    }

    @Autowired
    JanusGraphManagerClient managerClient;

    @GrpcClient("test")
    JanusGraphManagerServiceGrpc.JanusGraphManagerServiceBlockingStub managerStub;
    @GrpcClient("test")
    SchemaManagerServiceGrpc.SchemaManagerServiceBlockingStub schemaStub;

    TestBase(JGServer server) {
        this.server = server;
    }

    public JanusGraphManagerServiceGrpc.JanusGraphManagerServiceBlockingStub getManagerStub() {
        return managerStub;
    }

    public SchemaManagerServiceGrpc.SchemaManagerServiceBlockingStub getSchemaStub() {
        return schemaStub;
    }

    @Override
    public JGServer getServer() {
        return server;
    }

    public GraphTraversalSource getTraversal() {
        return traversal;
    }

    @Override
    public Cluster getCluster() {
        return cluster;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @BeforeAll
    void setupTraversalSource() throws Exception {
        server.start();
        traversal = traversal().withRemote("config/client/remote-graph.properties");
        cluster = Cluster.open("config/client/remote-objects.yaml");
        client = this.cluster.connect();

    }

    @AfterAll
    void close() {
        cluster.close();
        client.close();
    }
}
