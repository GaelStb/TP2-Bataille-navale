package Serveur;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServeur {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket ecoute = new ServerSocket(1500);
			System.out.println("Serveur lancé!");
			//int id=0;	
			while(true) {
			Socket client1 = ecoute.accept();
			Socket client2 = ecoute.accept();
			new ThreadChat(client1,client2).start();
			//id++;
			}
			} catch(Exception e) {
			// Traitement d'erreur
			}


	}

}
