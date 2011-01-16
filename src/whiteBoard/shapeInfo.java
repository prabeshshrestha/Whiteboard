/*
 * shapeInfo.java
 * @author Prabesh Shrestha
 * @email prabesh708@gmail.com
 * Created on September 8, 2007, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package whiteBoard;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.Serializable;

/**
 * This Class will contain the information about the whiteboard objects which can be painted.
 * this class should emplement serializable because it will be used by the remote method
 */
public class shapeInfo implements Serializable {

    boolean selected = false;
    /* color of the current shape object(Default:black)*/
    Color color = Color.BLACK;
    double start_x = 0, start_y = 0, end_x = 0, end_y = 0, mouseOffset_x = 0, mouseOffset_y = 0;
    /* width,height of the object*/
    double width, height;
    /* stroke width of the current Shape object*/
    float strWidth;
    GeneralPath gp;
    /* index in the whiteboard shape list*/
    int arrIndex;
    /*text content of the text object*/
    String text = "";
    /* shape type like Circle, Rectangle, FilledCircle, Line etc.*/
    String shapeType;
    /* Shape object for the current ShapeInfo object*/
    Shape shape = null;
    String clientName = "null";
    Point point, pnt;
    private int clientId;

    /**
     * Creates a new instance of shapeInfo with the default values given below
     */
    public shapeInfo() {
        arrIndex = -1;
        clientId = -1;
        height = 0;
        strWidth = 1.0f;
        shapeType = "";
        point = new Point(0, 0);
        width = 0;
    } /*end of constructor*/


    /** 
     * makes a exact replica of any shapeinfo object
     * copies information of one {@link ShapeInfo} object into another {@link ShapeInfo} object
     * @param oldShape Original ShapeInfo object from which to copy information
     */
    public void copyShape(shapeInfo oldShape) {
        this.color = oldShape.color;
        this.strWidth = oldShape.strWidth;
        this.shapeType = oldShape.shapeType;
        this.shape = oldShape.shape;
        this.start_x = oldShape.start_x;
        this.start_y = oldShape.start_y;
        this.end_x = oldShape.end_x;
        this.end_y = oldShape.end_y;
        this.mouseOffset_x = oldShape.mouseOffset_x;
        this.mouseOffset_y = oldShape.mouseOffset_y;
        this.arrIndex = oldShape.arrIndex;
        this.height = oldShape.height;
        this.width = oldShape.width;
        this.point = oldShape.point;
        this.clientId = oldShape.clientId;
    }

    /**
     * check if the Shape of the current ShapeInfo object contains the specified points
     * @param function which method called this method like copy,cut ..
     * @param x x coordinate
     * @param y y coordinate
     * @return true if x,y is in the shape else false
     */
    public boolean containPoint(double x, double y, String function) {
        boolean contains = false; //signifies whether or the shape contains the given point
        if (this.shapeType.equals("Line")) {
            Rectangle r = this.shape.getBounds();
            float m = (float) r.height / (float) r.width;
            System.out.println("  diff value " + String.valueOf(m * (x - r.x) - (y - r.y)) + " value of x " + x + " y " + y);
            if ((y - r.y) - (m * (x - r.x)) <= 5.0) {
                if ((x >= r.x) && (x <= r.x + r.width)) {
                    if ((y >= r.y) && (y <= r.y + r.height)) {
                        contains = true;
                        if (function.equals("mousePressed")) {
                            this.mouseOffset_x = this.start_x - (int) x;
                            this.mouseOffset_y = this.start_y - (int) y;
                        }
                    }
                }
            }
        } else {
            if (this.shape.contains(x, y)) {
                contains = true;
                if (function.equals("mousePressed")) {
                    Rectangle r = this.shape.getBounds();
                    this.mouseOffset_x = r.x - (int) x;
                    this.mouseOffset_y = r.y - (int) y;
                }
            }
        }
        return contains;
    }

    /*getter and setter methods for the above mentioned variables*/
    /*setter methods*/
    /**
     * sets the line parameter
     * @param x1 start x coordinate
     * @param y1 start y coordinate
     * @param x2 end x coordinate
     * @param y2 end y coordinate
     */
    public void setLineParm(double x1, double y1, double x2, double y2) {
        start_x = x1;
        start_y = y1;
        end_x = x2;
        end_y = y2;
    }

    /**
     * Sets the index of this object in the shape list of whiteboard
     * @param index index in the whiteboard shape list
     */
    public void setArrIndex(int index) {
        arrIndex = index;
    }

    /**
     * sets the color of the current Shape object
     * @param c color
     */
    public void setColor(Color c) {
        color = c;
    }

    public void setPoint(Point p) {
        pnt = p;
    }

    /**
     * sets the stroke width of the current Shape object
     * @param sw width of the stroke
     */
    public void setStrokeWidth(float sw) {
        strWidth = sw;
    }

    /**
     * sets the clientID received from whiteboard server to the current ShapeInfo object
     * @param clientid clientID
     */
    public void setClientID(int clientid) {
        clientId = clientid;
    }

    /**
     * returns the clientID of the current ShapeInfo object
     * @return clientID
     */
    public int getClientID() {
        return clientId;
    }

    /**
     * set the type of the object
     * @param stype shape type like Circle, Rectangle, FilledCircle, Line etc.
     */
    public void setShapeType(String stype) {
        shapeType = stype;
    }

    /**
     * set the type of the object
     *
     * @param wid
     */
    public void setWidth(Double wid) {
        width = wid;
    }

    /**
     * set the type of the object
     * @param h
     */
    public void setHeight(Double h) {
        height = h;
    }

    /**
     * sets the Shape object for the current ShapeInfo object
     * @param s Shape object
     */
    public void setShape(Shape s) {
        shape = s;
    }

    /**
     *sets the text for the text shape
     *@param textReceived string input in the text shape
     **/
    public void setText(String textReceived) {
        text = textReceived;
    }

    /**
     * set the current ShapeInfo object as selected or not selected
     * @param selection selected or not selected
     */
    public void setSelected(boolean selection) {
        selected = selection;
    }

    /**
     * set the current ShapeInfo object as selected or not selected
     *
     * @param p corner point of rectangle or circle etc
     */
    public void setCornerPoint(Point p) {
        point = p;
    }

    /**
     * sets the client name for the current ShapeInfo Object
     * @param cName client name
     */
    public void setClientName(String cName) {
        clientName = cName;
    }

    /**
     *return the text in the text shape
     * @return text text in the text shape
     **/
    public String getText() {
        return text;
    }

    /**
     * set the current ShapeInfo object as selected or not selected
     * @return point
     */
    public Point getCornerPoint() {
        return point;
    }

    /**
     * returns the index of this object in the shape list of whiteboard
     * @return index
     */
    public int getArrIndex() {
        return arrIndex;
    }

    /**
     * returns the client name of the current ShapeInfo object
     * @return client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * returns the width of the rectangle or the circle
     * @return width
     */
    public Double getWidth() {
        return width;
    }

    /**
     * returns the height of the rectangle or the circle
     * @return heigth
     */
    public Double getHeight() {
        return height;
    }

    /**
     * returns color of the current shape object
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * returns stroke width of the current object
     * @return stroke width
     */
    public float getStrokeWidth() {
        return strWidth;
    }

    /**
     * returns the current Shape object of the current ShapeInfo object
     * @return Shape object
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * returns the shape type of current ShapeInfo object
     * @return shape type
     */
    public String getShapeType() {
        return shapeType;
    }
}
