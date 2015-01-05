import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.Role;
import org.openstack4j.model.identity.Tenant;
import org.openstack4j.model.identity.User;

public class GestioneServer 
{
	ConsoleReader console = new ConsoleReader();
	public GestioneServer() 
	{
		
	}

	public void CreazioneServer(OSClient os) throws NumberFormatException, IOException, InterruptedException 
	{
		
		ServerCreate sc = Builders.server()
	            .name("ubuntu")
	            .flavor("42")
	            .image("9b5c8d2e-493d-4893-9402-974374cd9895")
	            .build();

	    Server server = os.compute().servers().boot(sc);

	    String id = server.getId();
	    System.out.println("\nIl server è stato creato con id = " + id);

	    Thread.sleep(5000);

		System.out.println("\nVuoi cancellare il server? \n 1 - si ; 0 - no");
		int cancella = console.readInt();
		if(cancella == 1)
		{
			os.compute().servers().delete(id);
			System.out.println("\nServer Cancellato");
		}
	}
	
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
	    
	    Thread.sleep(5000);
	}
	
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
		    	Thread.sleep(5000);
		    	System.out.println("\nIl server con id = " + idServer1[x] + " è stato cancellato");
		    }
		    else
		    	System.out.println("\nDevi inserire uno degli indici a video");
		}
		else
			System.out.println("\nNon ci sono server da cancellare");
	}
	
	public void CreazioneUtente(OSClient os) throws IOException
	{
		List<? extends Tenant> tenants = os.identity().tenants().list();
		
		System.out.println("\nLista dei Tenant: \n" + tenants.toString());
		
		System.out.println("\nInserisci il nome del Tenant \n");
		
		String tenant = console.readLine(); 
		
		// Create the Tenant
		Tenant Bob = os.identity().tenants().create(Builders.tenant().name(tenant).build());
		
		List<? extends User> users = os.identity().users().list();
						
		System.out.println("\nLista degli Users: \n" + users.toString());
		
		System.out.println("\nImmetti nome utente \n");
		
		String NameUser= console.readLine();
		
		System.out.println("\nImmetti password \n");
		
		String PasswordUser = console.readLine();
		
		System.out.println("\nImmetti email \n");
		
		String emailUser = console.readLine();
		
		// Create a User associated to the name tenant
		User Alice = os.identity().users()
		              .create(Builders.user()
		                                .name(NameUser)
		                                .password(PasswordUser)
		                                .email(emailUser)
		                                .tenant(Bob).build());

		// Associate the Member role to the Alice Doe
		Role memberRole = os.identity().roles().create("pasticciere");
		os.identity().roles().addUserRole(Bob.getId(), Alice.getId(), memberRole.getId());
		
		os.identity().users().enableUser(Alice.getId(), true);
		
		System.out.println("Tenant e membro creati");
		
	}
	
	public void AggiornaDatiUtente(OSClient os) throws IOException
	{
		System.out.println(os.identity().users().list().toString());
		System.out.println("Quale utente vuoi aggiornare? (scrivi il nome)");
		User john = os.identity().users().getByName(console.readLine());
		
		System.out.println("\nChe operazioni vuoi fare su: " + john.getName() + "?\n\n1 - Cambiare l'email \n" + "\n2 - Abilita o Disabilita Utente \n" + 
							"\n3 - Cambiare password (solo admin) \n" + "\n4 - Cancellare un ruolo \n" + "\n5 - Cancellare un utente \n" + 
							 "\n0 - Esci dal programma \n");
					
		int scelta = console.readInt();
        //verificare l'immissione di dati scorretti, modularizzarione e compute nova
		switch (scelta) 
		{
			case 1://CAMBIARE EMAIL
				System.out.println("\nInserisci indirizzo Email\n"); System.out.println("ciao come va?");
				String indirizzo = console.readLine();
				os.identity().users().update(john.toBuilder().email(indirizzo).build());
				System.out.println("\nEmail cambiata\n");
				break;
			case 2://ABILITA/DISABILITA UTENTE
				System.out.println("\ntrue - Abilita; false - Disabilita\n");
				String decisione = console.readLine();
				os.identity().users().enableUser(john.getId(), Boolean.parseBoolean(decisione));
				if(decisione.equals("true"))
					System.out.println("\nUtente Abilitato\n");
				else
					System.out.println("\nUtente Disabilito\n");
				break;
			case 3://CAMBIO PASSWORD
				System.out.println("\nInserisci la nuova password\n");
				String newpassword = console.readLine();
				os.identity().users().changePassword(john.getId(), newpassword);
				System.out.println("\nPassword cambiata\n");
				break;
			case 4://CANCELLARE RUOLO ora funziona, sostituito il metodo get col metodo create sopra (nel tenant)
				Role role = os.identity().roles().getByName("pasticciere");
				if (role == null)
					System.out.println("\nL'utente " + john.getName() +" non ricopre nessun ruolo!" );
				else
				{	
					os.identity().roles().delete(role.getId()); //metodo corretto e debuggato
				    System.out.println("\nIl ruolo di " + john.getName()+" e' stato cancellato con ruolo id: " + role.getId());
				}
				break;
			case 5://CANCELLARE UTENTE
				os.identity().users().delete(john.getId());
				System.out.println("\nL'utente " + john.getName()+" e' stato cancellato \n");
				break;
			case 0://USCITA
				System.exit(0);	
				break;
			default:
				break;
		}
	}
}	