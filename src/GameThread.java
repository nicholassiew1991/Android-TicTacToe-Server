import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    new ReceiveMessageThread(this.Player1, this.Player2).start();
    new ReceiveMessageThread(this.Player2, this.Player1).start();
  }
  
  private class ReceiveMessageThread extends Thread {
    
    private Socket SourceClient, DestinationClient;
    private DataInputStream dIn;
    
    private boolean isGameOver = false;

    ReceiveMessageThread(Socket SourceClient, Socket DestinationClient) {
      this.SourceClient = SourceClient;
      this.DestinationClient = DestinationClient;
    }
    
    @Override
    public void run() {
      while (isGameOver == false) {
        try {
          dIn = new DataInputStream(SourceClient.getInputStream());
          byte MessagesType = dIn.readByte();
          
          if (MessagesType == ServerGlobal.BOARD_STATUS) {
            SendBoardStatus();
          }
          if (MessagesType == ServerGlobal.CHAT_MESSAGE) {
            ForwardChatMessage();
          }
        }
        catch (java.io.EOFException | java.net.SocketException ex) {
          System.out.println(this.SourceClient + " disconnected.");
          SendPlayerDisconnectMessages();
          GameOver();
        }
        catch (IOException ex) {
          Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    
    private void SendBoardStatus() {
      
      try {
      
        System.out.println("Receive BoardStatus from: " + this.SourceClient);

        for (int a = 0; a < 3; a++) {
          for (int b = 0; b < 3; b++) {
            GameBoard[a][b] = dIn.readChar();
          }
        }
        
        DataOutputStream dOut = new DataOutputStream(this.DestinationClient.getOutputStream());
        dOut.writeByte(ServerGlobal.BOARD_STATUS);
        for (int a = 0; a < 3; a++) {
          for (int b = 0; b < 3; b++) {
            dOut.writeChar(GameBoard[a][b]);
          }
        }
        
        dOut.flush(); // Send out
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    
    private void ForwardChatMessage() {
      try {
        DataOutputStream dOut = new DataOutputStream(this.DestinationClient.getOutputStream());
        String ReceivedMessage = dIn.readUTF();
        
        System.out.println("Receive chat message from: " + this.SourceClient + " with: " + ReceivedMessage);
        
        dOut.writeByte(ServerGlobal.CHAT_MESSAGE);
        dOut.writeUTF(ReceivedMessage);
        dOut.flush();
        
        System.out.println(ReceivedMessage + " has sent to: " + this.DestinationClient);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    private void SendPlayerDisconnectMessages() {
      try {
        DataOutputStream dOut = new DataOutputStream(this.DestinationClient.getOutputStream());
        dOut.writeByte(ServerGlobal.CLIENT_DISCONNECT_WHILE_PLAYING);
        dOut.writeUTF("Opponent is disconnected from the game. You win.");
        dOut.flush(); // Send out
        this.DestinationClient.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    private void GameOver() {
      this.isGameOver = true;
    }
  }
}
