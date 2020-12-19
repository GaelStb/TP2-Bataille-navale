package Client;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class MainClient {
	public static void main(String[] args) {
		try {
			Socket client1 = new Socket("127.0.0.1", 1500);
			PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);

			new ListeningThread(client1).start();
			System.out.println("Connexion réussie!");
			Scanner sc=new Scanner(System.in);
			String message="";
			while (message!="quit") {
			message=sc.nextLine();
			out1.println(message);

			}
			sc.close();
			client1.close();

			} catch(Exception e) {
			e.getMessage();
			}

	}
}