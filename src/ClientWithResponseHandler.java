/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates the use of the {@link ResponseHandler} to simplify
 * the process of processing the HTTP response and releasing associated resources.
 */
public class ClientWithResponseHandler {
	
	private final static String url = "http://booking.airasia.com/Search.aspx?"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay1=7&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay2=28&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth1=2014-02&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth2=2014-02&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_ADT=2&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_CHD=0&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_INFANT=0&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListSearchBy=columnView&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$RadioButtonMarketStructure=OneWay&"
			//+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$RadioButtonMarketStructure=RoundTrip&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination1=HKT&"
			+ "ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin1=PVG&"
			+ "ControlGroupSearchView$ButtonSubmit=Search&"
			+ "ControlGroupSearchView$MultiCurrencyConversionViewSearchView$DropDownListCurrency=default&"
			+ "ControlGroupSearchView_AvailabilitySearchInputSearchViewdestinationStation1=HKT&"
			+ "ControlGroupSearchView_AvailabilitySearchInputSearchVieworiginStation1=PVG&"
			+ "MemberLoginSearchView$HFTimeZone=480&"
			+ "__EVENTARGUMENT=&"
			+ "__EVENTTARGET=&"
/*			+ "date_picker=02%2F20%2F2014&"
			+ "date_picker=&"
			+ "date_picker=02%2F28%2F2014&"
			+ "date_picker=&"*/
			+ "pageToken=";
			

    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        String responseStr = getStringFromInputStream(entity.getContent());
                        System.out.println(responseStr);
                        String startKey = "<div class=\"price\"><span>";
                        int startPoint = responseStr.indexOf(startKey) + startKey.length();
//                        int endPoint = responseStr.indexOf("<div class=\"price\"><span>");\
                       System.out.println(responseStr.substring(startPoint, startPoint+12));
                        
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
//            System.out.println(responseBody);
        } finally {
            httpclient.close();
        }
    }
    
    
 // convert InputStream to String
 	private static String getStringFromInputStream(InputStream is) {
  
 		BufferedReader br = null;
 		StringBuilder sb = new StringBuilder();
  
 		String line;
 		try {
  
 			br = new BufferedReader(new InputStreamReader(is));
 			while ((line = br.readLine()) != null) {
 				sb.append(line);
 			}
  
 		} catch (IOException e) {
 			e.printStackTrace();
 		} finally {
 			if (br != null) {
/* 				try {
 					br.close();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}*/
 			}
 		}
  
 		return sb.toString();
  
 	}
  

}
