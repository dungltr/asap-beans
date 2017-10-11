package Algorithms;
import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class App 
{   String SPARK_HOME=readhome("SPARK_HOME");
    String HADOOP_HOME=readhome("HADOOP_HOME");
    String HIVE_HOME=readhome("HIVE_HOME");
    String IRES_HOME=readhome("IRES_HOME");
    String node_pc = readhome("NODE_PC");
    /**
   * We use a logger to print the output. Sl4j is a common library which works with log4j, the
   * logging system used by Apache Spark.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public void main( String[] args ) throws Exception
    {      
    }
    
    public String getComputerName() throws UnknownHostException
    {
    String computername=InetAddress.getLocalHost().getHostName();
    return computername;
    }
    public String readhome(String filename) {
       String sCurrentLine = "nothing";
       try (BufferedReader br = new BufferedReader(new FileReader(System.getenv().get("HOME")+"/"+filename+".txt"))) {
			while ((sCurrentLine = br.readLine()) != null) {
                                return sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();                       
		}
       return sCurrentLine;
    }

}

