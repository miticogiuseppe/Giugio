import java.io.*;

public class ConsoleReader
{ 
	private BufferedReader reader;
  
	  public ConsoleReader()
	  { 
		  reader=new BufferedReader(new InputStreamReader (System.in));   
	  }
	
	  public String readLine() throws IOException
	  { 
		  String inputLine="";
		  try
		  { 
			  inputLine = reader.readLine();
		  }
		  catch(IOException e)
		  { 
			  System.out.println("\nDevi inserire una stringa alfanumerica");
			  System.out.println("\nProgramma Terminato");
			  System.exit(1);
		  }
		  return inputLine;
	  }  
	  
	  public int readInt() throws NumberFormatException, IOException
	  { 
			int n = 0;
			try
			{
				String inputString = readLine();
				n = Integer.parseInt(inputString);
			}
			catch(NumberFormatException e)
			{
				System.out.println("\nDevi inserire uno degli indici a video");
				System.out.println("\nProgramma Terminato");
				System.exit(1);
			}
			return n;
	  }  
	
	  public double readDouble () throws NumberFormatException, IOException
	  { 
		  double x = 0;
		  try
		  {
			  String inputString = readLine();
			  x = Double.parseDouble(inputString);
			  
		  }
		  catch(NumberFormatException e)
		  {
				System.out.println("\nDevi inserire un valore decimale");
				System.out.println("\nProgramma Terminato");
				System.exit(1);
		  }
		  return x;
	  }  
}