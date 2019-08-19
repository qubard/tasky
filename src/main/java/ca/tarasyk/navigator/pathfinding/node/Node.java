package ca.tarasyk.navigator.pathfinding.node;

import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.IPathable;

import java.util.Optional;

public class Node<S, D> implements IPathable<D> {

    private Optional<Node<S, D>> parent = Optional.ofNullable(null);
    private Optional<S> score = Optional.ofNullable(null);

    public void setParent(Node<S, D> parent) {
        this.parent = Optional.of(parent);
    }

    public Optional<Node<S, D>> getParent() {
        return this.parent;
    }

    public void setScore(S score) {
        this.score = Optional.of(score);
    }

    public Optional<S> getScore() {
        return this.score;
    }

    public Optional<D> getData() { return Optional.ofNullable(null); }

    /**
     * @return A path from the destination to the current node
     */
    @Override
    public Path<D> pathFrom() {
        Node<S, D> curr = this;
        Path<D> path = new Path<>(true);
        while (curr != null) {
            Optional<D> data = curr.getData();
            if (data.isPresent()) {
                path.pushNode(curr.getData().get());
            }
            curr = curr.getParent().orElse(null);
        }
        return path;
    }
}

