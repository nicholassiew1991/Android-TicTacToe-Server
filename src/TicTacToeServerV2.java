import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicTacToeServerV2 {
  
  final int LISTENING_PORT = 6666;
  
  ServerSocket TicTacToeServerSocket;
  
  List ClientList = new ArrayList();
  
  public TicTacToeServerV2() {
    new ListeningPort().start();
    new PrintClientList().start();
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
        if (ClientList.size() >= 2) {
          Socket Player1 = ((Socket)ClientList.get(0));
          Socket Player2 = ((Socket)ClientList.get(1));
          
          if (isClientConnecting(Player1) == false || isClientConnecting(Player2) == false) {
              System.out.println("One of the user disconnected.");
              continue;
          }else {
            GameThread Games = new GameThread(Player1, Player2);
            Games.start();
            
            //catch exception
            //ClientList.remove(0);
            //ClientList.remove(1);
            
            //change to:
            ClientList.clear();
            
          }
          
          // Make sure 2 client are connecting.
            // If not connecting, close the socket, and 'continue'.
            
            // If 2 are connecting, create a GameThread handing for the game.
            //Socket Player1 = ((Socket)ClientList.get(0));
            //Socket Player2 = ((Socket)ClientList.get(1));
            //GameThread Games = new GameThread((Socket)ClientList.get(0), (Socket)ClientList.get(1));
          
          try {
            Thread.sleep(1000);
          } 
          catch (InterruptedException ex) {
            Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
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
  
/* code move to Game Thread...   
  private void ReceiveBoardStatus(Socket ClientSocket) {
    
    ObjectInputStream in = null;
	try {
		in = new ObjectInputStream(ClientSocket.getInputStream());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    char[][] bb = null;
	try {
		bb = (char[][])in.readObject();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
    for (int a = 0; a < 3; a++) {
      for (int b = 0; b < 3; b++) {
        System.out.print(bb[a][b] + " ");
      }
      System.out.println("");
    }

  }
*/
}
