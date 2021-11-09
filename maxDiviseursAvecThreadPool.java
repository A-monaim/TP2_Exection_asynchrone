package execution_asynchrone;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class maxDiviseursAvecThreadPool {
	private final static int MAX = 25000;
    private static ConcurrentLinkedQueue<sousTache> tacheQueue;
    private static LinkedBlockingQueue<Resultat> resultatQueue;
    private volatile static int maxDiviseurs = 0;
	private volatile static int nombreAvecMax;
    
    private static class sousTache {
        int min, max; // début et fin de la plage des entiers à traiter
        
		public sousTache(int min, int max) {
			super();
			this.min = min;
			this.max = max;
		}

        public void calcul() {
        	int maxDiviseursTh = 0;
   	        int nombreAvecMaxTh = 0;
			// Effectuer le calcul puis ajouter le résultat dans la queue resultatQueue avec:
        	for(int j = min; j<=max; j++) {
	    		  int temp = diviseurCompteur(j);
		    	  if(temp>maxDiviseursTh) {
		    		   maxDiviseursTh = temp;
		    		   nombreAvecMaxTh = j;
		    	   }
	    	  }
            resultatQueue.add( new Resultat(maxDiviseursTh, nombreAvecMaxTh) );
			//
        }
    }
    
    //Une classe pour représenter le résultat d'une sous-tâche.
	private static class Resultat {
        int maxDiviseurParSousTache;  // Nombre maximal de diviseurs.
        int nombreAvecMaxSousTache;  // Quel entier a donné ce nombre maximal.
		//
		//
		public Resultat(int maxDiviseurParSousTache, int nombreAvecMaxSousTache) {
			super();
			this.maxDiviseurParSousTache = maxDiviseurParSousTache;
			this.nombreAvecMaxSousTache = nombreAvecMaxSousTache;
		}
        
    }

    private static class diviseurCompteurThread extends Thread {
        public void run() {
            while (true) {
                // Récupérer la sous-tâche à exécuter avec tacheQueue.poll()
            	sousTache x = tacheQueue.poll();
            	
				// effectuer le calcul sousTache.calcul()
            	x.calcul();
            }
        }
    }

    private static void diviseurCompteurAvecThreadPool(int numberOfThreads) throws InterruptedException {
        
		long tDebut = System.nanoTime();
		int k = 1;
		int numSousTache=0;
		resultatQueue = new LinkedBlockingQueue<Resultat>();
        tacheQueue = new ConcurrentLinkedQueue<sousTache>();
        
		// Créer une liste de threads de type diviseurCompteurThread et de taille nombreDeThread
        
        diviseurCompteurThread[] th = new diviseurCompteurThread[numberOfThreads];
        for(int i = 0; i<numberOfThreads ; i++) {
        	
        	th[i] = new diviseurCompteurThread();    
        }
        
        // Créer un certain nombre des sous-tâches, chaque sous-tâches s'occupe d'un certains nombre d'entiers 1000 par exemple.
		// ajouter les sous-tâches à tacheQueue --> tacheQueue.add(new sousTache(debut,fin)); 
        
        do {
        	
        	tacheQueue.add(new sousTache(k,k+999));
        	numSousTache++;
        	k += 1000;
        	
        }while(k<=MAX);
		
		// Démmarer les nombreDeThread threads --> Les threads exécuteront les tâches et les résultats seront placés dans la Queue des résultats resultatQueue.
        for(int i = 0; i<numberOfThreads ; i++) {
        	
        	th[i].start();
        	
        }
		// Calculer le résultat final --> lire tous les résultats de la Queue des résultats et combinez-les pour donner la réponse finale.
        for (int i = 0; i < numSousTache; i++) {
			//
            Resultat resultat = resultatQueue.take();
			//
            if( resultat.maxDiviseurParSousTache >maxDiviseurs) {
     		   maxDiviseurs = resultat.maxDiviseurParSousTache;
     		   nombreAvecMax = resultat.nombreAvecMaxSousTache;
     	   }
        }
		
		long tempsEcoule = System.nanoTime() - tDebut;

		// Imprimer le résultat final  
		System.out.println("Parmi les nombres entiers compris entre 1 et "+ MAX+ ", le nombre maximum de diviseurs est "+ maxDiviseurs+". Le plus petit nombre avec "+ maxDiviseurs+" diviseurs "
	       		+ "est " +nombreAvecMax+"."
	       		+ "Temps total écoulé :  "+tempsEcoule+" secondes.");
    }

    public static void main(String[] args) throws InterruptedException {
      Scanner clavier = new Scanner(System.in);
      // demander à l'utilisateur le nombre de threads dans le ThreadPool (entre 1 et 100)
      for(int k = 0; k<3;k++ ) {
    	  System.out.println("Entrez le nombre de threads entre 1 et 100 :");
          int nombreDeThread = clavier.nextInt();
          diviseurCompteurAvecThreadPool(nombreDeThread);
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