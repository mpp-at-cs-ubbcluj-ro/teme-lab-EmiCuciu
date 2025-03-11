package org.example.lab3;

public interface Repository<ID, T> {
    void add(T elem);

    void update(ID id, T elem);

    Iterable<T> findAll();

    void delete(ID id);
}
