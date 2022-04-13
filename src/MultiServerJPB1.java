
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class MultiServerJPB1 {
    
    private static final int SERVER_PORT = 8765;
    
    public static void main(String[] args) {
        //createCommunicationLoop();
        createMultithreadCommunicationLoop();
    }//end main
    
    public static void createMultithreadCommunicationLoop() {
        int clientNumber = 0;
        
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on " + new Date() + ".");
            //listen for new connection request
            while(true) {
                Socket socket = serverSocket.accept();
                clientNumber++;  //increment client num
            
                //Find client's host name 
                //and IP address
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("Connection from client " + 
                        clientNumber);
                System.out.println("\tHost name: " + 
                        inetAddress.getHostName());
                System.out.println("\tHost IP address: "+
                        inetAddress.getHostAddress());
                
                //create and start new thread for the connection
                Thread clientThread = new Thread(
                        new ClientHandler(clientNumber, socket, serverSocket));
                clientThread.start();  
            }//end while           
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
    }//end createMultithreadCommunicationLoop
    
    public static void createCommunicationLoop() {
        try {
            //create server socket
            ServerSocket serverSocket = 
                    new ServerSocket(SERVER_PORT);
            
            System.out.println("Server started at " +
                    new Date() + "\n");
            //listen for a connection
            //using a regular *client* socket
            Socket socket = serverSocket.accept();
            
            //now, prepare to send and receive data
            //on output streams
            DataInputStream inputFromClient = 
                    new DataInputStream(socket.getInputStream());
            
            DataOutputStream outputToClient =
                    new DataOutputStream(socket.getOutputStream());
            
            //server loop listening for the client 
            //and responding
            while(true) {
                String strReceived = inputFromClient.readUTF();
                
                if(strReceived.equalsIgnoreCase("hello")) {
                    System.out.println("Sending hello to client");
                    outputToClient.writeUTF("hello client!");
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
            }//end server loop
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
    }//end createCommunicationLoop
}
