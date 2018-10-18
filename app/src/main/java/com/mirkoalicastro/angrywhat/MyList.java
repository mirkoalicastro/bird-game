package com.mirkoalicastro.angrywhat;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
TODO devo inserire in coda
 */
public class MyList<T> implements Iterable<T> {
    private final MyIterator iterator;
    private Node<T> head = null;
    private int count;

    public interface FullyRemover<T> {
        void remove(T el);
    }

    public MyList() {
        this(null);
    }

    public MyList(FullyRemover<T> fullyRemover) {
        iterator = new MyIterator(fullyRemover);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    public void resetIterator(){
        iterator.next = head;
        iterator.cur = null;
    }

    public int size(){
        return count;
    }

    public void add(T key){
        Node<T> node = new Node<>(key);
        if(head != null) {
            head.prev = node;
            node.next = head;
        }
        head = node;
        count++;
        resetIterator();
    }

    private class MyIterator implements Iterator<T> {
        Node<T> next, cur;
        FullyRemover<T> fullyRemover;

        MyIterator(FullyRemover<T> fullyRemover) {
            this.fullyRemover = fullyRemover;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if(!hasNext())
                throw new NoSuchElementException();
            cur = next;
            T ret = cur.key;
            next = cur.next;
            return ret;
        }

        @Override
        public void remove() {
            if(cur == null)
                throw new IllegalStateException();
            T tmp = cur.key;
            if(cur == head) {
                head = head.next;
                if(head != null)
                    head.prev = null;
            } else {
                cur.prev.next = cur.next;
                if(cur.next!=null)
                    cur.next.prev = cur.prev;
            }
            if(fullyRemover != null)
                fullyRemover.remove(tmp);
            count--;
        }
    }

    private class Node<S>{
        final S key;
        Node<S> next, prev;

        Node(S key){
            this.key = key;
        }
    }
}
