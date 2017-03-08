package org.cloudfoundry.samples.music.repository.generators;

import org.cloudfoundry.samples.music.domain.DataObject;
import org.cloudfoundry.samples.music.domain.tools.ColumnRecorder;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;

@Profile({"postgres", "my-sql"})
public class DataQueryGenerator {

    private String table;
    private String field;
    private String value;
    private String condition;

    public DataQueryGenerator(String table) {
        this.table = table;
    }

    public String SELECT(DataObject dataObject) {
        return String.format("SELECT * FROM %s WHERE id='%s'", table, dataObject.getId());
    }

    public String SELECT(String id) {
        return String.format("SELECT * FROM %s WHERE id='%s'", table, id);
    }

    /**
     * Select all.
     * @return
     */
    public String SELECT() {
        return String.format("SELECT * FROM %s", table);
    }

    public String INSERT(DataObject dataObject) {
        fieldBuilder(dataObject.getContent());
        // TODO : if target data has extra columns
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", table, field, value);
        System.out.println("sql = " + sql);
        return sql;
    }

    public String UPDATE(DataObject dataObject) {
        fieldBuilder(dataObject.getContent());
        // TODO : if target data has extra columns
        String sql = String.format("UPDATE %s SET (%s) = (%s) WHERE id=%s", table, field, value, "'" + dataObject.getId() + "'");
        System.out.println("sql = " + sql);
        return sql;
    }

    public String DELETE(DataObject dataObject) {
        String sql = String.format("DELETE FROM %s WHERE id=%s", table, "'" + dataObject.getId() + "'");
        System.out.println("sql = " + sql);
        return sql;
    }

    public String DELETE(String id) {
        String sql = String.format("DELETE FROM %s WHERE id=%s", table, "'" + id + "'");
        System.out.println("sql = " + sql);
        return sql;
    }

    /**
     * Search all the fields if fields is null.
     * @param query
     * @param fields
     * @return
     */
    public String fuzzyQuery(String query, List<String> fields) {
        if(fields == null) {
            fields = ColumnRecorder.getInstance().getColumns();
        }
        String col = String.join("||", fields);
        String sql = String.format("SELECT * FROM %s WHERE (%s) LIKE '%s'", table, col, query);
        System.out.println("sql = " + sql);
        return sql;
    }

    private void fieldBuilder(Map<String, Object> dataMap) {
        StringBuffer field = new StringBuffer();
        StringBuffer value = new StringBuffer();
        for(Map.Entry<String, Object> map : dataMap.entrySet()) {
                field.append("," + map.getKey());
                value.append(",'" + map.getValue() + "'");
        }
        field.deleteCharAt(0);
        value.deleteCharAt(0);
        this.field = field.toString();
        this.value = value.toString();
    }

    private void conditionBuilder(Map<String, Object> dataMap) {
        StringBuffer condition = new StringBuffer();
        for(Map.Entry<String, Object> map : dataMap.entrySet()) {
            condition.append("," + map.getKey() + "=" + map.getValue().toString());
        }
        condition.deleteCharAt(0);
        this.condition = condition.toString();
    }

}
