package trab1Distribuida;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoInterface extends Remote {
	public int register(String username) throws RemoteException;

	public int joga(int id) throws RemoteException;

	public int encerra(int id) throws RemoteException;
}