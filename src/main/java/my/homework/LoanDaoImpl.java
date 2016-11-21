package my.homework;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class LoanDaoImpl implements LoanDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    public LoanDaoImpl(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withSchemaName(SCHEMA_NAME)
            .withTableName(TABLE_NAME);
    }

    @Override
    @Transactional
    public void addLoanApplication(long personalId, String name, String surname, String term, BigDecimal amount) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("personal_id", personalId)
            .addValue("name", name)
            .addValue("surname", surname)
            .addValue("term", term)
            .addValue("amount", amount);

        simpleJdbcInsert.execute(sqlParameterSource);
    }

    private static final String SCHEMA_NAME = "loan_app";
    private static final String TABLE_NAME = "loan";
}
