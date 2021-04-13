package Bjtu.CNSCC212.Group2.PDFSignWithIText;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;
import java.util.Base64;


public class Aes {
    private static final String ALGORITHM = "AES";
    private static final int KEYSIZE = 128;
    private static final String ENCODING_SCHEME = "UTF-8";
    private static final String ENCRYPT_SCHEME = "AES/CBC/PCKS5Padding";
    private static byte[] AESEncryptedKey = new byte[0];
    private static byte[] AESEncryptedIV = new byte[0];
    private static SecureRandom secureRandom = new SecureRandom();


    //Initial AES iv
    public byte[] AESIv(int KEYSIZE){
        byte[] iv = new byte[KEYSIZE / 8];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        byte[] salt = ivSpec.getIV();
        return salt;
    }


    //Initial AESkey
    public static SecretKey keyGenerator() throws NoSuchAlgorithmException {
        KeyGenerator secretGenerator = KeyGenerator.getInstance(ALGORITHM);
        secretGenerator.init(KEYSIZE);
        SecretKey secretKey = secretGenerator.generateKey();
        AESEncryptedKey = secretKey.getEncoded();
        return secretKey;
    }

    static Charset charset = Charset.forName(ENCODING_SCHEME);

    //AES Encrypt
    public static byte[] aesEncrypt(String dataToEncrypt,SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,secureRandom);
        byte[] encryptedData = dataToEncrypt.getBytes(charset) ;
        return cipher.doFinal(encryptedData);
    }

    //AES Decrypt
    public static String aesDecrypt(byte[] encryptedData,SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException{
//        byte[] iv = new byte[KEYSIZE / 8];
//        secureRandom.nextBytes(iv);
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,secureRandom);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData,ENCODING_SCHEME);
    }

    public static String getStrAESKey() {
        return Base64.getEncoder().encodeToString(AESEncryptedKey);
    }
}


