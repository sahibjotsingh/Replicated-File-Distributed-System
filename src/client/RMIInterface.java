
package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote 
{
    public String Operation(String input, String append) throws RemoteException;
    public void Update(boolean readAllowed, boolean writeAllowed) throws RemoteException;
    public void UpdateAll(boolean readAllowed, boolean writeAllowed) throws RemoteException;
    public void UpdateFile(String append) throws RemoteException;
    public void UpdateAllFile(String append) throws RemoteException;
}
