package edu.eci.arep.backend;

import java.util.HashMap;
import java.util.Map;

public class Repository<K, V>{
    private Map<K, V> repositorio = new HashMap<>();
    public void save(K key, V value){
        repositorio.put(key, value);
    }
    public V get(K key){
        return repositorio.get(key);
    }
}
