import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;

class TCPServer extends Thread {
   ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<>(10);

   public static void main(String argv[]) throws Exception {
      TCPServer server = new TCPServer();
      server.start();

      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");

      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");
      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");
      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");
   }

   public void add(String S) {
      buffer.offer(S);
   }

   public void run() {
      try {
         String wavfile;// = "bugs_answer.wav\n";

         ServerSocket welcomeSocket = new ServerSocket(6789);

         while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            System.out.println("Opening socket");

            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            try {
               while (true) {
                  wavfile = buffer.take();
                  outToClient.writeBytes(wavfile);
                  // Thread.sleep(5000);
               }
            } catch (Exception ex) {
            }
         }
      } catch (Exception ex) {
      }
   }
}