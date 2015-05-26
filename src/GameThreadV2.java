import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class GameThreadV2 extends Thread{
  
  Socket Player1, Player2;
  
  public GameThreadV2(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
    System.out.println("GameThread Started!");
    System.out.println(Player1);
    System.out.println(Player2);
   
    ReceiveBoardStatus(this.Player1);
  }
  
  private void ReceiveBoardStatus(Socket ClientSocket) {
	  
		try {
		  ByteBuffer bbb = ByteBuffer.allocate(100);
		  InputStream in = ClientSocket.getInputStream();
		  in.read(bbb.array());
		  String message = new String(bbb.array()).trim();
		  System.out.println("mes is " + message);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}
