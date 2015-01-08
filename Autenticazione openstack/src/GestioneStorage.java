import java.io.IOException;
import java.util.List;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.storage.block.Volume;

public class GestioneStorage 
{
	ConsoleReader console = new ConsoleReader();
	public GestioneStorage() 
	{
		
	}
	//LISTING DEI VOLUMI
	public void ListingVolume(OSClient os)
	{		
		List<? extends Volume> listavolumi = os.blockStorage().volumes().list();	
		System.out.println("\nI block storage sono i seguenti: \n");
		System.out.println(listavolumi.toString());
	}
	//CREAZIONE DEI VOLUMI
	public Volume CreaVolume(OSClient os) throws InterruptedException, IOException
	{
		System.out.println("\nChe nome vuoi dare al volume di storage? \n");
		String nomeVolume = console.readLine();
		System.out.println("\nChe descrizione vuoi dare al volume di storage? \n");
		String descrizioneVolume = console.readLine();
		System.out.println("\nQuanta memoria vuoi dare al volume di storage? (in gb)\n");
		int memoriaVolume = console.readInt();
		Volume v = os.blockStorage().volumes()
	             .create(Builders.volume()
	                .name(nomeVolume)
	                .description(descrizioneVolume)
	                .size(memoriaVolume)
	                .build()
	             );
		Thread.sleep(10000);
		System.out.println("\nVolume creato: \n"+ v.toString());
		return v;
	}
	//CANCELLAZIONE DEI VOLUMI
	public void CancellaVolume(OSClient os, String volumeId) throws InterruptedException
	{
		Thread.sleep(10000);
		System.out.println("\nVolume cancellato: \n");
		System.out.println(os.blockStorage().volumes().delete(volumeId));
	}
}