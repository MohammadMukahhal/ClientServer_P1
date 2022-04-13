
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



//Runnable class allows us to create a task
//to be run on a thread
public class ClientHandler implements Runnable {
    private Socket socket;  //connected socket
    private ServerSocket serverSocket;  //server's socket
    private int clientNumber;
    
    //create an instance
    public ClientHandler(int clientNumber, Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.clientNumber = clientNumber;
    }//end ctor
    
    
    //run() method is required by all
    //Runnable implementers
    @Override
    public void run() {
        //run the thread in here
        try {
            DataInputStream inputFromClient =
                    new DataInputStream(socket.getInputStream());
            
            DataOutputStream outputToClient = 
                    new DataOutputStream(socket.getOutputStream());
            
            
            //continuously serve the client
            while(true) {                
                String strReceived = inputFromClient.readUTF();
                System.out.println("\n\t[[Command " + strReceived +
                        " received from client " + clientNumber +"]]");
                
                if(strReceived.equalsIgnoreCase("hello")) {
                    System.out.println("Sending hello to client " +
                            clientNumber);
                    outputToClient.writeUTF("hello client " +
                            clientNumber + "!");
                }
                else if(strReceived.equalsIgnoreCase("quit")) {
                    System.out.println("Shutting down server...");
                    outputToClient.writeUTF("Shutting down server...");
                    serverSocket.close();
                    socket.close();
                    break;  //get out of loop
                }
                else {
                    System.out.println("Unknown command received: " 
                        + strReceived);
                    outputToClient.writeUTF("Unknown command.  "
                            + "Please try again.");
                    
                }
            }//end while
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
        
    }//end run
    
}//end ClientHandler
