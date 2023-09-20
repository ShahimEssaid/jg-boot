package com.essaid.jg.boot.tests;

import com.essaid.jg.boot.JGServer;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.server.GraphManager;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.util.MessageSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({TestSettings.class})
public class TestConfiguration {

    private final TestSettings testSettings;
    private final JGServer server;

    TestConfiguration(TestSettings testSettings, JGServer server) {
        this.testSettings = testSettings;
        this.server = server;
    }

    @Bean
    Cluster testCluster() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        TestSettings.RemoteSettings remoteSettings = testSettings.getRemote();
        if (remoteSettings == null) return null;

        TestSettings.ClusterSettings clusterSettings = remoteSettings.getCluster();
        if (clusterSettings == null) return null;

        Cluster.Builder build = Cluster.build();
        build.port(clusterSettings.getPort());
        build.addContactPoints(clusterSettings.getHosts().toArray(new String[0]));

        TestSettings.SerializerSettings serializerSettings = clusterSettings.getSerializer();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<? extends MessageSerializer> aClass = (Class<? extends MessageSerializer>) loader.loadClass(serializerSettings.getClassName());
        MessageSerializer<MessageSerializer> serializer = aClass.getConstructor().newInstance();
        serializer.configure(new HashMap<>(serializerSettings.getConfig()), null);
        build.serializer(serializer);

        return build.create();
    }


    @Bean
    GraphTraversalSource testTraversalSource() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        GraphTraversalSource source = null;

        if (testSettings.getRemote() != null){
            DriverRemoteConnection remoteConnection = DriverRemoteConnection.using(testCluster(), testSettings.remote.getSourceName());
            AnonymousTraversalSource<GraphTraversalSource> traversal = AnonymousTraversalSource.traversal();
            source = traversal.withRemote(remoteConnection);
        } else {
            if(server.isStarted()){
                GraphManager graphManager = server.getGremlinServer().getServerGremlinExecutor().getGraphManager();
                Graph graph = graphManager.getGraph(testSettings.embedded.getGraphName());
                source = AnonymousTraversalSource.traversal().withEmbedded(graph);

            }
        }
        return source;
    }


}
