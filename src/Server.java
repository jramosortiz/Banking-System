import java.io.*;
import java.net.*;


public class Server {


    public static void main(String[] args) throws IOException {
        CheckingAccount Account = null;
        int accountsInStore = 0;
        double amount;// get client input amount
        String input;//for client input
        Database.Init();




        // Creates a server socket listening on port 8080
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started on port 8080...");


            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle each client in its own thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        }

    }
}


