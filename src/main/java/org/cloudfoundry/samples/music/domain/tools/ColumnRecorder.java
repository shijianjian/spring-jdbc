package org.cloudfoundry.samples.music.domain.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by shijian on 07/03/2017.
 */
@Component
public class ColumnRecorder {

    private List<String> columns = new ArrayList();

    @Autowired
    public TableRefactor tableRefactor;

    public void addColumn(String column) throws SQLDataException {
        column = column.trim().toLowerCase();
        if(!columns.contains(column)) {
            tableRefactor.addColumn(column);
            columns.add(column);
        }
    }

    public void addColumns(List<String> columns) throws SQLDataException {
        for(String col : columns) {
            col = col.trim().toLowerCase();
            addColumn(col);
        }
    }

    public void removeColumn(String column) throws SQLDataException {
        column = column.trim().toLowerCase();
        tableRefactor.deleteColumn(column);
        columns.remove(column);
    }

    public void deleteAllColumns(){
        tableRefactor.deleteAllColumns();
        columns = new ArrayList<>();
    }

    /**
     * Return extra columns in the keySet.
     * Empty if it includes everything.
     * @param keySet
     * @return
     */
    public List<String> diff(Set<String> keySet) {
        List<String> cols = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for(String key : keySet) {
            keys.add(key.trim().toLowerCase());
        }
        for(String col : columns) {
            cols.add(col.trim().toLowerCase());
        }
        if(cols.containsAll(keys)) {
            // return no diff
            return new ArrayList();
        }
        List<String> res = new ArrayList<>();
        keys.removeAll(cols);
        res.addAll(keys);
        return res;
    }

    public List<String> getColumns() {
        if(columns == null)
            return new ArrayList<>();
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

}
