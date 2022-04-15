import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class ClientJPB1 {
    
    private static final int SERVER_PORT = 8765;

    public static void main(String[] args) throws IOException {

        DataOutputStream toServer;
        DataInputStream fromServer;
        Scanner input =
                new Scanner(System.in);

        //attempt to connect to the server
        Socket socket =
                new Socket("localhost", SERVER_PORT);

        //create input stream to receive data
        //from the server
        fromServer =
                new DataInputStream(socket.getInputStream());

        toServer =
                new DataOutputStream(socket.getOutputStream());
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    System.out.print("Send command to server:\t");
                    String msg = input.nextLine();

                    try {
                        // write on the output stream
                        toServer.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = fromServer.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();



    }//end main
}
