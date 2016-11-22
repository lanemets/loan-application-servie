package my.homework.dao;

import java.util.UUID;

public interface EventDao {

    void addLoanApplicationEvent(Long personId, UUID requestUid, int i);

}
