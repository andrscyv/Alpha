/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha;

import Interfaces.RegistryInt;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author El_jefe
 */
public class GameRegistry implements RegistryInt{
    HashMap<String, String> players;
    String serverIp;
    String multIp;
    int portTcp;
    public static long serial= 0;

    public GameRegistry(String serverIp, String multIp, int portTcp) throws RemoteException{
        super();
        this.serverIp = serverIp;
        this.multIp = multIp;
        this.portTcp = portTcp;
        this.players = new HashMap<>();
    }
    
    

    @Override
    public Bean registerNewPlayer(String username) throws RemoteException {
        
        String playerId = players.get(username);
        if( playerId == null){
            players.put(username, serial+"");
            serial++;
            playerId = players.get(username);
        }
        
        return new Bean(multIp, serverIp, portTcp, playerId);
        
    }
    
}
