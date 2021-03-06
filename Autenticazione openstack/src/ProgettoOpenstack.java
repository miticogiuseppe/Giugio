import java.io.IOException;
import java.util.List;
import org.openstack4j.api.*;
import org.openstack4j.api.exceptions.*;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.openstack.*;

public class ProgettoOpenstack 
{	
	public static void main(String[] args) throws ClientResponseException, ServerResponseException, InterruptedException, IOException, NumberFormatException
	{
		//FILE CONFIGURAZIONE
		ConfigurationProperties props = new ConfigurationProperties();
		
		boolean ciclo = true;
		ConsoleReader console = new ConsoleReader();
		double startTime = System.currentTimeMillis();
		
		/*se si desidera utilizzare la nuova versione e non si desidera modificare la installazione con gli host corretto è 
		possibile utilizzare la seguente istruzione prima di creare / autenticare un client*/
		OSFactory.enableLegacyEndpointHandling(true);
		
		//AUTENTICAZIONE
		String Username = props.getString("Username");
		String Password = props.getString("Password");
		String tenantName = props.getString("tenantName");
		
		Autenticazione login = new Autenticazione(Username, Password, tenantName);
		OSClient os = OSFactory.builder()
				.endpoint(login.authURL("/v2.0"))
	            .credentials(login.getUsername(), login.getPassword())
	            .tenantName(login.getTenant())
	            .authenticate();		
		
		while(ciclo)
		{   
			
			System.out.println("\nLista comandi: \n" + "\n1 - Creazione Server \n" + "\n2 - Cancellazione di tutti i Server \n" + 
								"\n3 - Cancellazione di un solo Server \n" + "\n4 - Creazione Tenant \n" + 
								"\n5 - Aggiornare dati utente \n" + "\n6 - Crea/Cancella Macchina(Flavor) \n" + 
								"\n7 - Diagnostica \n"+ "\n8 - Operazioni Server\n" + "\n9 - Storage\n" + 
								"\n10 - GestioneFile\n" + "\n0 - Esci dal programma \n");
			
			int input = console.readInt();
			
			GestioneServer gs = new GestioneServer();
			GestioneUtenti gu = new GestioneUtenti();
			GestioneFlavors gf = new GestioneFlavors();
			GestioneStorage st = new GestioneStorage();
			GestioneFile fl = new GestioneFile();
			Diagnostica d = new Diagnostica();
			switch (input) 
			{
				case 1://CREIAMO UN SERVER
					gs.CreazioneServer(os);
				break;
				
				case 2://CANCELLIAMO TUTTI I SERVER
					gs.CancellaTutti(os);
				break;
				    
				case 3://CANCELLA UN SOLO SERVER
					gs.CancellaUnServer(os);
				break;
				
				case 4://CREAZIONE TENANT, UTENTE E ASSEGNAZIONE RUOLO
					gu.CreazioneUtente(os);
				break;
					
				case 5://AGGIORNARE DATI UTENTE
					gu.AggiornaDatiUtente(os);
			    break;
			    
				case 6://CREARE/CANCELLARE FLAVOR
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
			    
				case 7://DIAGNOSTICA
					d.Analizza(os);
			    break;
			    
				case 8://OPERAZIONE SERVER
					gs.OperazioniServer(os);
				break;

				case 9://STORAGE
					System.out.println("\nQuale operazione vuoi eseguire? \n" + "\n1 - Crea Volume \n" + 
										"\n2 - Cancella Volume \n" + "\n3 - Lista dei volumi \n");
					int scelta = console.readInt();
					switch(scelta)
					{
						case 1://CREA VOLUME
							st.CreaVolume(os);
						break;
						
						case 2://CANCELLA VOLUME
							System.out.println("\nQuale volume vuoi cancellare? (inserisci id)\n");
							st.ListingVolume(os);
							String volumeId = console.readLine();
							st.CancellaVolume(os, volumeId);
						break;
						
						case 3://LISTING VOLUME
							st.ListingVolume(os);
						break;
					}
				break;
				
				case 10://GESTIONE FILE
					//LISTING ACCOUNT
					OSFactory.enableHttpLoggingFilter(true);
					//fl.RitornaAccount(os);
					//LISTING CONTAINER
					//fl.ListaContainer(os);
					
					fl.CreaContainer(os);
					//fl.CancellaContainer(os);
					//fl.CancellaOggetto(os);
					//fl.CopiaOggetto(os);
					//fl.downloadFile(os);
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