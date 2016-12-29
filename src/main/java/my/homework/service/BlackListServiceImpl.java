package my.homework.service;

import my.homework.dao.BlackListDao;
import my.homework.exception.BlackListedPersonIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

class BlackListServiceImpl implements BlackListService {

    private static final Logger logger = LoggerFactory.getLogger(BlackListServiceImpl.class);

    private final BlackListDao blackListDao;

    BlackListServiceImpl(BlackListDao blackListDao) {
        this.blackListDao = blackListDao;
    }

    @Override
    @Transactional
    public void checkBlackListed(long personalId) throws BlackListedPersonIdException {
        logger.debug("checking person exists in blacklist; personalId: {}", personalId);
        if (blackListDao.isPersonalIdBlackListed(personalId)) {
            logger.debug("personal has been blacklisted; personalId: {}", personalId);

            throw new BlackListedPersonIdException();
        }
    }
}
