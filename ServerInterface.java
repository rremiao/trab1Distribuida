import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote {
	public List<String> ler() throws RemoteException;

	public String inserir(String texto) throws RemoteException;

	public int deletar(String texto) throws RemoteException;

	public void endOfFile() throws RemoteException;
}