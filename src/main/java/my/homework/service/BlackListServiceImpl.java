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
    public void checkBlackListed(long personId) throws BlackListedPersonIdException {
        if (blackListDao.isPersonalIdBlackListed(personId)) {
            throw new BlackListedPersonIdException();
        }
    }
}
