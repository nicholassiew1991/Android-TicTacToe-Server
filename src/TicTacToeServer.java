import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicTacToeServer {
  
  final int DEFAULT_PORT = 6666;
  
  ServerSocket TicTacToeServerSocket;
  
  public TicTacToeServer() {
    try {
      TicTacToeServerSocket = new ServerSocket(DEFAULT_PORT);
      
      while (true) {
        System.out.println("Waiting connection....");
        
        Socket clientSocket = TicTacToeServerSocket.accept();
        System.out.println("Connection from: " + clientSocket);
      }
    } 
    catch (IOException ex) {
      Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}