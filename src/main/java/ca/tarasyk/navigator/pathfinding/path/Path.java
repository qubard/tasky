package ca.tarasyk.navigator.pathfinding.path;

import java.util.ArrayList;
import java.util.List;

public class Path<T> {

    protected List<T> nodes = new ArrayList<T>();

    /**
     * @param node Add a node to the path
     */
    public void addNode(T node) {
        this.nodes.add(node);
    }

    public T getNode(int i) {
        if (i < 0) i = 0;
        return this.nodes.get(this.nodes.size() - i - 1);
    }

    public List<T> getNodes() {
        return this.nodes;
    }
}
