/* file name  : ClientFrame.java
 * authors    : xhajek33, xblozo00
 */

package ija.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class ClientFrame extends JFrame{

    private int m, n;
    private JPanel frameContents;
    private JButton newGame;
    private JList lobbyPlayers;
    private JDialog newGameDialog;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }

    public ClientFrame() {
        this.m = 7;
        this.n = 7;
        this.frameContents = new JPanel();
        this.frameContents.setLayout(new BorderLayout());
        this.lobbyPlayers = new JList();
        
        this.start();
    }

    private void start() {
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

        //Seznam her
        frameContents.add(lobbyPlayers);

        //Tlacitko zacit hru
        newGame = new JButton("ZACIT HRAT");
        newGame.setPreferredSize( new Dimension(this.getWidth(), 50));
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDialog();
            }
        });
        frameContents.add(newGame, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void createDialog() {
        //if(this.newGameDialog != null)
            //return;
        JButton ngButton = new JButton("NOVA HRA");
        JButton sgButton = new JButton("ULOZENA HRA");
        this.newGameDialog = new JDialog(this);

        this.newGameDialog.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
        newGameDialog.setBounds(200, 300, 100, 100);
        ngButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGameDialog.dispose();
                createNGDialog();
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
        JComboBox<String> treasureCB = new JComboBox<>(treasureNumber);
        JLabel sizeLabel = new JLabel("VELIKOST DESKY");
        JTextField sizeField = new JTextField();
        this.newGameDialog = new JDialog(this);

        newGameDialog.add(treasureLabel);
        newGameDialog.add(treasureCB);
        newGameDialog.add(sizeLabel);
        newGameDialog.add(sizeField);
        newGameDialog.add(confirmButton);

        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setVisible(true);
        newGameDialog.setModal(true);
        newGameDialog.setLayout(new GridLayout(3, 0, 10, 10));
        JPanel pane = (JPanel) newGameDialog.getContentPane();
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        newGameDialog.pack();
    }
}
