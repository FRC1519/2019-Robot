import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

class TCPClient extends Thread {

    private static final int PORT = 5809;

    public static void main(String argv[]) throws Exception {
        new TCPClient().start();

    }

    public void run() {
        try {
            String modifiedSentence;
            System.out.println("start");

            Socket clientSocket = new Socket("10.15.19.2", PORT);
            System.out.println("open socket");

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("opened buffer");

            while (true) {
                System.out.println("waiting...");

                modifiedSentence = inFromServer.readLine();
                System.out.println("FROM SERVER: " + modifiedSentence);
                new PlaySound(modifiedSentence).start();
            }
        } catch (Exception ex) {
        }
    }
}