/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Frame1.java
 *
 * Created on 2010-05-30, 12:00:37
 */

package graf;
import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import java.awt.event.*;
/**
 *
 * @author Klauduœ
 */
public class Frame1 extends javax.swing.JFrame {
    //private Object frame;

    /** Creates new form Frame1 */
    public Frame1() {
        initComponents();
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        clear = new javax.swing.JButton();
        connect = new javax.swing.JButton();
        serverport = new javax.swing.JTextField();
        login = new javax.swing.JTextField();
        server = new javax.swing.JTextField();
        userport = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        exit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 255));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("LOGIN");

        jLabel2.setText("PASSWORD");

        jLabel3.setText("SERVER");

        jLabel4.setText("USER PORT");

        jLabel5.setText("SERVER PORT");

        clear.setText("CLEAR");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        connect.setText("CONNECTED");
        connect.setActionCommand("CONNECT");
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });

        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(323, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(userport, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(240, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(password)
                                .addComponent(login, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(63, 63, 63)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(connect)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(clear)))
                            .addComponent(serverport, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(server, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(server, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(clear)
                            .addComponent(connect))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(userport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        exit.setText("EXIT");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(348, Short.MAX_VALUE)
                .addComponent(exit)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        






    private void connectActionPerformed(java.awt.event.ActionEvent evt) {                                        

         try{
            login1= this.login.getText();
         /**
            if (login1.isEmpty())
            {
                 JOptionPane.showMessageDialog(this, "wprowadz login", "error", JOptionPane.ERROR_MESSAGE);
            }
            */
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "bledny login", "error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        try{
            password1 = this.password.getText();

          /**    if (password1.isEmpty())
            {
                 JOptionPane.showMessageDialog(this, "wprowadz haslo", "error", JOptionPane.ERROR_MESSAGE);
            }
*/

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "bledne haslo", "error", JOptionPane.ERROR_MESSAGE);
            return;

        }
        try{
            server1= this.server.getText();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "bledny serwer", "error", JOptionPane.ERROR_MESSAGE);
            return;

        }
        try{

            userport1 = this.userport.getText();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this.userport , "bledny port uzytkownika", "error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        try{

            serverport1 = this.serverport.getText();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this.userport , "bledny port uzytkownika", "error", JOptionPane.ERROR_MESSAGE);
            return;
        }



         /*

         if (login1.isEmpty()){
            JOptionPane.showMessageDialog(this.userport, "wprowadz login", "error", JOptionPane.ERROR_MESSAGE);

         }else{
             dispose();
             Grk2 frame2= new Grk2();
        // new Thread(new thread1()).start();

             frame2.setVisible(true);
            
            
         //new Thread(new thread1()).start();
            // connect.addActionListener(new connectAction.actionPerformed());
             //Frame1.frame.EXIT_ON_CLOSE;
         }
*/


//         if(  login1.contentEquals("") || password1.contentEquals("")|| userport1.contentEquals("")|| serverport1.contentEquals("") ||  server1.contentEquals(""))
  if(  login1.isEmpty() ||  password1.isEmpty() || (serverport1.isEmpty()) || (userport1.isEmpty()) || (server1.isEmpty()) )
         {

         JOptionPane.showMessageDialog(this.userport, "wprowadz wszystkie dane", "error", JOptionPane.ERROR_MESSAGE);
         }
         else{
           dispose();
                  Grk2 frame2 = new Grk2();
                   frame2.setVisible(true);

         }




         //  if(  !(login1.isEmpty()) &&  !(password1.isEmpty()) && !(serverport1.isEmpty())
        //         && !(userport1.isEmpty()) && !(server1.isEmpty()) ){
//JOptionPane.showMessageDialog(this.userport, "wprowadz wszystkie dane", "error", JOptionPane.ERROR_MESSAGE);

    //     if( (login1.isEmpty()) || (password1.isEmpty()) ||(serverport1.isEmpty())
     //            ||(userport1.isEmpty()) ||(server1.isEmpty()) ){

 
        //JOptionPane.showMessageDialog(this.userport, "wprowadz wszystkie dane", "error", JOptionPane.ERROR_MESSAGE);
       // }


      //   else {
      //        Grk2 frame2 = new Grk2();
      //  frame2.setVisible(true);
      //  }









        String spr,spr2, spr3, spr4, spr5;
        spr = login1;
        spr2 = password1;
        spr3 = serverport1;
        spr4 = userport1;
        spr5 = server1;

        System.out.print("login "+spr + "haslo "+spr2 +spr3+spr4 +"    "+ spr5);


                        //new Thread(new thread1()).start(); //Start the thread

    }

        //The thread
        public static class thread1 implements Runnable{
                public void run(){

                    JProgressBar pB = new JProgressBar();


                    for (int i=0; i<=100; i++){ //Progressively increment variable i
                                pB.setValue(i); //Set value
                                pB.repaint(); //Refresh graphics
                                try{Thread.sleep(50);} //Sleep 50 milliseconds
                                catch (InterruptedException err){}
                        }
                }





    }                                       




    private void loginActionPerformed(java.awt.event.ActionEvent evt) {                                      






    }                                     

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {                                     
    System.exit(0);
    }                                    

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {                                      

      login.setText("");
    password.setText("");
    server.setText("");
    userport.setText("");
    //serverport.setComponentOrientation(ComponentOrientation.UNKNOWN);
     serverport.setText("");




    }                                     

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                 try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
                catch (Exception e) {}
              //  new Frame1().setVisible(true);
                JProgressBar pB = new JProgressBar();
                 new Thread(new thread1()).start();

            }
        });
    }


      public String getLogin()
    {
    	return login1;
    }

    public String getPassword()
    {
    	return password1;
    }

    public String getUserport()
    {
    	return userport1;
    }

    public String getServerport()
    {
    	return serverport1;
    }


  public String getServer()
  {
	return server1;
  }







   private String login1;
   private String password1;
   private String userport1;
   private String serverport1;
   private String server1;





    // Variables declaration - do not modify                     
    private javax.swing.JButton clear;
    private javax.swing.JButton connect;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField login;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField server;
    private javax.swing.JTextField serverport;
    private javax.swing.JTextField userport;
    // End of variables declaration                   

}
