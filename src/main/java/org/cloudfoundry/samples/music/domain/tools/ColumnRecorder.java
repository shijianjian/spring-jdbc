package org.cloudfoundry.samples.music.domain.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijian on 07/03/2017.
 */
@Component
public class ColumnRecorder {

    // singleton
    private static ColumnRecorder instance = null;

    private List<String> columns = new ArrayList();

    protected ColumnRecorder() { }

    public static ColumnRecorder getInstance() {
        if(instance == null) {
            instance = new ColumnRecorder();
        }
        return instance;
    }

    // functionalities
    @Autowired
    private TableRefactor tableRefactor;

    public void addColumn(String column) throws SQLDataException {
        tableRefactor.addColumn(column);
        columns.add(column);
    }

    public void deleteColumn(String column) throws SQLDataException {
        tableRefactor.deleteColumn(column);
        columns.remove(column);
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

}
