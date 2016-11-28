package my.homework.dao;

import com.google.common.collect.Lists;
import my.homework.service.LoanApplication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class LoanApplicationDaoImpl implements LoanApplicationDao {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    LoanApplicationDaoImpl(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withSchemaName(SCHEMA_NAME)
            .withTableName(TABLE_NAME);
    }

    @Override
    public void addLoanApplication(
        long personalId,
        String name,
        String surname,
        String term,
        BigDecimal amount,
        String countryCode,
        String requestUid
    ) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("personal_id", personalId)
            .addValue("name", name)
            .addValue("surname", surname)
            .addValue("term", term)
            .addValue("amount", amount)
            .addValue("timestamp", LocalDateTime.now())
            .addValue("country_code", countryCode)
            .addValue("request_uid", requestUid);

        simpleJdbcInsert.execute(sqlParameterSource);
    }

    @Override
    public List<LoanApplication> getAllLoansApproved(@Nullable Long personalId) {
        List<LoanApplication> loansApproved = Lists.newArrayList();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        String query = SQL_GET_ALL_LOANS_APPROVED;
        if (null != personalId) {
            mapSqlParameterSource.addValue("p_personal_id", personalId);
            query = SQL_GET_ALL_LOANS_APPROVED_BY_USERS;
        }

        namedParameterJdbcTemplate.query(
            query,
            mapSqlParameterSource,
            (resultSet, rowNum) -> loansApproved.add(
                new LoanApplication(
                    resultSet.getString("term"),
                    resultSet.getBigDecimal("amount"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getLong("personal_id"),
                    resultSet.getString("request_uid")
                )
            )
        );

        return loansApproved;
    }

    @Override
    public LoanApplication getLoanApplicationByUid(String applicationUid) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("p_request_uid", applicationUid);
        LoanApplication loanApplication;
        try {
            loanApplication = namedParameterJdbcTemplate.queryForObject(
                SQL_GET_LOAN_BY_UID,
                mapSqlParameterSource,
                (resultSet, rowNum) -> new LoanApplication(
                    resultSet.getString("term"),
                    resultSet.getBigDecimal("amount"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getLong("personal_id"),
                    resultSet.getString("request_uid")
                )
            );
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
        return loanApplication;
    }

    private static final String SQL_GET_ALL_LOANS_APPROVED =
        "select * from loan_applications_schema.loan_application";
    private static final String SQL_GET_ALL_LOANS_APPROVED_BY_USERS =
        "select * from loan_applications_schema.loan_application la where la.personal_id = :p_personal_id";
    private static final String SQL_GET_LOAN_BY_UID =
        "select * from loan_applications_schema.loan_application la where la.request_uid = :p_request_uid";

    private static final String SCHEMA_NAME = "loan_applications_schema";
    private static final String TABLE_NAME = "loan_application";
}
