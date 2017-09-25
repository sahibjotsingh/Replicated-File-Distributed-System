
package Servers;

import RMIInterface.RMIInterface;
import Servers.Server2;
import Servers.Server1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server4 extends UnicastRemoteObject implements RMIInterface
{
    public boolean readAllowed=true;
    public boolean writeAllowed=true;
    String portNumber[]={"5001", "5002", "5003", "5005"};
    String IPaddress[]={"localhost", "localhost", "localhost", "localhost"};
    String serverName[]={"server1", "server2", "server3", "server5"};
    String filePath = "D:\\Server files\\Server4\\textfile.txt";
    protected Server4() throws RemoteException, NotBoundException, MalformedURLException 
    {
        super();
    }
    
    @Override
    public void Update(boolean readAllowed, boolean writeAllowed)
    {
        this.readAllowed=readAllowed;
        this.writeAllowed=writeAllowed;
    }

    
    @Override
    public void UpdateAll(boolean readAllowed, boolean writeAllowed) throws RemoteException
    {
        int i;
        //portNumber.length
        for (i=0;i<portNumber.length;++i) 
        { 	
            try 
            {
                RMIInterface service=(RMIInterface)Naming.lookup("rmi://" + IPaddress[i] + ":" + portNumber[i] + "/" + serverName[i]);
                service.Update(readAllowed, writeAllowed);
            } catch (NotBoundException | MalformedURLException ex) {
                Logger.getLogger(Server2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    @Override
    public void UpdateAllFile(String append)
    {
        int i;
         //portNumber.length
        for (i=0;i<portNumber.length;++i) 
        {
            try {
                RMIInterface service=(RMIInterface)Naming.lookup("rmi://" + IPaddress[i] + ":" + portNumber[i] + "/" + serverName[i]);
                service.UpdateFile(append);
            } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                Logger.getLogger(Server1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void UpdateFile(String append)
    {
        try
                {
                    File file =new File(filePath);
                    if(!file.exists())
                    {
                        file.createNewFile();
                    }
                FileWriter fw = new FileWriter(file,true);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(append);
            }

                }catch(IOException ioe){
                    System.out.println("Exception occurred:");
                }
    }
    
    @Override
    public String Operation(String operation, String append) throws RemoteException
    {
        int i=0;
        if("R".equals(operation))
        {
            if(readAllowed==true)
            { 
                    String content="";
                    readAllowed=true;
                    writeAllowed=false;
                    UpdateAll(readAllowed, writeAllowed);
                try 
                {
                    try 
                    {
                        BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
                        String lineText = "";     
                        while ((lineText = lineReader.readLine()) != null) {
                        content=content + lineText + "\n";
                    }
                    lineReader.close();
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                    Thread.sleep(5*1000);
                }
                catch(Exception e){
                    System.out.println("Oh no!!!");
                }
                    readAllowed=true;
                    writeAllowed=true;
                    UpdateAll(readAllowed, writeAllowed);
                    //message.setText("");
                    return content;
            }
            else if(readAllowed==false)
            {
                //message.setText("Reading is not allowed at this moment");
                return "Reading is not allowed at this moment";
            }
        }
        else
        {
            if(writeAllowed==true)
            {
                readAllowed=false;
                writeAllowed=false;
                UpdateAll(readAllowed, writeAllowed);
                try 
                {
                try
                {
                   
                    File file =new File(filePath);
                    if(!file.exists())
                    {
                        file.createNewFile();
                    }

                FileWriter fw = new FileWriter(file,true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(append);
                bw.close();
                }catch(IOException ioe){
                    System.out.println("Exception occurred:");
                    ioe.printStackTrace();
                }
                Thread.sleep(5*1000);
                }catch(Exception e){
                    System.out.println("Oh no!!!");
                }
                
                UpdateAllFile(append);
                readAllowed=true;
                writeAllowed=true;
                UpdateAll(readAllowed, writeAllowed);
                //message.setText("");
                return "Successfully written to file";
            }
            else if(writeAllowed==false)
            {
                //message.setText("Writing is not allowed at this moment");
                return "Writing is not allowed at this moment";
            }
        }
        return null;
    }
    
    public static void main(String[] args)
    {
        try 
        {
            Registry registry=LocateRegistry.createRegistry(5004);
            registry.rebind("server4", new Server4());
            System.err.println("Server ready");
        } catch (Exception e) {

            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}