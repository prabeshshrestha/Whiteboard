/*
 * WhiteBoardInterface.java
 *
 * Created on Nov 25, 2007, 7:19:01 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
import whiteBoard.shapeInfo;

public interface WhiteBoardInterface extends Remote {

    /**
     * method that registers the remote clients to current board server
     * @param cname client name
     * @return status
     * @throws java.rmi.RemoteException exception
     */
    String test(String cname) throws RemoteException;

    /**
     * appends the ShapeInfo item to the ShapeInfo list
     * @param item ShapeInfo object to be appended
     * @throws java.rmi.RemoteException exception
     */
    public void setShapeList(Vector shapeList) throws RemoteException;

    /**
     * board server calls this method to send the ShapeInfo Object to the client
     * @param item ShapeInfo object received from board server
     * @throws java.rmi.RemoteException exception
     */
    void appendChartItem(shapeInfo item) throws RemoteException;
}
