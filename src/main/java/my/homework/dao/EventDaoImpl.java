package my.homework.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.UUID;

public class EventDaoImpl implements EventDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    public EventDaoImpl(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withSchemaName(SCHEMA_NAME)
            .withTableName(TABLE_NAME);
    }

    @Override
    public void addLoanApplicationEvent(Long personalId, UUID requestUid, int status) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("personal_id", personalId)
            .addValue("request_uid", requestUid)
            .addValue("timestamp", LocalDateTime.now())
            .addValue("status", status);

        simpleJdbcInsert.execute(sqlParameterSource);
    }

    private static final String SCHEMA_NAME = "loan_applications_schema";
    private static final String TABLE_NAME = "application_events";
}
