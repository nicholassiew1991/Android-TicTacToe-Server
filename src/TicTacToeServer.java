import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicTacToeServer {
  
  final int LISTENING_PORT = 6666;
  
  ServerSocket TicTacToeServerSocket;
  
  List ClientList = new ArrayList();
  
  public TicTacToeServer() {
    new ListeningPort().start();
    new PrintClientList().start();
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
          ClientList.add(clientSocket);
          
          if (ClientList.size() == 2) {
            // Make sure 2 client are connecting.
            // If not connecting, close the socket, and 'continue'.
            
            // If 2 are connecting, create a GameThread handing for the game.
            /*Socket Player 1 = ((Socket) ClientList.get(0));
            GameThread Games = new GameThread((Socket)ClientList.get(0), (Socket)ClientList.get(1));*/
          }
        }
      } 
      catch (IOException ex) {
        Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    private boolean isClientConnecting(Socket Client) {
      return false;
    }
  }
  
  private class PrintClientList extends Thread {
    
    public void run() {
      while (true) {
        System.out.println(ClientList);
        try {
          Thread.sleep(1000);
        } 
        catch (InterruptedException ex) {
          Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
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