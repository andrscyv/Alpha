/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha;

import java.io.Serializable;

/**
 *
 * @author El_jefe
 */
public class Bean implements Serializable {
    public String multIp;
    public String serverIp;
    public int portTcp;
    public String playerId;

    public Bean(String multIp, String serverIp, int portTcp, String playerId) {
        this.multIp = multIp;
        this.serverIp = serverIp;
        this.portTcp = portTcp;
        this.playerId = playerId;
    }
    
    
    
}
