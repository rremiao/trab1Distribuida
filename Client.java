package trab1Distribuida;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Client extends UnicastRemoteObject implements ClientInterface {
	protected Client() throws RemoteException {
		super();
	}

	private static volatile String remoteHostName = null;
	private static ServerInterface serverInterface;
	private static List<String> portasServidor; //consome arquivos portasServer
	private static List<String> portasClient; //consome arquivos portasClient

	public static void main(String[] args) {

		FileScanner fileScanner = new FileScanner();
		portasClient = fileScanner.readPorts("./portas/portasClient.txt");

		try {
			System.setProperty("java.rmi.server.hostname", "localhost");//trocar pela porta do arquivo
			LocateRegistry.createRegistry(52369);//trocar pela porta do arquivo
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		try {
			String client = "rmi://" + "localhost" + ":52369/client";//trocar pela porta do arquivo
			Naming.rebind(client, new Client());
			System.out.println("Client is ready.");
		} catch (Exception e) {
			System.out.println("Client failed: " + e);
		}
		remoteHostName = "localhost";
		// jogadas = Integer.parseInt(args[3]);
		// id = solicitarRegistro(args);
	}

	// private static int solicitarRegistro(String[] args) {
	// 	try {
	// 		jogoInterface = connect();
	// 		int id = jogoInterface.register(args[2]);
	// 		System.out.println("register() successful, id: " + id);
	// 		return id;
	// 	} catch (RemoteException e) {
	// 		e.printStackTrace();
	// 	}
	// 	return -1;
	// }

	private static ServerInterface connect() {
		try {
			System.out.println("Connecting to server at : " + "rmi://" + remoteHostName + ":52369/server");//trocar pela porta do arquivo
			return (ServerInterface) Naming.lookup("rmi://" + remoteHostName + ":52369/server");//trocar pela porta do arquivo
		} catch (Exception e) {
			System.out.println("Client failed: ");
			e.printStackTrace();
		}
		return null;
	}

	// @Override
	// public void inicia() throws RemoteException {
	// 	System.out.println("Iniciando partida...");
	// 	Random rand = new Random();
	// 	for (int i = 0; i < jogadas; i++) {
	// 		if (encerrou) {
	// 			System.out.println("Jogadas encerradas pelo servidor!");
	// 			return;
	// 		}
	// 		try {
	// 			Thread.sleep(rand.nextInt((1500 - 500) + 1) + 500);
	// 			jogoInterface.joga(id);
	// 		} catch (InterruptedException | RemoteException e) {
	// 			e.printStackTrace();
	// 		}
	// 		System.out.println("Jogada " + i + " realizada!");
	// 	}
	// 	System.out.println("Todas as jogadas foram realizadas!");
	// 	try {
	// 		jogoInterface.encerra(id);			
	// 	} catch (RemoteException e) {
	// 		e.printStackTrace();
	// 	}
	// }

	// @Override
	// public void finaliza() throws RemoteException {
	// 	System.out.println("Voce foi encerrado pelo servidor, fim da partida!");
	// 	encerrou = true;
	// }

	// @Override
	// public void cutuca() throws RemoteException {
	// 	System.out.println("Recebeu uma cutucada do servidor, tudo OK!");
	// }

	@Override
	public void ler() throws RemoteException {
		System.out.println(0);
	}

	@Override
	public void inserir() throws RemoteException {
		System.out.println(1);
	}

	@Override
	public void deletar() throws RemoteException {
		System.out.println(2);
	}
}