package server;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;

public class Database {
    private static Database instance;
    private final JsonObject data;

    private Database() {
        JsonObject data1;
        try {
            data1 = new Gson().fromJson(Files.newBufferedReader(Paths.get("F:\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json")), JsonObject.class);
        } catch (IOException e) {
            data1 = new JsonObject();
        }
        data = data1;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public synchronized String set(JsonElement key, JsonElement value) {
        ArrayDeque<String> keySequence = new ArrayDeque<>(List.of(String.valueOf(key).replaceAll("\"", "")
                .replaceAll("\\[|]", "").split(",")));
        recursiveSet(keySequence, value, data);

        return ResponseGenerator.getOkResponse();
    }

    private void recursiveSet(ArrayDeque<String> keySequence, JsonElement value, JsonObject data) {
        String key = keySequence.removeFirst();
        if (data.getAsJsonObject().has(key)) {
            if (keySequence.isEmpty()) {
                data.remove(key);
                data.add(new Gson().toJson(key).replaceAll("\"", ""), value);
            } else {
                recursiveSet(keySequence, value, data.get(key).getAsJsonObject());
            }
        } else {
            if (keySequence.isEmpty()) {
                data.add(new Gson().toJson(key).replaceAll("\"", ""), value);
            } else {
                data.add(new Gson().toJson(key).replaceAll("\"", ""), new JsonObject());
            }
        }
    }

    public synchronized String get(JsonElement key) {
        System.out.println(data);
        ArrayDeque<String> keySequence = new ArrayDeque<>(List.of(String.valueOf(key).replaceAll("\"", "").replaceAll("\\[|]", "").split(",")));
        return recursiveGet(keySequence, data);
    }

    private String recursiveGet(ArrayDeque<String> keySequence, JsonElement data) {
        String key = keySequence.removeFirst();
        if (data.getAsJsonObject().has(key)) {
            if (keySequence.isEmpty()) {
                return ResponseGenerator.getValueResponse(data.getAsJsonObject().get(key));
            } else {
                return recursiveGet(keySequence, data.getAsJsonObject().get(key));
            }
        } else {
            return ResponseGenerator.getNoSuchKeyResponse();
        }
    }

    public synchronized String delete(JsonElement key) {
        ArrayDeque<String> keySequence = new ArrayDeque<>(List.of(String.valueOf(key).replaceAll("\"", "")
                .replaceAll("\\[|]", "").split(",")));
        return recursiveDelete(keySequence, data);
    }

    private String recursiveDelete(ArrayDeque<String> keySequence, JsonElement data) {
        String key = keySequence.removeFirst();
        if (data.getAsJsonObject().has(key)) {
            if (keySequence.isEmpty()) {
                data.getAsJsonObject().remove(key);
                return ResponseGenerator.getOkResponse();
            } else {
                return recursiveDelete(keySequence, data.getAsJsonObject().get(key));
            }
        } else {
            return ResponseGenerator.getNoSuchKeyResponse();
        }
    }
}
