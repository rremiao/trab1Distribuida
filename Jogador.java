package trab1Distribuida;

public class Jogador extends Thread implements Runnable {
	public String username;
	public Integer userId;
	public String remoteHostName;
	public boolean encerrou;

	public Jogador(String username, Integer userId, String remoteHostName) {
		super();
		this.username = username;
		this.userId = userId;
		this.remoteHostName = remoteHostName;
	}

	public Jogador() {
		super();
	}

	@Override
	public void run() {
		Server.loopingCutucadas(this);
	}
	
}