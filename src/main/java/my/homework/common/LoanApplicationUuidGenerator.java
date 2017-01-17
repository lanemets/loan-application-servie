package my.homework.common;

import java.util.concurrent.ThreadLocalRandom;
import org.apache.tomcat.util.buf.HexUtils;

class LoanApplicationUuidGenerator implements UuidGenerator {

    @Override
    public String generate(String name) {
        byte[] bytes = new byte[8];
        ThreadLocalRandom.current().nextBytes(bytes);
        return HexUtils.toHexString(bytes);
    }
}
