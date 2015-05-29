import java.io.IOException;
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
    //new PrintClientList().start();
    new PairingPlayer().start();
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
        }
      } 
      catch (IOException ex) {
        Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  private class PairingPlayer extends Thread {
    
    public void run() {
      while (true) {
        
        try {
          Thread.sleep(1000);
        } 
        catch (InterruptedException ex) {
          Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (ClientList.size() >= 2) {
          Socket Player1 = ((Socket)ClientList.get(0));
          Socket Player2 = ((Socket)ClientList.get(1));
          
          if (isClientConnecting(Player1) == false) {
            ClientList.remove(0);
          }
          else if (isClientConnecting(Player2) == false) {
            ClientList.remove(1);
          }
          else {
            new GameThread(Player1, Player2).start();
            ClientList.remove(1);
            ClientList.remove(0);
          }
        }
      }
    }
    
    private boolean isClientConnecting(Socket Client) {
      try {
        Client.sendUrgentData(0xFF);
        return true;
      } 
      catch (IOException ex) {
        return false;
      }
    }
  }
  
  private class PrintClientList extends Thread {
    
    public void run() {
      while (true) {
        System.out.println(ClientList);
        
        try {
          Thread.sleep(5000);
        } 
        catch (InterruptedException ex) {
          Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}
