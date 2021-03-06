import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
  List WiatForPairingList = new ArrayList();
  
  public TicTacToeServer() {
    new ListeningPort().start();
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
          new WaitPairingResponse(clientSocket).start();
          ClientList.add(clientSocket);
        }
      } 
      catch (IOException ex) {
        Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  private class WaitPairingResponse extends Thread {
    
    private Socket Client;
    private boolean IsDone = false;
    
    public WaitPairingResponse(Socket Client) {
      this.Client = Client;
    }
    
    private void Done() {
      IsDone = true;
    }
    
    @Override
    public void run() {
      
      while (this.IsDone == false) {
        try {
          System.out.println("Waiting pairing message from " + this.Client);
          ObjectInputStream in = new ObjectInputStream(this.Client.getInputStream());
          
          if (in.readByte() == ServerGlobal.CLIENT_WAIT_PAIRING) {
            WiatForPairingList.add(this.Client);
            System.out.println(this.Client + " is wait for pairing...");
            Done();
          }
        }
        catch (IOException ex) {
          ex.printStackTrace();
        }
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
          ex.printStackTrace();
        }
        
        if (WiatForPairingList.size() >= 2) {
          Socket Player1 = ((Socket)WiatForPairingList.get(0));
          Socket Player2 = ((Socket)WiatForPairingList.get(1));
          
          SendPlayerSymbol(Player1, 'O');
          SendPlayerSymbol(Player2, 'X');
          
          new GameThread(Player1, Player2);
          
          WiatForPairingList.remove(1);
          WiatForPairingList.remove(0);
        }
      }
    }
    
    private void SendPlayerSymbol(Socket Player, char Symbol) { 
      try {
        OutputStream ToPlayerOne = Player.getOutputStream();
        ObjectOutputStream OutToPlayer1 = new ObjectOutputStream(ToPlayerOne);
        OutToPlayer1.writeByte(ServerGlobal.SET_PLAYER_SYMBOL);
        OutToPlayer1.writeObject(Symbol);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
