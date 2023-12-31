package com.essaid.jg.boot;

import io.netty.handler.ssl.ClientAuth;
import org.apache.tinkerpop.gremlin.server.Settings;
import org.janusgraph.graphdb.server.JanusGraphSettings;
import org.janusgraph.graphdb.server.util.JanusGraphSettingsUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@ConfigurationProperties(prefix = "jg.server")
@Component
public class JGBootSettings extends JanusGraphSettings {

    private final Environment environment;

    public JGBootSettings(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    void init() {
        JanusGraphSettingsUtils.configureDefaults(this);
        System.out.println(this);
    }

    void setHost(String host) {
        this.host = host;
    }

    void setPort(int port) {
        this.port = port;
    }

    void setThreadPoolWorker(int worker) {
        this.threadPoolWorker = worker;
    }

    void setUseEpollEventLoop(boolean useEpollEventLoop) {
        this.useEpollEventLoop = useEpollEventLoop;
    }

    void setGremlinPool(int gremlinPool) {
        this.gremlinPool = gremlinPool;
    }

    void setThreadPoolBoss(int threadPoolBoss) {
        this.threadPoolBoss = threadPoolBoss;
    }

    void setEvaluationTimeout(long evaluationTimeout) {
        this.evaluationTimeout = evaluationTimeout;
    }

    void setEvaluationTimeout(int evaluationTimeout) {
        this.evaluationTimeout = evaluationTimeout;
    }

    void setMaxInitialLineLength(int maxInitialLineLength) {
        this.maxInitialLineLength = maxInitialLineLength;
    }

    void setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }

    void setMaxChunkSize(int maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    void setMaxAccumulationBufferComponents(int maxAccumulationBufferComponent) {
        this.maxAccumulationBufferComponents = maxAccumulationBufferComponent;
    }

    void setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        this.writeBufferHighWaterMark = writeBufferHighWaterMark;
    }

    void setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    }

    void setIdleConnectionTimeout(long idleConnectionTimeout) {
        this.idleConnectionTimeout = idleConnectionTimeout;
    }

    void setKeepAliveInterval(long keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    void setStrictTransactionManagement(boolean strictTransactionManagement) {
        this.strictTransactionManagement = strictTransactionManagement;
    }

    void setChannelizer(String channelizer) {
        this.channelizer = channelizer;
    }

    void setGraphManager(String graphManager) {
        this.graphManager = graphManager;
    }

    void setMaxWorkQueueSize(int maxWorkQueueSize) {
        this.maxWorkQueueSize = maxWorkQueueSize;
    }

    void setMaxSessionTaskQueueSize(int maxSessionTaskQueueSize) {
        this.maxSessionTaskQueueSize = maxSessionTaskQueueSize;
    }

    void setMaxParameters(int maxParameters) {
        this.maxParameters = maxParameters;
    }

    void setSessionLifetimeTimeout(long sessionLifetimeTimeout) {
        this.sessionLifetimeTimeout = sessionLifetimeTimeout;
    }

    void setUseGlobalFunctionCacheForSessions(boolean useGlobalFunctionCacheForSessions) {
        this.useGlobalFunctionCacheForSessions = useGlobalFunctionCacheForSessions;
    }

    void setUseCommonEngineForSessions(boolean useCommonEngineForSessions) {
        this.useCommonEngineForSessions = useCommonEngineForSessions;
    }

    void setMetrics(ServerMetrics metrics) {
        this.metrics = metrics;
    }

    void setGraphs(Map<String, String> graphs) {
        this.graphs = graphs;
    }

    //    void setScriptEngines(Map<String, ScriptEngineSettings> scriptEngines) {
    void setScriptEngines(Map<String, Object> scriptEngines) {

        if (scriptEngines.containsKey("gremlin-groovy")) {
            Settings.ScriptEngineSettings groovySettings = new Settings.ScriptEngineSettings();
            this.scriptEngines.put("gremlin-groovy", groovySettings);

            Map<String, Object> groovyEngineConfig = (Map<String, Object>) scriptEngines.getOrDefault("gremlin-groovy", Collections.emptyMap());

//            List<Object> scripts = new ArrayList<>();
            // plugins
            Map<String, Object> groovyEnginePlugins = (Map<String, Object>) groovyEngineConfig.getOrDefault("plugins", Collections.emptyMap());
            for (Map.Entry<String, Object> pluginEntry : groovyEnginePlugins.entrySet()) {
                // for each plugin
                String pluginName = pluginEntry.getKey();
                Map<String, Map<String, String>> pluginConfig = (Map<String, Map<String, String>>) pluginEntry.getValue();

                // each plugin has a Map<String, List> config object in the upstream Settings
                Map<String, Object> configSections = new HashMap<>();
                for (Map.Entry<String, Map<String, String>> pluginConfigSection : pluginConfig.entrySet()) {
                    // for each section
                    List<Object> list = Arrays.asList(pluginConfigSection.getValue().values().toArray());
                    configSections.put(pluginConfigSection.getKey(), list);

//                    if (pluginName.equals("org.apache.tinkerpop.gremlin.jsr223.ScriptFileGremlinPlugin") &&
//                    pluginConfigSection.getKey().equals("files")){
//                        scripts.addAll(list);
//                    }
                }
                groovySettings.plugins.put(pluginName, configSections);
            }
//            for(Object script: scripts ){
//                groovySettings.scripts.add((String) script);
//            }
            System.out.println(groovySettings);
        }

    }

    void setSerializers(List<SerializerSettings> serializers) {
        this.serializers = new ArrayList<>();
        this.serializers.addAll(serializers);
    }

    void setSsl(SslSettings ssl) {
        this.ssl = ssl;
    }

    void setAuthentication(AuthenticationSettings authentication) {
        this.authentication = authentication;
    }

    void setAuthorization(AuthorizationSettings authorization) {
        this.authorization = authorization;
    }

    void setEnableAuditLog(boolean enableAuditLog) {
        this.enableAuditLog = enableAuditLog;
    }

    void setProcessors(List<ProcessorSettings> processors) {
        this.processors = new ArrayList<>();
        this.processors.addAll(processors);
    }

    public static class ProcessorSettings extends Settings.ProcessorSettings {

        void setClassName(String className) {
            this.className = className;
        }

        void setConfig(Map<String, Object> config) {
            this.config = config;
        }

    }

    public static class AuthorizationSettings extends Settings.AuthorizationSettings {

        void setAuthorizer(String authorizer) {
            this.authorizer = authorizer;
        }

        void setConfig(Map<String, Object> config) {
            this.config = config;
        }

    }

    public static class AuthenticationSettings extends Settings.AuthenticationSettings {

        void setAuthenticator(String authenticator) {
            this.authenticator = authenticator;
        }

        void setAuthenticationHandler(String authenticationHandler) {
            this.authenticationHandler = authenticationHandler;
        }

        void setConfig(Map<String, Object> config) {
            this.config = config;
        }

    }

    static class SslSettings extends Settings.SslSettings {

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        void setKeyStore(String keyStore) {
            this.keyStore = keyStore;
        }

        void setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
        }

        void setTrustStore(String trustStore) {
            this.trustStore = trustStore;
        }

        void setTrustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
        }

        void setKeyStoreType(String keyStoreType) {
            this.keyStoreType = keyStoreType;
        }

        void setTrustStoreType(String trustStoreType) {
            this.trustStoreType = trustStoreType;
        }

        void setSslEnabledProtocols(List<String> sslEnabledProtocols) {
            this.sslEnabledProtocols = sslEnabledProtocols;
        }

        void setSslCipherSuites(List<String> sslCipherSuites) {
            this.sslCipherSuites = sslCipherSuites;
        }

        void setNeedClientAuth(String needClientAuth) {
            this.needClientAuth = ClientAuth.valueOf(needClientAuth);
        }

    }

    public static class SerializerSettings extends Settings.SerializerSettings {
        void setClassName(String className) {
            this.className = className;
        }

        void setConfig(Map<String, Object> config) {

            for (Map.Entry<String, Object> configEntry : config.entrySet()) {
                if (configEntry.getKey().equals("ioRegistries")) {
                    configEntry.setValue(new ArrayList<String>(((Map<String, String>) configEntry.getValue()).values()));
                }
            }

            this.config = config;
        }
    }

    static class ServerMetrics extends Settings.ServerMetrics {

        void setConsoleReporter(ConsoleReporterMetrics consoleReporter) {
            this.consoleReporter = consoleReporter;
        }

        void setCsvReporter(CsvReporterMetrics csvReporter) {
            this.csvReporter = csvReporter;
        }

        void setJmxReporter(JmxReporterMetrics jmxReporter) {
            this.jmxReporter = jmxReporter;
        }

        void setSlf4jReporter(Slf4jReporterMetrics slf4jReporter) {
            this.slf4jReporter = slf4jReporter;
        }
    }


    public static class ConsoleReporterMetrics extends Settings.ConsoleReporterMetrics {

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        void setInterval(long interval) {
            this.interval = interval;
        }
    }

    public static class CsvReporterMetrics extends Settings.CsvReporterMetrics {
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        void setInterval(long interval) {
            this.interval = interval;
        }

        void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    public static class JmxReporterMetrics extends Settings.JmxReporterMetrics {
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        void setDomain(String domain) {
            this.domain = domain;
        }

        void setAgentId(String agentId) {
            this.agentId = agentId;
        }
    }

    public static class Slf4jReporterMetrics extends Settings.Slf4jReporterMetrics {
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        void setInterval(long interval) {
            this.interval = interval;
        }

        void setLoggerName(String loggerName) {
            this.loggerName = loggerName;
        }
    }

    public static abstract class BaseMetrics extends Settings.BaseMetrics {
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static abstract class IntervalMetrics extends Settings.IntervalMetrics {
        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        void setInterval(long interval) {
            this.interval = interval;
        }
    }


//    public static abstract class HostPortIntervalMetrics extends Settings.HostPortIntervalMetrics {
//        void setEnabled(boolean enabled) {
//            this.enabled = enabled;
//        }
//
//        void interval(long interval) {
//            this.interval = interval;
//        }
//
//        void setHost(String host) {
//            this.host = host;
//        }
//
//        void setPort(int port) {
//            this.port = port;
//        }
//    }
//
//    public static class GraphiteReporterMetrics extends Settings.GraphiteReporterMetrics {
//        void setEnabled(boolean enabled) {
//            this.enabled = enabled;
//        }
//
//        void interval(long interval) {
//            this.interval = interval;
//        }
//
//        void setHost(String host) {
//            this.host = host;
//        }
//
//        void setPort(int port) {
//            this.port = port;
//        }
//
//        void setPrefix(String prefix) {
//            this.prefix = prefix;
//        }
//
//    }
//
//    public static class GangliaReporterMetrics extends Settings.GangliaReporterMetrics {
//        void setEnabled(boolean enabled) {
//            this.enabled = enabled;
//        }
//
//        void interval(long interval) {
//            this.interval = interval;
//        }
//
//        void setHost(String host) {
//            this.host = host;
//        }
//
//        void setPort(int port) {
//            this.port = port;
//        }
//
//        void setAddressingMode(String addressingMode){
//            this.addressingMode = addressingMode;
//        }
//
//        void setTtl( int ttl){
//            this.ttl = ttl;
//        }
//
//        void setProtocol31(boolean protocol31){
//            this.protocol31 = protocol31;
//        }
//
//        void hostUUID(UUID hostUUID){
//            this.hostUUID = hostUUID;
//        }
//    }


}
