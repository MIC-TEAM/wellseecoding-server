package com.wellseecoding.server.infra.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import lombok.NonNull;
import org.apache.commons.codec.binary.Hex;

public class DirectJweTokenMapper implements JwtTokenMapper {
    private final JWEHeader jweHeader;
    private final JWEEncrypter jweEncrypter;
    private final JWEDecrypter jweDecrypter;

    public DirectJweTokenMapper(@NonNull String encryptionAlgorithm, @NonNull String key) {
        try {
            // https://www.iana.org/assignments/jose/jose.xhtml
            jweHeader = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.parse(encryptionAlgorithm));
            jweEncrypter = new DirectEncrypter(Hex.decodeHex(key));
            jweDecrypter = new DirectDecrypter(Hex.decodeHex(key));
        } catch (Exception cause) {
            throw new JweMapperCreateException(cause);
        }
    }

    @Override
    public String serialize(String payload) {
        try {
            JWEObject jweObject = new JWEObject(jweHeader, new Payload(payload));
            jweObject.encrypt(jweEncrypter);
            return jweObject.serialize();
        } catch (Exception cause) {
            throw new JweSerializeException(cause);
        }
    }

    @Override
    public String deserialize(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            jweObject.decrypt(jweDecrypter);
            return jweObject.getPayload().toString();
        } catch (Exception cause) {
            throw new JweDeserializeException(cause);
        }
    }
}
