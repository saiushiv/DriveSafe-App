

import java.util.Random;
import java.util.Scanner;

import javax.naming.InitialContext;



public class MutexMain 
{	
	private int initiator=1;
	public int getInitiator() 
	{
		return initiator;
	}

	public void setInitiator(int initiator)
	{
		this.initiator = initiator;
	}
	private static long seed;
	private static Random random;
	private static int countRequests=0;
	static 
	{
        // this is how the seed was set in Java 1.4
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }
	
	public static void main(String[] args) 
	{
		
		MutexMain broad = new MutexMain();
		//Check the topology/nodes and the ports.
		TreeGeneration.checkNodes();
	
		try 
		{	
			
			Thread.sleep(3000);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		// Wait till the TCP listener is up and running.
		//TCPReceive.doWait();
		
		//Process the nodes, number them and if I am node 1, build spanning tree by sending
		//application messages.
		//System.out.println("calling node process");
		SystemManager.nodeProcess();
		
		try 
		{
			
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		
		
		//Print the neighbours of individual node
		TreeGeneration.printNeighbours();
	
		
		//Print the children of individual node
		TreeGeneration.printChildren();
		if(SystemManager.getnodenumber()==new MutexMain().getInitiator())
		{	
				System.out.println("\n\nSpanning tree built\n");
				System.out.println("Initiating token management...\n");
		}
		try 
		{
			
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		
		//If I am initiator start broadcasting.
		
		//broad.doBroadcast();
		broad.manageToken();
				
			
		
	}
	void manageToken()
	{
		if(SystemManager.getnodenumber()==new MutexMain().getInitiator())
		{
			//System.out.println(TimeStamp.getTimeStamp()+" : "+"I am node "+SystemManager.getnodenumber()+" and having token");
			
			System.out.println("Total number of critical section requests per node: "+TreeGeneration.noOfRequests);
			System.out.println("Mean delay between two consecutive critical section requests: "+TreeGeneration.delayInRequests);
			System.out.println("Mean duration of critical section: "+TreeGeneration.durationInCS+"\n");
			SystemManager.setInitiatorParent(new MutexMain().getInitiator());
			CSLogger.writeNodeEntry("I am node "+SystemManager.getnodenumber()+" and having token"+" : "+SystemManager.childQueue());
			SystemManager.setHasToken(true);
			try 
			{
				
				Thread.sleep(999);
			} 
			catch (InterruptedException e) 
			{		
				e.printStackTrace();
			}		
		}
		else
		{
			try 
			{
				
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{		
				e.printStackTrace();
			}
		}
		
		new MutexMain().startRaymond();	
	}
	
	void startRaymond()
	{
		
		int node;
		node = randomRequestNode();
	//	while((node!=SystemManager.getnodenumber()) || node== new BroadcastMain().getInitiator())
		while((node!=SystemManager.getnodenumber()))
		{
			//if(node== new BroadcastMain().getInitiator())
				//node = randomRequestNode();
			node = randomRequestNode();
		}
		//System.out.println(TimeStamp.getTimeStamp()+" : "+"I am node :"+SystemManager.getnodenumber()+" and my random number is "+node);
		CSLogger.writeNodeEntry("I am node :"+SystemManager.getnodenumber()+" and my random number is "+node+" : "+SystemManager.childQueue());
		if(countRequests<TreeGeneration.noOfRequests)
		{
			cs_enter(node);
			countRequests++;
		}
		try 
		{
			
			Thread.sleep(10);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
			
		
		
		
		/*else if(TCPSend.getnodenumber()==4)
		{
			TokenHandler.sentRequest = true;
			SystemManager.addInChildQueue(TCPSend.getnodenumber());
			System.out.println("I am node "+TCPSend.getnodenumber()+" and sending token req. to my parent node "+TokenHandler.parent);
			TCPSend.requestToken(TokenHandler.parent, TreeGeneration.getPort(TokenHandler.parent));
		}*/
		
	}
	
	static int randomRequestNode()
	{
		int noOfNodes = TreeGeneration.hostname.size();
		//System.out.println(TreeGeneration.hostname.size());
		Random r = new Random();		
		int randomNode = r.nextInt(noOfNodes) + 1;
		if(randomNode <= noOfNodes){
		return randomNode;
		}
		else{
			return randomRequestNode();
		}
	}
	/**
     * Returns a real number from an exponential distribution with rate lambda.
     * @throws IllegalArgumentException unless <tt>lambda > 0.0</tt>
     */
	
    public static double exp(double lambda) {
        if (!(lambda > 0.0))
            throw new IllegalArgumentException("Rate lambda must be positive");
        return -Math.log(1 - uniform()) / lambda;
    }
	
	/**
     * Return real number uniformly in [0, 1).
     */
    public static double uniform() 
	{
        return random.nextDouble();
    }
	void cs_enter(int node)
	{
		
	//	while(node== new BroadcastMain().getInitiator())
		//	node = randomRequestNode();
		//System.out.println(TimeStamp.getTimeStamp()+" : "+"I am node :"+SystemManager.getnodenumber()+" and I am in my cs_enter");
		CSLogger.writeNodeEntry("I am node :"+SystemManager.getnodenumber()+" and requesting for CS"+" : "+SystemManager.childQueue());
		if(SystemManager.getnodenumber()==node)
		{
			
			SystemManager.addInChildQueue(SystemManager.getnodenumber());
			if(!SystemManager.SentRequest())
			{
				SystemManager.setSentRequest(true);
				//System.out.println(TimeStamp.getTimeStamp()+" : "+"I am node "+SystemManager.getnodenumber()+" and sending token req. to my parent node "+TokenHandler.parent);
				CSLogger.writeNodeEntry("I am node "+SystemManager.getnodenumber()+" and sending token req. to my parent node "+SystemManager.getParent()+" : "+SystemManager.childQueue());
				SystemManager.requestToken(SystemManager.getParent(), TreeGeneration.getPort(SystemManager.getParent()));
			}
		}
	}
	public static void executeCS()
	{
		
		CSLogger.writeCSEntry("I am node: "+TCPSend.getnodenumber()+" and Entering in my Critical Section.");
		//CSLogger.writeCSEntry("I am node: "+TCPSend.getnodenumber()+" and Executing my Critical Section.");
		CSLogger.writeNodeEntry("I am node: "+TCPSend.getnodenumber()+" and Entering in my Critical Section."+" : "+SystemManager.childQueue());
		//System.out.println(TimeStamp.getTimeStamp()+" : "+"I am node: "+TCPSend.getnodenumber()+" and Executing my Critical Section.");
		
		
		if(countRequests%(TreeGeneration.noOfRequests/10)==0)
		{
			System.out.println("Node :"+SystemManager.getnodenumber()+" CS executed: "+countRequests+" times");
		}
		try 
		{
			// Mean duration of critical section 10 then lamda = 1/10
			Thread.sleep(Math.round(exp(1.0/TreeGeneration.durationInCS)));
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		cs_exit();
	}
		public static void cs_exit()
		{		
			CSLogger.writeCSEntry("I am node: "+TCPSend.getnodenumber()+" and Exiting my Critical Section.\n");
			CSLogger.writeNodeEntry("I am node: "+TCPSend.getnodenumber()+" and Exiting my Critical Section."+" : "+SystemManager.childQueue()+"\n");		
			SystemManager.exitCriticalSection();
			try 
			{
				//Mean delay between two consecutive critical section requests then lamda = 1/50
				Thread.sleep(Math.round(exp(1/TreeGeneration.delayInRequests)));

			} 
			catch (InterruptedException e) 
			{		
				e.printStackTrace();
			}
			CSLogger.writeNodeEntry(TCPSend.getnodenumber()+" : CS Exit done. "+" : "+SystemManager.childQueue()+"\n");
			new MutexMain().startRaymond();
			
		}
	
	
	public void doBroadcast()
	{
		
		if(TCPSend.getnodenumber()==new MutexMain().getInitiator())
		{	
				//System.out.println("Broadcast initiated\n");
				
				TreeGeneration.sendBroadcast();			
		}	
		
	}
	
	
}
