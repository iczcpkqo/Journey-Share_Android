package com.journey.service.database;

import java.util.HashMap;
import java.util.Map;

public interface Db<T> {

    public String save(T t);

    public String updateByDocumentId(T t,String id);

    public String deleteByDocumentId(String id);

    public Map<String, Object> selectByDocumentId(String id);
}
