import java.util.List;
import java.util.Map;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.model.compute.VNCConsole.Type;


public class Diagnostica 
{	
	public Diagnostica() 
	{

	}
	
	public void Analizza(OSClient os)
	{
		String consoleOutput = os.compute().servers().getConsoleOutput("b6ad71df-cdd1-4753-8067-8155d8c111dc", 50);
		System.out.println("\nL'output della console è il seguente: \n" + consoleOutput);
	
		VNCConsole console = os.compute().servers().getVNCConsole("b6ad71df-cdd1-4753-8067-8155d8c111dc", Type.NOVNC);
		System.out.println("\nL'output della console è il seguente: \n" + console.toString());
	
		Map<String, ? extends Number> diagnostics = os.compute().servers().diagnostics("b6ad71df-cdd1-4753-8067-8155d8c111dc");
		System.out.println("\nL'output della diagnostica è la seguente: \n" + diagnostics.toString());
			
    	//List<? extends Server> servers = os.compute().servers().list();
	
		List<? extends Server> servers1 = os.compute().servers().list(false);
		System.out.println("\nI Server sono i seguenti: \n" + servers1.toString());
	}
}
