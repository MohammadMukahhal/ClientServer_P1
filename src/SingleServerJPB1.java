
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
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
            boolean isLoggedIn = false;
            boolean isAdmin = false;
            String currentUser = "";
            
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
                String param3 = "";
                if(data.length > 1){
                    param1 = data[1];
                    if(data.length > 2){
                        param2 = data[2];
                        if(data.length > 3){
                            param3 = data[3];
                        }
                    }
                }
                if(command.equalsIgnoreCase("LOGIN")) {
                    System.out.println("Sending Client Login Message");
                    //outputToClient.writeUTF("Login Called for username: " + param1 + " and password: " + param2);
                    if(loginArray.contains(param1 + " " + param2)){
                        outputToClient.writeUTF("SUCCESS");
                        System.out.println( param1 + " Logged in successfully");
                        isLoggedIn = true;
                        currentUser = param1;
                        if(currentUser.equals("root")){
                            isAdmin = true;
                        }
                    }
                    else{
                        outputToClient.writeUTF("FAILURE: Please provide correct username and password.  Try again.");
                        System.out.println( param1 + " Did not login, failed attempt, maybe a scam?");
                    }
                }
                else if(command.equalsIgnoreCase("SOLVE")) {
                    if(param1.equals("-c") && isLoggedIn == true){
                        if(param2 != "" && param3 == "") {
                            String output = solveCircle(param2);
                            outputToClient.writeUTF(output);
                            System.out.println(output);
                        }
                        else{
                            outputToClient.writeUTF("Error:  No radius found");
                            System.out.println("Error:  No radius found");
                        }
                    }
                    else if(param1.equals("-r")){

                       outputToClient.writeUTF( solveRect(param2, param3) );

                    }
                    else if(!isLoggedIn){
                        outputToClient.writeUTF("Error:  Can not do that without being logged in");
                        System.out.println("Error:  User attempted to solve without logging in");
                    }
                    else{
                        outputToClient.writeUTF("Invalid operation");
                        System.out.println( currentUser + " called an invalid operation in SOLVE");
                    }
                }
                else if(command.equalsIgnoreCase("LIST")) {
                    if(isLoggedIn) {
                        String solutionsFilePath = "Database/" + currentUser + "_solutions.txt";
                        File solutionsFile = new File(
                                solutionsFilePath);
                        BufferedReader brSolution
                                = new BufferedReader(new FileReader(solutionsFile));
                        ArrayList<String> solutionArray = new ArrayList<>();
                        String solutionString;
                        while ((solutionString = brSolution.readLine()) != null) {
                            // Print the string
                            solutionArray.add(solutionString);
                        }
                        String output;
                        //outputToClient.writeUTF("\n" + currentUser + "\n");
                        output = "\n" + currentUser;
                        if(solutionArray.isEmpty()){
                            solutionArray.add("          No interactions yet");
                        }
                        for (String element : solutionArray) {
                            output = output + "\n" + element;
                        }
                        if(!isAdmin && param1 == "-all"){
                            outputToClient.writeUTF("Error:  you are not the root user");
                        }
                        else if(param1 == "") {
                            outputToClient.writeUTF(output);
                        }
                        else if(isAdmin && param1 == "-all"){
                            File solutionsFileJohn = new File(
                                    "Database/john_solutions.txt");
                            BufferedReader brSolutionJohn
                                    = new BufferedReader(new FileReader(solutionsFileJohn));
                            ArrayList<String> solutionArrayJohn = new ArrayList<>();
                            while ((solutionString = brSolutionJohn.readLine()) != null) {
                                // Print the string
                                solutionArrayJohn.add(solutionString);
                            }
                            output = output + "\n john";
                            if(solutionArrayJohn.isEmpty()){
                                solutionArray.add("          No interactions yet");
                            }
                            for (String element : solutionArrayJohn) {
                                output = output + "\n" + element;
                            }

                            File solutionsFileSally = new File(
                                    "Database/sally_solutions.txt");
                            BufferedReader brSolutionSally
                                    = new BufferedReader(new FileReader(solutionsFileSally));
                            ArrayList<String> solutionArraySally = new ArrayList<>();
                            while ((solutionString = brSolutionSally.readLine()) != null) {
                                // Print the string
                                solutionArraySally.add(solutionString);
                            }
                            output = output + "\n sally";
                            if(solutionArraySally.isEmpty()){
                                solutionArray.add("          No interactions yet");
                            }
                            for (String element : solutionArraySally) {
                                output = output + "\n" + element;
                            }

                            File solutionsFileQiang = new File(
                                    "Database/qiang_solutions.txt");
                            BufferedReader brSolutionQiang
                                    = new BufferedReader(new FileReader(solutionsFileQiang));
                            ArrayList<String> solutionArrayQiang = new ArrayList<>();
                            while ((solutionString = brSolutionQiang.readLine()) != null) {
                                // Print the string
                                solutionArrayQiang.add(solutionString);
                            }
                            output = output + "\n qiang";
                            if(solutionArrayQiang.isEmpty()){
                                solutionArray.add("          No interactions yet");
                            }
                            for (String element : solutionArrayQiang) {
                                output = output + "\n" + element;
                            }
                            outputToClient.writeUTF(output);
                        }

                    }
                    else{
                        outputToClient.writeUTF("Error:  Can not do that without being logged in");
                        System.out.println("Error:  User attempted to list without logging in");
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
    private static String solveCircle(String param2){
        double circum = 2*Math.PI*Double.parseDouble(param2);
        double area = Math.PI * Math.pow(Double.parseDouble(param2),2);
        DecimalFormat df = new DecimalFormat("##.##");
        return "Circle’s circumference is " + df.format(circum) + " and area is " + df.format(area);
    }
    private static String solveRect(String param2, String param3){
        if(param3 == ""){
            param3 = param2;
        }
        double perimeter = (Double.parseDouble(param2) + Double.parseDouble(param3))*2;
        double area = Double.parseDouble(param2) * Double.parseDouble(param3);
        DecimalFormat df = new DecimalFormat("##.##");
        return "The area is " + df.format(area) + " and perimeter is " + df.format(perimeter);
    }
}
