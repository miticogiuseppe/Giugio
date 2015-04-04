import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

 
public class ConfigurationProperties 
{
	  //CERCARE DI RENDERE IL PATH ASSOLUTO INDIPENDENTEMENTE DALLA MACCHINA SU CUI ANDRÀ A GIRARE
      //FILE PROPERTIES
      private static String fileProperties="/home/giupino/gitRep/Autenticazione openstack/src/config.properties"; //PATH DI GIOVANNI"/home/giovannidipasquale/git/Giugio/Autenticazione openstack/src/config.properties"
      public ConfigurationProperties()
      {
        
      }
      
      //RECUPERA LA PROPERTY RICHIESTA
      public String getString(String pKey) 
      {
          String myReturn="";
          Properties props = new Properties();
          try
          {
              props.load(new FileInputStream(fileProperties));
              //LEGGO IL VALORE DI RITORNO
              myReturn = props.getProperty(pKey);
          }
          catch(IOException e) 
          {
              System.out.println("ERRORE NELLA LETTURA DEL FILE: " + fileProperties + "\nERRORE: " + e.getMessage());
          }
          return myReturn ;
      }
      
     //QUESTO METODO PERMETTE DI SCRIVERE UN VALORE NEL FILE CONFIG.PROPERTIES
      public void setProperty(String pKey, String pValue)  
      {
          Properties properties = new Properties();
          try 
          {
              properties.load(new FileInputStream(fileProperties));
              properties.setProperty(pKey, pValue);
          } 
          catch(IOException e) 
          {      
                System.out.println("ERRORE NELLA LETTURA DEL FILE: " + fileProperties + "\nERRORE: " + e.getMessage());
          }
            
        //SALVO IL FILE
        try 
        {
            properties.store(new FileOutputStream("config.properties"), null);
        } 
        catch (IOException e) 
        {
             System.out.println("ERRORE NEL SALVATAGGIO DEL FILE " + fileProperties + "\nERRORE: " + e.getMessage());
        }
    }
}