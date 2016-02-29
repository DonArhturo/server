package server;

/**
 * Socket programming example: TCP Server
 * DATS/ITPE2410 Networking and Cloud Computing, Spring 2016
 * HiOA
 **/
import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ServerClients
{
    public static void main(String[] args) throws IOException, ScriptException
    {
        int portNumber = 5555; // Default port to use

        if (args.length > 0)
        {
            if (args.length == 1)
                portNumber = Integer.parseInt(args[0]);
            else
            {
                System.err.println("Usage: java EchoUcaseServer [<port number>]");
                System.exit(1);
            }
        }

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        
        System.out.println("Hi, I am EchoUCase TCP server!");

        // try() with resource makes sure that all the resources are automatically
        // closed whether there is any exception or not!!!
        try (
                // Create server socket with the given port number
                ServerSocket serverSocket =
                        new ServerSocket(portNumber);
                // create connection socket, server begins listening
                // for incoming TCP requests
                Socket connectSocket = serverSocket.accept();

                // Stream writer to the connection socket
                PrintWriter out =
                        new PrintWriter(connectSocket.getOutputStream(), true);
                // Stream reader from the connection socket
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connectSocket.getInputStream()));
        )
        {
            String receivedText = "";
            // read from the connection socket
            while ((receivedText = in.readLine()) != null)
            {
                Pattern p = Pattern.compile("[^0-9+-/*]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(receivedText);
                boolean b = m.find();

                if(b){
                    System.out.println("feil");
                    //Here its just sends back a small(first) part of the sentence.. why?
                    String error = receivedText + ".. It is not a valid binary expression, so I can't evaluate it.";
                    out.println(error);
                }else{
                    out.println(engine.eval(receivedText));
                }
            }
            System.out.println("I am done, Bye!");
        } catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}