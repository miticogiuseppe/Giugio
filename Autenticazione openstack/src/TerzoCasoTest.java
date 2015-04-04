import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.DLPayload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.storage.object.options.CreateUpdateContainerOptions;
import org.openstack4j.model.storage.object.options.ObjectPutOptions;
import org.openstack4j.openstack.OSFactory;

public class TerzoCasoTest 
{
	//CREAZIONE DI UN CONTAINER
	public static void CreaContainer(OSClient os)
	{
		os.objectStorage().containers().create("v1/AUTH_0613b45860a545608c638f40b6bf7938/Rossi",
													CreateUpdateContainerOptions.create()
													.accessAnybodyRead());
    }
	
	//LISTING CONTAINER
	/*public static void ListaContainer(OSClient os)
	{
		List<? extends SwiftContainer> containers = os.objectStorage()
                .containers()
                .list();
		System.out.println("\nLista dei containers: \n" + containers.toString());
	}*/
	
	//CANCELLAZIONE DI UN CONTAINER
	public static void CancellaContainer(OSClient os)
	{
		String nomecontainer = "v1/AUTH_0613b45860a545608c638f40b6bf7938/Rossi";
		os.objectStorage().containers().delete(nomecontainer);
    }
	
	//CREAZIONE DELL'OGGETTO
	public static void CreaOggetto(OSClient os)
	{
		String path = "/home/giupino/gitRep/Autenticazione openstack/src/CiaoMondo.java";
		File f = new File(path);
		
		os.objectStorage().objects().put("v1/AUTH_0613b45860a545608c638f40b6bf7938/Rossi", "ProvaOggetto", 
                							Payloads.create(f), 
                							ObjectPutOptions.create());
	}
	
	//CANCELLAZIONE DELL'OGGETTO
	public static void CancellaOggetto(OSClient os)
	{
		os.objectStorage().objects().delete("v1/AUTH_0613b45860a545608c638f40b6bf7938/Rossi", "ProvaOggetto");
	}
	
	//DOWNLOAD E LETTURA DEL FILE
	public static void downloadFile(OSClient os) throws IOException
	{
		DLPayload scaricato = os.objectStorage().objects().download("v1/AUTH_0613b45860a545608c638f40b6bf7938/Rossi", "ProvaOggetto");
		File file = new File("prova.txt");
		scaricato.writeToFile(file);
		FileReader leggi =new FileReader(file);
		BufferedReader b = new BufferedReader(leggi);
		String s;
		while(true) 
		{
			s = b.readLine();
		    if(s==null)
		    	break;
		    System.out.println(s);
		}
		leggi.close();
	}
	
	public static void main(String[] args) throws IOException 
	{
		//FILE CONFIGURAZIONE
		ConfigurationProperties props = new ConfigurationProperties();
		
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
		
		double startTime = System.currentTimeMillis();
		
		//CREAZIONE CONTAINER
		CreaContainer(os);
		
		double endtime = System.currentTimeMillis();
		double tempo = (endtime - startTime)/1000;
		System.out.println("\nTEMPO CREAZIONE CONTAINER: \n" + String.valueOf(tempo));
		
		//CREAZIONE DELL'OGGETTO
		CreaOggetto(os);
		
		double endtime1 = System.currentTimeMillis();
		double tempo1 = (endtime1 - startTime)/1000;
		System.out.println("\nTEMPO CREAZIONE OGGETTO: \n" + String.valueOf(tempo1)+ "\n") ;
		
		//DOWNLOAD OGGETTO
		downloadFile(os);
		
		double endtime2 = System.currentTimeMillis();
		double tempo2 = (endtime2 - startTime)/1000;
		System.out.println("\nTEMPO DOWNLOAD FILE: \n" + String.valueOf(tempo2));
		
		//CANCELLAZIONE DELL'OGGETTO
		CancellaOggetto(os);

		double endtime3 = System.currentTimeMillis();
		double tempo3 = (endtime3 - startTime)/1000;
		System.out.println("\nTEMPO CANCELLAZIONE OGGETTO: \n" + String.valueOf(tempo3));
		
		//CANCELLAZIONE DI UN CONTAINER
		CancellaContainer(os);
		
		double endtime4 = System.currentTimeMillis();
		double tempo4 = (endtime4 - startTime)/1000;
		System.out.println("\nTEMPO CANCELLAZIONE CONTAINER: \n" + String.valueOf(tempo4));
		
		//TEMPO ESECUZIONE
		double endtime5 = System.currentTimeMillis();
		double tempo5 = (endtime5 - startTime)/1000;
		System.out.println("\nTEMPO PROGRAMMA: \n" + String.valueOf(tempo5));
	}
}