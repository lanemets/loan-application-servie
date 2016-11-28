package my.homework.common;

import org.apache.tomcat.util.buf.HexUtils;

import java.util.concurrent.ThreadLocalRandom;

class LoanApplicationUuidGenerator implements UuidGenerator {

    @Override
    public String generate() {
        byte[] bytes = new byte[8];
        ThreadLocalRandom.current().nextBytes(bytes);
        return HexUtils.toHexString(bytes);
    }
}
