

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class TreeGeneration
{
	public static BufferedReader br = null;
	public static ArrayList<String> hosts = new ArrayList<String>();
	public static ArrayList<String> hostname = new ArrayList<String>(10);
	public static ArrayList<Integer> ports = new ArrayList<Integer>();
	public static ArrayList<String> topology = new ArrayList<String>();
	public static ArrayList<Integer> children = new ArrayList<Integer>();
	public static ArrayList<String> neighbours = new ArrayList<String>();
	public static double delayInRequests = 0.0;
	public static double durationInCS = 0.0;
	public static int noOfRequests = 0;
	
	// Fn to collect the details about the topology and ports.
	public static void checkNodes()
	{
		try {
			 
			String sCurrentLine; 				
			br = new BufferedReader(new FileReader("conf.txt")); 
			while ((sCurrentLine = br.readLine()) != null) 
			{
				if(sCurrentLine.startsWith("#"))
					continue;
				if(sCurrentLine.startsWith("No. of requests"))
				{
					String[] part = sCurrentLine.split(":");
					noOfRequests = Integer.parseInt(part[1].replaceAll("\\s",""));
					continue;
				}		
				if(sCurrentLine.startsWith("Mean Delay in Requests"))
				{
					String[] part = sCurrentLine.split(":");
					delayInRequests = Double.parseDouble(part[1].replaceAll("\\s",""));
					continue;
				}
				if(sCurrentLine.startsWith("Mean Duration in CS"))
				{
					String[] part = sCurrentLine.split(":");
					durationInCS = Double.parseDouble(part[1].replaceAll("\\s",""));
					continue;
				}
				String[] parts = sCurrentLine.split("\t");
				String oneNode = parts[0];	
				int port = Integer.parseInt(parts[1]);
				String topo = parts[2];
				//System.out.println(oneNode);
				//System.out.println(ports);
				hostname.add(oneNode+".utdallas.edu");	
				topology.add(topo);
				ports.add(port);
			}		
			
			for(int i=0;i<hostname.size();i++)				
			{
				//System.out.println(hostname.get(i)+" : "+ports.get(i));			
				String[] parts = topology.get(i).split(" ");
				for(int j=0;j<parts.length;j++)
				{
					int index= Integer.parseInt(parts[j]);
					//System.out.println(hostname.get(index-1)+", listening on: "+ports.get(index-1));
				}
				//System.out.println();
			}
			//Start listening to the port provided.
			//System.out.println(TCPSend.getnodenumber());
			for(int i=0;i<hostname.size();i++)
			{				
				if(hostname.get(i).trim().equals(InetAddress.getLocalHost().getHostName()))
				{
					hosts.add(hostname.get(i));					
					TCPReceive.setListen_port(ports.get(i));
					// Start the listener port to accept any incoming connections.					
					//System.out.println("Starting Tcp listener thread");
					Thread listener = new Thread(new TCPReceive());
					listener.start();
				}
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void generateSpanningTree(int node, int root)
	{
		
			String[] parts = topology.get(node-1).split(" ");
			for(int j=0;j<parts.length;j++)
			{
				int index= Integer.parseInt(parts[j]);
				//System.out.println(index+", "+ports.get(index-1)+", "+root);
				TCPSend.Tcpsend_spanningApplicationMsg(index, ports.get(index-1), root);
				//System.out.println(hostname.get(index-1)+", listening on: "+ports.get(index-1));
			}	
			
	}
	public static void sendBroadcast()
	{	
	
			for(int childNode: children)
			{
				TCPSend.Tcpsend_broadcastMsg(childNode, getPort(childNode));
			}	
			
	}
	public static void printNeighbours()
	{
			
		CSLogger.writeTreeEntry("Printing neighbours");
		for(String neighbour: neighbours)
		{
			CSLogger.writeTreeEntry(neighbour+", ");
			//System.out.print(neighbour+", ");
		}
		
	}
	public static void printChildren()
	{
		
		//System.out.println("Printing neighbours");
		
		//System.out.println();
		
		for(int child: children)
		{
			
			//System.out.print(neighbour+", ");
		}
				
	}
	
	public static int getPort(int node)
	{		
		return ports.get(node-1);
	}
}
