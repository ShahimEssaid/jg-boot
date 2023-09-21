package hold;

import com.essaid.jg.boot.JGServer;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.tinkerpop.gremlin.server.GraphManager;
import org.janusgraph.core.ConfiguredGraphFactory;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.graphdb.management.ConfigurationManagementGraph;
import org.janusgraph.graphdb.management.utils.ConfigurationManagementGraphNotEnabledException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class MainTests extends TestBase
//        implements AddVertext, PrintSchema

{


    @Autowired
    MainTests(JGServer server) {
        super(server);
    }


    @Test
    void graphManager() throws ConfigurationManagementGraphNotEnabledException {
        GraphManager graphManager = this.server.getGremlinServer().getServerGremlinExecutor().getGraphManager();
        ConfigurationManagementGraph instance = ConfigurationManagementGraph.getInstance();

        Map<String, Object> map = new HashMap<>();
        map.put("gremlin.graph", "org.janusgraph.core.JanusGraphFactory");
        map.put("storage.backend", "inmemory");
        MapConfiguration mapConfiguration = new MapConfiguration(map);
        Map<String, Object> templateConfiguration = instance.getTemplateConfiguration();
        if (templateConfiguration == null) {
            instance.createTemplateConfiguration(mapConfiguration);
        }

        Map<String, Object> graph11 = instance.getConfiguration("graph1");

        JanusGraph graph1 = null;
        if (graph11 == null) {
            graph1 = ConfiguredGraphFactory.create("graph1");
        } else {
            graph1 = ConfiguredGraphFactory.open("graph1");
        }


        System.out.println("HEllow");


    }
}
