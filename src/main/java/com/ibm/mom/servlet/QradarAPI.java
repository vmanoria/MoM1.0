package com.ibm.mom.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class QradarAPI {

	
		
		 public static void main(String[] args) throws ClientProtocolException, IOException {
			  HttpClient client = new DefaultHttpClient();
			  HttpGet request = new HttpGet("https://172.16.60.10/api/siem/offenses");
			 
			  request.setHeader("Authorization", "BasicAuth");
			  request.setHeader("UserName", "admin");
			  request.setHeader("Password", "q1d3m0");
			  request.setHeader("Content-Type", "application/json");
			  request.setHeader("Accept", "application/json");
			  HttpResponse response = client.execute(request);
			  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			  String line = "";
			  while ((line = rd.readLine()) != null) {
			    System.out.println(line);
			  }
		 }

}


