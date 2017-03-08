package org.cloudfoundry.samples.music.domain;

import org.cloudfoundry.samples.music.domain.tools.ColumnRecorder;

import java.sql.SQLDataException;
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

    /**
     * Check every column is recorded or not.
     */
    public boolean IsAllColumnsRecorded() {
        ColumnRecorder columnRecorder = ColumnRecorder.getInstance();
        List<String> cols = columns;
        cols.removeAll(columnRecorder.getColumns());
        if(cols.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * put all columns to table.
     */
    public void recordColumns() throws SQLDataException {
        ColumnRecorder columnRecorder = ColumnRecorder.getInstance();
        List<String> cols = columns;
        cols.removeAll(columnRecorder.getColumns());
        if(cols.isEmpty()) {
            return;
        }
        for(String col : cols) {
            columnRecorder.addColumn(col);
        }
    }

}
