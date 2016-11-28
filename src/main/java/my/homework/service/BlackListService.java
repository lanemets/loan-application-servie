package my.homework.service;

import my.homework.exception.BlackListedPersonIdException;

public interface BlackListService {

    void checkBlackListed(long personId) throws BlackListedPersonIdException;

}
