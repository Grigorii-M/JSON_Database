package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;

public class ResponseGenerator {
    public static String getValueResponse(JsonElement value) {
        HashMap<String, JsonElement> response = new HashMap<>();
        response.put("response", new JsonPrimitive("OK"));
        response.put("value", value);
        return new Gson().toJson(response);
    }

    public static String getOkResponse() {
        HashMap<String, String> response = new HashMap<>();
        response.put("response", "OK");
        return new Gson().toJson(response);
    }

    public static String getNoSuchKeyResponse() {
        HashMap<String, String> response = new HashMap<>();
        response.put("response", "ERROR");
        response.put("reason", "No such key");
        return new Gson().toJson(response);
    }

    public static String getErrorResponse() {
        HashMap<String, String> response = new HashMap<>();
        response.put("response", "ERROR");
        return new Gson().toJson(response);
    }
}
