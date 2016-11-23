package my.homework.service;

import my.homework.exception.BlackListedPersonIdException;

public interface BlackListService {

    boolean isPersonalIdBlackListed(long personId) throws BlackListedPersonIdException;

}
