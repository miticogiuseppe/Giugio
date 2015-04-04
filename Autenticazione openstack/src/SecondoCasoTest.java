import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.openstack.OSFactory;

public class SecondoCasoTest 
{
	//CANCELLA TUTTI I SERVER
	public static void CancellaTutti(OSClient os) throws InterruptedException
	{
		int contatore=0;
		List<? extends Server> listaserver = os.compute().servers().list(false);
		String [] idServer = new String[listaserver.size()];	
		
		Pattern p = Pattern.compile("id"); // the expression
	    Matcher m = p.matcher(listaserver.toString()); // the source
	    while(m.find())
	    {
	        System.out.println( (listaserver.toString()).substring(m.start()+3, m.start()+39));
	        idServer[contatore] = (listaserver.toString()).substring(m.start()+3, m.start()+39);			        
	        os.compute().servers().delete(idServer[contatore]);
	        contatore++;
	    }
	    
	    boolean t=true;
	    //CONTROLLA FINO A QUANDO NON CI SONO PIÙ SERVER
  		while(t)
  		{
  		    if(os.compute().servers().list(false).size()==0)
  		    {
  		    	System.out.println("\nServer cancellati");
  		    	t=false;
  		    }
  		}
	}
		
	public static void main(String[] args) throws InterruptedException 
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
		CancellaTutti(os);
		
		//TEMPO ESECUZIONE
		double endtime = System.currentTimeMillis();
		double tempo = (endtime - startTime)/1000;
		System.out.println("\nTempo Programma: \n" + String.valueOf(tempo));
	}
}