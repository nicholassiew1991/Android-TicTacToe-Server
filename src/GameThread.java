import java.net.Socket;

public class GameThread extends Thread{
  
  Socket Player1, Player2;
  
  public GameThread(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
  }
  
}