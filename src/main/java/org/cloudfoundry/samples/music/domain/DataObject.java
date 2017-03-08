package org.cloudfoundry.samples.music.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by shijian on 07/03/2017.
 */
public class DataObject {

    private String id;
    private List<String> columns;
    private Map<String, Object> content;

    public DataObject(Map<String, Object> content) {
        this.content = content;
        this.columns = new ArrayList<>();
        idDistributor();
        columnCollector();
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private void idDistributor() {
        if(content.containsKey("id")){
            id = content.get("id").toString().trim();
            content.remove("id", id);
        } else {
            id = generateId();
        }
    }

    private void columnCollector() {
        for(Map.Entry<String, Object> item : content.entrySet()) {
            columns.add(item.getKey().toString());
        }
    }

}
