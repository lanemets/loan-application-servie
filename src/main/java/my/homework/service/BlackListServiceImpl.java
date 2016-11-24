package my.homework.service;

import my.homework.dao.BlackListDao;
import my.homework.exception.BlackListedPersonIdException;
import org.springframework.transaction.annotation.Transactional;

class BlackListServiceImpl implements BlackListService {

    private final BlackListDao blackListDao;

    BlackListServiceImpl(BlackListDao blackListDao) {
        this.blackListDao = blackListDao;
    }

    @Override
    @Transactional
    public boolean isPersonalIdBlackListed(long personId) throws BlackListedPersonIdException {
        return blackListDao.isPersonalIdBlackListed(personId);
    }
}
