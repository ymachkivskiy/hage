package org.hage.platform.component.structure.graph;

import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.structure.Position;

import java.io.Serializable;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hage.platform.component.structure.graph.GraphType.UNDIRECTED;
import static org.hage.util.CollectionUtils.nullSafeCopy;

@ToString
@Getter
public class GraphDefinition implements Serializable {
    private final GraphType graphType;
    private final List<Edge> graphEdges;
    private final List<Position> positions;

    public GraphDefinition(GraphType graphType, List<Position> vertices, List<Edge> edges) {
        vertices = nullSafeCopy(vertices);
        edges = nullSafeCopy(edges);

        checkNotNull(graphType, "Graph graphType is not specified");
        checkArgument(!vertices.isEmpty(), "Graph could not be empty");


        this.graphType = graphType;
        this.graphEdges = edges;
        this.positions = vertices;
    }

    public GraphDefinition(List<Position> vertices) {
        this(UNDIRECTED, vertices, null);
    }
}
