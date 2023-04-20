package trab1Distribuida;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	public void ler() throws RemoteException;

	public void inserir() throws RemoteException;

	public void deletar() throws RemoteException;
}