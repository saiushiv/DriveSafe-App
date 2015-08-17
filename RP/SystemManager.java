import java.util.ArrayList;

// SystemManager is responsible for handling the system calls made by the application level of a node.. 


public class SystemManager 
{	
	public static void nodeProcess()
	{
		TCPSend.nodeProcess();
	}
	public static int getnodenumber()
	{
		
		return TCPSend.getnodenumber();
	}
	public static String getHostName()
	{
			
		return TCPSend.getHostName();
	}	
	
	public static void requestToken(int node, int port)
	{
			
		TCPSend.requestToken(node, port);
	}
	public static void exitCriticalSection()
	{
		TokenHandler.setHolderWhileInCS(false);
		if(!TokenHandler.childQueueIsEmpty())
		{
			TokenHandler.setHasToken(false)							;	
			TCPSend.grantToken(TokenHandler.childQueue.get(0), TreeGeneration.getPort(TokenHandler.childQueue.get(0)));							
			TokenHandler.setParent(TokenHandler.childQueue.get(0));
			TokenHandler.childQueue.remove(0);
		}
	}
	
	public static boolean presentInQueue(int node)
	{
		boolean present= false;
		if(TokenHandler.childQueue.contains(node))
			present=true;
		return present;
	}
	
	public static void setInitiatorParent(int node)
	{
		TokenHandler.setParent(node);
	}
	
	public static ArrayList<Integer> childQueue()
	{
		return TokenHandler.childQueue;
	}
	
	public static void addInChildQueue(int node)
	{
		TokenHandler.childQueue.add(node);
	}
	
	public static void setHasToken(boolean value)
	{
		TokenHandler.setHasToken(value);
	}


	
	public static int getParent() 
	{
		return TokenHandler.getParent();
	}

	public static void setParent(int parent) {
		TokenHandler.setParent(parent);
	}


	public static boolean HasToken() {
		return TokenHandler.HasToken();
	}

	public static boolean SentRequest() {
		return TokenHandler.SentRequest();
	}

	public static void setSentRequest(boolean sentRequest) {
		TokenHandler.setSentRequest(sentRequest);
	}

	public static boolean isHolderWhileInCS() {
		return TokenHandler.isHolderWhileInCS();
	}

	public static void setHolderWhileInCS(boolean holderWhileInCS) {
		TokenHandler.setHolderWhileInCS(holderWhileInCS);
	}
	
}
