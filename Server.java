package trab1Distribuida;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Server extends UnicastRemoteObject implements ServerInterface {

	private static List<String> portasServer; //consome arquivo portasServer
	private static List<String> portasClient; //consome arquivo portasClient


	public Server() throws RemoteException {
	}

	public static void main(String[] args) throws RemoteException {
		try {
			System.setProperty("java.rmi.server.hostname", "localhost");
			LocateRegistry.createRegistry(52369); //trocar pela porta do arquivo
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		try {
			String server = "rmi://" + "localhost" + ":52369/server"; //trocar pela porta do arquivo
			Naming.rebind(server, new Server());
			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Serverfailed: " + e);
		}
	}


	@Override
	public int lock() throws RemoteException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'lock'");
	}

	@Override
	public int unlock(int id) throws RemoteException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'unlock'");
	}

	// private static void iniciarPartida() {
	// 	for (Jogador j : jogadores) {

	// 		ClientInterface jogador = lookup(j, "rmi://" + j.remoteHostName + ":52369/client");

	// 		try {
	// 			jogador.ler(); //jogador.inicia();
	// 		} catch (RemoteException e) {
	// 			e.printStackTrace();
	// 		}
	// 	}

	// }

	// @Override
	// public int register(String username) {
	// 	int val = rnd.nextInt();
	// 	try {
	// 		Jogador j = new Jogador(username, val, getClientHost());
	// 		jogadores.add(j);
	// 		System.out.println("Registrado jogador " + jogadores.size() + " " + username + " id: " + val);
	// 		j.start();
	// 		return val;
	// 	} catch (Exception e) {
	// 		System.out.println("Failed to get client IP");
	// 		e.printStackTrace();
	// 	}
	// 	return -1;
	// }


	// @Override
	// public int joga(int id) throws RemoteException {
	// 	Jogador jog = jogadores.stream().filter(elem -> id == elem.userId).collect(Collectors.toList()).get(0);
	// 	// Jogada...
	// 	if (Math.random() < 0.01) {

	// 		ClientInterface clientInterface = lookup(jog, "rmi://" + jog.remoteHostName + ":52369/client");
	// 		System.out.println("Finalizando jogador " + jog.username + " " + jog.remoteHostName + " 1% de chance");
	// 		try {
	// 			jog.encerrou = true;
	// 			System.out.println("Encerrando jogador " + jog.username + "!");
	// 			clientInterface.deletar(); //clientInterface.finaliza();
	// 		} catch (RemoteException e) {
	// 			e.printStackTrace();
	// 		}
	// 		return -1;
	// 	} else {
	// 		return 1;
	// 	}
	// }

	// @Override
	// public int encerra(int id) throws RemoteException {
	// 	Jogador jog = jogadores.stream().filter(elem -> id == elem.userId).collect(Collectors.toList()).get(0);
	// 	jog.encerrou = true;
	// 	System.out.println("Jogador " + jog.username + " Terminou suas jogadas!");

	// 	if (validaTodosJogadoresEncerrados()) { // todos encerraram. reinicia o servidor
	// 		System.out.println("Todos os jogadores terminaram suas jogadas!");
	// 		System.out.println("Fim do jogo!");
	// 	}

	// 	return 0;
	// }

	// private boolean validaTodosJogadoresEncerrados() {
	// 	return jogadores.stream().filter(elem -> false == elem.encerrou).collect(Collectors.toList()).isEmpty();
	// }

	private static ClientInterface lookup(String connectLocation) {
		ClientInterface clientInterface = null;
		try {
			System.out.println("Respondendo Callback client em : " + connectLocation + " " );
			clientInterface = (ClientInterface) Naming.lookup(connectLocation);
		} catch (Exception e) {
			System.out.println("Callback failed: ");
			e.printStackTrace();
		}
		return clientInterface;
	}
}