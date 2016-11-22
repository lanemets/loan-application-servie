package my.homework.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.UUID;

class LoanApplicationDaoImpl implements LoanApplicationDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    LoanApplicationDaoImpl(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withSchemaName(SCHEMA_NAME)
            .withTableName(TABLE_NAME);
    }

    @Override
    @Transactional
    public void addLoanApplication(
        long personalId,
        String name,
        String surname,
        String term,
        BigDecimal amount,
        UUID requestUid
    ) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("personal_id", personalId)
            .addValue("name", name)
            .addValue("surname", surname)
            .addValue("term", term)
            .addValue("amount", amount)
            .addValue("request_uid", requestUid);

        simpleJdbcInsert.execute(sqlParameterSource);
    }

    private static final String SCHEMA_NAME = "loan_applications_schema";
    private static final String TABLE_NAME = "loan_application";
}
