/* file name  : ClientFrame.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import ija.client.*;
import ija.shared.board.MazeBoard;
import ija.shared.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;


public class ClientFrame extends JFrame{

    private JPanel lobbyPane;
    private JPanel gamePane;
    private JPanel MAINPane;
    private CardLayout cardLayout = new CardLayout();
    
    private JButton newGameButton;
    private JButton refreshButton;
    private JList lobbyPlayersList;
    
    private JDialog newGameDialog;
    private JDialog challDialog;
    private JDialog challFailDialog;
    
    private Client connect;
    private static ClientFrame instance; //singleton!

    private ClientFrame() {
        this.connect = null;
        this.init();
    }
    
    public static ClientFrame getInstance() {
        if(instance == null)
            instance = new ClientFrame();
        return instance;
    }

    private void init() {
        this.MAINPane = new JPanel();
        this.MAINPane.setLayout(new BorderLayout());

        this.lobbyPane = new JPanel();
        this.lobbyPane.setLayout(new BorderLayout());
        
        this.gamePane = new JPanel();
        this.gamePane.setLayout(new BorderLayout());
        
        MAINPane.setLayout(this.cardLayout);
        setTitle("IJA - Labyrint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(800, 600));
        MAINPane.add(lobbyPane,"lobby");
        MAINPane.add(gamePane,"game");

        //Jmeno hry
        JLabel name = new JLabel("LABYRINTUSUS", SwingConstants.CENTER);
        name.setPreferredSize(new Dimension(this.getWidth(), 100));
        lobbyPane.add(name, BorderLayout.NORTH);

        this.lobbyPlayersList = new JList();
        this.lobbyPlayersList.setSelectionModel(new DefaultListSelectionModel(){
            public void setSelectionInterval(int index0,int index1){
                if(index1-index0>= 3)
                    index1=index0+2;
                super.setSelectionInterval(index0, index1);
            }
            public void addSelectionInterval(int index0,int index1){
                int selLen = lobbyPlayersList.getSelectedIndices().length;
                if(selLen >= 3) //osetreni vybrani maximalne tri hracu
                    return; 
                if(index1-index0 >= 3 -selLen)
                    index1=index0+2-selLen;
                if(index1 < index0)
                    return;
                super.addSelectionInterval(index0, index1);  
            }   
        });
        //Seznam hracu
        lobbyPane.add(lobbyPlayersList);
        
        //Tlacitko obnoveni seznamu hracu
        refreshButton = new JButton("OBNOVIT");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(true,DataUnit.MsgID.C_UPDLOBBY));
            }
        });
        lobbyPane.add(refreshButton, BorderLayout.EAST);
        
        //Tlacitko zacit hru
        newGameButton = new JButton("VYZVAT HRACE A HRAT");
        newGameButton.setPreferredSize( new Dimension(this.getWidth(), 50));
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList <Integer> selected = new ArrayList <>();
                for(Object o : lobbyPlayersList.getSelectedValuesList()) {
                    selected.add(((LobbyPlayer) o).getID());
                }
                //createDialog();
                connect.send(new DataUnit(selected,DataUnit.MsgID.C_CHALLPL));
            }
        });
        lobbyPane.add(newGameButton, BorderLayout.SOUTH);
        connect = new Client();
        
        add(MAINPane);
        this.cardLayout.show(MAINPane,"lobby");
    }
    
    public void setNGButton(boolean b) {
        newGameButton.setEnabled(b);
    }
    
    public void updateLobby(ArrayList<Integer> inLobby) {
        this.lobbyPlayersList.setVisible(false);
        DefaultListModel listModel = new DefaultListModel();
        for(int i = 0; i < inLobby.size(); i++) {
            listModel.addElement(new LobbyPlayer(inLobby.get(i)));
        }
        this.lobbyPlayersList.setModel(listModel);
        this.lobbyPlayersList.setVisible(true);
    }
    
    public void showChallDialog() {
        JButton yesButton = new JButton("Ano");
        JButton noButton = new JButton("Ne");
        this.challDialog = new JDialog(this);
        JLabel label = new JLabel("Jsi vyzvan ke hre, prijimas?");
        challDialog.setBounds(200, 300, 100, 100);
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(true,DataUnit.MsgID.C_RESP_CHALLPL));
                challDialog.dispose();
            }
        });
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(false,DataUnit.MsgID.C_RESP_CHALLPL));
                challDialog.dispose();
            }
        });
        challDialog.add(label);
        challDialog.add(yesButton);
        challDialog.add(noButton);

        challDialog.setLocationRelativeTo(this);
        challDialog.setVisible(true);
        challDialog.setModal(true);
        challDialog.setLayout(new GridLayout(3, 0, 10, 10));
        JPanel pane = (JPanel) challDialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        challDialog.pack();
    }
    
    public void challFailDialog() {
        this.challFailDialog = new JDialog(this);
        JLabel label = new JLabel("Vyzva selhala.");
        JButton okButton = new JButton("OK");
        challFailDialog.setBounds(200, 300, 100, 100);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(true,DataUnit.MsgID.C_OK_LOBBY));
                challFailDialog.dispose();
            }
        });
        
        challFailDialog.add(label);
        challFailDialog.add(okButton);
        
        challFailDialog.setLocationRelativeTo(this);
        challFailDialog.setVisible(true);
        challFailDialog.setModal(true);
        challFailDialog.setLayout(new GridLayout(2, 0, 10, 10));
        JPanel pane = (JPanel) challFailDialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        challFailDialog.pack();       
    }

    public void chooseGDialog() {
        //if(this.newGameDialog != null)
            //return;
        JButton ngButton = new JButton("NOVA HRA");
        JButton sgButton = new JButton("ULOZENA HRA");
        this.newGameDialog = new JDialog(this);

        //this.newGameDialog.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        newGameDialog.setBounds(200, 300, 100, 100);
        ngButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGameDialog.dispose();
                createNGDialog();
            }
        });
        sgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(new int [] {-1,-1},DataUnit.MsgID.C_CHOSENG));
                newGameDialog.dispose();
                //createGDialog();
            }
        });

        newGameDialog.add(ngButton);
        newGameDialog.add(sgButton);

        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setVisible(true);
        newGameDialog.setModal(true);
        newGameDialog.setLayout(new GridLayout(2, 0, 10, 10));
        JPanel pane = (JPanel) newGameDialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        newGameDialog.pack();
    }

    private void createNGDialog() {

        JButton confirmButton = new JButton("POTVRDIT");
        String[] treasureNumber = {"12", "24"};
        JLabel treasureLabel = new JLabel("POCET POKLADU");
        final JComboBox<String> treasureCB = new JComboBox<>(treasureNumber);
        JLabel sizeLabel = new JLabel("VELIKOST HRANY DESKY");
        String [] edgeNumber = {"5*5","6*6","7*7","8*8","9*9","10*10","11*11"};
        final JComboBox<String> edgeCB = new JComboBox<>(edgeNumber);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = 0,k = 0;
                k = (treasureCB.getSelectedIndex() * 12) + 12;
                n = edgeCB.getSelectedIndex() + 5;
                System.out.println("n: " + n + ", k: " + k);
                connect.send(new DataUnit(new int [] {n,k},DataUnit.MsgID.C_CHOSENG));
                newGameDialog.dispose();
                //createGDialog();
            }
        });

        this.newGameDialog = new JDialog(this);

        newGameDialog.add(treasureLabel);
        newGameDialog.add(treasureCB);
        newGameDialog.add(sizeLabel);
        newGameDialog.add(edgeCB);
        newGameDialog.add(confirmButton);

        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setVisible(true);
        newGameDialog.setModal(true);
        newGameDialog.setLayout(new GridLayout(3, 0, 10, 10));
        JPanel pane = (JPanel) newGameDialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        newGameDialog.pack();
    }
    
    
    public void showGame(MazeBoard g) {
        
        GridPanel maze = new GridPanel(this, g.getSize(), g);
        //JFrame newWindow = new JFrame();
        /*newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newWindow.setPreferredSize(new Dimension(800, 600));
        newWindow.add(panel);
        newWindow.setVisible(true);*/
        gamePane.removeAll();
        gamePane.add(maze);
        this.cardLayout.show(MAINPane, "game");
    }
    
    
    
    public static void main(String[] args) {
        ClientFrame window = ClientFrame.getInstance();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ClientFrame.getInstance().setVisible(true);
            }
        });
    }
}
