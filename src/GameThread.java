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

  private char[][] GameBoard;

  public GameThread(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
    System.out.println("GameThread Started!");
    StartReceiveMessagesThread();
  }
  
  private void StartReceiveMessagesThread() {
    new ReceiveMessagesThread(this.Player1, this.Player2).start();
    new ReceiveMessagesThread(this.Player2, this.Player1).start();
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

          switch (in.readByte()) {
            case ServerGlobal.BOARD_STATUS:
              GameBoard = (char[][]) in.readObject();
              SendBoardStatus(this.DestinationClient);
              break;
          }
        }
        catch (java.io.StreamCorruptedException ex) {
          ex.printStackTrace();
          break;
        }
        catch (IOException | ClassNotFoundException ex) {
          Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}
