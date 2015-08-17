

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

public class TCPSend {

	//private static final String[] hostname = { "dc01.utdallas.edu",
		//	"dc02.utdallas.edu", "dc03.utdallas.edu", "dc04.utdallas.edu",
			//"dc05.utdallas.edu" };
	
	
	
	private static TCPSend sync = new TCPSend();

	private static int process_node;
	private static String host;
	
	

	public static void nodeProcess()
	{
		
		try {
			host = InetAddress.getLocalHost().getHostName();
			int itr = 0;
			for (String h : TreeGeneration.hostname) 
			{
				itr++;				
				if (h.equalsIgnoreCase(host)) 
				{
					process_node = itr;
					
					System.out.println("Process node: "+process_node +", listening on port: "+TreeGeneration.getPort(process_node));
				}
				
			}
		} catch (UnknownHostException e)
		{
			System.out.println("Failed : Host name");
			e.printStackTrace();
		}
		try 
		{
			
			Thread.sleep(3000);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		if(process_node==new MutexMain().getInitiator())
		{
			
			TreeGeneration.generateSpanningTree(process_node, process_node);	
		}
	}

	public static void doWait()
	{
		synchronized (sync) {
			try {
				sync.wait();
			} catch (InterruptedException e) {
				e.getMessage();
			}
		}
	}

	public static void doNotify() {
		synchronized (sync) {
			sync.notify();
		}
	}
	
	public static void requestToken(int node, int port)
	{
				
		Socket srv_socket;
		try {
			CSLogger.writeNodeEntry("Node: "+ TCPSend.getnodenumber()+" REQ_TOKEN from node: "+process_node+" to node "+node+" : "+SystemManager.childQueue());
			//System.out.println(TimeStamp.getTimeStamp()+" : "+"Node: "+ TCPSend.getnodenumber()+" REQ_TOKEN from node: "+process_node+" to node "+node);
			srv_socket = new Socket(TreeGeneration.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(
					srv_socket.getOutputStream(), true);
			msg_send.println("REQ_TOKEN from node: "+process_node);			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void grantToken(int node, int port)
	{
				
		Socket srv_socket;
		try {
			CSLogger.writeNodeEntry("Node: "+ TCPSend.getnodenumber()+" GRANT_TOKEN from node: "+process_node+" to node "+node+" : "+SystemManager.childQueue());
			//System.out.println(TimeStamp.getTimeStamp()+" : "+"Node: "+ TCPSend.getnodenumber()+" GRANT_TOKEN from node: "+process_node+" to node "+node);
			srv_socket = new Socket(TreeGeneration.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(
					srv_socket.getOutputStream(), true);
			msg_send.println("GRANT_TOKEN from node: "+process_node);			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// Fn to send broadcast msg to the selected node.
	public static void Tcpsend_broadcastMsg(int node, int port) 
	{	
				
		Socket srv_socket;
		try {
			srv_socket = new Socket(TreeGeneration.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(
					srv_socket.getOutputStream(), true);
			
			msg_send.println("BROADCAST_MSG from node: "+process_node);
			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	// Fn to send broadcast ack to the selected node.
	public static void Tcpsend_broadcastAck(int node, int port) 
	{	
		
		Socket srv_socket;
		try {
			srv_socket = new Socket(TreeGeneration.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(
					srv_socket.getOutputStream(), true);
			msg_send.println("BROADCAST_ACK_MSG from node: "+process_node);
			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	// Fn to send application msg to the selected node.
	public static void Tcpsend_spanningApplicationMsg(int node, int port, int root) 
	{	
		
		Socket srv_socket;
		try {
			srv_socket = new Socket(TreeGeneration.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(
					srv_socket.getOutputStream(), true);
			msg_send.println("APP_MSG from node, root-node: "+process_node+", "+root);
			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//Fn to send application ack to the selected node.
	public static void Tcpsend_sapnningApplicationAck(int node, int port) 
	{		
					
		Socket srv_socket;
		try {
			srv_socket = new Socket(TreeGeneration.hostname.get(node-1), port);
			PrintWriter msg_send = new PrintWriter(
					srv_socket.getOutputStream(), true);
			msg_send.println("APP_ACK from node: "+process_node);
			

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static int getnodenumber()
	{
		
		return process_node;
	}
	public static String getHostName()
	{
			
		return host;
	}	
}
