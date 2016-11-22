package my.homework.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

class BlackListedDaoImpl implements BlackListedDao {

    private final JdbcTemplate jdbcTemplate;

    BlackListedDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Boolean doesBlackListContainPersonalId(long personalId) {
        return jdbcTemplate.queryForObject(
            SQL_CHECK_BLACKLISTED_PERSON_QUERY,
            new Object[]{personalId},
            Boolean.class
        );
    }

    private static final String SQL_CHECK_BLACKLISTED_PERSON_QUERY =
        "select exists(select * from loan_applications_schema.blacklisted_persons where personal_id = ?)";
}
