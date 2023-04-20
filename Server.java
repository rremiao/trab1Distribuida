package trab1Distribuida;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.util.stream.Collectors;

public class Server extends UnicastRemoteObject implements JogoInterface {

	private static final long serialVersionUID = 8074725936858325732L;
	private static Random rnd = new Random();
	private static volatile List<Jogador> jogadores = new ArrayList<>();

	public Server() throws RemoteException {
	}

	public static void main(String[] args) throws RemoteException {
		if (args.length != 2) {
			System.out.println("Usage: java Server <server ip> <nr jogadores>");
//			args = new String[2];
//			args[0] = "localhost";
//			args[1] = "1";
			System.exit(1);
		}

		try {
			System.setProperty("java.rmi.server.hostname", args[0]);
			LocateRegistry.createRegistry(52369);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		try {
			String server = "rmi://" + args[0] + ":52369/server";
			Naming.rebind(server, new Server());
			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Serverfailed: " + e);
		}

		aguardandoJogadores(Integer.parseInt(args[1]));
		iniciarPartida();

	}

	private static void aguardandoJogadores(int numJogadores) {
		System.out.println("Aguardando novos jogadores.. necessarios " + numJogadores + " jogadores para iniciar");
		while (jogadores.size() < numJogadores) {
			// aguarda entrar todos os jogadores.
		}
		System.out.println("Numero de Jogadores Alcancados! Iniciando as jogadas");
	}

	private static void iniciarPartida() {
		for (Jogador j : jogadores) {

			JogadorInterface jogador = lookup(j, "rmi://" + j.remoteHostName + ":52369/client");

			try {
				jogador.inicia();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public int register(String username) {
		int val = rnd.nextInt();
		try {
			Jogador j = new Jogador(username, val, getClientHost());
			jogadores.add(j);
			System.out.println("Registrado jogador " + jogadores.size() + " " + username + " id: " + val);
			j.start();
			return val;
		} catch (Exception e) {
			System.out.println("Failed to get client IP");
			e.printStackTrace();
		}
		return -1;
	}

	public static void loopingCutucadas(Jogador jog) {
		while (!jog.encerrou) {
			JogadorInterface jogadorInterface = lookup(jog, "rmi://" + jog.remoteHostName + ":52369/client");
			try {
				Thread.sleep(3000l);
				if (!jog.encerrou) // valor pode ter sido mudado pro alguma thread durante o sleep desta
					jogadorInterface.cutuca();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Encerrado Looping de cutucadas do jogador " + jog.username + " " + jog.remoteHostName);
	}

	@Override
	public int joga(int id) throws RemoteException {
		Jogador jog = jogadores.stream().filter(elem -> id == elem.userId).collect(Collectors.toList()).get(0);
		// Jogada...
		if (Math.random() < 0.01) {

			JogadorInterface jogadorInterface = lookup(jog, "rmi://" + jog.remoteHostName + ":52369/client");
			System.out.println("Finalizando jogador " + jog.username + " " + jog.remoteHostName + " 1% de chance");
			try {
				jog.encerrou = true;
				System.out.println("Encerrando jogador " + jog.username + "!");
				jogadorInterface.finaliza();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public int encerra(int id) throws RemoteException {
		Jogador jog = jogadores.stream().filter(elem -> id == elem.userId).collect(Collectors.toList()).get(0);
		jog.encerrou = true;
		System.out.println("Jogador " + jog.username + " Terminou suas jogadas!");

		if (validaTodosJogadoresEncerrados()) { // todos encerraram. reinicia o servidor
			System.out.println("Todos os jogadores terminaram suas jogadas!");
			System.out.println("Fim do jogo!");
		}

		return 0;
	}

	private boolean validaTodosJogadoresEncerrados() {
		return jogadores.stream().filter(elem -> false == elem.encerrou).collect(Collectors.toList()).isEmpty();
	}

	private static JogadorInterface lookup(Jogador j, String connectLocation) {
		JogadorInterface jogadorInterface = null;
		try {
			System.out.println("Respondendo Callback client em : " + connectLocation + " " + j.username);
			jogadorInterface = (JogadorInterface) Naming.lookup(connectLocation);
		} catch (Exception e) {
			System.out.println("Callback failed: ");
			e.printStackTrace();
		}
		return jogadorInterface;
	}
}