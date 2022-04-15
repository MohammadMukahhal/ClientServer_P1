
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;


public class MultiServerJPB1 {
    
    private static final int SERVER_PORT = 8765;
    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    static Vector<String> isActive = new Vector<>();
    static Vector<String> isUser = new Vector<>();
    
    public static void main(String[] args) {
        //createCommunicationLoop();
        createMultithreadCommunicationLoop();
    }//end main
    
    public static void createMultithreadCommunicationLoop() {
        int clientNumber = 0;
        
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on " + new Date() + ".");
            isUser.add("john");
            isUser.add("qiang");
            isUser.add("root");
            isUser.add("sally");
            //listen for new connection request
            while(true) {
                Socket socket = serverSocket.accept();
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
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
                        new ClientHandler(clientNumber, socket, serverSocket,outputToClient,inputFromClient));
                clientThread.start();

            }//end while
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }//end createMultithreadCommunicationLoop
}
