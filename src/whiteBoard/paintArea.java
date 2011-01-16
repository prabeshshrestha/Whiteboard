/*
 * paintArea.java
 *
 * Created on August 22, 2007, 9:45 PM
 */
package whiteBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import mainBoard.mainGUI;
import remote.ServerBoardInterface;

public class paintArea extends javax.swing.JPanel implements remote.WhiteBoardInterface {
    /*selector selected in the mainGUI or not / draw outline or not*/

    boolean selector, drawOutLine;
    //paste selected or not
    boolean past = false, once = false, acopy = true, deleteOnce = true;
    boolean deleter, select = false;
    BufferedImage img = null;
    /*font color of the text shape / color of the object*/
    Color fcolor, color;
    double x, y;
    /*height,weight of the object*/
    double height, width;
    double offset_x = 0.0, offset_y = 0.0, x_1 = 0.0, y_1 = 0.0;
    double xp = 0.0, yp = 0.0, prev_x, prev_y;
    int YES_NO;
    /*stroke width*/
    float strWidth;
    /*font the text shape*/
    Font f;
    GeneralPath polyline;
    /* stores the index of the object be deleted when delete is 
     * selected in the popUP menu*/

    /* just the flag used in text shape*/
    int flagtext = 0, cutDelete;
    /* font size,style index of the text shape*/
    int fontsize, fontStyle;
    /* font style of the text shape*/
    int[] fstyle = {Font.PLAIN, Font.BOLD, Font.ITALIC};
    /* client id of this whiteboard*/
    int clientid;
    /* mainboard for this paintarea*/
    mainGUI mainBoard;
    Rectangle2D rect;
    ServerBoardInterface server;
    String action, text, shape = "";
    Shape copyShape;
    shapeInfo moveShape = new shapeInfo();
    /* freshly passed shapeinfo*/
    shapeInfo shapeInfoPassed;
    /* the latest use shapeinfo*/
    shapeInfo latest = null;
    /* shapeInfo used to store temporary shape information that will be copied or cut */
    shapeInfo copyPaste;
    /* client name of this whiteboard*/
    String clientName;
    Point poi;
    /* create dynamic list of shapes to be stored*/
    public Vector shapeList;
    /* not used right now*/
    boolean load = false;
    boolean objectSelected = false;
    boolean cutb;
    int arrListIndex;

    /** Creates new instance of paintArea */
    public paintArea() {
        initComponents();
        /* set the initial arrListIndex */
        arrListIndex = -1;
        this.server = null;
        color = Color.GREEN;
        copyPaste = new shapeInfo();
        clientName = null;
        clientid = -1;
        deleter = false;
        Font[] allFonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (int i = 0; i < allFonts.length; i++) {
            String fname = allFonts[i].getName();
            fontName.addItem(fname);
        }
        fontStyl.addItem("Normal");
        fontStyl.addItem("Bold");
        fontStyl.addItem("Italic");

        height = 0.0;

        past = false;
        poi = new Point(0, 0);

        shapeList = new Vector(); //creates a vector shapeList
        strWidth = 1.0f;
        selector = false;

        rect = null;
        width = 0.0;
        x = 0.0;
        y = 0.0;
    } /*end of constructor*/


    public void cleanUp() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * clears the board
     * */
    public void clearBoard() {
        if (shapeList != null) {
            YES_NO = JOptionPane.showConfirmDialog(this, " Save this ?");
        }
        if (YES_NO == 0) {
            System.out.println("want to save ?");
            mainBoard.save();
            shapeList.clear();
            repaint();
        } else if (YES_NO == 1) {
            shapeList.clear();
            repaint();
        } else {
        }
    }

    public void selectorWork() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setShapes(Vector sh) {
        shapeList = sh;
        this.repaint();
    }

    public Vector getShapeList() {
        return shapeList;
    }

    public void setShapeList(Vector shapeLis) throws RemoteException {
        this.shapeList = shapeLis;
        repaint();
    }

    public boolean saveFile(File saveFile, String outDir) {
        ObjectOutputStream oos;
        String outFile = null;
        String outObjectFile = null;
        BufferedImage bufferedImage;

        /* proceed only if there are objects to be written */
        if (shapeList != null) {
            String ext = getExtension(saveFile);
            if (ext == null) {
                ext = "jpg";
                outFile = saveFile.getName() + "." + ext;
            } else if (!(ext.equals("png") || ext.equals("jpg") || ext.equals("gif"))) {
                ext = "jpg";
                outFile = getName(saveFile);
                outFile = outFile + "." + ext;
            } else {
                outFile = saveFile.getName();
            }
            outFile = outDir + "/" + outFile;
            outObjectFile = outDir + "/" + getName(saveFile);

            /* Save the array list as the object File */
            try {
                /* open the object output stream */
                oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(outObjectFile + ".out")));
                /* save object instance (ArrayList shapeList) to the file */
                Vector saveList = resetShapes(shapeList);
                oos.writeObject(shapeList);
                oos.close();
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }

            /* save as image file */
            bufferedImage = new BufferedImage(this.getBounds().width, this.getBounds().height, BufferedImage.TYPE_INT_RGB);

            // Create a graphics contents on the buffered image
            Graphics2D g2d = bufferedImage.createGraphics();
            /*set the background of the image to white */
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, this.getBounds().width, this.getBounds().width);
            // Draw graphics
            for (int i = 0; i < shapeList.size(); i++) {
                shapeInfo sh = (shapeInfo) shapeList.get(i); //retrieve all the shapes from shapelist and draw them
                if (sh.color == null) {
                    sh.color = Color.BLACK;
                }
                g2d.setColor(sh.color);
                if (sh.shapeType.equals("FilledRectangle") || sh.shapeType.equals("FilledCircle")) {
                    g2d.fill(sh.shape);
                } else {
                    g2d.draw(sh.shape);
                }
            }
            g2d.dispose();
            RenderedImage rendImage = bufferedImage;
            /* Write generated image to a file */
            try {
                // Save as the extension file
                File file = new File(outFile);
                ImageIO.write(rendImage, ext, file);
            } catch (IOException e) {
            }
            return true;
        } else {
            return false;
        }
    }

    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private String getName(File f) {
        String name = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            name = s.substring(0, i);
        } else if (i < 0) {
            name = f.getName();
        }
        //System.out.println("Name:"+name);
        return name;
    }

    public void reducePacket() {
        try {
            server.Broadcast_DeleteShape(shapeList);
            System.out.print("send to server");
            latest = null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.print("not send to server");
        }
    }

    @SuppressWarnings("unchecked")
    private Vector resetShapes(Vector shapeList) {
        Vector newShape = new Vector();
        int index = 0;
        if (shapeList != null) {
            for (int i = 0; i < shapeList.size(); i++) {
                shapeInfo old = (shapeInfo) shapeList.get(i);
                {
                    shapeInfo save = new shapeInfo();
                    save.setShape(old.getShape());
                    save.setColor(old.getColor());
                    save.setStrokeWidth(old.getStrokeWidth());
                    save.setShapeType(old.getShapeType());
                    save.setArrIndex(index++);
                    newShape.add(save);
                }
            }
        }
        System.out.println("Sending new list");
        return newShape;
    }

    /**
     *receives the instance of maingui of which this paintarea is a part
     *@param app mainGUI instance send from MainGUI frame
     **/
    public void setWhiteAppInstance(mainGUI app) {
        mainBoard = app;
    }

    /**
     * sets the client id of the current whiteboard
     * @param t_clientid client id
     */
    public void setClientid(int t_clientid) {
        this.clientid = t_clientid;
    }

    /**
     * returns if the delete icon is selected or not
     * @return boolean
     */
    public boolean getDeleter() {
        return deleter;
    }

    /**
     * returns if the select icon is selected or not
     * @return boolean
     */
    public boolean getselector() {
        return selector;
    }

    /**
     *sets the boolean for if the selector icon is pressed
     *@param set boolean
     **/
    public void setselector(boolean set) {
        selector = set;
    }

    /**
     *sets the boolean for if the deletor icon is pressed
     *@param set boolean
     **/
    public void setDeleter(boolean set) {
        deleter = set;
    }

    /**
     * sets the client name for the current whiteboard
     * @param cname client name
     */
    public void setClientName(String cname) {
        clientName = cname;
    }

    /**
     *here the main job of painting is accomplished
     *this method is called when repaint is called in any place within this file
     **/
    // mark method as a superclass method
    // that has been overridden
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; //typecasting graphics to graphics2d
        int i; //variable used in the for loop
        for (i = 0; i < shapeList.size(); i++) {
            //retrieve all the shapes from shapelist and draw them
            shapeInfo sh = (shapeInfo) shapeList.get(i); //gets one shapeinfo object at a time
            g2.setColor(sh.color); //sets the color as is in the shapeinfo
            g2.setStroke(new BasicStroke(sh.strWidth)); //sets the stroke as is in the shapeinfo
            if (sh.shapeType.equals("FilledRectangle") || sh.shapeType.equals("FilledCircle")) {
                g2.fill(sh.shape); //to produce the filled shape
            } else if (sh.shapeType.equals("Freehand")) {
                g2.draw(sh.gp);
            } else if (sh.shapeType.equalsIgnoreCase("Text")) {
                Point po;
                po = sh.pnt; //gets the point informatin from shapeinfo class
                setFont(f);
                g2.drawString(sh.text, (int) po.getX(), (int) po.getY());
            } else {
                g2.draw(sh.shape); //draws the shape that is in the shapelist
            }
            if (drawOutLine == true) {
                g2.draw(rect);
                drawOutLine = false;
            }
        } //end of for loop
    }

    /**
     *sets the shape
     *@param shapeName name of the shape passed like circle,rectangle etc
     **/
    public void setShape(String shapeName) {
        shape = shapeName;
    }

    /**
     *freshly passed shapeInfo object that will be drawn in the paintarea
     ** @param shape
     */
    public void setShapeInfo(shapeInfo shape) {
        shapeInfoPassed = new shapeInfo();
        shapeInfoPassed = shape;
    }

    /**
     *saves the shapelist in a file board.out
     ** @return trueof false
     */
    public boolean saveCurrentInstance() {
        ObjectOutputStream oos;
        String outFile;
        outFile = "board.out"; //name of the file where to save the list
        if (shapeList != null) {        //if there are some shapes in the whiteboard
            try {
                /* open the object output stream */
                oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
                /* save object instance (ArrayList shapeList) to the file */
                oos.writeObject(shapeList);
                oos.close();
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
            return true; //if saved return true
        } else {
            return false; //if not saved because no shapes in the list return false
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textframe = new javax.swing.JDialog();
        textok = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        fontName = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fontStyl = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        textcolor = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        fontSize = new javax.swing.JSlider();
        textenter = new javax.swing.JPanel();
        textenterhere = new javax.swing.JTextField();
        copycut = new javax.swing.JPopupMenu();
        copy = new javax.swing.JMenuItem();
        paste = new javax.swing.JMenuItem();

        textframe.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        textframe.setTitle("Text Enter Here");
        textframe.setBackground(new java.awt.Color(0, 102, 102));
        textframe.setBounds(new java.awt.Rectangle(50, 50, 0, 0));
        textframe.setFont(new java.awt.Font("Abbess", 2, 12));
        textframe.setLocationByPlatform(true);
        textframe.setMinimumSize(new java.awt.Dimension(300, 300));
        textframe.setModal(true);

        textok.setText("Add");
        textok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textokMouseClicked(evt);
            }
        });

        jPanel1.setMaximumSize(new java.awt.Dimension(200, 500));

        fontName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Arial" }));
        fontName.setMaximumSize(new java.awt.Dimension(25, 50));
        fontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontNameActionPerformed(evt);
            }
        });

        jLabel1.setText("Font");

        jLabel2.setText("Style");

        fontStyl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        fontStyl.setMaximumSize(new java.awt.Dimension(125, 50));

        jLabel3.setText("Color");

        textcolor.setBackground(new java.awt.Color(51, 51, 255));
        textcolor.setMaximumSize(new java.awt.Dimension(15, 100));

        javax.swing.GroupLayout textcolorLayout = new javax.swing.GroupLayout(textcolor);
        textcolor.setLayout(textcolorLayout);
        textcolorLayout.setHorizontalGroup(
            textcolorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
        );
        textcolorLayout.setVerticalGroup(
            textcolorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        jLabel4.setText("Size");

        fontSize.setMajorTickSpacing(5);
        fontSize.setMaximum(50);
        fontSize.setMinorTickSpacing(1);
        fontSize.setPaintLabels(true);
        fontSize.setPaintTicks(true);
        fontSize.setSnapToTicks(true);
        fontSize.setValue(12);
        fontSize.setMaximumSize(new java.awt.Dimension(127, 46));

        textenter.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter your text here"));
        textenter.setMaximumSize(new java.awt.Dimension(327, 327));
        textenter.setPreferredSize(new java.awt.Dimension(100, 200));

        textenterhere.setMaximumSize(new java.awt.Dimension(10, 200));

        javax.swing.GroupLayout textenterLayout = new javax.swing.GroupLayout(textenter);
        textenter.setLayout(textenterLayout);
        textenterLayout.setHorizontalGroup(
            textenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textenterLayout.createSequentialGroup()
                .addComponent(textenterhere, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        textenterLayout.setVerticalGroup(
            textenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textenterLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(textenterhere, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(textcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addComponent(fontName, 0, 85, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(fontStyl, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(textenter, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(textcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(fontStyl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(fontName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                            .addComponent(fontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(textenter, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout textframeLayout = new javax.swing.GroupLayout(textframe.getContentPane());
        textframe.getContentPane().setLayout(textframeLayout);
        textframeLayout.setHorizontalGroup(
            textframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textframeLayout.createSequentialGroup()
                .addGroup(textframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(textframeLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(textok)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        textframeLayout.setVerticalGroup(
            textframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textframeLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textok)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        copy.setText("Item");
        copy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                copyMouseReleased(evt);
            }
        });
        copycut.add(copy);

        paste.setText("Item");
        paste.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pasteMouseReleased(evt);
            }
        });
        copycut.add(paste);

        setBackground(new java.awt.Color(255, 255, 255));
        setAutoscrolls(true);
        setMaximumSize(null);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
// TODO add your handling code here:
    this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
}//GEN-LAST:event_formMouseEntered

    @SuppressWarnings("unchecked")
private void textokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textokMouseClicked
// When the ok button of the text dialog box is clicked:
    textframe.setVisible(false);
    System.out.println("2nd func");
    String txt = textenterhere.getText();
    Color col = textcolor.getBackground();
    String fs = (String) fontName.getSelectedItem();
    fontsize = fontSize.getValue();
    fontStyle = fstyle[fontStyl.getSelectedIndex()];
    f = new Font(fs, fontStyle, fontsize);
    txt = txt.trim();
    if (shapeInfoPassed != null) {
        //if some shape button is pressed in the mainGUI
        if (flagtext == 1) {
            shapeInfoPassed.setText(txt); //set the text received from the dialog box
            shapeInfoPassed.setColor(col);
            System.out.println("added 1 shape");
            latest = shapeInfoPassed; //mark this shapeinfo as the latest shape
            shapeList.add(shapeInfoPassed); //add the shapeinfo to the vector
            textenterhere.setText("");
            flagtext = 0; //returns the flag to the original condition
            repaint();
       }
    }
}//GEN-LAST:event_textokMouseClicked

    @SuppressWarnings("unchecked")
private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
// When the mouse is dragged in the paintarea:
    double xcod = evt.getX();
    double ycod = evt.getY();
    shapeInfo sh = new shapeInfo();
    boolean withInBound = false;
    int i;
    if (xcod < x) {
        x_1 = xcod;
    }
    if (ycod < y) {
        y_1 = ycod;
    }
    poi.x = (int) x_1;
    poi.y = (int) y_1;
    width = Math.abs(xcod - x);
    height = Math.abs(ycod - y);
    if (shapeInfoPassed != null) {
        //if some shape button is pressed in the mainGUI
        shapeInfo she = new shapeInfo(); //create a new shapeinfo object
        she.copyShape(shapeInfoPassed); //copy the shapeinfopassed to new shapeinfo
        shapeList.remove(shapeList.size() - 1);
        if (shapeInfoPassed.shapeType.equals("Rectangle") || shapeInfoPassed.shapeType.equals("FilledRectangle")) {
            she.shape = new Rectangle2D.Double(x_1, y_1, width, height);
            she.setHeight(height);
            she.setWidth(width);
        } else if (shapeInfoPassed.shapeType.equals("Freehand")) {
            polyline.lineTo(xcod, ycod);
            she.gp = polyline;
        } else if (shapeInfoPassed.shapeType.equals("Circle") || shapeInfoPassed.shapeType.equals("FilledCircle")) {
            she.shape = new Ellipse2D.Double(x_1, y_1, width, height);
            she.setHeight(height);
            she.setWidth(width);
            //she.setCornerPoint(poi);
        } else if (shapeInfoPassed.shapeType.equals("Line")) {
            //BasicStroke bs=new BasicStroke(shapeInfoPassed.strWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
            she.setLineParm(x, y, xcod, ycod);
            she.shape = new Line2D.Double(she.start_x, she.start_y, she.end_x, she.end_y);
        } //end of line
        else if(shapeInfoPassed.shapeType.equalsIgnoreCase("eraser") || shapeInfoPassed.shapeType.equals(null)){
            System.out.println("test");
        }
        shapeList.add(she); //add she to the shapelist
        latest = she; //sets she as the latest shapeinfo
        repaint();
    } //end of if shapeinfo passed!=null
}//GEN-LAST:event_formMouseDragged

    @SuppressWarnings(value = "unchecked")
private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
// TODO add your handling code here:
        int i;
        shapeInfo sh;
        prev_x = evt.getX();
        prev_y = evt.getY();
        Point pointCorner;
        pointCorner = evt.getPoint();
        x = evt.getX();
        y = evt.getY();
        width = evt.getX() - x;
        height = evt.getY() - y;
        x_1 = x;
        y_1 = y;
        if (selector == false) {
            shapeInfo dsh;
            if (deleter == true) {
                for (i = shapeList.size() - 1; i > -1; i--) {
                    dsh = (shapeInfo) shapeList.get(i);
                    if(dsh.shapeType.equalsIgnoreCase("text")||dsh.shapeType.equalsIgnoreCase("Freehand")) { 
                                     System.out.println("error"); 
                    }
                    else if (dsh.containPoint(x, y, "mousePressed")) {
                         if(deleteOnce) {  
                             deleteOnce=false;
                             shapeList.removeElementAt(i);
                             System.out.println("shape removed");
                         }   
                     }   
                }  
                deleteOnce=true;
                deleter = false;
                repaint();
            }        //end of if selector false
            if (shapeInfoPassed != null) {
                //if some shape button selected in the mainGUI
                if (shapeInfoPassed.shapeType.equals("Rectangle") || shapeInfoPassed.shapeType.equals("FilledRectangle")) {
                    shapeInfoPassed.shape = new Rectangle2D.Double(x, y, width, height); //add a shape to the shapeinfo class
                    shapeInfoPassed.setCornerPoint(pointCorner);
                    shapeList.add(shapeInfoPassed); //add the shapeinfo to the vector
                    latest = shapeInfoPassed; //mark this shapeinfo as the latest shape
                } else if (shapeInfoPassed.shapeType.equals("Freehand")) {
                    polyline = new GeneralPath();
                    polyline.moveTo(x, y);
                    polyline.lineTo(x, y);
                    shapeInfoPassed.gp = polyline;
                    shapeInfoPassed.setCornerPoint(pointCorner);
                    shapeList.add(shapeInfoPassed);
                    latest = shapeInfoPassed;
                } else if (shapeInfoPassed.shapeType.equalsIgnoreCase("Text")) {
                    if (flagtext == 0) {
                        flagtext = 1;
                        shapeInfoPassed.setPoint(evt.getPoint());
                        textframe.setLocation(evt.getXOnScreen(), evt.getYOnScreen());
                        textframe.setVisible(true);
                        textframe.setTitle("Type Your Text Here");
                   //     System.out.println("into the add");
                    }
                    shapeInfoPassed.setCornerPoint(pointCorner);
                    shapeList.add(shapeInfoPassed); //add the shapeinfo to the vector
                  //  System.out.println("text added 2 shapelist");
                    latest = shapeInfoPassed; //mark this shapeinfo as the latest shape
                    sendPacket();
                    //  repaint();
                } else if (shapeInfoPassed.shapeType.equals("Circle") || shapeInfoPassed.shapeType.equals("FilledCircle")) {
                    shapeInfoPassed.shape = new Ellipse2D.Double((int) x_1, (int) y_1, (int) width, (int) height); //add a shape to the shapeinfo class
                    shapeInfoPassed.setCornerPoint(pointCorner);
                    shapeList.add(shapeInfoPassed); //add the shapeinfo to the vector
                    latest = shapeInfoPassed; //mark this shapeinfo as the latest shape
                } else if (shapeInfoPassed.shapeType.equals("Line")) {
                    shapeInfoPassed.setLineParm(x, y, x, y);
                    shapeInfoPassed.shape = new Line2D.Double(shapeInfoPassed.start_x, shapeInfoPassed.start_y, shapeInfoPassed.end_x, shapeInfoPassed.end_y);
                    shapeInfoPassed.setCornerPoint(pointCorner);
                    shapeList.add(shapeInfoPassed);
                    latest = shapeInfoPassed;
                } else if (shapeInfoPassed.shapeType.equals("Selector")) {
                } //end of if line
                else if(shapeInfoPassed.shapeType.equalsIgnoreCase("eraser")|| shapeInfoPassed.shapeType.equals(null) ){
                  System.out.println("test");
                 }
            }
               else if(shapeInfoPassed.shapeType.equalsIgnoreCase("eraser") || shapeInfoPassed.shapeType.equals(null)){
            System.out.println("test");
        }
        } else if (selector == true) {
            selector = false;
            drawOutLine = true;
            for (i = shapeList.size() - 1; i > -1; i--) {
                sh = (shapeInfo) shapeList.get(i);
                if (sh.containPoint(x, y, "mousePressed")) {
                  //  System.out.println("inside the shape");
                    Double dh = sh.getHeight() + 10;
                    Double dw = sh.getWidth() + 10;
                    //  System.out.println(dh);
                    //   System.out.println(dw);
                    Point tp = sh.getCornerPoint(); // getcorner point function not working
                    System.out.println(tp);
                    double tx = tp.getX() - 5;
                    double ty = tp.getY() - 5;
                    //rect=new Rectangle2D.Double(x,y,dw,dh);
                    rect = new Rectangle2D.Double(tx, ty, dw, dh);
                    String display = sh.shapeType;
                    System.out.println("yes the point is contained " + display);
                    break;
                }
            }
            
            repaint();
        }
}//GEN-LAST:event_formMousePressed

private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
// TODO add your handling code here:
    x_1 = 0.0;
   
    y_1 = 0.0;
    x = evt.getX();
    y = evt.getY();
    xp = evt.getX();
    yp = evt.getY();
    if(shapeInfoPassed.shapeType.equalsIgnoreCase("eraser")){
        reducePacket();
    }
    else{
        sendPacket();
    }

}//GEN-LAST:event_formMouseReleased

private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
     int i;//GEN-LAST:event_formMouseClicked
    if (evt.getButton() != MouseEvent.BUTTON1) {
        if (past == true) {
            copy.setEnabled(false);
            copy.setText("Copy");
            paste.setText("Paste");
            paste.setEnabled(true);
            copycut.show(this, evt.getX(), evt.getY());
            past=false;
            acopy=true;
        } else if (acopy) {
           shapeInfo sh;
            for (i = shapeList.size() - 1; i > -1; i--) {
                sh = (shapeInfo) shapeList.get(i);
                if (sh.containPoint(x, y, "mousePressed")) {
                    copy.setEnabled(true);
                    copy.setText("Copy");
                    paste.setText("Paste");
                    paste.setEnabled(false);
                    copyShape=sh.getShape();
                    System.out.println("hello"+copyShape);
                    copyPaste.copyShape(sh);
                    copycut.show(this, evt.getX(), evt.getY());
                    System.out.println("shape copied");
                  acopy=false;
                  past=true;
            }
        }}
    }
}                                 

private void fontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontNameActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_fontNameActionPerformed

private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
    // TODO add your handling code here:
}//GEN-LAST:event_formMouseMoved

private void copyMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_copyMouseReleased
    // After the copy menu is pressed only paste item will be displayed
    paste.setEnabled(true);
    past = true;
    acopy=false;
}//GEN-LAST:event_copyMouseReleased

    @SuppressWarnings("unchecked")
private void pasteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasteMouseReleased
    // After the paste menu is pressed only paste item will not be displayed:
    past = false;
    acopy=true;
    Double cph = copyPaste.getHeight();
    Double cpw = copyPaste.getWidth();
    if(copyShape.toString().startsWith("java.awt.geom.Rectangle2D")){
        Rectangle2D rec = new Rectangle2D.Double(xp,yp,cpw,cph);
        copyPaste.setShape(rec);
    }
    else {
        Ellipse2D ellipse = new Ellipse2D.Double(xp,yp,cpw,cph);
        copyPaste.setShape(ellipse);
    }
    shapeList.add(copyPaste); //add copyPaste to the shapelist
    latest=null;
    repaint(); 
}//GEN-LAST:event_pasteMouseReleased

/**
     * Remote board Server method
     * Clients call this method when it wants to connect to the remote board server
     * @return status
     * @throws java.rmi.RemoteException exception
     */

    /**
     * sets the remote board server interface reference
     * @param t_server remote server
     */
     public void setServer(ServerBoardInterface t_server) {
    	this.server = t_server;
    }
     
    public String test(String cname) throws RemoteException {
        return ("this is client responding");
    }
    
     /**
     * Remote client method.
     * board server calls this method to send the ShapeInfo Object to the client
     * @param item ShapeInfo object received from board server
     * @throws java.rmi.RemoteException exception
     */
    @SuppressWarnings("unchecked")
     public void appendChartItem(shapeInfo item) throws RemoteException  {
        this.shapeList.add(item);   
        this.repaint();
     }    
    
     public void sendPacket() {
        try {
            server.Broadcast_AddShape(latest);
            System.out.print("send to server");    
            latest=null;
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            System.out.print("not send to server");
        }            
}
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copy;
    private javax.swing.JPopupMenu copycut;
    private javax.swing.JComboBox fontName;
    private javax.swing.JSlider fontSize;
    private javax.swing.JComboBox fontStyl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem paste;
    private javax.swing.JPanel textcolor;
    private javax.swing.JPanel textenter;
    private javax.swing.JTextField textenterhere;
    private javax.swing.JDialog textframe;
    private javax.swing.JButton textok;
    // End of variables declaration//GEN-END:variables
     
}
