package org.cloudfoundry.samples.music.repository.generators;

import org.cloudfoundry.samples.music.domain.DataObject;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Profile({"postgres", "my-sql"})
public class DataQueryGenerator {

    private String table;
    private String field;
    private String value;
    private String condition;
    private String aesKey = "775CE8220A5E23BC93E6D72D7419430BD7C4854C0FD49B8347DBDCF70A656FE9";

    public DataQueryGenerator(String table) {
        this.table = table;
    }

    public String SELECT(DataObject dataObject) {
        decryptionBuilder(dataObject);
        return String.format("SELECT %s FROM %s WHERE id='%s';", field, table, dataObject.getId());
    }

    public String SELECT(String id) {
//        decryptionBuilder();
        return String.format("SELECT %s FROM %s WHERE id='%s';", field, table, id);
    }

    /**
     * Select all.
     * @return
     */
    public String SELECT() {
//        decryptionBuilder();
        return String.format("SELECT %s FROM %s;", field, table);
    }

    public String INSERT(DataObject dataObject) {
        encryptionBuilder(dataObject);
        // TODO : if target data has extra columns
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", table, field, value);
        System.out.println("sql = " + sql);
        return sql;
    }

    public String UPDATE(DataObject dataObject) {
        encryptionBuilder(dataObject);
        // TODO : if target data has extra columns
        String sql = String.format("UPDATE %s SET (%s) = (%s) WHERE id=%s;", table, field, value, "'" + dataObject.getId() + "'");
        System.out.println("sql = " + sql);
        return sql;
    }

    public String DELETE(DataObject dataObject) {
        String sql = String.format("DELETE FROM %s WHERE id=%s;", table, "'" + dataObject.getId() + "'");
        System.out.println("sql = " + sql);
        return sql;
    }

    public String DELETE(String id) {
        String sql = String.format("DELETE FROM %s WHERE id=%s;", table, "'" + id + "'");
        System.out.println("sql = " + sql);
        return sql;
    }

    public String DELETEALL() {
        String sql = String.format("DELETE FROM %s;", table);
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
            return ";";
        }
        List<String> list = new ArrayList<>();
        for(String field : fields) {
            if(!field.equals("id")) {
                list.add("LOWER(" + "PGP_SYM_DECRYPT(" + field + "::bytea, '" + this.aesKey + "')" + ")");
            }
        }
        String col = String.join("||", list);
        String[] queryArray = query.trim().split(" ");
        String condition = "";
        for(int i=0; i<queryArray.length; i++) {
            condition += String.format("AND ((%s) LIKE LOWER('%%%s%%')) ", col, queryArray[i]);
        }
        condition = condition.substring(condition.indexOf("AND") + "AND".length());
        decryptionBuilder(fields);
        // using %% to escape %
        String sql = String.format("SELECT %s FROM %s WHERE (%s);", this.field, table, condition);
        System.out.println("sql = " + sql);
        return sql;
    }

    private void fieldBuilder(DataObject dataObject) {
        StringBuffer field = new StringBuffer();
        StringBuffer value = new StringBuffer();
        field.append("id");
        value.append("'"+dataObject.getId()+"'");
        for(Map.Entry<String, Object> map : dataObject.getContent().entrySet()) {
            if(map.getKey() == "encryption") {
                continue;
            }
            field.append("," + map.getKey());
            // in postgres, we use '' instead of '.
            value.append(",'" + map.getValue().toString().replace("'", "''") + "'");
        }
        this.field = field.toString();
        this.value = value.toString();
    }

    private void encryptionBuilder(DataObject dataObject) {
        StringBuffer field = new StringBuffer();
        StringBuffer value = new StringBuffer();
        field.append("id");
        value.append("'"+dataObject.getId()+"'");
        for(Map.Entry<String, Object> map : dataObject.getContent().entrySet()) {
            field.append("," + map.getKey());
            // in postgres, we use '' instead of '.
            value.append("," + "PGP_SYM_ENCRYPT('"+ map.getValue().toString().replace("'", "''") +"', '" + this.aesKey + "')");
        }
        this.field = field.toString();
        this.value = value.toString();
    }

    private void decryptionBuilder(DataObject dataObject) {
        StringBuffer field = new StringBuffer();
        StringBuffer value = new StringBuffer();
        field.append("id");
        value.append("'"+dataObject.getId()+"'");
        for(Map.Entry<String, Object> map : dataObject.getContent().entrySet()) {
            field.append("," + "PGP_SYM_DECRYPT('" + map.getKey() + "', '" + this.aesKey + "') as " + map.getKey());
            // in postgres, we use '' instead of '.
            value.append(",'" + map.getValue().toString().replace("'", "''") + "'");
        }
        this.field = field.toString();
        this.value = value.toString();
    }

    private void decryptionBuilder(List<String> columns) {
        StringBuffer field = new StringBuffer();
        field.append("id");
        for(String column : columns) {
            if(!column.equals("id")) {
                // cast every column into bytea type for Decryption
                field.append("," + "PGP_SYM_DECRYPT(" + column + "::bytea, '" + this.aesKey + "') as " + column);
            }
        }
        this.field = field.toString();
    }

    private void conditionBuilder(DataObject dataObject) {
        StringBuffer condition = new StringBuffer();
        for(Map.Entry<String, Object> map : dataObject.getContent().entrySet()) {
            condition.append("," + map.getKey() + "=" + map.getValue().toString());
        }
        condition.deleteCharAt(0);
        this.condition = condition.toString();
    }

}
