package trab1Distribuida;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	public int lock() throws RemoteException;

	public int unlock(int id) throws RemoteException;
}