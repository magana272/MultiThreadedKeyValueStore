import java.rmi.Remote;
import java.rmi.RemoteException;
import Client.Packet;


public interface KeyValue extends Remote {
     Packet Put(Packet p) throws RemoteException;
     Packet Get(Packet p) throws RemoteException;
     Packet Delete(Packet p) throws RemoteException;

}
