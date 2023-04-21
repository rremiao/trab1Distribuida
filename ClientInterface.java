import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	public void encerra() throws RemoteException;

	public void timeout() throws RemoteException;

}