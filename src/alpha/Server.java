/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha;

import Interfaces.RegistryInt;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author El_jefe
 */
public class Server {
    //private ArrayList<Integer> scoreTable;
    private static Random r = new Random();
    
    private static void multiCastMonster(String multIp, int serial) throws Exception{
         	 
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
    
        private static void multiCastWinner(String multIp, int serial, String winnerId) throws Exception{
         	 
	MulticastSocket s =null;
   	 try {
                
                InetAddress group = InetAddress.getByName(multIp); // destination multicast group 
	    	s = new MulticastSocket(6300);
	   	s.joinGroup(group); 
                //s.setTimeToLive(10);
                //System.out.println("Messages' TTL (Time-To-Live): "+ s.getTimeToLive());
                //int monster = r.nextInt(9) + 1;
                String msg="-1;"+serial + ";"+winnerId;
                byte [] m = msg.getBytes();
	    	DatagramPacket messageOut = 
			new DatagramPacket(m, m.length, group, 6299);
	    	s.send(messageOut);
                System.out.println("Se envio ganador: " + msg);

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
        
        private static String winnerExist( HashMap<String, Integer> scoreTable, int winnerAmount){
            String res = null;
            for(Map.Entry<String, Integer> entry : scoreTable.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if( value >= winnerAmount )
                    res = key;
                // do what you have to do here
                // In your case, another loop.
                System.out.println("id : "+key+" score: "+value);
            }
            return res;
        }
    
    public static void main(String[] args) throws Exception {
        //Configuracion de la comunicacion
        System.setProperty("java.net.preferIPv4Stack", "true");
        String multIp = "224.9.6.3";
        String serverIp = "localhost";
        int portTcp = 4447;
        int serial = 0;
        int winnerAmount = 3;
        boolean isFirst = true;
        long waitAnswerMilis = 3000; 
        long startTime;
        boolean stopWaiting = false;
        DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
        ServerSocket listenSocket ;
        String roundWinner;
        TcpConnection conn;
        HashMap<String, Integer> scoreTable = new HashMap<String, Integer>();
        
        System.setProperty("java.security.policy","file:"+System.getProperty("user.dir")+ "/src/alpha/rmi.policy");

        if (System.getSecurityManager() == null) {
           System.setSecurityManager(new SecurityManager());
        }
        LocateRegistry.createRegistry(1099);   /// default port
        String name = "GameRegistry";
        GameRegistry engine = new GameRegistry(serverIp, multIp, portTcp);
        RegistryInt stub =
            (RegistryInt) UnicastRemoteObject.exportObject(engine, 0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(name, stub);
        System.out.println("GameRegistry en linea...");
        String winnerId;
        
        while( true ){
            try {
                winnerId = Server.winnerExist(scoreTable, winnerAmount);
                if( winnerId != null ){
                    System.out.println("=============================================================");
                    System.out.println("Ganador global: "+ winnerId);
                    System.out.println("=============================================================");
                    scoreTable = new HashMap<String, Integer>();
                }
                Server.multiCastMonster(multIp, serial);
                listenSocket = new ServerSocket(portTcp);
                conn = new TcpConnection(listenSocket, scoreTable);
                conn.start();
                Thread.sleep(2000);
                listenSocket.close();
                conn.join();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            serial++;
        }
        
    }
    
}
