package my.homework.service;

import my.homework.dao.BlackListedDao;
import my.homework.exception.BlackListedPersonIdException;

class BlackListServiceImpl implements BlackListService {

    private final BlackListedDao blackListedDao;

    BlackListServiceImpl(BlackListedDao blackListedDao) {
        this.blackListedDao = blackListedDao;
    }

    @Override
    public void checkBlackListedPersonalId(long personId) throws BlackListedPersonIdException {
        if (blackListedDao.doesBlackListContainPersonalId(personId)) {
            throw new BlackListedPersonIdException();
        }
    }
}
