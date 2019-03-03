/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 *
 * @author El_jefe
 */
public class Client {
    public int id;
    public String userName;
    public String multIp;
    public int portTcp;
    public int serverIp;
    
    public Client(){
        System.setProperty("java.net.preferIPv4Stack", "true");
    }
    void listen() throws Exception{
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
                    System.out.println("Waiting for messages");
                    DatagramPacket messageIn = 
			new DatagramPacket(buffer, buffer.length);
 		    s.receive(messageIn);
                    aux = new String(messageIn.getData()).trim().split(";");
                    for( int i = 0 ; i < 2; i++){
                        msgDeco[i] = Integer.parseInt(aux[i]);
                        System.out.print(msgDeco[i] + " ");
                        //System.out.println("");
                    }
                    System.out.println("");
                    if( lastId != msgDeco[1]){
                        System.out.println("Mensaje nuevo");
                        lastId = msgDeco[1];
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
    
    public static void main(String[] args) throws Exception{
        Client c = new Client();
        c.multIp = "224.9.6.3";
        c.listen();
    }
    
}
