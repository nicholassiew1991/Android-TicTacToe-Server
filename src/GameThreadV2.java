import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.portable.OutputStream;

public class GameThread extends Thread{
  
  private Socket Player1, Player2;
  
  private static int GameCount = 0;
  private char[][] bb;
  public GameThread(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
    
    System.out.println("GameThread Started!");
    GameThread.GameCount++;
    System.out.println(Player1);
    System.out.println(Player2);
    
    
  }
  
  public int GetGameCount() {
    return GameThread.GameCount;
  }
  
  @Override
  public void run() {
	  ReceiveBoardStatus(this.Player1);
  }
  
  private void ReceiveBoardStatus(Socket ClientSocket) {
    
    while(true){
    	try {
    	      ObjectInputStream in = new ObjectInputStream(ClientSocket.getInputStream());
    	      bb = (char[][])in.readObject();
    	      
    	      for(int i=0;i<3;i++){
    	      	for(int j=0;j<3;j++){
    	      		System.out.print(bb[i][j]+" ");
    	      	}
    	      	System.out.println();
    	      }
    	      
    	      SendBoardStatus(this.Player2);
    	    } 
    	    catch (IOException ex) {
    	      Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
    	    } 
    	    catch (ClassNotFoundException ex) {
    	      Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
    	    }
    }
  }
  
  private void SendBoardStatus(Socket client){
	  try {
	      OutputStream outToClient = (OutputStream) client.getOutputStream();
	      ObjectOutputStream out = new ObjectOutputStream(outToClient);
	      out.writeObject(bb);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }catch( java.lang.ClassCastException e){
	    	System.out.println("Cast problem");
	    }
  }
}
