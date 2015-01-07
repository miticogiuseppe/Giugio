import java.io.IOException;
import java.util.List;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Flavor;

public class GestioneFlavors 
{
	ConsoleReader console = new ConsoleReader();
	public GestioneFlavors() 
	{

	}
	//CREA MACCHINA(FLAVOR): PER IL SERVER
	public Flavor CreaFlavor(OSClient os) throws NumberFormatException, IOException
	{
		System.out.print("\nVuoi usare una macchina predefinita oppure vuoi crearne una nuova? 1 - Crea Macchina; 2 - Usa Macchina ");
		int scelta = 0;
		scelta = console.readInt();
		if(scelta == 1)
		{
			System.out.print("\nChe nome vuoi assegnare alla macchina? ");
			String n = console.readLine();
			
			System.out.print("\nQuante ram vuoi assegnare alla macchina? ");
			int r = console.readInt();
			
			System.out.print("\nQuante vcpu vuoi assegnare alla macchina? ");
			int vc = console.readInt();
			
			System.out.print("\nQuanto spazio (GB) vuoi assegnare alla macchina? ");
			int d = console.readInt();
			
			Flavor flavor = Builders.flavor()
			                        .name(n)
			                        .ram(r)
			                        .vcpus(vc)
			                        .disk(d)
			                        .build();
		
			flavor = os.compute().flavors().create(flavor);
			System.out.print("\nLa macchina (flavor) è stata creata ");
			System.out.println("\nCaratteristiche Macchina: " + flavor.toString());
			return flavor;
		}
		else
		{
			System.out.print("\nQuale macchina vuoi scegliere? ");
			List<? extends Flavor> flavors = os.compute().flavors().list();
			System.out.print("\nLista macchine: " + flavors.toString());
			System.out.print("\nInserisci l'id della macchina scelta: ");
			String id = console.readLine();
			Flavor flavor = os.compute().flavors().get(id);
			System.out.print("\nCaratteristiche Macchina: " + flavor.toString());
			return flavor;
		}
	}
	//CREA MACCHINA(FLAVOR): INDIPENDENTE DAL SERVER
	public Flavor CreaFlavor (boolean x, OSClient os) throws IOException
	{
		System.out.print("\nChe nome vuoi assegnare alla macchina? ");
		String n = console.readLine();
		
		System.out.print("\nQuante ram vuoi assegnare alla macchina? ");
		int r = console.readInt();
		
		System.out.print("\nQuante vcpu vuoi assegnare alla macchina? ");
		int vc = console.readInt();
		
		System.out.print("\nQuanto spazio (GB) vuoi assegnare alla macchina? ");
		int d = console.readInt();
		
		Flavor flavor = Builders.flavor()
		                        .name(n)
		                        .ram(r)
		                        .vcpus(vc)
		                        .disk(d)
		                        .build();
	
		flavor = os.compute().flavors().create(flavor);
		
		System.out.print("\nLa macchina (flavor) è stata creata ");
		
		System.out.println("\nCaratteristiche Macchina: " + flavor.toString());
		
		return flavor;
	}
	
	//CANCELLA FLAVOR
	public void CancellaFlavor(String flavorId, OSClient os)
	{
		if(os.compute().flavors().get(flavorId)!=null)
		{
			os.compute().flavors().delete(flavorId);
			System.out.println("\nFlavor cancellato ");
		}
		else
			System.out.println("\nFlavor non presente ");
	}
}