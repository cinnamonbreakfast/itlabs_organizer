package com.organizer.web.utils;

public class StringConverter {
    int begin;
    int end;
    String str;
    char [] array;
    public StringConverter(String str){
        this.str = str; init();
    }
    void init(){
        array = str.toCharArray();
        begin =0;
        if(array.length<=8)
            end = (int)(Math.pow(2,array.length));
        else
            end = (int)(Math.pow(2,8));
    }
    public boolean hasNext(){
        if(begin+1==end)
        {
            return false;
        }
        return true;
    }

    public String getPermutation(){
        char [] permutation = new char[array.length];
        for (int j =0; j < array.length; j++) {
            permutation[j] = (isBitSet(begin, j)) ? Character.toUpperCase(array[j]) : array[j];
        }
        ++begin;
        return String.valueOf(permutation);
    }
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
    boolean isBitSet(int n, int offset) {
        return (n >> offset & 1) != 0;
    }
}
