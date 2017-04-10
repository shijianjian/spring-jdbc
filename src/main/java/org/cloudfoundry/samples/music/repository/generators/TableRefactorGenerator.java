package org.cloudfoundry.samples.music.repository.generators;

/**
 * Created by shijian on 06/03/2017.
 */
public class TableRefactorGenerator {

    private String table;

    public TableRefactorGenerator(String table) {
        this.table = table;
    }

    public String getColumns() {
        return String.format("SELECT column_name FROM information_schema.columns WHERE table_name='%s';", table);
    }

    public String addColumn(String col) {
        return String.format("ALTER TABLE %s ADD COLUMN %s TEXT;", table, col);
    }

    public String dropColumn(String col) {
        return String.format("ALTER TABLE %s DROP COLUMN %s RESTRICT;", table, col);
    }

    public String renameColumns(String col, String newName) {
        return String.format("ALTER TABLE %s RENAME COLUMN %s TO %s;", table, col, newName);
    }

}
