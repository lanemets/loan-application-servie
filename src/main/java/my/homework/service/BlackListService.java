package my.homework.service;

import my.homework.exception.BlackListedPersonIdException;

interface BlackListService {

    boolean isPersonalIdBlackListed(long personId) throws BlackListedPersonIdException;

}
