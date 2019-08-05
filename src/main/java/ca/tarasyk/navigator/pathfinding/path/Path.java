package ca.tarasyk.navigator.pathfinding.path;

import java.util.ArrayList;
import java.util.List;

public class Path<T> {

    private List<T> nodes = new ArrayList<T>();

    public void addNode(T node) {
        this.nodes.add(node);
    }

    public List<T> getNodes() {
        return this.nodes;
    }
}
