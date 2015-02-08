import java.util.List;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.storage.object.SwiftContainer;

public class GestioneFile 
{	
	public GestioneFile() 
	{
		
	}
	//LISTING DEI CONTAINERS
	public void ListaContainer(OSClient os)
	{
		List<? extends SwiftContainer> containers = os.objectStorage().containers().list();
		System.out.println("\nLista dei containers: \n" + containers.toString());
	}
	//QUESTO METODO NON FUNZIONA DA IL PROBLEMA CHE NON TROVA UNA DETERMINATA ISTANZA
	//CREAZIONE DI UN CONTAINER
	public void CreaContainer(OSClient os) throws InterruptedException
	{
		System.out.println("\nContainer creato: \n" + os.objectStorage().containers().create("Pippo"));
		Thread.sleep(10000);
	}
}