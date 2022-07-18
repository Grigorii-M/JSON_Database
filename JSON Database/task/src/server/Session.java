package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class Session implements Callable<String> {

    private final Database database;
    private final Socket socket;
    public Session(Socket socket) {
        this.socket = socket;
        database = Database.getInstance();
    }

    @Override
    public String call() {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String input = inputStream.readUTF();
            String result = executeCommand(input);
            if ("exit".equals(result)) {
                return "exit";
            }
            outputStream.writeUTF(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String executeCommand(String command) {
        Gson gson = new Gson();
        JsonObject fromJson = gson.fromJson(command, JsonObject.class);

        String commandType = String.valueOf(fromJson.get("type")).replaceAll("\"", "");
        if ("exit".equals(commandType)) {
            return "exit";
        } else if ("get".equals(commandType)) {
            return database.get(fromJson.get("key"));
        } else if ("set".equals(commandType)) {
            return database.set(fromJson.get("key"), fromJson.get("value"));
        } else if ("delete".equals(commandType)) {
            return database.delete(fromJson.get("key"));
        } else {
            return ResponseGenerator.getErrorResponse();
        }
    }
}
