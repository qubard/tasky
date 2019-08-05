package ca.tarasyk.navigator.pathfinding.path.node;

import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.IPathable;

import java.util.Optional;

public class Node<P, S, D> implements IPathable<D> {

    private Optional<Node<P, S, D>> parent = Optional.ofNullable(null);
    private Optional<S> score = Optional.ofNullable(null);

    public void setParent(Node<P, S, D> parent) {
        this.parent = Optional.of(parent);
    }

    public Optional<Node<P,S, D>> getParent() {
        return this.parent;
    }

    public void setScore(S score) {
        this.score = Optional.of(score);
    }

    public Optional<S> getScore() {
        return this.score;
    }

    @Override
    public Path<D> pathFrom() {
        return null;
    }
}

