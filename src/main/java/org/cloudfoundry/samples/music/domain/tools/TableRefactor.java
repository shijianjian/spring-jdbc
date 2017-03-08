package org.cloudfoundry.samples.music.domain.tools;

import org.cloudfoundry.samples.music.repository.generators.TableRefactorGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLDataException;
import java.util.List;

/**
 * Created by shijian on 05/03/2017.
 */
@Repository
public class TableRefactor {

    JdbcTemplate jdbcTemplate;

    @Value("${my.default.table}")
    private String table;

    @Autowired
    ColumnRecorder columnRecorder;

    @Inject
    public TableRefactor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void addColumn(String col) throws SQLDataException {
        List<String> cols = columnRecorder.getColumns();
        if(cols.contains(col.trim().toLowerCase())){
            SQLDataException e = new SQLDataException("Column exists");
            throw e;
        }
        TableRefactorGenerator tgen = new TableRefactorGenerator(table);
        jdbcTemplate.update(tgen.addColumn(col.trim().toLowerCase()));
    }

    public void deleteColumn(String col) throws SQLDataException {
        List<String> cols = columnRecorder.getColumns();
        if(!cols.contains(col.trim().toLowerCase())){
            SQLDataException e = new SQLDataException("Column not exists");
            throw e;
        }
        TableRefactorGenerator tgen = new TableRefactorGenerator(table);
        jdbcTemplate.update(tgen.dropColumn(col.trim().toLowerCase()));
    }

    public void renameColumn(String oldCol, String newCol) throws SQLDataException {
        List<String > cols = columnRecorder.getColumns();
        if(!cols.contains(oldCol.trim().toLowerCase())){
            SQLDataException e = new SQLDataException("Column "+ oldCol.trim().toLowerCase() +" not exists");
            throw e;
        }
        if(!cols.contains(newCol.trim().toLowerCase())){
            SQLDataException e = new SQLDataException("Column "+ newCol.trim().toLowerCase() +" exists");
            throw e;
        }
        TableRefactorGenerator tgen = new TableRefactorGenerator(table);
        jdbcTemplate.update(tgen.renameColumns(oldCol.trim().toLowerCase(), newCol.trim().toLowerCase()));
    }

}
