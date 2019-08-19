package ca.tarasyk.navigator.pathfinding.path;

import java.util.ArrayList;
import java.util.List;

public class Path<T> {

    protected List<T> nodes = new ArrayList<T>();
    protected boolean reversed;

    public Path(boolean reversed) {
        this.reversed = reversed;
    }

    /**
     * @param node Push a node to the path
     */
    public void pushNode(T node) {
        this.nodes.add(node);
    }

    public T getNode(int i) {
        if (i < 0) i = 0;
        return this.nodes.get(reversed ? this.nodes.size() - i - 1 : i);
    }

    public List<T> getNodes() {
        return this.nodes;
    }
}
