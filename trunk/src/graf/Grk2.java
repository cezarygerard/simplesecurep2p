/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame.java
 *
 * Created on 2010-05-30, 11:38:44
 */

package graf;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Klaudus
 */
public class Grk2 extends javax.swing.JFrame {

    /** Creates new form NewJFrame */
    public Grk2() {
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
                catch (Exception e) {}
        initComponents();
        progBar = new JProgressBar(0, 100);
        jPanel2.add(progBar);
        progBar.setBounds(70, 10, 280, 20);
        new Thread(new thread1()).start();

        /*for (int i=0; i<=100; i++){ //Progressively increment variable i
        progBar.setValue(i); //Set value
        progBar.repaint();
        }*/
    }

    public static class thread1 implements Runnable{
                public void run(){
                    while(true){
                        for (int i=0; i<=100; i++){ //Progressively increment variable i
                                progBar.setValue(i); //Set value
                                progBar.repaint(); //Refresh graphics
                                try{Thread.sleep(50);} //Sleep 50 milliseconds
                                catch (InterruptedException err){}
                                if (i == 100) i =0;
                        }
                    }
                }
        }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cancel = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jFileChooser1 = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setText("CONNECTING");

        cancel.setText("CANCEL");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(jLabel1)
                .addContainerGap(164, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(337, Short.MAX_VALUE)
                .addComponent(cancel)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(cancel))
        );

        exit.setText("EXIT");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(exit)
                        .addGap(22, 22, 22))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exit)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {                                     
     System.exit(0);
    }                                    

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {                                       
      dispose();
        Frame1 frame1 = new Frame1();
         frame1.setVisible(true);

    //Grk2 prog = new Grk2();
    //prog.setVisible(true);

    }                                      

    
//   static JFrame panel = new Frame();

 //JProgressBar bar = new JProgressBar();
    //
// panel.add(bar);

  //  bar.setBounds(10, 10, 280, 20);
   // bar.addAncestorListener(new Function());
    //add(bar);




    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Grk2().setVisible(true);
            }
        });
    }





    /*
    //new variables needed to progressbar
    private TimerTask task;
    private int fr = 0;
    private Timer tempo;
    private int speed = 1000;
    
    // new metods
    private void startAnimation(final JProgressBar b){
    tempo = new Timer();
    task = new TimerTask() {
public void run(){
   // fr = Math.round
   //
            }
        };
    
    }
    
    }
    
    private void stopAnimation(final JProgressBar b){
    tempo.cancel();
    task.cancel();
    setValue(0);

    }
    
    
    */
    
    
    
    
    
    
 
    static JProgressBar progBar;
    // Variables declaration - do not modify
    private javax.swing.JButton cancel;
    private javax.swing.JButton exit;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration

}
