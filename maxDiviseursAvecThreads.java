package execution_asynchrone;

import java.util.Scanner;

public class maxDiviseursAvecThreads {
	
	private final static int MAX = 25000;
	   
	   private volatile static int maxDiviseurs = 0;
	   
	   private volatile static int nombreAvecMax;
	   
	   private static void combinerResultat(int maxDiviseurParThread, int nombreAvecMaxParThread) {
	      // Combiner les résultats de différents threads
		   if(maxDiviseurParThread>maxDiviseurs) {
    		   maxDiviseurs = maxDiviseurParThread;
    		   nombreAvecMax = nombreAvecMaxParThread;
    	   }
	   }
	   
	   private static class diviseurCompteurThread extends Thread {
	      int min, max, th;
	      private volatile static int maxDiviseursTh = 0;
	      private volatile static int nombreAvecMaxTh;
	      public diviseurCompteurThread(int min, int max, int th) {
			super();
			this.min = min;
			this.max = max;
			this.th = th;
		}


	      public void run() {
	       
	        // Calculer le nombre maximum de diviseurs pour les entiers entre min et max : maxDiviseursTh,nombreAvecMaxTh
	    	  for(int j = min; j<=max; j++) {
	    		  int temp = diviseurCompteur(j);
		    	  if(temp>maxDiviseursTh) {
		    		   maxDiviseursTh = temp;
		    		   nombreAvecMaxTh = j;
		    	   }
	    	  }
	    	  
	         combinerResultat(maxDiviseursTh,nombreAvecMaxTh);
	      }
	   }
	   
	   private static void diviseurCompteurAvecThread(int nombreDeThread) {
	      long tDebut = System.nanoTime();
	      diviseurCompteurThread[] th = new diviseurCompteurThread[nombreDeThread];
	      int div = MAX/nombreDeThread;
	      for (int i = 0; i < nombreDeThread; i++) {
	         // départager les entiers entre 1 et MAX entre les nombreDeThread Threads
	    	  th[i] = new diviseurCompteurThread(div*(i),div*(i+1),i);
	    	  
	      }
	       
	      // Lancer les threads
	      for (int i = 0; i < nombreDeThread; i++) {
		    	  th[i].start();
		    	// Attendez que chaque thread soit mort 
		    	  try {
					th[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
	     
	      long tempsEcoule = System.nanoTime() - tDebut;

	      // Imprimer le résultat final   
	      System.out.println("Parmi les nombres entiers compris entre 1 et "+ MAX+ ", le nombre maximum de diviseurs est "+ maxDiviseurs+". Le plus petit nombre avec "+ maxDiviseurs+" diviseurs "
		       		+ "est " +nombreAvecMax+"."
		       		+ "Temps total écoulé :  "+tempsEcoule+" secondes.");
	   }
	   
	   public static void main(String[] args) {
		   for(int k =0 ; k<5; k++) {
			   Scanner clavier = new Scanner(System.in);
			      // demander à l'utilisateur un nombre de threads entre 1 et 100
			      System.out.println("Entrez le nombre de threads entre 1 et 100 :");
			      int nombreDeThread = clavier.nextInt();
			      diviseurCompteurAvecThread(nombreDeThread);
		   }
	      
	   }
	   
	   public static int diviseurCompteur(int N) {
	      // Calculer le nombre de diviseurs pour un entier donnée N
		   int temp = 0;
		      // Calculer le nombre de diviseurs pour un entier donnée N
			   for( int i = 1; i<=N; i++) {
				   
				   if(N%i==0) {
					  temp++;  
				   }
			   }
			   return temp;
	   }
}
