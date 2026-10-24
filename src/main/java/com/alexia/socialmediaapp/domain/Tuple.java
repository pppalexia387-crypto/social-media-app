package com.alexia.socialmediaapp.domain;

import java.util.Objects;

public class Tuple<E, E1> {
    private final E left;
    private final E1 right;

    public Tuple(E left, E1 right) {
        this.left = left;
        this.right = right;
    }

    public E getLeft() {
        return left;
    }

    public E1 getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(left, tuple.left) && Objects.equals(right, tuple.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}