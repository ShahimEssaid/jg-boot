package hold;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.graphdb.grpc.GetJanusGraphContextByGraphNameRequest;
import org.janusgraph.graphdb.grpc.schema.GetVertexLabelsRequest;
import org.janusgraph.graphdb.grpc.schema.GetVertexLabelsResponse;
import org.janusgraph.graphdb.grpc.schema.SchemaManagerClient;
import org.janusgraph.graphdb.grpc.types.JanusGraphContext;
import org.janusgraph.graphdb.grpc.types.VertexLabel;
import org.junit.jupiter.api.Test;

import java.util.List;

public interface AddVertext extends ITestBase {




    @Test
    default void addVertex() {
        GraphTraversal<Vertex, Vertex> iterate = getTraversal().addV("TestVertex").property("one", 1).iterate();
        System.out.println("Ran: " + getTraversal().V().toList());


        JanusGraphContext context = getManagerStub().getJanusGraphContextByGraphName(GetJanusGraphContextByGraphNameRequest.newBuilder().setGraphName("graph").build())
                .getContext();

        GetVertexLabelsRequest request = GetVertexLabelsRequest.newBuilder().setContext(context).build();
        GetVertexLabelsResponse vertexLabels = getSchemaStub().getVertexLabels(request);

        SchemaManagerClient client = new SchemaManagerClient(context,getManagerStub().getChannel());
        List<VertexLabel> vertexLabels1 = client.getVertexLabels();


        System.out.println(vertexLabels);


    }
}
