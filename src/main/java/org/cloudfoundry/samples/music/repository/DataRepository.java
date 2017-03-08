package org.cloudfoundry.samples.music.repository;

import org.cloudfoundry.samples.music.domain.DataObject;
import org.cloudfoundry.samples.music.repository.generators.DataQueryGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

/**
 * Created by shijian on 05/03/2017.
 */
@Service
@Repository
public class DataRepository {

    JdbcTemplate jdbcTemplate;

    @Inject
    public DataRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // TODO
    public void addColumnIfNotExist(String col) {
        jdbcTemplate.execute("");
    }


    // following methods are for CRUD
    public Map<String, Object> findOne(DataObject dataObject, String table) {
        DataQueryGenerator cq = new DataQueryGenerator(table);
        return jdbcTemplate.queryForMap(cq.SELECT(dataObject));
    }

    public Map<String, Object> findOne(String id, String table) {
        DataQueryGenerator cq = new DataQueryGenerator(table);
        return jdbcTemplate.queryForMap(cq.SELECT(id));
    }

    public List<Map<String, Object>> findAll(String table) {
        DataQueryGenerator cq = new DataQueryGenerator(table);
        return jdbcTemplate.queryForList(cq.SELECT());
    }

    public List<Map<String, Object>> insertItem(DataObject dataObject, String table) throws SQLDataException {
        DataQueryGenerator cq = new DataQueryGenerator(table);
        jdbcTemplate.update(cq.INSERT(dataObject));
        return jdbcTemplate.queryForList(cq.SELECT(dataObject));
    }

    public List<Map<String, Object>> updateItem(DataObject dataObject, String table) throws SQLDataException {
        DataQueryGenerator cq = new DataQueryGenerator(table);

        jdbcTemplate.update(cq.UPDATE(dataObject));

        return jdbcTemplate.queryForList(cq.SELECT(dataObject));
    }

    public void deleteItem(DataObject dataObject, String table) throws SQLDataException {
        DataQueryGenerator cq = new DataQueryGenerator(table);
        jdbcTemplate.update(cq.DELETE(dataObject));
    }

    public void deleteItem(String id, String table) throws SQLDataException {
        DataQueryGenerator cq = new DataQueryGenerator(table);
        jdbcTemplate.update(cq.DELETE(id));
    }

    public List<Map<String, Object>> fuzzySearch(String query, List<String> fields, String table) {
        DataQueryGenerator cq = new DataQueryGenerator(table);
        return jdbcTemplate.queryForList(cq.fuzzyQuery(query, fields));
    }
}
