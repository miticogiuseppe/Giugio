import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;

public class GestioneServer extends ComputeNova
{
	private Server server;
	ConsoleReader console = new ConsoleReader();
	//COSTRUTTORE
	public GestioneServer() 
	{
		
	}
	
	//CREA SERVER
	public void CreazioneServer(OSClient os) throws NumberFormatException, IOException, InterruptedException 
	{
		GestioneFlavors gf = new GestioneFlavors();
		ServerCreate sc = Builders.server()
	            .name("ubuntu")
	            .flavor(gf.CreaFlavor(os).getId())
	            .image("ac55ac3f-afa0-44ea-894a-1f8703449fdd")
	            .build();

	    server = os.compute().servers().boot(sc);

	    String id = server.getId();
	    System.out.println("\nIl server è stato creato con id = " + id);

	    Thread.sleep(15000);
	    
		System.out.println("\nVuoi cancellare il server? \n 1 - si ; 0 - no");
		int cancella = console.readInt();
		if(cancella == 1)
		{
			os.compute().servers().delete(id);
			System.out.println("\nServer Cancellato");
		}
	}
	
	//CANCELLA TUTTI I SERVER
	public void CancellaTutti(OSClient os) throws InterruptedException
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
	    
	    if(os.compute().servers().list(false).size()==0)
	    	System.out.println("\nNon ci sono server");
	    else 
	    	System.out.println("\nServer Cancellati");
	    
	    Thread.sleep(15000);
	}
	
	//CANCELLA UN SOLO SERVER
	public void CancellaUnServer(OSClient os) throws NumberFormatException, IOException, InterruptedException
	{
		if(os.compute().servers().list(false).size()!=0)
		{
			int contatore1 = 0;
			List<? extends Server> listaserver1 = os.compute().servers().list(false);
			String [] idServer1 = new String[listaserver1.size()];	
			
			Pattern p1 = Pattern.compile("id");
		    Matcher m1 = p1.matcher(listaserver1.toString());
		    while(m1.find())
		    { 
		    	idServer1[contatore1] = (listaserver1.toString()).substring(m1.start()+3, m1.start()+39);			        
		    	contatore1++;
		    }
		   
		    for(int i = 0; i<idServer1.length; i++)
		    	System.out.println("\n" + i + " - " + idServer1[i]);
		    
		    System.out.println("\nQuale id vuoi cancellare?");
		    int x = console.readInt();
		    
		    if (x>=0 && x<idServer1.length)
		    {
		    	os.compute().servers().delete(idServer1[x]);
		    	Thread.sleep(15000);
		    	System.out.println("\nIl server con id = " + idServer1[x] + " è stato cancellato");
		    }
		    else
		    	System.out.println("\nDevi inserire uno degli indici a video");
		}
		else
			System.out.println("\nNon ci sono server da cancellare");
	}
	
	//OPERAZIONI SERVER
	public void OperazioniServer(OSClient os) throws NumberFormatException, IOException, InterruptedException
	{	
		int contatore1 = 0;
		List<? extends Server> listaserver1 = os.compute().servers().list(false);
		if(!listaserver1.isEmpty())
		{
			String [] idServer1 = new String[listaserver1.size()];	
			
			Pattern p1 = Pattern.compile("id");
		    Matcher m1 = p1.matcher(listaserver1.toString());
		    while(m1.find())
		    { 
		    	idServer1[contatore1] = (listaserver1.toString()).substring(m1.start()+3, m1.start()+39);			        
		    	contatore1++;
		    }
		   
		    for(int i = 0; i<idServer1.length; i++)
		    	System.out.println("\n" + i + " - " + idServer1[i] + " - " + os.compute().servers().get(idServer1[i]).getStatus().toString());
			
		    System.out.print("\nQuale server scegli? indicare l'id del server su cui eseguire delle operazioni: ");
		    
		    String x = console.readLine();

				System.out.print("\n1 - PAUSE	Metti in pausa il server" 
								+ "\n2 - UNPAUSE	Togli il server dalla pausa"  
								+ "\n3 - STOP	Ferma il server" 
								+ "\n4 - START	Avvia il server"
								+ "\n5 - LOCK	Blocca il server"
								+ "\n6 - UNLOCK	Sblocca il server"
								+ "\n7 - SUSPEND	Sospendi il server"
								+ "\n8 - RESUME	Svegliare il server"
								+ "\n9 - RESCUE	Fai il backup del server"
								+ "\n10 - UNRESCUE	Cancellare il backup del server"
								+ "\n11 - SHELVE	Metti il server nello stack"
								+ "\n12 - SHELVE_OFFLOAD	Rimuove un'istanza nello stack dal nodo di computazione"
								+ "\n13 - UNSHELVE	Rimuovi il server dallo stack"
								+ "\n0 - NESSUNA OPERAZIONE");
				
				System.out.println();
				int operazione = console.readInt();
				
				switch (operazione) 
				{
					case 1:
						os.compute().servers().action(x, Action.PAUSE);
						Thread.sleep(15000);
					break;
					case 2:
						os.compute().servers().action(x, Action.UNPAUSE);
						Thread.sleep(15000);
					break;
					case 3:
						os.compute().servers().action(x, Action.STOP);
						Thread.sleep(15000);
					break;
					case 4:
						os.compute().servers().action(x, Action.START);
						Thread.sleep(15000);
					break;
					case 5:
						os.compute().servers().action(x, Action.LOCK);
						Thread.sleep(15000);
					break;
					case 6:
						os.compute().servers().action(x, Action.UNLOCK);
						Thread.sleep(15000);
					break;
					case 7:
						os.compute().servers().action(x, Action.SUSPEND);
						Thread.sleep(15000);
					break;
					case 8:
						os.compute().servers().action(x, Action.RESUME);
						Thread.sleep(15000);
					break;
					case 9:
						os.compute().servers().action(x, Action.RESCUE);
						Thread.sleep(15000);
					break;
					case 10:
						os.compute().servers().action(x, Action.UNRESCUE);
						Thread.sleep(15000);
					break;
					case 11:
						os.compute().servers().action(x, Action.SHELVE);
						Thread.sleep(15000);
					break;
					case 12:
						os.compute().servers().action(x, Action.SHELVE_OFFLOAD);
						Thread.sleep(15000);
					break;
					case 13:
						os.compute().servers().action(x, Action.UNSHELVE);
						Thread.sleep(15000);
					break;
					case 0:	
					break;
				}
				System.out.println("\nL'operazione: " + operazione + " è stata eseguita, lo stato della macchina è: \n" + os.compute().servers().get(x).getStatus().toString());
			}
		else
		{
			System.out.println("Non ci sono Server");
		}
	}
}