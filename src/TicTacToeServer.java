import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicTacToeServer {
  
  final int LISTENING_PORT = 6666;
  
  ServerSocket TicTacToeServerSocket;
  
  public TicTacToeServer() {
    new ListeningPort().start();
  }
  
  private class ListeningPort extends Thread {
    
    @Override
    public void run() {
      try {
        TicTacToeServerSocket = new ServerSocket(LISTENING_PORT);
        while (true) {
          System.out.println("Waiting connection....");
        
          Socket clientSocket = TicTacToeServerSocket.accept();
          System.out.println("Connection from: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        }
      } catch (IOException ex) {
        Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  private void ReceiveBoardStatus(Socket ClientSocket) {
    
    /*ObjectInputStream in = new ObjectInputStream(ClientSocket.getInputStream());
    char[][] bb = (char[][])in.readObject();
        
    for (int a = 0; a < 3; a++) {
      for (int b = 0; b < 3; b++) {
        System.out.print(bb[a][b] + " ");
      }
      System.out.println("");
    }*/
  }
}