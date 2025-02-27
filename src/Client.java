import Client.Packet;
import org.json.JSONException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;



public class Client extends Thread {

    enum RequestType{
        PUT,
        GET,
        DELETE
    }
    private final Registry cRegistry;
    private String HostNameorIPAddress;


    public Client(String HostNameorIPAddres, int PortNumber) throws RemoteException {
        this.HostNameorIPAddress = HostNameorIPAddres;
        this.cRegistry = LocateRegistry.getRegistry(HostNameorIPAddres, PortNumber);
    }

    public final Registry getRegistry(){
        return this.cRegistry;
    }
    public final KeyValue getStub() throws NotBoundException, RemoteException {
        Registry r =  getRegistry();
        return (KeyValue) r.lookup("KeyValue");
    }





    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(args.length);
            System.err.println("Port Number and IP Number Must be Provided");
            System.exit(1);
        }
        String IPString = args[0];
        String PortString = args[1];
        int PortNumber = Integer.parseInt(PortString);
        String userText;
        Client c = null;
        KeyValue stub = null;
        try {
            c = new Client(IPString, PortNumber);
        } catch (RemoteException e) {
            System.err.println("Could not establish Client. The port could be busy");
            System.exit(0);
        }

        try {
            stub = c.getStub();
        } catch (NotBoundException | RemoteException e) {
            System.err.println("Couldn't connect to server");
            System.exit(0);
        }

        Example ex = new Example(stub);
        ex.runExample();
        Scanner userInput = new Scanner(System.in);
        while (true) {
            if(userInput.hasNext()) {
                userText = userInput.nextLine();
                Packet p;
                try {
                    p = new Packet(userText);
                } catch (JSONException e) {
                    p = null;
                    p.logMalformedRequest();
                    continue;
                }

                switch (p.getType()) {
                    case GET:
                        try {
                            p = stub.Get(p);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        p.logResponseClient();
                        break;
                    case PUT:
                        try {
                            p = stub.Put(p);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        p.logResponseClient();
                        break;
                    case DELETE:
                        try {
                            p = stub.Delete(p);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        p.logResponseClient();
                        break;
                    default:
                        System.err.println("Type not recongnized");
                }
            }
        }
    }
}
