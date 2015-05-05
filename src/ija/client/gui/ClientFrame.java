/* file name  : ClientFrame.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import ija.client.*;
import ija.server.board.MazeBoard;
import ija.shared.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;


public class ClientFrame extends JFrame{

    private int n;
    private JPanel frameContents;
    private JButton newGame;
    private JButton refresh;
    private JList lobbyPlayers;
    private JDialog newGameDialog;
    private JDialog challDialog;
    private JDialog challFailDialog;
    private Client connect;
    private static ClientFrame instance; //singleton!

    private ClientFrame() {
        this.n = 7;
        this.frameContents = new JPanel();
        this.frameContents.setLayout(new BorderLayout());
        this.connect = null;
        
        this.init();
    }
    
    public static ClientFrame getInstance() {
        if(instance == null)
            instance = new ClientFrame();
        return instance;
    }

    private void init() {

        setLayout(new BorderLayout());
        setTitle("IJA - Labyrint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(800, 600));
        add(frameContents);

        //Jmeno hry
        JLabel name = new JLabel("LABYRINTHIAN", SwingConstants.CENTER);
        name.setPreferredSize(new Dimension(this.getWidth(), 100));
        frameContents.add(name, BorderLayout.NORTH);

        this.lobbyPlayers = new JList();
        this.lobbyPlayers.setSelectionModel(new DefaultListSelectionModel(){
            public void setSelectionInterval(int index0,int index1){
                if(index1-index0>= 3)
                    index1=index0+2;
                super.setSelectionInterval(index0, index1);
            }
            public void addSelectionInterval(int index0,int index1){
                int selLen = lobbyPlayers.getSelectedIndices().length;
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
        frameContents.add(lobbyPlayers);
        
        //Tlacitko obnoveni seznamu hracu
        refresh = new JButton("OBNOVIT");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(true,DataUnit.MsgID.C_UPDLOBBY));
            }
        });
        frameContents.add(refresh, BorderLayout.EAST);
        
        //Tlacitko zacit hru
        newGame = new JButton("VYZVAT HRACE A HRAT");
        newGame.setPreferredSize( new Dimension(this.getWidth(), 50));
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList <Integer> selected = new ArrayList <>();
                for(Object o : lobbyPlayers.getSelectedValuesList()) {
                    selected.add(((LobbyPlayer) o).getID());
                }
                //createDialog();
                connect.send(new DataUnit(selected,DataUnit.MsgID.C_CHALLPL));
            }
        });
        frameContents.add(newGame, BorderLayout.SOUTH);
        connect = new Client();
        //setVisible(true);
        
    }
    
    public void setNGButton(boolean b) {
        newGame.setEnabled(b);
    }
    
    public void updateLobby(ArrayList<Integer> inLobby) {
        this.lobbyPlayers.setVisible(false);
        DefaultListModel listModel = new DefaultListModel();
        for(int i = 0; i < inLobby.size(); i++) {
            listModel.addElement(new LobbyPlayer(inLobby.get(i)));
        }
        this.lobbyPlayers.setModel(listModel);
        this.lobbyPlayers.setVisible(true);
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
        newGameDialog.setBounds(800, 300, 100, 100);
        //TODO Sprav to, zmenit velikosti oken a vycentrovat, vscka stejnou velikost
        newGameDialog.pack();
    }

    public void showFrame() {
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(0);
        colours.add(1);
        colours.add(2);
        colours.add(3);
        MazeBoard gameBoard = MazeBoard.createMazeBoard(5, 24, 4, colours);
        gameBoard.newGame();
        GridPanel p = new GridPanel(this, 5, gameBoard);
        JFrame karel = new JFrame();
        karel.setTitle("IJA - Labyrint");
        karel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        karel.setPreferredSize(new Dimension(800, 600));
        karel.setMinimumSize(new Dimension(800, 600));
        karel.add(p);
        karel.setVisible(true);
        System.out.println("kareeeeeeeeeeeeeeel");
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
