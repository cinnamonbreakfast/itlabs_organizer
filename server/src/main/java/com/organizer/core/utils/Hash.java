package com.organizer.core.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Hash {
    public static String md5(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte [] messDigest = md.digest(input.getBytes("UTF-8"));
            BigInteger bigInt = new BigInteger(1,messDigest);
            String hash = bigInt.toString(16);
            while(hash.length()<32){
                hash="0"+hash;
            }
            return hash;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
