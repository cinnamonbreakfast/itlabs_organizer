package com.organizer.core.service.sms;

import static org.toilelibre.libe.curl.Curl.curl;
import static org.toilelibre.libe.curl.Curl.$;

import com.organizer.core.utils.ParameterStringBuilder;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsSender {

    String url = "https://app.smso.ro/api/v1/";
    String API_KEY ="mTr3xmoP3M9usuncicnqdD57DbxHlXWTpz4uePpz";
    public void sendSms(String sender,String body){
        try {
            URL url = new URL("http://example.com");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Map<String , String> params = new HashMap<>();
            params.put("sender","4");
            params.put("to",sender);
            params.put("body",body);
            params.put("apiKey","mTr3xmoP3M9usuncicnqdD57DbxHlXWTpz4uePpz");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(params));
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println(inputLine);
        }
        catch (Exception e){

        }

        //https://app.smso.ro/api/v1/send?sender=4&to=0722334455&body=Message%20Body&apiKey=mTr3xmoP3M9usuncicnqdD57DbxHlXWTpz4uePpz

    }
}
