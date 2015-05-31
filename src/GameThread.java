
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread {

  private Socket Player1, Player2;

  private char[][] GameBoard;

  public GameThread(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
    System.out.println("GameThread Started!");
    new ReceiveMessages(this.Player1, this.Player2).start();
    new ReceiveMessages(this.Player2, this.Player1).start();
  }
  
  private void SendPlayerSymbol() { 
    try {
      OutputStream ToPlayerOne = Player1.getOutputStream();
      ObjectOutputStream OutToPlayer1 = new ObjectOutputStream(ToPlayerOne);
      OutToPlayer1.writeByte(ServerGlobal.GET_PLAYER_SYMBOL);
      OutToPlayer1.writeChar('O');
      
      OutputStream ToPlayerTwo = Player2.getOutputStream();
      ObjectOutputStream OutToPlayer2 = new ObjectOutputStream(ToPlayerTwo);
      OutToPlayer2.writeByte(ServerGlobal.GET_PLAYER_SYMBOL);
      OutToPlayer1.writeChar('X');
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

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
      System.out.println("Cast problem");
    }
  }

  private class ReceiveMessages extends Thread {

    private Socket SourceClient, DestinationClient;

    ReceiveMessages(Socket SourceClient, Socket DestinationClient) {
      this.SourceClient = SourceClient;
      this.DestinationClient = DestinationClient;
    }

    @Override
    public void run() {
      while (true) {
        try {
          ObjectInputStream in = new ObjectInputStream(this.SourceClient.getInputStream());

          switch (in.readByte()) {
            case ServerGlobal.CLIENT_CONNECTED:
              System.out.println(in.readUTF());
              break;
              
            case ServerGlobal.BOARD_STATUS:
              GameBoard = (char[][]) in.readObject();
              SendBoardStatus(this.DestinationClient);
              break;
          }
        }
        catch (IOException | ClassNotFoundException ex) {
          Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}
