import java.io.IOException;
import org.openstack4j.api.*;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.*;

//TEMPO DI CREAZIONE DI UNA MACCHINA VIRTUALE
public class PrimoCasoTest 
{
	//CREA SERVER
	public static void CreazioneServer(OSClient os) throws NumberFormatException, IOException, InterruptedException 
	{
		System.out.print("\nnome del server: MioServer\n" );
		
		ServerCreate sc = Builders.server()
	            .name("MioServer")
	            .flavor("1")
	            .image("ebbdd60f-160c-40d5-b6a9-d4a47369caf3")
	            .build();

	    Server server = os.compute().servers().boot(sc);

	    String id = server.getId();
	    System.out.println("\nIl server è stato creato con id = " + id);
	    
	    //CONTROLLA FINO A QUANDO IL SERVER CREATO DIVENTA ATTIVO
		while(true)
		{
			if(os.compute().servers().get(id).getStatus().toString()=="ACTIVE")
				break;
		}

		System.out.println("lo stato del server è: \n" + os.compute().servers().get(id).getStatus().toString());
	}
		
	public static void main(String[] args) throws NumberFormatException, IOException, InterruptedException 
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
		
		//CREAZIONE SERVER
		CreazioneServer(os);
		
		//TEMPO ESECUZIONE
		double endtime = System.currentTimeMillis();
		double tempo = (endtime - startTime)/1000;
		System.out.println("\nTempo Programma: \n" + String.valueOf(tempo));		
	}
}