//import java.util.Timer;
//import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.List;

import org.openstack4j.api.*;
import org.openstack4j.api.exceptions.*;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.openstack.*;

public class ProgettoOpenstack 
{
	
	// ESEGUE QUALCOSA MENTRE IL SERVER VIENE ATTIVATO
	/*public static Server waitUntilServerActive(OSClient os, Server server) throws ClientResponseException, ServerResponseException, InterruptedException 
	{
	    String serverId = server.getId();
	    boolean serverIsReady = false;
	    Server server2 = null;

	    while ( !serverIsReady ) {
	        server2 = os.compute().servers().get(serverId);

	        if ( server2.getStatus().toString().equals("ACTIVE") ) {
	            // The server is now ACTIVE
	            serverIsReady = true;               
	        }

	        // Wait a little bit, to avoid sending too many petitions away continuoulsy
	        TimeUnit.SECONDS.sleep(15);
	    }

	    return server2;
	}*/
	
	public static void main(String[] args) throws ClientResponseException, ServerResponseException, InterruptedException, IOException, NumberFormatException
	{
		boolean ciclo = true;
		ConsoleReader console = new ConsoleReader();
		double startTime = System.currentTimeMillis();
		 
		System.out.println("Il programma ha iniziato");
		
		/*se si desidera utilizzare la nuova versione e non si desidera modificare la installazione con gli host corretto è 
		possibile utilizzare la seguente istruzione prima di creare / autenticare un client*/
		OSFactory.enableLegacyEndpointHandling(true);
		
		//AUTENTICAZIONE
	    System.out.print("\nInserisci Username: ");
		String Username = console.readLine();
		System.out.println();		
	    System.out.print("Inserisci Password: ");
		String Password = console.readLine();
		System.out.println();
		
		System.out.print("Inserisci tenantName: ");
		String tenantName = console.readLine();
		System.out.println();
		
		Autenticazione login = new Autenticazione(Username, Password, tenantName);
		OSClient os = OSFactory.builder()
	            .endpoint("http://172.16.216.214:5000/v2.0")
	            .credentials(login.getUsername(), login.getPassword())
	            .tenantName(login.getTenant())
	            .authenticate();		
		
		while(ciclo)
		{   
			
			System.out.println("\nLista comandi: \n" + "\n1 - Creazione Server \n" + "\n2 - Cancellazione di tutti i Server \n" + 
								"\n3 - Cancellazione di un solo Server \n" + "\n4 - Creazione Tenant \n" + "\n5 - Aggiornare dati utente \n" + 
								"\n6 - Crea/Cancella Macchina(Flavor) \n" + "\n7 - Diagnostica \n"+ "\n0 - Esci dal programma \n");
			
			int input = console.readInt();
			
			GestioneServer gs = new GestioneServer();
			GestioneUtenti gu = new GestioneUtenti();
			GestioneFlavors gf = new GestioneFlavors();
			Diagnostica d = new Diagnostica();
			switch (input) 
			{
				case 1: //CREIAMO UN SERVER
					gs.CreazioneServer(os);
				break;
				
				case 2: //CANCELLIAMO TUTTI I SERVER
					gs.CancellaTutti(os);
				break;
				    
				case 3: //CANCELLA UN SOLO SERVER
					gs.CancellaUnServer(os);
				break;
				
				case 4: //CREAZIONE TENANT, UTENTE E ASSEGNAZIONE RUOLO
					gu.CreazioneUtente(os);
				break;
					
				case 5: // AGGIORNARE DATI UTENTE
					gu.AggiornaDatiUtente(os);
			    break;
			    
				case 6: // CREARE/CANCELLARE FLAVOR
					System.out.print("\nVuoi creare/cancellare una macchina? 1 - crea; 2 - cancella: ");
					
					if(console.readInt() == 1)
						gf.CreaFlavor(true, os);
					else
					{
						System.out.print("\nQuale macchina vuoi cancellare? ");
						List<? extends Flavor> flavors = os.compute().flavors().list();
						System.out.print("\nLista macchine: " + flavors.toString());
						System.out.print("\nInserisci l'id della macchina scelta: ");
						String flavorId = console.readLine();
						gf.CancellaFlavor(flavorId, os);
					}
			    break;
			    
				case 7: // DIAGNOSTICA
					d.Analizza(os);
			    break;
			    
				case 0://USCIRE DAL PROGRAMMA
					ciclo = false;
				break;
			}
		}
	
		double endtime = System.currentTimeMillis();
		double tempo = (endtime - startTime)/1000;
		System.out.println("Programma Terminato con successo");
		System.out.println("\nTempo Programma: \n" + String.valueOf(tempo));
		
		System.out.print("Vuoi riavviare il programma? ");
		
		String ricomincia = console.readLine();
		
		switch (ricomincia) 
		{
			case "y":
				main(null);
			break;
				
			case "n":
				System.exit(0);
			break;
		}
	}
}