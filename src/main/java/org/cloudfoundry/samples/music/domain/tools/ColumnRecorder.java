package org.cloudfoundry.samples.music.domain.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by shijian on 07/03/2017.
 */
@Component
@Scope("singleton")
public class ColumnRecorder {

    private List<String> columns = new ArrayList();

    @Autowired
    public TableRefactor tableRefactor;

    public void addColumn(String column) throws SQLDataException {
        tableRefactor.addColumn(column);
        columns.add(column);
    }

    public void deleteColumn(String column) throws SQLDataException {
        tableRefactor.deleteColumn(column);
        columns.remove(column);
    }

    public void deleteAllColumns(){
        tableRefactor.deleteAllColumns();
        columns.removeAll(columns);
    }

    /**
     * Return extra columns in the keySet.
     * Empty if it includes everything.
     * @param keySet
     * @return
     */
    public List<String> diff(Set<String> keySet) {
        if(columns.containsAll(keySet)) {
            return new ArrayList();
        }
        List<String> res = new ArrayList<>();
        keySet.removeAll(columns);
        res.addAll(keySet);
        return res;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

}
