package org.cloudfoundry.samples.music.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cloudfoundry.samples.music.domain.tools.ColumnRecorder;
import org.cloudfoundry.samples.music.repository.generators.TableRefactorGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shijian on 07/03/2017.
 */
@Configuration
public class TableInitializer {

    private static final Log logger = LogFactory.getLog(TableInitializer.class);

    private JdbcTemplate jdbcTemplate;

    @Value("${my.default.table}")
    private String table;

    @Autowired
    ColumnRecorder columnRecorder;

    @Inject
    public TableInitializer(DataSource dataSource) throws SQLDataException {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @PostConstruct
    private void tableInitialization() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS \"" + table + "\"( \"id\" VARCHAR(50) primary key);");
        try {
            jdbcTemplate.execute("Alter TABLE " + table + " ADD COLUMN id VARCHAR(50)");
        } catch (Exception e) {}
        jdbcTemplate.execute(String.format("ALTER TABLE %s ALTER COLUMN %s TYPE VARCHAR(50);", table, "id"));

        logger.info("Create/find table '" + table + "'");
    }

    @PostConstruct
    private void columnInitialization() throws SQLDataException {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(new TableRefactorGenerator(table).getColumns());
        List<String> cols = new ArrayList<>();
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                cols.add(entry.getValue().toString());
                logger.info("Find column '" + entry.getValue().toString() + "'");
            }
        }
        columnRecorder.setColumns(cols);
    }

}
