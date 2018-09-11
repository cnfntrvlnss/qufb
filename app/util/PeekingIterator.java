package util;

import java.util.Iterator;

public class PeekingIterator<T> implements Iterator<T> {
    private T value;
    private Iterator<T> iterator;

    public PeekingIterator(Iterator<T> iterator){
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return value != null || iterator.hasNext();
    }

    @Override
    public T next() {
        if(value == null) return iterator.next();
        T ret = value;
        value = null;
        return ret;
    }

    public T peek(){
        if(value == null) value = iterator.next();
        return value;
    }
}
