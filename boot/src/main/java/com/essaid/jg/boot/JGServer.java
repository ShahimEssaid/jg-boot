package com.essaid.jg.boot;

import com.jcabi.manifests.Manifests;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.tinkerpop.gremlin.server.GraphManager;
import org.apache.tinkerpop.gremlin.server.GremlinServer;
import org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor;
import org.janusgraph.graphdb.grpc.JanusGraphContextHandler;
import org.janusgraph.graphdb.grpc.JanusGraphManagerServiceImpl;
import org.janusgraph.graphdb.grpc.schema.SchemaManagerImpl;
import org.janusgraph.graphdb.management.JanusGraphManager;
import org.janusgraph.graphdb.server.JanusGraphServer;
import org.janusgraph.graphdb.server.JanusGraphSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
public class JGServer extends JanusGraphServer {

    private static final Logger logger = LoggerFactory.getLogger(JGServer.class);

    private final JGBootSettings settings;
    private CompletableFuture<Void> serverStarted;
    private CompletableFuture<Void> serverStopped = null;
    private GremlinServer gremlinServer = null;
    private Server grpcServer;

    public JGServer(JGBootSettings settings) {
        super(null);
        this.settings = settings;
        this.gremlinServer = new GremlinServer(settings);
    }

    private static void configure(ServerGremlinExecutor serverGremlinExecutor) {
        GraphManager graphManager = serverGremlinExecutor.getGraphManager();
        if (!(graphManager instanceof JanusGraphManager)) {
            return;
        }
        ((JanusGraphManager) graphManager).configureGremlinExecutor(serverGremlinExecutor.getGremlinExecutor());
    }

    private static void printHeader() {
        logger.info(getHeader());
        logger.info("JanusGraph Version: {}", Manifests.read(MANIFEST_JANUSGRAPH_VERSION_ATTRIBUTE));
        logger.info("TinkerPop Version: {}", Manifests.read(MANIFEST_TINKERPOP_VERSION_ATTRIBUTE));
    }

    private static String getHeader() {
        return "                                                                      " + System.lineSeparator() +
                "   mmm                                mmm                       #     " + System.lineSeparator() +
                "     #   mmm   m mm   m   m   mmm   m\"   \"  m mm   mmm   mmmm   # mm  " + System.lineSeparator() +
                "     #  \"   #  #\"  #  #   #  #   \"  #   mm  #\"  \" \"   #  #\" \"#  #\"  # " + System.lineSeparator() +
                "     #  m\"\"\"#  #   #  #   #   \"\"\"m  #    #  #     m\"\"\"#  #   #  #   # " + System.lineSeparator() +
                " \"mmm\"  \"mm\"#  #   #  \"mm\"#  \"mmm\"   \"mmm\"  #     \"mm\"#  ##m#\"  #   # " + System.lineSeparator() +
                "                                                         #            " + System.lineSeparator() +
                "                                                         \"            " + System.lineSeparator();
    }

    @Override
    public JanusGraphSettings getJanusGraphSettings() {
        return settings;
    }

    public synchronized CompletableFuture<Void> start() {

        printHeader();
        if (this.serverStarted != null) {
            return this.serverStarted;
        }

        serverStarted = new CompletableFuture<>();
        try {
            logger.info("Configuring JGServer Server from {}", this.settings);

            CompletableFuture<Void> grpcServerFuture = CompletableFuture.completedFuture(null);
            if (settings.getGrpcServer().isEnabled()) {
                grpcServerFuture = CompletableFuture.runAsync(() -> {
                    GraphManager graphManager = gremlinServer.getServerGremlinExecutor().getGraphManager();
                    this.grpcServer = createGrpcServer(this.settings, graphManager);
                    try {
                        grpcServer.start();
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                });
            }
            CompletableFuture<Void> gremlinServerFuture = gremlinServer.start()
                    .thenAcceptAsync(JGServer::configure);
            serverStarted = CompletableFuture.allOf(gremlinServerFuture, grpcServerFuture);
        } catch (Exception ex) {
            serverStarted.completeExceptionally(ex);
        }
        return serverStarted;

    }

    public synchronized boolean isStarted() {
        if (this.serverStarted == null) return false;
        this.serverStarted.join();
        return true;
    }

    public synchronized GremlinServer getGremlinServer() {
        return gremlinServer;
    }

    private Server createGrpcServer(JGBootSettings janusGraphSettings, GraphManager graphManager) {
        JanusGraphContextHandler janusGraphContextHandler = new JanusGraphContextHandler(graphManager);
        return ServerBuilder
                .forPort(janusGraphSettings.getGrpcServer().getPort())
                .addService(new JanusGraphManagerServiceImpl(janusGraphContextHandler))
                .addService(new SchemaManagerImpl(janusGraphContextHandler))
                .build();
    }

    @PreDestroy
    public void close() {
        stop().join();
    }

    public synchronized CompletableFuture<Void> stop() {
        if (gremlinServer == null) {
            return CompletableFuture.completedFuture(null);
        }
        if (serverStopped != null) {
            return serverStopped;
        }
        if (grpcServer != null) {
            grpcServer.shutdownNow();
        }
        serverStopped = gremlinServer.stop();
        return serverStopped;
    }

}
