package com.organizer.web.utils;

public class StringConverter {
    public static String convertFirstLetterUpper(String str){
        str = str.strip().toLowerCase();
        try{
           str =  str.substring(0,1).toUpperCase()+str.substring(1);
        }catch (Exception e){
            str="";
        }
        return str;
    }

    public static String converToUpper(String str ){
        str = str.strip().toUpperCase();
        return str;
    }
}
