/*
 * ServerBoardInterface.java
 *
 * Created on Nov 25, 2007, 2:26:48 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
import whiteBoard.shapeInfo;

public interface ServerBoardInterface extends Remote {

    /**
     * method that registers the remote clients to current board server
     * @param client
     * @param cname client name
     * @return status
     * @throws java.rmi.RemoteException exception
     */
    int Regist(WhiteBoardInterface client, String cname) throws RemoteException;

    /**
     * sends the ShapeInfo from one client to other clients
     * @param clientid clientId that sent the request to send
     * @param item ShapeInfo object to send to other clients
     * @throws java.rmi.RemoteException exception
     */
    void Broadcast_AddShape(shapeInfo item) throws RemoteException;

    /**
     * sends the information of deleted ShapeInfo from one client to other clients
     * @param clientid clientId that sent the request to send
     * @param item ShapeInfo object to send to other clients
     * @throws java.rmi.RemoteException exception
     */
    void Broadcast_DeleteShape(Vector shapelist) throws RemoteException;

    void setGlobalShapeList(Vector shapelist) throws RemoteException;
}
