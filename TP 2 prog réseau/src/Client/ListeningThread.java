package Client;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;


public class ListeningThread extends Thread{
	BufferedReader in1;

	
	public ListeningThread(Socket client1) throws IOException {
		in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));

	}
	
	public void run(){
		try {
		while (true) {
			System.out.println(in1.readLine());
		}
		}catch (IOException e) {};
	}
}
