package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunctions {

    public static String createHash(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] b = md.digest();

            StringBuffer buf = new StringBuffer("");
            for(int i=0; i<b.length; i++) {
                int val = b[i];
                if(val < 0) val += 256;
                if(val < 16) buf.append("0");
                buf.append(Integer.toHexString(val));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
