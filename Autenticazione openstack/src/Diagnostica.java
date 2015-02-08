import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.model.compute.VNCConsole.Type;


public class Diagnostica 
{	
	ConsoleReader console = new ConsoleReader();
	
	public Diagnostica() 
	{

	}
	
	public void Analizza(OSClient os) throws IOException
	{
		List<? extends Server> servers1 = os.compute().servers().list(false);
		System.out.println();
		System.out.println("\nI Server sono i seguenti: \n\n" + servers1.toString());
		System.out.println();
		System.out.println("\nScegli il server per la diagnostica: \n");
		System.out.println();
		
		String idServer = console.readLine();
		
		String consoleOutput = os.compute().servers().getConsoleOutput(idServer, 50);
		System.out.println("\nL'output della console è il seguente: \n\n" + consoleOutput);
	
		VNCConsole console = os.compute().servers().getVNCConsole(idServer, Type.NOVNC);
		System.out.println("\nL'output della console è il seguente: \n\n" + console.toString());
	
		Map<String, ? extends Number> diagnostics = os.compute().servers().diagnostics(idServer);
		System.out.println("\nL'output della diagnostica è la seguente: \n\n" + diagnostics.toString());
	}
}
