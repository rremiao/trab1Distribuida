package trab1Distribuida;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Client extends UnicastRemoteObject implements JogadorInterface {

	private static final long serialVersionUID = 4444815691400839830L;

	protected Client() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static volatile int jogadas;
	private static volatile String remoteHostName = null;
	private static volatile int id;
	private static volatile boolean encerrou = false;
	private static JogoInterface jogoInterface;

	public static void main(String[] args) {

		if (args.length != 4) {
			System.out.println("Usage: java Client <server ip> <client ip> <nickname> <nro de jogadas>");
//			args = new String[4];
//			args[0] = "localhost";
//			args[1] = "localhost";
//			args[2] = "user1";
//			args[3] = "10";
			System.exit(1);
		}

		try {
			System.setProperty("java.rmi.server.hostname", args[1]);
			LocateRegistry.createRegistry(52369);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		try {
			String client = "rmi://" + args[1] + ":52369/client";
			Naming.rebind(client, new Client());
			System.out.println("Client is ready.");
		} catch (Exception e) {
			System.out.println("Client failed: " + e);
		}
		remoteHostName = args[0];
		jogadas = Integer.parseInt(args[3]);
		id = solicitarRegistro(args);
	}

	private static int solicitarRegistro(String[] args) {
		try {
			jogoInterface = connect();
			int id = jogoInterface.register(args[2]);
			System.out.println("register() successful, id: " + id);
			return id;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private static JogoInterface connect() {
		try {
			System.out.println("Connecting to server at : " + "rmi://" + remoteHostName + ":52369/server");
			return (JogoInterface) Naming.lookup("rmi://" + remoteHostName + ":52369/server");
		} catch (Exception e) {
			System.out.println("Client failed: ");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void inicia() throws RemoteException {
		System.out.println("Iniciando partida...");
		Random rand = new Random();
		for (int i = 0; i < jogadas; i++) {
			if (encerrou) {
				System.out.println("Jogadas encerradas pelo servidor!");
				return;
			}
			try {
				Thread.sleep(rand.nextInt((1500 - 500) + 1) + 500);
				jogoInterface.joga(id);
			} catch (InterruptedException | RemoteException e) {
				e.printStackTrace();
			}
			System.out.println("Jogada " + i + " realizada!");
		}
		System.out.println("Todas as jogadas foram realizadas!");
		try {
			jogoInterface.encerra(id);			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void finaliza() throws RemoteException {
		System.out.println("Voce foi encerrado pelo servidor, fim da partida!");
		encerrou = true;
	}

	@Override
	public void cutuca() throws RemoteException {
		System.out.println("Recebeu uma cutucada do servidor, tudo OK!");
	}
}