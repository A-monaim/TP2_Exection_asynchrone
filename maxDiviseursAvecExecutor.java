package execution_asynchrone;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class maxDiviseursAvecExecutor {
    private final static int MAX = 25000;
    private volatile static int maxDiviseurs = 0;
	private volatile static int nombreAvecMax;
   
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
		public int getMaxDiviseurParSousTache() {
			return maxDiviseurParSousTache;
		}
		public int getNombreAvecMaxSousTache() {
			return nombreAvecMaxSousTache;
		}
        
    }
    
 
	private static class sousTache implements Callable<Resultat>{
        int min, max; // début et fin de la plage des entiers à traiter
		//La sous-tâche est exécutée lorsque la méthode call() est appelée
        public sousTache(int min, int max) {
			super();
			this.min = min;
			this.max = max;
		}
        public Resultat call() {
            int maxDiviseursTh = 0;
			int nombreAvecMaxTh = 0;
			//
			//
        	for(int j = min; j<=max; j++) {
	    		  int temp = diviseurCompteur(j);
		    	  if(temp>maxDiviseursTh) {
		    		   maxDiviseursTh = temp;
		    		   nombreAvecMaxTh = j;
		    	   }
	    	  }
            return new Resultat(maxDiviseursTh,nombreAvecMaxTh);
        }
		
    } 

        
    private static void diviseurCompteurAvecExecutor(int nombreDeThread) throws InterruptedException, ExecutionException {
   
        // Créer l'ExecutorService et un ArrayList pour stocker les Futures
        
        long tDebut = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(nombreDeThread);
        ArrayList<sousTache> taches = new ArrayList<>();
        ArrayList<Future<Resultat>> resultats = new ArrayList<>();
        int k = 1;
        int nombreDeTaches = 0;
        // Créer les sous-tâches et ajouter-les à l'executor. Chaque sous-tâche traite une plage de 1000 entiers.
        do {
        	nombreDeTaches++;
        	k += 1000;
        	
        }while(k<=MAX);
        k=1;
        for (int i = 0; i < nombreDeTaches; i++) {
            //
            // Soumettre la sous-tâche à l'excutor--> retourne un objet de type Future
            // Ajouter l'objet de type Future dans ArrayList
			//
        	Future<Resultat> future = executor.submit(new sousTache(k,k+999));
        	k += 1000;
        	resultats.add(future);
        }
		
		// Au fur et à mesure que l'excutor exécute les tâches, les résultats deviennent disponibles dans les Futures qui sont stockés dans l'ArrayList. 
		// Obtenir les résultats et combiner-les pour produire le résultat final
        //
        for (Future<Resultat> res : resultats) {
            //
        	if( res.get().maxDiviseurParSousTache >maxDiviseurs) {
      		   maxDiviseurs = res.get().maxDiviseurParSousTache;
      		   nombreAvecMax = res.get().nombreAvecMaxSousTache;
      	   }
        }
        
        
		long tempsEcoule = System.nanoTime() - tDebut;

		// Imprimer le résultat final 
		
		System.out.println("Parmi les nombres entiers compris entre 1 et "+ MAX+ ", le nombre maximum de diviseurs est "+ maxDiviseurs+". Le plus petit nombre avec "+ maxDiviseurs+" diviseurs "
	       		+ "est " +nombreAvecMax+"."
	       		+ "Temps total écoulé :  "+tempsEcoule+" secondes.");
        
        // terminir l'executor.        
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
      Scanner clavier = new Scanner(System.in);
      // demander à l'utilisateur le nombre de threads dans le ThreadPool (entre 1 et 100)
      for(int k = 0; k<3;k++ ) {
    	  System.out.println("Entrez le nombre de threads entre 1 et 100 :");
          int nombreDeThread = clavier.nextInt();
          diviseurCompteurAvecExecutor(nombreDeThread);
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