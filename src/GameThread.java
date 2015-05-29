import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread{
  
  private Socket Player1, Player2;
  
  private char[][] GameBoard;
  
  public GameThread(Socket Player1, Socket Player2) {
    this.Player1 = Player1;
    this.Player2 = Player2;
    
    System.out.println("GameThread Started!");
  }
  
  @Override
  public void run() {
	  ReceiveBoardStatus(this.Player1);
  }
  
  private void ReceiveBoardStatus(Socket ClientSocket) {
    
    while(true){
    	try {
    	      ObjectInputStream in = new ObjectInputStream(ClientSocket.getInputStream());
    	      GameBoard = (char[][])in.readObject();
    	      
    	      for(int i=0;i<3;i++){
    	      	for(int j=0;j<3;j++){
    	      		System.out.print(GameBoard[i][j]+" ");
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
      OutputStream outToClient = client.getOutputStream();
	    ObjectOutputStream out = new ObjectOutputStream(outToClient);
	    out.writeObject(GameBoard);
	  } 
    catch (IOException e) {
      e.printStackTrace();
	  }
    catch(ClassCastException e){
      System.out.println("Cast problem");
	  }
  }
}
