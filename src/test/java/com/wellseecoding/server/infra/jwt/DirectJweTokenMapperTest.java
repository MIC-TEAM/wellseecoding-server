package com.wellseecoding.server.infra.jwt;

import com.nimbusds.jose.EncryptionMethod;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectJweTokenMapperTest {
    private static final int RANDOM_STRING_COUNT = 100;
    private static final String RANDOM_STRING = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_COUNT);
    private static final String ENCRYPTION_ALGORITHM = EncryptionMethod.A256GCM.getName();
    private static final String KEY = "38792F423F4528482B4D6251655468576D5A7133743677397A24432646294A40";

    @Test
    public void encrypt_and_decrypt() {
        DirectJweTokenMapper tokenMapper = new DirectJweTokenMapper(ENCRYPTION_ALGORITHM, KEY);
        assertEquals(RANDOM_STRING, tokenMapper.deserialize(tokenMapper.serialize(RANDOM_STRING)));
    }
}
