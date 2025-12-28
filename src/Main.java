import com.sun.source.tree.TryTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {

        try (Socket clientSocket = new Socket("LocalHost", 8080)) {


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner Input = new Scanner(System.in);


                while (clientSocket.isConnected())
                {


                    String line = in.readLine();
                    if (line == null) {break;}//if line == null server probably shutdown

                   do {
                        if (line.equals("END")) {break;}
                        System.out.println(line);
                    }while ((line = in.readLine()) != null);



                    String message = Input.nextLine();
                    out.println(message);
                }

                System.out.println("Server is disconnected");
                //close clientsocket, printWriter, and bufferReader
                clientSocket.close();
                in.close();
                out.close();





        }
    }
}




