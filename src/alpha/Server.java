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
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author El_jefe
 */
public class Server {
    private ArrayList<Integer> scoreTable;
    private static Random r = new Random();
    
    private static void multiCastMonster(String multIp, int portTcp, int serial) throws Exception{
         	 
	MulticastSocket s =null;
   	 try {
                
                InetAddress group = InetAddress.getByName(multIp); // destination multicast group 
	    	s = new MulticastSocket(6300);
	   	s.joinGroup(group); 
                //s.setTimeToLive(10);
                //System.out.println("Messages' TTL (Time-To-Live): "+ s.getTimeToLive());
                int monster = r.nextInt(9) + 1;
                String msg=monster+";"+serial;
                byte [] m = msg.getBytes();
	    	DatagramPacket messageOut = 
			new DatagramPacket(m, m.length, group, 6299);
	    	s.send(messageOut);
                System.out.println("Se envio: " + msg);

	    	s.leaveGroup(group);		
 	    }
         catch (SocketException e){
             System.out.println("Socket: " + e.getMessage());
             throw e;
	 }
         catch (IOException e){
             System.out.println("IO: " + e.getMessage());
             throw e;
         }
	 finally {
            if(s != null) s.close();
        }
        
    }
    
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        String multIp = "224.9.6.3";
        int portTcp = 4446;
        int serial = 0;
        boolean isFirst = true;
        
        while( true ){
            try {
                Server.multiCastMonster(multIp, portTcp, serial);
                Thread.sleep(2000);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            serial++;
        }
        
    }
    
}
