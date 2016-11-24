package my.homework.controller;

import java.util.concurrent.ThreadLocalRandom;
import org.apache.tomcat.util.buf.HexUtils;

class LoanApplicationUuidGenerator implements UuidGenerator {

    @Override
    public String generate() {
        byte[] bytes = new byte[8];
        ThreadLocalRandom.current().nextBytes(bytes);
        return HexUtils.toHexString(bytes);
    }
}
