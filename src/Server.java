import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

import Client.Packet;

public class Server implements KeyValue {

    KeyValueStore kv ;
    ExecutorService executor;
    public Server(int portNumber) {
        this.kv = new KeyValueStore();
        this.executor = Executors.newFixedThreadPool(32);
    }

    @Override
    public Packet Put(Packet p) throws RemoteException {
        Callable<Packet> putTask = () -> {
             {
                kv.put(p.getKey(), p.getValue());
                p.setResponse("KEY Value Successfully Set");
                p.logResponseServer();
            }
            return p;
        };
        Future<Packet> future =executor.submit(putTask);
        try {
            return  future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Packet Get(Packet p) throws RemoteException {
        Callable<Packet> putTask = () -> {
            {
                p.setResponse(kv.get(p.getKey()));
                p.logResponseServer();
            }
        return p;};
        Future<Packet> future =executor.submit(putTask);
        try {
            return  future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Packet Delete(Packet p) throws RemoteException {
        Callable<Packet> putTask = () -> {
           {
                kv.delete(p.getKey());
                p.setResponse("Key-Value Successfully Deleted");
                p.logResponseServer();}
        return p;};
        Future<Packet> future = executor.submit(putTask);
        try {
            return  future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        int port = 1099;
        try {
            Server obj = new Server(port);
            LocateRegistry.createRegistry(port);
            System.out.println("RMI Registry started on port " + port);
            KeyValue stub = (KeyValue) UnicastRemoteObject.exportObject(obj, port);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("KeyValue", stub);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
