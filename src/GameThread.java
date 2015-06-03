import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread {

  private Socket Player1, Player2;

  private char[][] GameBoard = new char[3][3];

  public GameThread(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
    System.out.println("GameThread Started!");
    StartReceiveMessagesThread();
  }
  
  private void StartReceiveMessagesThread() {
    new NewReceiveMessageThread(this.Player1, this.Player2).start();
    new NewReceiveMessageThread(this.Player2, this.Player1).start();
  }
  
  private class NewReceiveMessageThread extends Thread {
    
    private Socket SourceClient, DestinationClient;

    NewReceiveMessageThread(Socket SourceClient, Socket DestinationClient) {
      this.SourceClient = SourceClient;
      this.DestinationClient = DestinationClient;
    }
    
    @Override
    public void run() {
      while (true) {
        try {
          DataInputStream dIn = new DataInputStream(SourceClient.getInputStream());
          byte MessagesType = dIn.readByte();
          
          if (MessagesType == ServerGlobal.BOARD_STATUS) {
            
            System.out.println("Receive BoardStatus from: " + this.SourceClient);
            
            for (int a = 0; a < 3; a++) {
              for (int b = 0; b < 3; b++) {
                GameBoard[a][b] = dIn.readChar();
                System.out.print(GameBoard[a][b] + " ");
              }
              System.out.println("");
            }
            
            NewSendBoardStatus(this.DestinationClient);
          }
        }
        catch (IOException ex) {
          Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    
    private void NewSendBoardStatus(Socket DestClient) {
      try {
        char[][] BoardStatus = GameBoard;
        DataOutputStream dOut = new DataOutputStream(DestClient.getOutputStream());
        dOut.writeByte(ServerGlobal.BOARD_STATUS);
        for (int a = 0; a < 3; a++) {
          for (int b = 0; b < 3; b++) {
            dOut.writeChar(BoardStatus[a][b]);
          }
        }
        dOut.flush(); // Send out
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  //<editor-fold defaultstate="collapsed" desc="Not use temporary">
  private void SendBoardStatus(Socket DestClient) {
    try {
      OutputStream outToClient = DestClient.getOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(outToClient);
      out.writeByte(ServerGlobal.BOARD_STATUS);
      out.writeObject(GameBoard);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (ClassCastException e) {
      e.printStackTrace();
    }
  }

  private class ReceiveMessagesThread extends Thread {

    private Socket SourceClient, DestinationClient;

    ReceiveMessagesThread(Socket SourceClient, Socket DestinationClient) {
      this.SourceClient = SourceClient;
      this.DestinationClient = DestinationClient;
    }

    @Override
    public void run() {
      while (true) {
        try {
          ObjectInputStream in = new ObjectInputStream(this.SourceClient.getInputStream());
        }
        catch (java.io.StreamCorruptedException ex) {
          ex.printStackTrace();
          break;
        }
        catch (IOException ex) {
         ex.printStackTrace();
        }
      }
    }
  }
  //</editor-fold>
}
