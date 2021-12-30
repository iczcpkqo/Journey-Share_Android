package com.journey.service;

public interface Db<T> {

    public String save(T t);

    public String updateByDocumentId(T t,String id);

    public String deleteByDocumentId(String id);

    public T selectByDocumentId(String id);
}
