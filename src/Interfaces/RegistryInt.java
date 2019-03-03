/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import alpha.Bean;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author El_jefe
 */
public interface RegistryInt extends Remote {
    
    public Bean registerNewPlayer(String username) throws RemoteException;
    
}
