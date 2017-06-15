package cordova.plugin.restful;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import java.io.UnsupportedEncodingException;

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
        } else if (action.equals("post")) {
            String user = args.getString(0);
            String pass = args.getString(1);
            String url  = args.getString(2);
            String body = args.getString(3);
            this.post(user, pass, url, body, callbackContext);
            return true;
        }
        return false;
    }

    private void get(String user, String pass, String url, final CallbackContext callbackContext) {
        if (user != null && user.length() > 0) {

            client.setBasicAuth(user, pass);
            client.get(url, null, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    String errorMsg = "GET, onFailure\n"+
                    "\nstatusCode: "+Integer.toString(statusCode)+
                    "\nJSONObject.errorResponse: "+errorResponse.toString()+
                    "";
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
                        e.printStackTrace();
                    }
                }

            });

        } else {
            callbackContext.error("Get, Expected one non-empty string argument.");
        }
    }

    private void post(String user, String pass, String url, String body, final CallbackContext callbackContext) {

        if (user != null && user.length() > 0) {

            client.setBasicAuth(user, pass);

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
                    String errorMsg = "Post, onFailure\n"+
                    "\nstatusCode: "+Integer.toString(statusCode)+
                    "\nerrorResponse: "+new String(errorResponse)+
                    "";
                    callbackContext.error(errorMsg);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        } else {
            callbackContext.error("Post, Expected one non-empty string argument.");
        }
    }
}
