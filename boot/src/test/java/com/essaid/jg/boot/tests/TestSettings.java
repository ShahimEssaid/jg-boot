package com.essaid.jg.boot.tests;

import lombok.Getter;
import lombok.Setter;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "jg.tests.settings")
public class TestSettings {

    RemoteSettings remote;
    EmbeddedSettings embedded;

    @Setter
    @Getter
    public static class EmbeddedSettings {
        private String graphName;
    }

    @Setter
    @Getter
    public static class RemoteSettings {
        private ClusterSettings cluster;
        private String sourceName;
    }

    @Setter
    @Getter
    public static class SerializerSettings {
        private String className;
        private Map<String, List<String>> config = new HashMap<>();
    }

    @Setter
    @Getter
    public static class ClusterSettings {
        private List<String> hosts = new ArrayList<>();
        private int port = 8182;
        private SerializerSettings serializer = new SerializerSettings();
    }


}
