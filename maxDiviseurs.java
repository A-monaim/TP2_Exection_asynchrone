package execution_asynchrone;

public class maxDiviseurs {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] MAX = {25000, 50000,100000,150000,200000,  250000};
		int [] maxDiviseurs = {0, 0,0,0,0,  0};
		int [] nombreAvecMax = {0, 0,0,0,0,  0};
		long [] tempsEcoule = {0, 0,0,0,0,  0};
		for(int j = 0; j<6; j++) {
		   System.out.println("Pour "+MAX[j]);
		   int N;              
	       int temp=0;
	       long tDebut = System.nanoTime();
	       
	       for ( N = 2;  N <= MAX[j];  N++ ) {
	       
	           // Calculer le nombre de diviseurs de N.
	    	   for( int i = 1; i<=N; i++) {
	    		   
	    		   if(N%i==0) {
	    			  temp++;  
	    		   }
	    	   }
			   // Mettre à jour maxDiviseurs et nombreAvecMax si nécessaire
	    	   if(temp>maxDiviseurs[j]) {
	    		   maxDiviseurs[j] = temp;
	    		   nombreAvecMax[j] = N;
	    	   }
	    	   
	    	   temp=0;
	       
	       }
	       tempsEcoule[j] = System.nanoTime() - tDebut;
	       
	       //imprimer le résultat
	       
	       System.out.println("Parmi les nombres entiers compris entre 1 et "+ MAX[j]+ ", le nombre maximum de diviseurs est "+ maxDiviseurs[j]+". Le plus petit nombre avec "+ maxDiviseurs[j]+" diviseurs "
	       		+ "est " +nombreAvecMax[j]+"."
	       		+ "Temps total écoulé :  "+tempsEcoule[j]+" secondes.");
	   }

	}

}
