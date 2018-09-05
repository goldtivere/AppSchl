/**
 * @author: micheal abobade
 * @email: pagims2003@yahoo.com
 * @mobile: 234-8065-711-043
 * @date: 2016-08-17
 */
package com.schlmgt.logic;

import javax.crypto.Cipher;
import java.security.Key;
import java.util.Base64;


import javax.crypto.spec.SecretKeySpec;

public class AESencrp {

    private static final String ALGO = "AES";
    /*
    private static final byte[] keyValue = new byte[]{'K', 'i', 'n', 'g', 'i',
        'm', 's', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
     */
    //take this to the property file
    private static final byte[] keyValue = new byte[]{'S', 'i', 'n', 'g', 'o',
        'f', 'T', 'h', 'e', 'L', 'O', 'R', 'D', 'G','O','D'};

    public static String encrypt(String data) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());        
        String encryptedValue = Base64.getUrlEncoder().encodeToString(encVal);
        return encryptedValue;

    }

    public static String decrypt(String encryptedData) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedVal = Base64.getDecoder().decode(encryptedData);       
        byte[] decVal = c.doFinal(decodedVal);
        String decryptedValue = new String(decVal);
        return decryptedValue;

    }

    public static Key generateKey() throws Exception {

        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
    /**public static void main(String [] args)
    {
        try
        {
            System.out.println(encrypt("Goldemilan"));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }**/
   
}
