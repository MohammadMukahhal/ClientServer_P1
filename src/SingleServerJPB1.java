
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;


public class SingleServerJPB1 {
    
    private static final int SERVER_PORT = 8765;
    
    public static void main(String[] args) {
        createCommunicationLoop();
    }//end main
    
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

            File loginFile = new File(
                    "Database/login.txt");
            BufferedReader br
                    = new BufferedReader(new FileReader(loginFile));
            ArrayList<String> loginArray = new ArrayList<>();
            // Declaring a string variable
            String loginString;
            // Condition holds true till
            // there is character in a string
            while ((loginString = br.readLine()) != null)
                // Print the string
                loginArray.add(loginString);
            
            //server loop listening for the client 
            //and responding
            while(true) {
                String strReceived = inputFromClient.readUTF();
                String[] data = strReceived.split(" ");
                String command = data[0];
                String param1 = "";
                String param2 = "";
                if(data.length > 1){
                    param1 = data[1];
                    if(data.length > 2){
                        param2 = data[2];
                    }
                }
                if(command.equalsIgnoreCase("LOGIN")) {
                    System.out.println("Sending Client Login Message");
                    //outputToClient.writeUTF("Login Called for username: " + param1 + " and password: " + param2);
                    if(loginArray.contains(param1 + " " + param2)){
                        outputToClient.writeUTF("SUCCESS");
                        System.out.println( param1 + " Logged in successfully");
                    }
                    else{
                        outputToClient.writeUTF("FAILURE: Please provide correct username and password.  Try again.");
                        System.out.println( param1 + " Did not login, failed attempt, maybe a scam?");
                    }
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
