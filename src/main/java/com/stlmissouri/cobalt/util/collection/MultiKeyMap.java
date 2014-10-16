package com.stlmissouri.cobalt.util.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 2:38 PM
 * Use:  Map which requires multiple keys. "2D Map"
 */
public interface MultiKeyMap<K, K2, V> {

    public void put(K key, K2 key2, V val);

    public V get(K key, K2 key2);

    public V remove(K key, K2 key2);

    public Set<K> keySet();

    public Set<K2> keySet(K key);

    public Set<Map.Entry<K, Map<K2, V>>> entrySet();

    public Set<Map.Entry<K2, V>> entrySet(K key);

    public void fullClear();

    public void clear();

    public void clear(K key);

    public boolean isEmpty();

    public boolean isEmpty(K key);

    public int size();

    public int size(K key);

    public boolean containsKey(K key);

    public boolean containsKey(K key, K2 key2);

    public Collection<Map<K2, V>> values();

    public Collection<V> values(K key);

    public boolean safe(K key);

}
