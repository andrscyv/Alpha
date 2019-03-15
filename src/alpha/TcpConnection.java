/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author El_jefe
 */
public class TcpConnection extends Thread {
    
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    HashMap<String,Integer> scoreTable;
    ServerSocket listenSocket;
    boolean winner;
    long initTime;
    ArrayList<Long> rtt;
    public TcpConnection (ServerSocket listenSocket, HashMap<String,Integer> scoreTable, long initTime, ArrayList<Long> rtt ) {
        this.listenSocket = listenSocket;
        this.scoreTable = scoreTable;
        winner = false;
        this.initTime = initTime;
        this.rtt = rtt;
    }

    @Override
    public void run(){
        Integer score;
        //long receivedTime;
        try {			                 // an echo server
            
            while(true){
                clientSocket = listenSocket.accept();
                in = new DataInputStream(clientSocket.getInputStream());
                out =new DataOutputStream(clientSocket.getOutputStream());
                if( !winner ){
                    String roundWinner = in.readUTF();
                    score = scoreTable.get(roundWinner);
                    scoreTable.put(roundWinner,score == null ? new Integer(1) : new Integer(score.intValue() + 1));
                    //System.out.println("Ganador de la ronda: " + roundWinner + " puntaje : "+scoreTable.get(roundWinner));
                    winner = true;
                }
                clientSocket.close();
                rtt.add(System.nanoTime()-initTime);
            }
        } 
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } 
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        }
        //System.out.println("Tcp connection stopped listening");
//        finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e){
//                System.out.println(e);
//            }
//            }
        }
}
