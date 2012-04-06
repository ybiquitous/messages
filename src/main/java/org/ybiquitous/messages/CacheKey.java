package org.ybiquitous.messages;

import java.util.Arrays;

final class CacheKey {

    private final Object[] _elements;

    public CacheKey(Object... elements) {
        this._elements = elements.clone();
    }

    public Object[] elements() {
        return this._elements.clone();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this._elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CacheKey that = (CacheKey) obj;
        return Arrays.equals(this._elements, that._elements);
    }

    @Override
    public String toString() {
        return Arrays.toString(this._elements);
    }
}