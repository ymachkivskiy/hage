package org.hage.platform.component.structure.connections.graph;

import lombok.Value;
import org.hage.platform.component.structure.connections.Position;

import java.io.Serializable;

@Value
public class Edge implements Serializable {
    Position source;
    Position destination;
}
