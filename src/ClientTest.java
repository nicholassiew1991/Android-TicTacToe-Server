
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTest {
  
  public static void main(String[] args) {
    
    try {
      Socket a = new Socket("192.168.191.1", 6666);
      a.close();
    } catch (IOException ex) {
      Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
