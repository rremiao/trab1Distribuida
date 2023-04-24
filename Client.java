import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;

public class Client extends UnicastRemoteObject implements ClientInterface {
	protected Client() throws RemoteException {
		super();
	}

	private static volatile String remoteHostName = null;
	private static ServerInterface serverInterface;
	private static List<String> portasServidor; // consome arquivos portasServer
	private static List<String> portasClient; // consome arquivos portasClient
	private static List<String> instrucoes;
	private static int thisPort = 0;
	private static int serverPort = 0;
	private static int random = new Random().nextInt(4 - 0) + 0;

	public static void main(String[] args) {

		FileScanner fileScanner = new FileScanner();
		portasClient = fileScanner.readPorts("portasClient.txt");
		portasServidor = fileScanner.readPorts("portasServer.txt");
		instrucoes = fileScanner.readInstructions();

		thisPort = Integer.valueOf(portasClient.get(random));
		serverPort = Integer.valueOf(portasServidor.get(random));

		try {
			System.setProperty("java.rmi.server.hostname", "localhost");
			LocateRegistry.createRegistry(thisPort);
			System.out.println("java RMI registry created at port: "+ thisPort);
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		try {
			String client = "rmi://" + "localhost" + ":" + thisPort + "/client";
			Naming.rebind(client, new Client());
			System.out.println("Client is ready.");
		} catch (Exception e) {
			System.out.println("Client failed: " + e);
		}
		remoteHostName = "localhost";
		runInstructions();
	}

	private static ServerInterface connect() {
		try {
			System.out.println("Connecting to server at : " + "rmi://" + remoteHostName + ":" + serverPort + "/server");
			return (ServerInterface) Naming.lookup("rmi://" + remoteHostName + ":" + serverPort + "/server");
		} catch (Exception e) {
			System.out.println("Client failed: ");
			e.printStackTrace();
		}
		return null;
	}

	private static void runInstructions() {
		serverInterface = connect();
		int i = 0;
		try {
			for (String s : instrucoes) {
				if (s.contains("ler")) {
					ler();
					i++;
				} else if (s.contains("inserir")) {
					int index = s.indexOf("(") + 1;
					String texto = s.substring(index, s.length() - 1) + "\n";

					inserir(texto);
					i++;
				} else if (s.contains("deletar")) {
					int index = s.indexOf("(") + 1;
					String texto = s.substring(index, s.length() - 1) + "\n";

					deletar(texto);
					i++;
				} else if (i == instrucoes.size()) {
					serverInterface.endOfFile();
				} else
					throw new InstructionException("INSTRUÇÃO INVÁLIDA");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstructionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<String> ler() throws RemoteException {
		List<String> content = serverInterface.ler();
		System.out.println("Resultado Leitura");
		for(String s : content) {
			System.out.println(s);
		}
		return serverInterface.ler();
	}

	public static String inserir(String texto) throws RemoteException {
		System.out.println("Texto Inserido:" + texto);
		return serverInterface.inserir(texto);
	}

	public static int deletar(String texto) throws RemoteException {
		System.out.println("Texto removido:" + texto);
		return serverInterface.deletar(texto);
	}

	@Override
	public void encerra() throws RemoteException {
		System.out.println("Servidor lhe desconectou");
		System.exit(0);
	}

	@Override
	public void timeout() throws RemoteException {
		System.out.println("Servidor lhe desconectou por timeout");
		System.exit(0);
	}

}