import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Time;

public class CSLogger 
{
	private static String unique= TimeStamp.giveUniqueName();
	
	public static void writeCSEntry(String event)
	{
		PrintWriter writer;
		String path = "/home/004/b/bx/bxs123330/AOS/";		
		try 
		{
			writer = new PrintWriter(new FileOutputStream(new File(path+"Critical_Section/CS_Info.txt"),true));			
			writer.append(TimeStamp.getTimeStamp()+" : "+event);			
			writer.println();			
			writer.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 		
		
	}
	
	public static void writeNodeEntry(String event)
	{
		PrintWriter writer;
		String path = "/home/004/b/bx/bxs123330/AOS/";
		int node = SystemManager.getnodenumber();	
		try 
		{
			writer = new PrintWriter(new FileOutputStream(new File(path+"Critical_Section/Info_logs/node_"+node+"_info_"+unique+".txt"),true));			
			writer.append(TimeStamp.getTimeStamp()+" : "+event);			
			writer.println();			
			writer.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 		
		
	}
	public static void writeTreeEntry(String event)
	{
		PrintWriter writer;
		String path = "/home/004/b/bx/bxs123330/AOS/";
		int node = SystemManager.getnodenumber();	
		try 
		{
			writer = new PrintWriter(new FileOutputStream(new File(path+"Critical_Section/Tree_logs/node_"+node+"_info_"+unique+".txt"),true));			
			writer.append(TimeStamp.getTimeStamp()+" : "+event);			
			writer.println();			
			writer.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 		
		
	}
}
