package my.homework.dao;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

class BlackListDaoImpl implements BlackListDao {

    private final JdbcTemplate jdbcTemplate;

    BlackListDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Boolean isPersonalIdBlackListed(long personalId) {
        return jdbcTemplate.queryForObject(
            SQL_CHECK_BLACKLISTED_PERSON_QUERY,
            new Object[]{personalId},
            Boolean.class
        );
    }

    private static final String SQL_CHECK_BLACKLISTED_PERSON_QUERY =
        "select exists(select * from loan_applications_schema.blacklisted_persons where personal_id = ?)";
}
