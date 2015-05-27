import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread{
  
  private Socket Player1, Player2;
  
  private static int GameCount = 0;
  
  public GameThread(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
    System.out.println("GameThread Started!");
    GameThread.GameCount++;
    System.out.println(Player1);
    System.out.println(Player2);
    
    ReceiveBoardStatus(this.Player1);
  }
  
  public int GetGameCount() {
    return GameThread.GameCount;
  }
  
  @Override
  public void run() {
    
    while (true) {
      
    }
  }
  
  private void ReceiveBoardStatus(Socket ClientSocket) {
    
    while(true){
    	try {
    	      ObjectInputStream in = new ObjectInputStream(ClientSocket.getInputStream());
    	      char[][] bb = (char[][])in.readObject();
    	      
    	      for(int i=0;i<3;i++){
    	      	for(int j=0;j<3;j++){
    	      		System.out.print(bb[i][j]+" ");
    	      	}
    	      	System.out.println();
    	      }
    	    } 
    	    catch (IOException ex) {
    	      Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
    	    } 
    	    catch (ClassNotFoundException ex) {
    	      Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
    	    }
    }
  }
}
