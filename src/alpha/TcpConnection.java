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
    public TcpConnection (ServerSocket listenSocket, HashMap<String,Integer> scoreTable) {
        this.listenSocket = listenSocket;
        this.scoreTable = scoreTable;
    }

    @Override
    public void run(){
        Integer score;
        try {			                 // an echo server
            clientSocket = listenSocket.accept();
            in = new DataInputStream(clientSocket.getInputStream());
            out =new DataOutputStream(clientSocket.getOutputStream());
            String roundWinner = in.readUTF();
            score = scoreTable.get(roundWinner);
            scoreTable.put(roundWinner,score == null ? new Integer(1) : new Integer(score.intValue() + 1));
            System.out.println("Ganador de la ronda: " + roundWinner + " puntaje : "+scoreTable.get(roundWinner));
            clientSocket.close();
        } 
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } 
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        } 
//        finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e){
//                System.out.println(e);
//            }
//            }
        }
}
