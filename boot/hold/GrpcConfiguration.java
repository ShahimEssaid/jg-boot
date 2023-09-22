package com.essaid.jg.boot.grcp;

import com.essaid.jg.boot.JGServer;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import net.devh.boot.grpc.client.inject.GrpcClientBeans;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.tinkerpop.gremlin.server.GraphManager;
import org.janusgraph.graphdb.grpc.JanusGraphContextHandler;
import org.janusgraph.graphdb.grpc.JanusGraphManagerClient;
import org.janusgraph.graphdb.grpc.JanusGraphManagerServiceGrpc;
import org.janusgraph.graphdb.grpc.JanusGraphManagerServiceImpl;
import org.janusgraph.graphdb.grpc.schema.SchemaManagerClient;
import org.janusgraph.graphdb.grpc.schema.SchemaManagerImpl;
import org.janusgraph.graphdb.grpc.schema.SchemaManagerServiceGrpc;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@GrpcClientBeans({
        @GrpcClientBean(client = @GrpcClient("test"), clazz = JanusGraphManagerServiceGrpc.JanusGraphManagerServiceStub.class)
})

public class GrpcConfiguration {

    private final JGServer server;
    private final ConfigurableApplicationContext context;

    GrpcConfiguration(JGServer server, ConfigurableApplicationContext context) {
        this.server = server;
        this.context = context;
    }

    @Bean
    JanusGraphManagerClient graphManagerClient(JanusGraphManagerServiceGrpc.JanusGraphManagerServiceStub stub) {
        return new JanusGraphManagerClient(stub.getChannel());
    }



    @Bean
    GraphManager graphManager(JGServer jgServer) {
        return jgServer.getGremlinServer().getServerGremlinExecutor().getGraphManager();
    }

    @Bean
    JanusGraphContextHandler janusGraphContextHandler(GraphManager graphManager) {
        return new JanusGraphContextHandler(graphManager);
    }

    @GrpcService
    JanusGraphManagerServiceImpl janusGraphManagerService(JanusGraphContextHandler contextHandler) {
        return new JanusGraphManagerServiceImpl(contextHandler);
    }

    @GrpcService
    SchemaManagerImpl schemaManager(JanusGraphContextHandler contextHandler) {
        return new SchemaManagerImpl(contextHandler);
    }

}
