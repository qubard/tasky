package ca.tarasyk.navigator.pathfinding.path.node;

import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.IPathable;

import java.util.Optional;

public class Node<P, S> implements IPathable<P> {

    private Optional<Node<P,S>> parent;
    private Optional<S> score;

    public void setParent(Node<P,S> parent) {
        this.parent = Optional.of(parent);
    }

    public Optional<Node<P,S>> getParent() {
        return this.parent;
    }

    public void setScore(S score) {
        this.score = Optional.of(score);
    }

    public Optional<S> getScore() {
        return this.score;
    }

    @Override
    public Path<P> pathFrom() {
        Node<P, S> curr = this.getParent().get();
        while (curr != null) {
            curr = curr.getParent().orElse(null);
        }
        return null;
    }
}

