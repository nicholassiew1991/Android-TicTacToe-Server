
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
  }

  private void SendBoardStatus(Socket DestClient) {
    try {
      OutputStream outToClient = DestClient.getOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(outToClient);
      out.writeObject(GameBoard);
    } 
    catch (IOException e) {
      e.printStackTrace();
    } 
    catch (ClassCastException e) {
      System.out.println("Cast problem");
    }
  }

  private class ReceiveBoardStatus extends Thread {

    private Socket Player1, Player2;

    ReceiveBoardStatus(Socket Player1, Socket Player2) {
      this.Player1 = Player1;
      this.Player2 = Player2;
    }

    @Override
    public void run() {
      while (true) {
        try {
          ObjectInputStream in = new ObjectInputStream(this.Player1.getInputStream());
          GameBoard = (char[][]) in.readObject();
          SendBoardStatus(this.Player2);
        } 
        catch (IOException | ClassNotFoundException ex) {
          Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}
