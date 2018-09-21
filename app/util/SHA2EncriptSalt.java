package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA2EncriptSalt {
    public  static  void  main(String args[]){
        //待加密的字符串
        String encryptString ="123456";
        //获取盐
        String salt=getSalt();
        System.err.println("生成的强随机数是:"+salt);
        //sha2加盐加密
       String sha2EncriptString = sha2EncryptSalt(encryptString,salt);
       System.err.println("sha2加盐加密后的字符串为:"+sha2EncriptString);
    }


    /**
     * sha2加盐加密的算法
     * @param encryptStr：待加密的字符串
     * @param salt:盐
     * @return
     */
    public static String sha2EncryptSalt(String encryptStr, String salt){
        MessageDigest md = null;//为应用程序提供信息摘要算法的功能
        String encryptCode = null;//加密后的字符串
        byte[] bt = (encryptStr + salt).getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            encryptCode = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return encryptCode;
    }
    //生成随机盐
    public static String getSalt(){
        SecureRandom sr;//强随机数生成
        byte[] salt = new byte[16];//加盐字符，关于盐值长度的一个经验值是长度至少要和hash加密函数的返回值长度保持一致。
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");//既指定了加密算法又指定了包
            sr.nextBytes(salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salt.toString();
    }
    //byte转string
    public static String bytes2Hex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
