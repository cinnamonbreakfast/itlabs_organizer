package com.organizer.web.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Regex {

    public int phoneMatcher(String prefix,String phone){
        if(prefix.length()==0||phone.length()==0){
            return 0;
        }
        String pattern = patternBuilder(prefix);
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(phone);
        if (m.find( )) {
         return m.group(0).length();
        }else {
            return 0;
        }
    }
    static String patternBuilder (String prefix){

        StringBuilder pattern = new StringBuilder();
        pattern.append("(^(");
        String plus = "[+]";
        char[] chars = prefix.toCharArray() ;
        int j =0;
        while(j<prefix.length()){

            String substr= prefix.substring(j,prefix.length());
            System.out.println(substr);
            pattern.append("(");
            pattern.append(plus+substr);
            pattern.append(")");
            if(j!=prefix.length()-1){
                pattern.append("|");
            }
            ++j;
        }
        pattern.append("|[+]))|(^(");
        j=0;
        while(j<prefix.length()){

            String substr= prefix.substring(j,prefix.length());
            pattern.append("(");
            pattern.append(substr);
            pattern.append(")");
            if(j!=prefix.length()-1){
                pattern.append("|");
            }
            ++j;
        }
        pattern.append("))");
        return pattern.toString();
        }
}
