/* file name  : ClientFrame.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import ija.client.*;
import ija.shared.board.MazeBoard;
import ija.shared.*;
import ija.shared.player.Player;

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
    private JPanel sidePane;
    private JPanel freeStonePane;
    private CardLayout cardLayout = new CardLayout();
    
    private JButton newGameButton;
    private JButton refreshButton;
    private JList lobbyPlayersList;
    private GridTile freeStoneTile;
    private GridPanel maze;
    private JTextArea console;
    
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

    public Client getConnect() {
        return connect;
    }
    
    public class SelectedListCellRenderer extends DefaultListCellRenderer {
     @Override
     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         if (isSelected) {
             c.setBackground(new Color(0xFFC373));
             c.setFont(new Font("Verdana", Font.BOLD, 15));
             //c.setForeground(new Color(0x25567B));
         }
         return c;
     }
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
        //setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(800, 600));
        MAINPane.add(lobbyPane,"lobby");
        MAINPane.add(gamePane,"game");

        //Jmeno hry
        JLabel name = new JLabel("LABYRINT", SwingConstants.CENTER);
        name.setFont(new Font("Verdana", Font.PLAIN, 24));
        name.setForeground(new Color(0xFFC373)); //yellow
        name.setPreferredSize(new Dimension(this.getWidth(), 100));
        lobbyPane.add(name, BorderLayout.NORTH);

        this.lobbyPlayersList = new JList();
        lobbyPlayersList.setCellRenderer(new SelectedListCellRenderer());
        lobbyPlayersList.setBackground(Color.WHITE);
        lobbyPlayersList.setFont(new Font("Verdana", Font.PLAIN, 15));
        lobbyPlayersList.setForeground(new Color(0x25567B));
        lobbyPlayersList.setSelectionModel(new DefaultListSelectionModel(){
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if(index1-index0>= 3)
                    index1=index0+2;
                super.setSelectionInterval(index0, index1);
            }
            @Override
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
        this.refreshButton = new JButton("OBNOVIT");
        refreshButton.setFont(new Font("Verdana", Font.BOLD, 16));
        if (refreshButton.isEnabled()){
            refreshButton.setBackground(new Color(0x25567B)); //blue
            refreshButton.setForeground(new Color(0xFFC373)); //yellow
        }
        else {
            refreshButton.setBackground(new Color(0x96ADC2));
            refreshButton.setForeground(Color.DARK_GRAY);
        }
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(true,DataUnit.MsgID.C_UPDLOBBY));
            }
        });
        lobbyPane.add(refreshButton, BorderLayout.EAST);
        
        //Tlacitko zacit hru
        newGameButton = new JButton("VYZVAT HRÁČE A HRÁT");
        newGameButton.setPreferredSize( new Dimension(this.getWidth(), 50));
        newGameButton.setFont(new Font("Verdana", Font.BOLD, 16));
        if (newGameButton.isEnabled()){
            newGameButton.setBackground(new Color(0x25567B)); //blue
            newGameButton.setForeground(new Color(0xFFC373)); //yellow
        }
        else {
            newGameButton.setBackground(new Color(0x96ADC2));
            newGameButton.setForeground(Color.DARK_GRAY);
        }
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lobbyPlayersList.getSelectedIndices().length <=0)
                    return;
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
        
        lobbyPane.setBackground(Color.GRAY);
        
        add(MAINPane);
        this.cardLayout.show(MAINPane,"lobby");
    }
   
    public void setNGButton(boolean b) {
        newGameButton.setEnabled(b);
        if (newGameButton.isEnabled()){
            newGameButton.setBackground(new Color(0x25567B)); //blue
            newGameButton.setForeground(new Color(0xFFC373)); //yellow
        }
        else {
            newGameButton.setBackground(new Color(0x96ADC2)); //light blue
            newGameButton.setForeground(Color.DARK_GRAY);
        }
    }

    public void setLobbyButtons(boolean b) {
        refreshButton.setEnabled(b);
        if (refreshButton.isEnabled()){
            refreshButton.setBackground(new Color(0x25567B)); //blue
            refreshButton.setForeground(new Color(0xFFC373)); //yellow
        }
        else {
            refreshButton.setBackground(new Color(0x96ADC2));
            refreshButton.setForeground(Color.DARK_GRAY);
        }
        lobbyPlayersList.setEnabled(b);
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
        setNGButton(false);
        setLobbyButtons(false);
        JButton yesButton = new JButton("ANO");
        JButton noButton = new JButton("NE");
        yesButton.setFont(new Font("Verdana", Font.PLAIN, 15));
        yesButton.setBackground(new Color(0x25567B)); //blue
        yesButton.setForeground(new Color(0xFFC373)); //yellow
        noButton.setFont(new Font("Verdana", Font.PLAIN, 15));
        noButton.setBackground(new Color(0x25567B)); //blue
        noButton.setForeground(new Color(0xFFC373)); //yellow
        this.challDialog = new JDialog(this);
        challDialog.getContentPane().setBackground(Color.GRAY);
        
        JLabel label = new JLabel("Jsi vyzván ke hře, přijímáš?");
        label.setFont(new Font("Verdana", Font.BOLD, 15));
        label.setForeground(Color.WHITE); 
        challDialog.setBounds(300, 400, 100, 100);
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
        challDialog.setModal(true);
        challDialog.setLayout(new GridLayout(3, 0, 10, 10));
        JPanel pane = (JPanel) challDialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        challDialog.pack();
        challDialog.setLocationRelativeTo(this);
        challDialog.setVisible(true);
    }
    
    public void showChallFailDialog() {
        if(challDialog != null)
            challDialog.dispose();
        this.challFailDialog = new JDialog(this);
        challFailDialog.getContentPane().setBackground(Color.GRAY);
        JLabel label = new JLabel("Výzva selhala.");
        label.setFont(new Font("Verdana", Font.BOLD, 15));
        label.setForeground(Color.WHITE); 
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Verdana", Font.BOLD, 15));
        okButton.setBackground(new Color(0x25567B)); //blue
        okButton.setForeground(new Color(0xFFC373)); //yellow
        challFailDialog.setBounds(300, 400, 100, 100);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect.send(new DataUnit(true,DataUnit.MsgID.C_OK_LOBBY));
                challFailDialog.dispose();
                setNGButton(true);
                setLobbyButtons(true);
            }
        });
        
        challFailDialog.add(label);
        challFailDialog.add(okButton);

        challFailDialog.setModal(true);
        challFailDialog.setLayout(new GridLayout(2, 0, 10, 10));
        JPanel pane = (JPanel) challFailDialog.getContentPane();
        pane.setBorder(new EmptyBorder(0, 60,20 ,60));
        challFailDialog.pack(); 
        challFailDialog.setLocationRelativeTo(this);
        challFailDialog.setVisible(true);
    }

    public void chooseGDialog() {
        JButton ngButton = new JButton("NOVÁ HRA");
        JButton sgButton = new JButton("ULOŽENÁ HRA");
        ngButton.setFont(new Font("Verdana", Font.BOLD, 15));
        ngButton.setBackground(new Color(0x25567B)); //blue
        ngButton.setForeground(new Color(0xFFC373)); //yellow
        sgButton.setFont(new Font("Verdana", Font.BOLD, 15));
        sgButton.setBackground(new Color(0x25567B)); //blue
        sgButton.setForeground(new Color(0xFFC373)); //yellow        
        
        this.newGameDialog = new JDialog(this);
        newGameDialog.getContentPane().setBackground(Color.GRAY);

        //this.newGameDialog.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        newGameDialog.setBounds(300, 400, 100, 100);
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

        newGameDialog.setModal(true);
        newGameDialog.setLayout(new GridLayout(2, 0, 10, 10));
        JPanel pane = (JPanel) newGameDialog.getContentPane();
        pane.setBorder(new EmptyBorder(5, 50,20 ,50));
        newGameDialog.pack();
        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setVisible(true);
    }

    private void createNGDialog() {

        JButton confirmButton = new JButton("POTVRDIT");      
        confirmButton.setFont(new Font("Verdana", Font.BOLD, 15));
        confirmButton.setBackground(new Color(0x25567B)); //blue
        confirmButton.setForeground(new Color(0xFFC373)); //yellow
        
        String[] treasureNumber = {"12", "24"};
        JLabel treasureLabel = new JLabel("POČET POKLADŮ");
        treasureLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        treasureLabel.setForeground(Color.WHITE); 
        final JComboBox<String> treasureCB = new JComboBox<>(treasureNumber);
        
        JLabel sizeLabel = new JLabel("VELIKOST HRANY DESKY");
        sizeLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        sizeLabel.setForeground(Color.WHITE); 
        String [] edgeNumber = {"5*5","6*6","7*7","8*8","9*9","10*10","11*11"};
        final JComboBox<String> edgeCB = new JComboBox<>(edgeNumber);
        
        treasureCB.setFont(new Font("Verdana", Font.BOLD, 14));
        treasureCB.setBackground(new Color(0x25567B)); //blue
        treasureCB.setForeground(new Color(0xFFC373));
        
        edgeCB.setFont(new Font("Verdana", Font.BOLD, 14));
        edgeCB.setBackground(new Color(0x25567B)); //blue
        edgeCB.setForeground(new Color(0xFFC373)); //yellow
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
        newGameDialog.getContentPane().setBackground(Color.GRAY);

        newGameDialog.add(treasureLabel);
        newGameDialog.add(treasureCB);
        newGameDialog.add(sizeLabel);
        newGameDialog.add(edgeCB);
        newGameDialog.add(confirmButton);

        newGameDialog.setModal(true);
        newGameDialog.setLayout(new GridLayout(3, 0, 10, 10));
        JPanel pane = (JPanel) newGameDialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        newGameDialog.setBounds(800, 300, 100, 100);
        newGameDialog.pack();
        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setVisible(true);
    }

    public void showGame(MazeBoard g) {
        setVisible(false);

        //Vytvoreni panelu s herni deskou
        this.maze = new GridPanel(this, g);
        gamePane.setLayout(new BorderLayout());
        gamePane.removeAll();
        JPanel westPane = new JPanel();
        westPane.setBackground(new Color(0xFFC373));
        westPane.setLayout(new FlowLayout());

        westPane.add(maze);

        //Vytvoreni bocniho panelu
        JPanel eastPane = new JPanel();
        eastPane.setLayout(new GridBagLayout());
        eastPane.setPreferredSize(new Dimension(200, 100));
        eastPane.setBackground(new Color(0x25567B));//(0x17577e)); //blue
        GridBagConstraints c = new GridBagConstraints();
              
        //Ziskani barev ostanich hracu
        ArrayList <JPanel> plBoxes = new ArrayList <JPanel>();
        ArrayList<Player> players = g.getPlayers();

        for(int i = 0; i < players.size() ; i++) {
            if(g.getPlayerByID(connect.getMyID()).getColor() == players.get(i).getColor()) {
                continue;
            }
            else {
                JPanel tmp = new JPanel();
                JLabel tmpLabel = new JLabel();
                tmpLabel.setFont(new Font("Verdana", Font.BOLD, 12));
                tmpLabel.setForeground(Color.WHITE); 
                tmpLabel.setVerticalAlignment(JLabel.CENTER);
                tmpLabel.setHorizontalAlignment(JLabel.CENTER);
                tmpLabel.setPreferredSize(new Dimension(40,40));
                tmpLabel.setText(players.get(i).getTreasureCount() + "/" + (g.getDeckSize()/players.size()));
                switch(players.get(i).getColor()) {
                    case 0:
                        tmp.setBackground(Color.BLUE);
                        break;
                    case 1:
                        tmp.setBackground(Color.GREEN);
                        break;
                    case 2:
                        tmp.setBackground(Color.RED);
                        break;
                    case 3:
                        tmp.setBackground(Color.YELLOW);
                        break;
                }
                tmp.add(tmpLabel);
                plBoxes.add(tmp);
            }
        }
        
        freeStonePane = new JPanel();
        freeStonePane.add(maze.getFreeStone());
        freeStonePane.setBackground(new Color(0x96ADC2));
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = plBoxes.size();
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(40,0,20,0);  //top,left, bottom, right
        eastPane.add(freeStonePane,c);
        
         //Ziskani barvy hrace       
        JPanel playerColBox = new JPanel();
        switch(g.getPlayerByID(connect.getMyID()).getColor()) {
            case 0:
                playerColBox.setBackground(Color.BLUE);
                break;
            case 1:
                playerColBox.setBackground(Color.GREEN);
                break;
            case 2:
                playerColBox.setBackground(Color.RED);
                break;
            case 3:
                playerColBox.setBackground(Color.YELLOW);
                break;
        }
        
        c.ipady = 80;
        c.ipadx = 80;
        c.gridwidth = plBoxes.size();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(20,0,0,0);
        eastPane.add(playerColBox,c);

        switch(plBoxes.size()) {
            case 1:
                c.ipady = 0;
                c.ipadx = 0;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = 0;
                c.gridy = 2;
                c.anchor = GridBagConstraints.CENTER;
                c.insets = new Insets(20,0,0,0);
                eastPane.add(plBoxes.get(0),c);
                break;
            case 2:
                c.ipady = 0;
                c.ipadx = 0;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = 0;
                c.gridy = 2;
                c.anchor = GridBagConstraints.WEST;
                c.insets = new Insets(20,0,0,2);
                eastPane.add(plBoxes.get(0),c);
                c.ipady = 0;
                c.ipadx = 0;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = 1;
                c.gridy = 2;
                c.anchor = GridBagConstraints.EAST;
                c.insets = new Insets(20,2,0,0);  //bottom,left, right,top
                eastPane.add(plBoxes.get(1),c);
                break;
            case 3:
                c.ipady = 0;
                c.ipadx = 0;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = 0;
                c.gridy = 2;
                c.anchor = GridBagConstraints.WEST;
                c.insets = new Insets(20,0,0,2);  //bottom,left, right,top
                eastPane.add(plBoxes.get(0),c);
                c.ipady = 0;
                c.ipadx = 0;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = 1;
                c.gridy = 2;
                c.anchor = GridBagConstraints.CENTER;
                c.insets = new Insets(20,2,0,2);  //bottom,left, right,top
                eastPane.add(plBoxes.get(1),c);
                c.ipady = 0;
                c.ipadx = 0;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = 2;
                c.gridy = 2;
                c.anchor = GridBagConstraints.EAST;
                c.insets = new Insets(20,2,0,0);  //bottom,left, right,top
                eastPane.add(plBoxes.get(2),c);
                break;    
        }
        c.weighty = 1.0;
        c.gridwidth = plBoxes.size();
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.CENTER;
        eastPane.add(new JLabel(),c);
        
        this.console = new JTextArea();
        console.setBackground(Color.DARK_GRAY);
        console.setForeground(Color.WHITE);
        console.setEditable(false);
        
        //Pridani panelu do okna
        gamePane.add(westPane, BorderLayout.CENTER);
        gamePane.add(eastPane, BorderLayout.LINE_END);
        gamePane.add(console, BorderLayout.SOUTH);

        //Zobrazeni herniho rozlozeni
        this.cardLayout.show(MAINPane, "game");
        setVisible(true);
        pack();
    }
    
    public void setConsoleText(String input) {
        this.console.setText(input);
    }
    
    
    public void refreshGame(MazeBoard g) {
        maze.setGameBoard(g);
        maze.init();
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
