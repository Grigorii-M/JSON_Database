package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final int PORT = 23456;

    public static void main(String[] args) {
        System.out.println("Server started!");
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Session session = new Session(serverSocket.accept());
                Future<String> future = executorService.submit(session);
                if ("exit".equals(future.get())) {
                    break;
                }
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
