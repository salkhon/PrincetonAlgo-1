package com.search;

public interface SymbolTable<K extends Comparable<K>, V> {
    public void put(K key, V value);
    public V get(K key);
}
