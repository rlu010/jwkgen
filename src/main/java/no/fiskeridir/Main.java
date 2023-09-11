package no.fiskeridir;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class Main {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    String ALGORITHM = "RSA";
    String keyID = "my-key-id";
    int KEYZISE = 2048;
    String publicKeyName = "my-public-key.pem";
    String privateKeyName = "my-public-key.pem";

    // Generate the RSA key pair
    KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);
    gen.initialize(KEYZISE);
    KeyPair keyPair = gen.generateKeyPair();

    // Convert to JWK format
    JWK jwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
        .privateKey((RSAPrivateKey) keyPair.getPrivate())
        .keyUse(KeyUse.SIGNATURE)
        .keyID(keyID)
        .algorithm(JWSAlgorithm.RS256)
        .issueTime(new Date())
        .build();

    PublicKey publicKey = keyPair.getPublic();
    PrivateKey privateKey = keyPair.getPrivate();

    DataOutputStream dos = null;
    try {
      dos = new DataOutputStream(new FileOutputStream(publicKeyName));
      dos.write(publicKey.getEncoded());
      dos.flush();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (dos != null) {
        try {
          dos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }

    try {
      dos = new DataOutputStream(new FileOutputStream(privateKeyName));
      dos.write(privateKey.getEncoded());
      dos.flush();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        dos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println("[" + jwk.toPublicJWK() + "]");
  }
}