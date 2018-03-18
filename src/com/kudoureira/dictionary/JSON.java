package com.kudoureira.dictionary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kudoureira.crawlKindle.ParsedBook;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class JSON {
    private String outputPath = "./object-path-sample.json";
    private String processingServer = "";
    private ArrayList<ParsedBook> parsedCollection;
    private String tempJSON;

    public JSON(ArrayList<ParsedBook> parsedCollection) {
        this.parsedCollection = parsedCollection;
    }

    public void createJSON() {
        Gson gson = new Gson();
        tempJSON = gson.toJson(parsedCollection);
    }

    public ArrayList<String> postJSON() throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        String dockerEndpoint = "http://192.168.99.100:8080/processBook";
        String developmentEndpoint = "http://localhost:3000/processBook";

        HttpPost httpPost = new HttpPost(developmentEndpoint);

        StringEntity entity = new StringEntity(tempJSON, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity returnEntity = response.getEntity();

        ArrayList<String> processedLinks = new ArrayList<>();

        if(returnEntity != null) {
            String strResponse = EntityUtils.toString(returnEntity, "UTF-8");
            JSONObject result = new JSONObject(strResponse);
            JSONArray linksList = result.getJSONArray("links");

            for(Object link : linksList) {
                processedLinks.add(link.toString());
            }

            System.out.println("this is links " + linksList);
            System.out.println("this is processed links " + processedLinks);
        }

//        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        client.close();

        return processedLinks;
    }

    public void fileJSON() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter temp = new FileWriter(outputPath);
//        System.out.println(bookWords);
        try {
            temp = new FileWriter(outputPath);
            gson.toJson(parsedCollection, temp);
//            temp.close();

        } finally {
            temp.close();
        }
    }
}
