package com.stlmissouri.cobalt.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 2:14 PM
 * Use:  Map with 2 keys intentionally created with minimal checks and looping.
 */
public class HashMultiKeyMap<K, K2, V> implements MultiKeyMap<K, K2, V> {

    private Map<K, Map<K2, V>> data;

    public HashMultiKeyMap() {
        data = new HashMap<>();
    }

    public void put(K key, K2 key2, V val) {
        data.get(key).put(key2, val);
    }

    public V get(K key, K2 key2) {
        return this.data.get(key).get(key2);
    }

    public V remove(K key, K2 key2) {
        return this.data.get(key).remove(key2);
    }

    public Set<K> keySet() {
        return this.data.keySet();
    }

    public Set<K2> keySet(K key) {
        return this.data.get(key).keySet();
    }

    public Set<Map.Entry<K, Map<K2, V>>> entrySet() {
        return this.data.entrySet();
    }

    public Set<Map.Entry<K2, V>> entrySet(K key) {
        return this.data.get(key).entrySet();
    }

    public void fullClear() {
        this.data.clear();
    }

    public void clear() {
        for (Map<K2, V> set : this.data.values()) {
            set.clear();
        }
    }

    public void clear(K key) {
        this.data.get(key).clear();
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public boolean isEmpty(K key) {
        return this.data.get(key).isEmpty();
    }

    public int size() {
        int size = 0;
        for (Map<K2, V> set : this.data.values()) {
            size += set.size();
        }
        return size;
    }

    public int size(K key) {
        return this.data.get(key).size();
    }

    public boolean containsKey(K key) {
        return this.data.containsKey(key);
    }

    public boolean containsKey(K key, K2 key2) {
        return this.data.get(key).containsKey(key2);
    }

    public Collection<Map<K2, V>> values() {
        return this.data.values();
    }

    public Collection<V> values(K key) {
        return this.data.get(key).values();
    }

    public boolean safe(K key) {
        return this.checkKey(key);
    }

    private boolean checkKey(K key) {
        if (this.data.containsKey(key))
            return true;
        else
            this.data.put(key, new HashMap<K2, V>());
        return false;
    }

}
