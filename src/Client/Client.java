package Client;

import RMIInterface.RMIInterface;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client 
{
    public JLabel      clientLabel;
    public JTextField  message1, message2;
    public JButton     read, write, clear;
    public JTextArea   content;

    String portNumber[]={"5001", "5002", "5003", "5004", "5005"};
    String IPaddress[]={"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
    String serverName[]={"server1", "server2", "server3", "server4", "server5"};
    
    public Client() throws NotBoundException, MalformedURLException, RemoteException
    {
        setup();
    }
    
    public void setup() throws NotBoundException, MalformedURLException, RemoteException
    {
        Random rand = new Random(); 
        
        JFrame jframe = new JFrame("Client Menu");
        JPanel panel = (JPanel) jframe.getContentPane();
        panel.setLayout(null);
        jframe.setSize(420, 340);
        
        content = new JTextArea();
        content.setFont(new Font("Tahoma", 0, 12));
        panel.add(content);
        content.setBounds(10, 45, 300, 200);
        
        message1 = new JTextField();
        message1.setFont(new Font("Tahoma", 0, 12));
        message1.setEditable(false);
        panel.add(message1);
        message1.setBounds(100, 10, 210, 20);
        
        read = new JButton("Read");
        read.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    
                    int i=rand.nextInt(5);
                    try {
                        RMIInterface service = (RMIInterface)Naming.lookup("rmi://" + IPaddress[i] + ":" + portNumber[i] + "/" + serverName[i]);
                         message1.setText("Your request is being checked by" + serverName[i]);
                        content.setText(service.Operation("R", ""));
                        message1.setText("");
                    } catch (NotBoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        panel.add(read);
        read.setFont(new Font("Tahoma", 0, 12));
        read.setBounds(10, 10, 70, 20);
        
        message2 = new JTextField();
        message2.setFont(new Font("Tahoma", 0, 12));
        message2.setEditable(false);
        panel.add(message2);
        message2.setBounds(100, 260, 210, 20);
        
        write = new JButton("Write");
        write.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    int i=rand.nextInt(5);
                    message2.setText("Writing to file");
                    try {
                        RMIInterface service = (RMIInterface)Naming.lookup("rmi://" + IPaddress[i] + ":" + portNumber[i] + "/" + serverName[i]);
                        message2.setText("Your request is being checked by" + serverName[i]);
                        content.setText(service.Operation("W",content.getText()));
                        message2.setText("");
                    } catch (NotBoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        panel.add(write);
        write.setFont(new Font("Tahoma", 0, 12));
        write.setBounds(10, 260, 70, 20);
        
        clear = new JButton("clear");
        clear.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                content.setText("");
            }
        }
        );
        panel.add(clear);
        clear.setFont(new Font("Tahoma", 0, 12));
        clear.setBounds(330, 70, 70, 20);
        
        jframe.setVisible(true);
    }
    
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException
    {
        Client c=new Client();
    }
}
