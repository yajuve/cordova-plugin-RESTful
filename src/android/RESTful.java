package cordova-plugin-restful;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class RESTful extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("get")) {
            String param = args.getString(0);
            this.get(param, callbackContext);
            return true;
        }else if (action.equals("post")) {
            String param = args.getString(0);
            this.post(message, callbackContext);
            return true;
        }
        return false;
    }

    private void get(String param, CallbackContext callbackContext) {
        if (param != null && param.length() > 0) {
            callbackContext.success(param);
        } else {
            callbackContext.error("Get, Expected one non-empty string argument.");
        }
    }

    private void post(String param, CallbackContext callbackContext) {
        if (param != null && param.length() > 0) {
            callbackContext.success(param);
        } else {
            callbackContext.error("Post, Expected one non-empty string argument.");
        }
    }
}
