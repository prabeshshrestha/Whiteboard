/*
 * BoardServer.java
 *
 * Created on Nov 25, 2007, 2:23:16 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import whiteBoard.shapeInfo;

public class BoardServer extends UnicastRemoteObject implements ServerBoardInterface {
    /*this server will connect using this port number(Default if port number not specified)*/
    private static int port = 9999;
    /*address of the server*/
    String thisAddress;
    /* stores the list of paintarea(i.e the working area of the clients)
     * and their names(given by the user themselves) connected to this server
     */
    private static Vector<WhiteBoardInterface> clients = new Vector<WhiteBoardInterface>();
    private static Vector clientNameList = new Vector();
    /*stores the global shapelist*/
    Vector globalShapeList;
    /*connection serverStartUpMessage(server is started or not)*/
    String serverStartUpMessage;

    public BoardServer() throws RemoteException {
        /*if BoardServer does not get port number from user it uses default port number(i.e 9999^)*/
        super(port);
    }

    /**
     * creates an instance of remote board server
     * @param port port number to start this server
     * @throws java.rmi.RemoteException exception
     * @throws java.net.UnknownHostException
     */
    public BoardServer(int port) throws RemoteException {
        BoardServer.port = port;
        globalShapeList = new Vector();
        try {
            /* get the address of this computer 
             * i.e the server to make it easy for other clients to connect*/
            thisAddress = Inet4Address.getLocalHost().toString();
            this.serverStartUpMessage = "Server Up and Running at:" + thisAddress + " and port " + port;
        } catch (UnknownHostException unknownHostException) {
            this.serverStartUpMessage = "Server failed to start.Please check the configuration properly";
        }

        System.out.println(this.serverStartUpMessage);
    }

    /**
     * Remote board Server method
     * Clients call this method when it wants to connect to the remote board server
     * clients reference and name is stored in the list for later references
     * @param cName
     * @return number in the list (first come first serve) starting with 0
     * @throws java.rmi.RemoteException exception
     */
    @SuppressWarnings(value = "unchecked")
    public int Regist(WhiteBoardInterface client, String cName) throws RemoteException {
        /* check if the client has already registered earlier*/
        if (clients.contains(client) && clientNameList.contains(cName)) {
            System.out.println(cName+" already connected");
            return 500;
        } else if (clientNameList.contains(cName)) {
            System.out.println("Client with the name:- "+cName+" already connected");
            return -999;
        }
        if ((clients.contains(client) == false) && (clientNameList.contains(cName) == false)) {
            /*add clients name and reference to the corresponding vectors*/
            clients.add(client);
            clientNameList.add(cName);
            
            /*sets the global shapelist to the newly added client*/
            client.setShapeList(globalShapeList);
            
            /*returns the index of newly added client*/
            return clients.indexOf(client);
        } else {
            return -1;
        }
    }

    /**
     * Remote board server method
     * client calls it when it wants to send the new or modified ShapeInfo object to all other clients
     * @param clientid clientId that sent the request
     * @param item ShapeInfo Object that is sent
     * @throws java.rmi.RemoteException exception
     */
    @SuppressWarnings(value = "unchecked")
    public void Broadcast_AddShape(shapeInfo item) throws RemoteException {
        globalShapeList.add(item);
        for (int i = 0; i < clients.size(); i++) {
            try {
                ((clients.elementAt(i))).appendChartItem(item);
            } catch (Exception e) {
                System.out.println("error" + e);
            }
        }
    }

    /**
     * Remote board server method
     * client calls it when it wants to send the new or modified ShapeInfo object to all other clients
     * @param clientid clientId that sent the request
     * @param item ShapeInfo Object that is sent
     * @throws java.rmi.RemoteException exception
     */
    @SuppressWarnings(value = "unchecked")
    public void Broadcast_DeleteShape(Vector shapelist) throws RemoteException {
        this.globalShapeList = shapelist;
        for (int i = 0; i < clients.size(); i++) {
            clients.elementAt(i).setShapeList(globalShapeList);
        }
    }

    public void setGlobalShapeList(Vector shapelist) throws RemoteException {
        this.globalShapeList = shapelist;
    }

    /**
     * main method
     * @param args command line arguments if any inputted by the user in the server
     * @throws java.net.UnknownHostException
     * @throws java.net.MalformedURLException
     */
    public static void main(String[] args) throws UnknownHostException, MalformedURLException {
        try {
            /*creates the registry at the port specified*/
            Registry registry = LocateRegistry.createRegistry(port);
            BoardServer server = new BoardServer(port);
            
            /*binds the server under the name Boardserver in the registry*/
            registry.rebind("Boardserver", server);
        } catch (RemoteException ex) {
            System.out.println("start sever fail..." + ex.getMessage());
            System.exit(0);
            Logger.getLogger(BoardServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
