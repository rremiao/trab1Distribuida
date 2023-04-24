import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Server extends UnicastRemoteObject implements ServerInterface {

	private static List<String> portasServidor; // consome arquivo portasServer
	private static List<String> portasClient; // consome arquivo portasClient
	private static List<String> arquivoBase;
	private static int thisPort = 0;
	private static int clientPort = 0;
	private static int random = new Random().nextInt(4 - 0) + 0;
	private static ClientInterface clientInterface;

	public Server() throws RemoteException {
	}

	public static void main(String[] args) throws RemoteException {

		FileScanner fileScanner = new FileScanner();
		portasClient = fileScanner.readPorts("portasClient.txt");
		portasServidor = fileScanner.readPorts("portasServer.txt");
		arquivoBase = fileScanner.readBaseFile();

		clientPort = Integer.valueOf(portasClient.get(random)); // esse recebe por parametro, nao random
		thisPort = Integer.valueOf(portasServidor.get(random));

		try {
			System.setProperty("java.rmi.server.hostname", "localhost");
			LocateRegistry.createRegistry(thisPort);
			System.out.println("java RMI registry created at port: " + thisPort);
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		try {
			String server = "rmi://" + "localhost" + ":" + thisPort + "/server";
			Naming.rebind(server, new Server());
			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Serverfailed: " + e);
		}
	}

	private static ClientInterface lookup() {
		ClientInterface clientInterface = null;
		String connectLocation = "rmi://localhost:" + clientPort + "/client";
		try {
			System.out.println("Respondendo Callback client em : " + connectLocation + " ");
			clientInterface = (ClientInterface) Naming.lookup(connectLocation);
		} catch (Exception e) {
			System.out.println("Callback failed: ");
			e.printStackTrace();
		}
		return clientInterface;
	}

	@Override
	public List<String> ler() throws RemoteException {
		this.sleep();
		return arquivoBase;
	}

	@Override
	public String inserir(String texto) throws RemoteException {
		this.sleep();
		this.lock();
		arquivoBase.add(texto);

		try {
			FileWriter writerObj = new FileWriter("arquivoBase.txt", true);
			writerObj.write(texto);
			writerObj.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.unlock();
		return arquivoBase.get(arquivoBase.size() - 1);
	}

	@Override
	public int deletar(String texto) throws RemoteException {
		System.out.println(arquivoBase.size());
		this.sleep();
		this.lock();
		Collections.replaceAll(arquivoBase, texto, "");
		this.unlock();

		try {
			FileWriter writerObj = new FileWriter("arquivoBase.txt", true);

			for (String s : arquivoBase) {
				writerObj.append(s);
			}

			writerObj.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arquivoBase.size();
	}

	@Override
	public void endOfFile() throws RemoteException {
		clientInterface = lookup();
		clientInterface.encerra();
	}

	public void lock() throws RemoteException {
		// TODO Auto-generated method stub
		try {
			System.out.println("trava");
		} catch (Exception e) {
			// TODO: handle exception
			throw new UnsupportedOperationException("Unimplemented method 'lock'");
		}

	}

	public void unlock() throws RemoteException {
		// TODO Auto-generated method stub
		try {
			System.out.println("destrava");
		} catch (Exception e) {
			// TODO: handle exception
			throw new UnsupportedOperationException("Unimplemented method 'unlock'");
		}

	}

	public void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}