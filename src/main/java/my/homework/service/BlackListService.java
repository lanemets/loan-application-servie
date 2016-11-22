package my.homework.service;

import my.homework.exception.BlackListedPersonIdException;

public interface BlackListService {

    void checkBlackListedPersonalId(long personId) throws BlackListedPersonIdException;

}
