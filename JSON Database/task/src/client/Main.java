package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (Socket socket = new Socket(ADDRESS, PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            Args arguments = new Args();
            JCommander.newBuilder().addObject(arguments).build().parse(args);
            String msg = parseCommandLineArgs(arguments);
            //msg = "{\"type\":\"delete\",\"key\":[\"person\",\"car\",\"year\"]}";
            output.writeUTF(msg);
            System.out.println("Sent: " + msg);

            String str = input.readUTF();
            System.out.println("Received: " + str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseCommandLineArgs(Args args) {
        if (args.getFileName() != null) {
            File file = new File(System.getProperty("user.dir") + "/src/client/data/" + args.getFileName());
            try (Scanner fileScanner = new Scanner(file)) {
                return fileScanner.nextLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        JsonRequest request = new JsonRequest();
        if (args.getType() != null) {
            request.setType(args.getType());
        }

        if (args.getKey() != null) {
            request.setKey(args.getKey());
        }

        if (args.getValue() != null) {
            request.setValue(args.getValue());
        }

        return new Gson().toJson(request);
    }


}
