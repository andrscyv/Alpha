/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha;

import Interfaces.RegistryInt;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author El_jefe
 */
public class Client extends Thread {
    public String id;
    public String userName;
    public String multIp;
    public int portTcp;
    public String serverIp;
    
    public Client(String serverIp, String userName){
        System.setProperty("java.net.preferIPv4Stack", "true");
        this.serverIp = serverIp;
        this.userName = userName;
        this.registerPlayer();
    }
    @Override
    public void run(){
        ByteBuffer wrapped;
        MulticastSocket s =null;
        int[] msgDeco = new int[2];
        String[] aux;
        int lastId = -1;
        //int i = 0;
   	 try {
                InetAddress group = InetAddress.getByName(multIp); // destination multicast group 
	    	s = new MulticastSocket(6299);
	   	s.joinGroup(group); 

	    	byte[] buffer = new byte[20];
 	   	while( true ) {
                   // System.out.println("Cliente "+id +" escuchando ... ");
                    DatagramPacket messageIn = 
			new DatagramPacket(buffer, buffer.length);
 		    s.receive(messageIn);
                    aux = new String(messageIn.getData()).trim().split(";");
                    for( int i = 0 ; i < 2; i++){
                        msgDeco[i] = Integer.parseInt(aux[i]);
                        //System.out.print(msgDeco[i] + " ");
                        //System.out.println("");
                    }
                    //System.out.println("");
                    if( lastId != msgDeco[1]){
                        //System.out.println("Mensaje nuevo");
                        lastId = msgDeco[1];
                        this.send(id);
                    }
                        
                    
  	     	}
//	    	s.leaveGroup(group);		
 	    }
         catch (SocketException e){
             System.out.println("Socket: " + e.getMessage());
             //System.out.println(e.);
	 }
         catch (IOException e){
             System.out.println("IO: " + e.getMessage());
         }
	 finally {
            if(s != null) s.close();
        } 
    }
    
    public void send(String msg){
        Socket s = null;
        try {
            int serverPort = portTcp;

            s = new Socket(serverIp, serverPort);
            //s = new Socket();
            //s.connect(new InetSocketAddress(serverIp, serverPort), 500);
         //   s = new Socket("127.0.0.1", serverPort);    
            DataInputStream in = new DataInputStream( s.getInputStream());
            DataOutputStream out =
                    new DataOutputStream( s.getOutputStream());
            out.writeUTF(msg);        	// UTF is a string encoding 
            System.out.println("Cliente " + id+ " envio "+ msg);

//            String data = in.readUTF();	      
//            System.out.println("Received: "+ data) ;      
        } 
        catch (UnknownHostException e) {
            System.out.println("Sock:"+e.getMessage()); 
        }
        catch (EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } 
        catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
        } finally {
            if(s!=null) 
                try {
                    s.close();
                } catch (IOException e){
                System.out.println("close:"+e.getMessage());}
                }
        }
    
    public void registerPlayer(){
        System.setProperty("java.security.policy","file:/Users/El_jefe/NetBeansProjects/Alpha/src/alpha/rmi.policy");

        if (System.getSecurityManager() == null) {
           System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "GameRegistry";
            Registry registry = LocateRegistry.getRegistry(serverIp); // server's ip address args[0]
            RegistryInt gameRegistry = (RegistryInt) registry.lookup(name);
            Bean info = gameRegistry.registerNewPlayer(userName);
            this.multIp = info.multIp;
            this.id = info.playerId;
            this.portTcp = info.portTcp;
            

            
        } catch (Exception e) {
            System.err.println("exception");
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) throws Exception{
        Client c = new Client("localhost", "primero");
        c.start();
        c = new Client("localhost", "segundo");
        c.start();
        for(int i = 0; i < 30; i++){
            c = new Client("localhost", i+"");
            c.start();
        }
    }
    
}
