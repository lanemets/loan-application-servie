package my.homework.dao;

import com.google.common.collect.Lists;
import my.homework.LoanApplicationRequest;
import my.homework.service.Loan;
import my.homework.service.LoanApplicationStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class LoanApplicationDaoImpl implements LoanApplicationDao {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    LoanApplicationDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withSchemaName(SCHEMA_NAME)
            .withTableName(TABLE_NAME);
    }

    @Override
    @Transactional
    public void addLoanApplication(LoanApplicationRequest loanApplicationRequest, UUID requestUid, LoanApplicationStatus status) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("personal_id", loanApplicationRequest.getPersonId())
            .addValue("name", loanApplicationRequest.getName())
            .addValue("surname", loanApplicationRequest.getSurname())
            .addValue("term", loanApplicationRequest.getTerm())
            .addValue("amount", loanApplicationRequest.getAmount())
            .addValue("status", status.getValue())
            .addValue("timestamp", LocalDateTime.now())
            .addValue("request_uid", requestUid);

        simpleJdbcInsert.execute(sqlParameterSource);
    }

    @Override
    public Collection<Loan> getAllLoansApproved() {
        List<Map<String, Object>> resultMaps = jdbcTemplate.queryForList(SQL_GET_ALL_LOANS_APPROVED, LoanApplicationStatus.OK.getValue());
        List<Loan> loansApproved = Lists.newArrayList();
        resultMaps.forEach(
            map -> loansApproved.add(
                new Loan(
                    map.get("term").toString(),
                    new BigDecimal(map.get("amount").toString()),
                    map.get("name").toString(),
                    map.get("surname").toString(),
                    UUID.fromString(map.get("request_uid").toString())
                )
            )
        );
        return loansApproved;
    }

    private static final String SQL_GET_ALL_LOANS_APPROVED = "select * from loan_applications_schema.loan_application la where la.status = ?";

    private static final String SCHEMA_NAME = "loan_applications_schema";
    private static final String TABLE_NAME = "loan_application";
}
