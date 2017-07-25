package cordova.plugin.restful;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import java.io.UnsupportedEncodingException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class echoes a string called from JavaScript.
 */
public class RESTful extends CordovaPlugin {

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("get")) {
            String user = args.getString(0);
            String pass = args.getString(1);
            String url = args.getString(2);
            this.get(user, pass, url, callbackContext);
            return true;
        } else if (action.equals("getSimple")) {
            String url = args.getString(0);
            this.get(url, callbackContext);
            return true;
        } else if (action.equals("post")) {
            String user = args.getString(0);
            String pass = args.getString(1);
            String url  = args.getString(2);
            String body = args.getString(3);
            int timeout = args.getInt(4);
            this.post(user, pass, url, body, timeout, callbackContext);
            return true;
        }else if (action.equals("checkAdmin")) {
            String user = args.getString(0);
            String pass = args.getString(1);
            String url  = args.getString(2);
            this.checkAdmin(user, pass, url, callbackContext);
            return true;
        }else if (action.equals("isReachable")) {
            String url = args.getString(0);

            this.isReachable(url, callbackContext);
            return true;
        }else if (action.equals("checkService")) {
            String url = args.getString(0);

            this.get(url, callbackContext);
            return true;
        }
        return false;
    }

    private void get(String user, String pass, String url, final CallbackContext callbackContext) {
        if (user != null && user.length() > 0 || pass != null && pass.length() > 0 || url != null && url.length() > 0) {

            client.setBasicAuth(user, pass);
            client.get(url, null, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    // String errorMsg = "GET, onFailure\n"+
                    // "\nstatusCode: "+Integer.toString(statusCode)+
                    // "\nJSONObject.errorResponse: "+errorResponse.toString()+
                    // "";

                    String errorMsg = "onFailure, GET " +
                        "Status Code: " + Integer.toString(statusCode) + ", " +
                        "Error message: " + throwable.getMessage() +
                        " ";

                    callbackContext.error(errorMsg);
                }

                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    String errorMsg = "onFailure, GET " +
                        "Status Code: " + Integer.toString(statusCode) + ", " +
                        "Error message: " + throwable.getMessage() +
                        " ";

                    callbackContext.error(errorMsg);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray

                    try {

                        callbackContext.success(response);

                    } catch (Exception e) {
                        e.printStackTrace();
                        callbackContext.error("catch error onSuccess (JSONObject)");
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    // Pull out the first event on the public timeline

                    try {

                        callbackContext.success(timeline);
                        System.out.println(timeline.toString());

                    } catch (Exception e) {
                        callbackContext.error("catch error onSuccess (JSONObject)");
                        e.printStackTrace();
                    }
                }

            });

        } else {
           callbackContext.error("Get, needs 3 arguments : user: String, pass: String, url: String.");
        }
    }// get

    private void post(String user, String pass, String url, String body, int timeout, final CallbackContext callbackContext) {

        if (user != null && user.length() > 0 || pass != null && pass.length() > 0 || url != null && url.length() > 0 || body != null && body.length() > 0) {

            client.setBasicAuth(user, pass);
            client.setTimeout(timeout);
            final String urlMsg = url;  

            String jsonString   = body;
            JSONArray jsonArray = null;

            try {
                jsonArray = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject json         = null;
            ByteArrayEntity entity  = null;
            try {

                entity = new ByteArrayEntity(jsonArray.toString().getBytes("UTF-8"));
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(this.cordova.getActivity().getApplicationContext(), url, entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    String resp = new String(response);
                    callbackContext.success(resp);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    // String errorMsg = "Post, onFailure\n"+
                    // "\nstatusCode: "+Integer.toString(statusCode)+
                    // "\nerrorResponse: "+new String(errorResponse)+
                    // "";

                    String errorMsg = "onFailure, POST " +
                        "Status Code: " + Integer.toString(statusCode) + ", " +
                        "Error message: " + e.getMessage() + ", " +
                        "URL: " + urlMsg +
                        " ";
                    
                    callbackContext.error(errorMsg);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        } else {
            callbackContext.error("Post, needs 4 arguments : user: String, pass: String, url: String, body: String.");
        }
    }// post

     private void checkAdmin(String user, String pass, String url, final CallbackContext callbackContext) {
        if (user != null && user.length() > 0 || pass != null && pass.length() > 0 || url != null && url.length() > 0) {

            client.setBasicAuth(user, pass);
            //client.setConnectTimeout(2000);

            client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    String resp = new String(response);
                    callbackContext.success(resp);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    // String errorMsg = "checkAdmin, onFailure\n" +
                    //         "\nstatusCode: " + Integer.toString(statusCode) +
                    //         "\nJSONObject.errorResponse: " + new String(errorResponse) +
                    //         "";
                    String errorMsg = "onFailure checkAdmin, " +
                            "Status Code: " + Integer.toString(statusCode) + ", " +
                            "Error message: " + e.getMessage() +
                            " ";
    
                    callbackContext.error(errorMsg);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });


        } else {
            callbackContext.error("CheckAdmin, needs 3 arguments : user: String, pass: String, url: String.");
        }
    }// checkAdmin


    private void get(String url, final CallbackContext callbackContext) {
        final String urlFinal = url;
        if (url != null && url.length() > 0) {

            client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    String resp = new String(response);
                    callbackContext.success(resp);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                    // printStackTrace to String
                    // StringWriter sw = new StringWriter();
                    // PrintWriter pw = new PrintWriter(sw);
                    // e.printStackTrace(pw);
                    // String = printStackTraceString = sw.toString();

                    String errorMsg = "onFailure, " +
                            "Status Code: " + Integer.toString(statusCode) + ", " +
                            "URL: " + urlFinal + ", " +
                            "Error message: " + e.getMessage() +
                            " ";
    
                    callbackContext.error(errorMsg);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });


        } else {
            callbackContext.error("get simple, needs 1 argument : url: String.");
        }
    }// get simple without basic authentication


    public boolean isReachable(String targetUrl, final CallbackContext callbackContext)
    {
        
        if (targetUrl != null && targetUrl.length() > 0) {
        try
        {
        	HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(
                    targetUrl).openConnection();
            httpUrlConnection.setRequestMethod("HEAD");
            int responseCode = httpUrlConnection.getResponseCode();
            System.out.println("ok");
            callbackContext.success("ok : "+targetUrl);
            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (UnknownHostException noInternetConnection)
        {

        	noInternetConnection.printStackTrace();
            callbackContext.error("Error not reachable");
            return false;
        }
        catch (Exception e)
        {	
        	e.printStackTrace();
            callbackContext.error("Error not reachable");
            return false;
        }
        }else{
            callbackContext.error("target url must not be null or empty");
            return false;
        }
    }

}
