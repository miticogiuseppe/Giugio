import java.io.IOException;
import java.util.List;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.Role;
import org.openstack4j.model.identity.Tenant;
import org.openstack4j.model.identity.User;


public class GestioneUtenti extends IdentityKeystone
{
	ConsoleReader console = new ConsoleReader();
	public GestioneUtenti() 
	{
		
	}
	
	//CREAZIONE TENANT, USER E ASSEGNAZIONE RUOLO ALL'UTENTE
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
	//AGGIORNA DATI UTENTE
	public void AggiornaDatiUtente(OSClient os) throws IOException
	{
		System.out.println(os.identity().users().list().toString());
		System.out.println("Quale utente vuoi aggiornare? (scrivi il nome)");
		User john = os.identity().users().getByName(console.readLine());
		
		System.out.println("\nChe operazioni vuoi fare su: " + john.getName() + "?\n\n1 - Cambiare l'email \n" + "\n2 - Abilita o Disabilita Utente \n" + 
							"\n3 - Cambiare password (solo admin) \n" + "\n4 - Cancellare un ruolo \n" + "\n5 - Cancellare un utente \n" + 
							 "\n0 - Esci dal programma \n");
					
		int scelta = console.readInt();
        //verificare l'immissione di dati scorretti
		switch (scelta) 
		{
			case 1://CAMBIARE EMAIL
				System.out.println("\nInserisci indirizzo Email\n");
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
			case 4://CANCELLARE RUOLO
				Role role = os.identity().roles().getByName("pasticciere");
				if (role == null)
					System.out.println("\nL'utente " + john.getName() +" non ricopre nessun ruolo!" );
				else
				{	
					os.identity().roles().delete(role.getId());
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
