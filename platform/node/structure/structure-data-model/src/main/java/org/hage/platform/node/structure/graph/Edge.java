package org.hage.platform.node.structure.graph;

import lombok.Value;
import org.hage.platform.node.structure.Position;

import java.io.Serializable;

@Value
public class Edge implements Serializable {
    Position source;
    Position destination;
}
