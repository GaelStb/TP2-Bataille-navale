	package Serveur;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ThreadChat extends Thread {
	BufferedReader in1;
	PrintWriter out1;
	BufferedReader in2;
	PrintWriter out2;


//Méthode qui permet de vérifier si les deux clients sont bien connectés au serveur

public ThreadChat (Socket client1, Socket client2) {
		try {
			in1 = new BufferedReader (new InputStreamReader(client1.getInputStream()));
			out1 = new PrintWriter (client1.getOutputStream(), true);
			in2 = new BufferedReader (new InputStreamReader(client2.getInputStream()));
			out2 = new PrintWriter (client2.getOutputStream(), true);
			
			out1.println("Les deux clients sont connectés, Vous êtes le joueur 1. \n");
			out2.println("Les deux clients sont connectés, Vous êtes le joueur 2. \n");
		}catch(Exception e) {}
}


//Méthode qui cible une case spécifique

public int[] Cible(Scanner scan, PrintWriter out, PrintWriter out_adversaire) { 
	
	out.println("Dans quelle case veux-tu placer ton bateau ?");
	out_adversaire.println("En attente du choix de ton adversaire...");
	
	String write = scan.nextLine();
	
	if (write.length() == 2) {
		
	int x = Character.getNumericValue(write.charAt(0)); 
	int y = Character.getNumericValue(write.charAt(1));
	
	out.println("Tu as placé ton bateau à partir de la case : " + write);
	
	int[] case_cible = {x,y};

	return case_cible;
	
	} else {
		
		out.println("Il y a une erreur dans la saisie, réessaye s'il te plait :");
		return Cible(scan,out,out_adversaire); 
	}
}

//Méthode qui remplie la grille 

public boolean[][] Remplissage (Scanner scan, PrintWriter out, PrintWriter out_adversaire, Boat[] liste){
	
	boolean[][] grille = new boolean[10][10];
	
	for (int a=0; a<liste.length; a++) { 
		
		for (int b=0 ; b<liste[a].Nombre; b++) { 
			
			boolean superposition = false;
			
			out.println("Où veux-tu placer ton bateau de " + liste[a].Type + " de taile " + liste[a].Longueur+"?");
			
			String position = scan.nextLine();
			if(position.length()==3) { 
				
				int x = Character.getNumericValue(position.charAt(0));
				int y = Character.getNumericValue(position.charAt(1));
				char orientation = position.charAt(2);
				
				if(orientation == 'N') { //Nord
					
					for(int i=0; i<liste[a].Longueur; i++) { 
						if(grille[x-i][y] == true) {superposition = true;} 
					}
				}
				
				if(orientation == 'S') { //Sud
					
					for(int i=0; i<liste[a].Longueur; i++) {
						if(grille[x+i][y] == true) {superposition = true;}
					}
				}
				
				if(orientation == 'E') { //Est
					
					for(int i=0; i<liste[a].Longueur; i++) {
						if(grille[x][y+i] == true) {superposition = true;}
					}
				}
				
				if(orientation == 'O') { //Ouest
					
					for(int i=0; i<liste[a].Longueur; i++) {
						if(grille[x][y-i] == true) {superposition = true;}
					}
				}
				
				if (superposition == false) {
					
					if(orientation == 'N') { 
						
						for(int i=0; i<liste[a].Longueur; i++) { 
							grille[x-i][y] = true; 
						}
					}
					
					if(orientation == 'S') {
						
						for(int i=0; i<liste[a].Longueur; i++) {
							grille[x+i][y] = true;
						}
					}
					
					if(orientation == 'E') {
						
						for(int i=0; i<liste[a].Longueur; i++) {
							grille[x][y+i] = true;
						}
					}
					
					if(orientation == 'O') {
						
						for(int i=0; i<liste[a].Longueur; i++) {
							grille[x][y-i] = true;
						}
					}
					
					Affichage(grille, grille,out,1);
					
				}else {
					
					b-=1;
					out.println("Il semblerait qu'un bateau soit déjà positionné ici");
				}
			}else {
				
				b-=1;
				out.println("Le format rentré n'est pas le bon, réessaye s'il te plait :");
			}
		}
	}
	
	return grille;
}


//Méthode qui permet d'afficher la grille

public void Affichage (boolean[][] grille, boolean[][] grille_adversaire, PrintWriter out, int ind) { 
	
	if (ind == 0) {
		out.println("Voici ta grille ainsi que celle de ton adversaire :");
		
	}if(ind == 1) {
		
		out.println("Voici ta grille dans son etat actuelle");
	}
	
	for(int i=0; i<10 ;i++) { 
		
		for(int y=0; y<10; y++) { 
			
			if(grille[i][y]==true) {out.print("  X  ");}
			else {out.print("  -  ");}
		}
		
		out.print("|   |");  
		
		for(int y=0; y<10; y++) {
			
			if(grille_adversaire[i][y]==true) {out.print("  X  ");}
			
			else {out.print("  -  ");}
		}
		
		out.println("");  
	}
}


//Méthode qui liste les différents types de bateau, leur taille et leur nombre

public Boat[] Boatliste (){ 
	
	Boat b1 = new Boat("Porte-avion",5,1);
	Boat b2 = new Boat("Croiseur",4,1);
	Boat b3 = new Boat("Sous-marin",3,1);
	Boat b4 = new Boat("Torpilleur",2,2);
	Boat[] liste = {b1,b2,b3,b4};
	return liste;
}


//Méthode qui calcule la place que prend un bateau 

public int LongueurBateau(Boat[] liste) { 
	
	int Longueur = 0;
	
	for (int i=0; i<liste.length; i++) {
		
		Longueur += liste[i].Nombre * liste[i].Longueur; 
	}
	return Longueur;
}

//Méthode qui verifier si une case est touchée ou non


public int Resultat (boolean[][] grille_adversaire, int compteur, int[] cible, PrintWriter out, boolean[][] grille_cible) { 
	
	if (grille_adversaire[cible[0]][cible[1]]==true) {
		compteur = compteur-1;
		out.println("Well done ! Tu as touché un bateau !");
	}
	
	else {
		out.println("Dommage t'as tiré à côté... ");
	}
	
	grille_adversaire[cible[0]][cible[1]] = false;
	grille_cible[cible[0]][cible[1]] = true;
	return compteur;
}



//Méthode qui annonce le résultat final

public void AfficherGagnant(int compteur1, int compteur2, PrintWriter out1, PrintWriter out2) {
	
	if (compteur1 == compteur2) {
		out1.println("Tous les bateaux ont été coulés dans les deux camps, égalité parfaite ! ");
		out2.println("Tous les bateaux ont été coulés dans les deux camps, égalité parfaite ! ");
		
	} else if (compteur1 < compteur2) {
		out1.println("Tes bateaux ont tous été coulé...une revanche ? ");
		out2.println(" Tu as coulé tous les bateaux de ton adversaire ! Félicitations");
		
	}else {
		out1.println(" Tu as coulé tous les bateaux de ton adversaire ! Félicitations");
		out2.println("Tes bateaux ont tous été coulé...une revanche ? ");
	}
}



//Méthode qui permet de recommencer une partie

public String NouvellePartie(PrintWriter out, Scanner scan) {  
	
	out.println("Veux-tu recommencer une partie ?");
	String write =  scan.nextLine();
	
	return write;
}



//Déroulement de la partie 

public void run() {
	
	out1.println("Pour jouer, c'est très simple ! Il te suffit de placer tes bateaux en indiquant la ligne puis le numéro de colonne puis l'orientation.(Exemple : 54E)\n"
	+"Ensuite, il te suffit de viser une case en indiquant la ligne puis la colonne. (Exemple : 42 "+"C'est parti !") ;
	out2.println("Pour jouer, c'est très simple ! Il te suffit de placer tes bateaux en indiquant la ligne puis le numéro de colonne puis l'orientation.(Exemple : 54E)\n"
	+"Ensuite, il te suffit de viser une case en indiquant la ligne puis la colonne. (Exemple : 42 "+"C'est parti !") ;		
	
	Scanner scan = new Scanner(in1); 
	Scanner scan_bis = new Scanner(in2);
	
	Boat[] liste_bateaux = Boatliste(); 
	
	String Client = "oui"; 
	String Adversaire = "oui";
	
	try {
		while ((Adversaire.equals("oui")) && (Adversaire.equals("oui"))) { //Restart
			
			int compteur_client1 = LongueurBateau(liste_bateaux); //Compteurs initialisés
			int compteur_client2 = LongueurBateau(liste_bateaux);
			
			boolean[][] grille_cible_1 = new boolean[10][10]; //Creation de la grille
			boolean[][] grille_cible_2 = new boolean[10][10];
			
			
			boolean[][] grille_client1 = Remplissage(scan,out1,out2,liste_bateaux); 
			boolean[][] grille_client2 = Remplissage(scan_bis,out2,out1,liste_bateaux);
			
			while ((compteur_client1!=0) && (compteur_client2!=0)){  
				
				Affichage(grille_client1,grille_cible_1,out1,0); 
				Affichage(grille_client2,grille_cible_2,out2,0);
				
				int[] cible_1 = Cible(scan,out1,out2); //Choix de la case
				int[] cible_2 = Cible(scan_bis,out2,out1);
				
				compteur_client1 = Resultat(grille_client1, compteur_client1, cible_2, out2, grille_cible_2); //Calcul du nombre de cases restantes
				compteur_client2 = Resultat(grille_client2, compteur_client2, cible_1, out1, grille_cible_1);
				
				out1.println("\n" + "Il vous reste "+compteur_client1+" case(s) non touchée(s) !"); 
				out2.println("\n" + "Il vous reste "+compteur_client2+" case(s) non touchée(s) !");
			}
			
			AfficherGagnant(compteur_client1, compteur_client2, out1, out2); 
			
			Client = NouvellePartie(out1, scan); 
			Client = NouvellePartie(out1, scan_bis);				
			
		}	
	}catch(Exception e) {e.getMessage();}
}
}