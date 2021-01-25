package com.example.exercise1.Asyncs;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.Scanner;
//asynctask for getting xml file(local)
public class Retrieve extends AsyncTask<String, Void, String> {
    final String URL = "http://10.50.201.128/index.xml";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(URL);
            HttpResponse httpresponse = httpclient.execute(httpget);
            Scanner sc = new Scanner(httpresponse.getEntity().getContent());
            HttpEntity entity = httpresponse.getEntity();
            String output = EntityUtils.toString(entity);
            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}