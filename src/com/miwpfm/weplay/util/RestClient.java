package com.miwpfm.weplay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestClient {

	public enum RequestMethod  {
		GET,
		PUT,
		DELETE,
		POST
	}

    private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;
    
    private String url;
    private int responseCode;
    private String message;
    private String response;

    private static HttpContext _context;
    private static BasicCookieStore _cookieStore;
    
    public String getResponse() {
        return response;
    }
    
    public JSONObject getJsonResponse() throws JSONException{
    	JSONObject json = new JSONObject(response);    	
		return json;
    }
    
    public JSONArray getJsonArrayResponse() throws JSONException{
    	JSONArray jsonArray = new JSONArray(response);
    	return jsonArray;
    }
    
    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RestClient(String url)
    {
        this.url = url;
        this.params = new ArrayList<NameValuePair>();
        this.headers = new ArrayList<NameValuePair>();
    }    
    
    public ArrayList<NameValuePair> getParams() {
		return params;
	}

	public void setParams(ArrayList<NameValuePair> params) {
		this.params = params;
	}

	public ArrayList<NameValuePair> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<NameValuePair> headers) {
		this.headers = headers;
	}

	public void AddParam(String name, String value)
    {
        this.params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception
    {
        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";
                //api/me/recommended-games?{lat=40.5126674,long=-3.6742816}
                //api/me/recommended-games?lat=40.5126093&long=-3.6742078
                if(!this.params.isEmpty()){
                	combinedParams += "?";
                    for(NameValuePair p : this.params)
                    {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
                        if(combinedParams.length() > 1)
                       {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                    
                }
                
                url += combinedParams;
                //HttpGet request = new HttpGet(url + combinedParams);
                HttpGet request = new HttpGet(url);
                
                //add headers
                if(!headers.isEmpty()){
	                for(NameValuePair h : headers)
	                {
	                    request.addHeader(h.getName(), h.getValue());
	                }
                }
               
                executeRequest(request, url);
                break;
            }
            case POST:
            {
                HttpPost request = new HttpPost(url);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }

                executeRequest(request, url);
                break;
            }
            case PUT:
            {
                HttpPut request = new HttpPut(url);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }

                executeRequest(request, url);
                break;
            }
        }
    }

    private void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();
        HttpContext context = RestClient.getHttpContextInstance();
        context.setAttribute(ClientContext.COOKIE_STORE, RestClient.getCookieStoreInstance());
        HttpResponse httpResponse;

        try {
        	//Encapsulamos        	
            //((HttpResponse) request).setEntity(new UrlEncodedFormEntity(params)); 
            
        	httpResponse = client.execute(request,context);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }
           

        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    public static synchronized HttpContext getHttpContextInstance() {
        if (_context == null) {
            _context = new BasicHttpContext();
        }
        return _context;
    }

    public static synchronized BasicCookieStore getCookieStoreInstance() {
        if (_cookieStore == null) {
            _cookieStore = new BasicCookieStore();
        }
        return _cookieStore;
    }
}
