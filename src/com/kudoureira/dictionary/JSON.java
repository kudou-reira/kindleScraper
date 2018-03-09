package com.kudoureira.dictionary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSON {
    private String outputPath = "./object-path-sample.json";
    private ArrayList<Word> bookWords;
    private String tempJSON;

    public JSON(ArrayList<Word> bookWords) {
        this.bookWords = bookWords;
    }

    public void test() {
        System.out.println("this is json class running");
    }

    public void createJSON() {
        Gson gson = new Gson();
        tempJSON = gson.toJson(bookWords);
    }

    public void postJSON() throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:3000/processBook");

        StringEntity entity = new StringEntity(tempJSON, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println("this is response" + response);
//        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        client.close();
    }

    public void fileJSON() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter temp = new FileWriter(outputPath);
//        System.out.println(bookWords);
        try {
            temp = new FileWriter(outputPath);
            gson.toJson(bookWords, temp);
//            temp.close();

        } finally {
            temp.close();
        }
    }
}
