package com.mietdevelopers.GameCalc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dmitriy on 16.03.16.
 */
public class GameCalc {

    private JFrame frame;

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel activePanel;
    private JPanel settingsPanel;
    private JPanel topSetPanel;
    private JPanel midSetPanel;
    private JPanel botSetPanel;
    private JPanel winPanel;
    private JPanel gamePanel;
    private JPanel playerInfo;

    private Settings settings;
    private ArrayList<Player> players;
    private ArrayList<Player> loosers;
    private ArrayList<JTextField> nameFields;
    private ArrayList<JTextField> scoreFields;
    private JComboBox<String> comboBox;
    private JTextField scoreField;

    private String[] counts = {"2", "3", "4", "5", "6"};

    private boolean stopGame;

    public GameCalc() {
        players = new ArrayList<Player>();
        players.add(new Player("Игрок 1", 0));
        players.add(new Player("Игрок 2", 0));

        loosers = new ArrayList<Player>();

        nameFields = new ArrayList<JTextField>();
        scoreFields = new ArrayList<JTextField>();

        settings = new Settings();
        settings.setPlayersCount(players.size());

        stopGame = false;
    }

    public static void main(String[] args) {
        GameCalc gameCalc = new GameCalc();
        gameCalc.go();
    }

    public void go() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.out.println("Something wrong with system GUI.");
        }

        buildGUI();
    }

    public void buildGUI() {
        frame = new JFrame("GameCalc");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exit();
            }
        });

        frame.setIconImage(getImageIcon("icon.png").getImage());

        mainPanel = new JPanel(new BorderLayout());
        settingsPanel = new JPanel(new BorderLayout());

        //Initial menu
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton resetButton = new JButton("Новая игра");
        JButton settingsButton = new JButton("Настройки");
        JButton exitButton = new JButton("Выход");

        resetButton.addActionListener(new ResetClickListener());
        settingsButton.addActionListener(new SettingsClickListener());
        exitButton.addActionListener(new ExitClickListener());

        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(resetButton);
        menuPanel.add(settingsButton);
        menuPanel.add(exitButton);

        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        //menuPanel.setBackground(new Color(150, 150, 150));

        comboBox = new JComboBox<>(counts);
        comboBox.addActionListener(new CountChangedListener());

        settingsPanel = createSettingsPanel();
        gamePanel = createGamePanel();
        winPanel = createWinPanel();

        activePanel = settingsPanel;

        mainPanel.add(BorderLayout.CENTER, activePanel);
        mainPanel.add(BorderLayout.EAST, menuPanel);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setBounds(50, 50, 460, 300);
        frame.setVisible(true);
    }

    public JPanel createWinPanel() {
        JPanel wPanel = new JPanel(new BorderLayout());

        JPanel topWinPanel = new JPanel(new BorderLayout());
        JLabel topWinLabel = new JLabel("Введите очки");
        topWinPanel.add(BorderLayout.CENTER, topWinLabel);
        topWinPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel midWinPanel = new JPanel(new GridLayout(6,2));
        scoreFields.clear();
        for (int i=0; i<6; i++) {
            JLabel playerName = new JLabel();
            JTextField playerScore = new JTextField();

            if (i < settings.getPlayersCount()) {
                playerName.setText(players.get(i).getName() + " :");
                playerScore.setText("0");
                playerScore.setEditable(true);
                scoreFields.add(playerScore);
            } else {
                playerName.setText("*** EMPTY ***");
                playerScore.setEditable(false);
            }

            midWinPanel.add(playerName);
            midWinPanel.add(playerScore);
        }

        JPanel botWinPanel = new JPanel();
        botWinPanel.setLayout(new BoxLayout(botWinPanel, BoxLayout.Y_AXIS));
        JButton winApplyButton = new JButton("Применить");
        winApplyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        winApplyButton.addActionListener(new WinApplyListener());

        botWinPanel.add(winApplyButton);
        botWinPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        wPanel.add(BorderLayout.NORTH, topWinPanel);
        wPanel.add(BorderLayout.CENTER, midWinPanel);
        wPanel.add(BorderLayout.SOUTH, botWinPanel);

        wPanel.setBorder(new EmptyBorder(10,10,10,10));

        return wPanel;
    }

    public JPanel createSettingsPanel() {
        JPanel setPanel = new JPanel(new BorderLayout());

        topSetPanel = new JPanel(new GridLayout(2,2));
        JLabel countLabel = new JLabel("Количество игроков :");
        JLabel scoreLabel = new JLabel("Граничное значение :");
        scoreField = new JTextField();
        scoreField.setText("" + settings.getMaxScore());

        topSetPanel.add(countLabel);
        topSetPanel.add(comboBox);
        topSetPanel.add(scoreLabel);
        topSetPanel.add(scoreField);

        topSetPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        midSetPanel = new JPanel(new GridLayout(6,2));
        nameFields.clear();
        for (int i=0; i<6; i++) {
            JLabel playerName = new JLabel("Игрок " + (i+1) + " :");
            JTextField nameField = new JTextField();

            if ( i < settings.getPlayersCount() ) {
                nameField.setEditable(true);
                nameField.setText(players.get(i).getName());
                nameFields.add(nameField);
            } else {
                nameField.setEditable(false);
            }

            midSetPanel.add(playerName);
            midSetPanel.add(nameField);
        }

        botSetPanel = new JPanel();
        botSetPanel.setLayout(new BoxLayout(botSetPanel, BoxLayout.Y_AXIS));
        JButton settingsApplyButton = new JButton("Применить");
        settingsApplyButton.addActionListener(new SettingsApplyListener());
        settingsApplyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        botSetPanel.add(settingsApplyButton);
        botSetPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        setPanel.add(BorderLayout.NORTH, topSetPanel);
        setPanel.add(BorderLayout.CENTER, midSetPanel);
        setPanel.add(BorderLayout.SOUTH, botSetPanel);

        setPanel.setBorder(new EmptyBorder(10,10,10,10));

        return setPanel;
    }

    public JPanel createGamePanel() {
        stopGame = false;

        JPanel gmPanel = new JPanel();

        gmPanel.setLayout(new BoxLayout(gmPanel, BoxLayout.Y_AXIS));
        playerInfo = new JPanel(new GridLayout(1,4));

        JLabel nameHeader = new JLabel("Имя");
        JLabel scoreHeader = new JLabel("Счет");
        JLabel progressHeader = new JLabel("");
        JLabel winHeader = new JLabel("");

        nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        scoreHeader.setHorizontalAlignment(SwingConstants.CENTER);
        progressHeader.setHorizontalAlignment(SwingConstants.CENTER);
        winHeader.setHorizontalAlignment(SwingConstants.CENTER);

        playerInfo.add(nameHeader);
        playerInfo.add(scoreHeader);
        playerInfo.add(progressHeader);
        playerInfo.add(winHeader);

        gmPanel.add(playerInfo);

        loosers.clear();

        for (Player player : players) {
            if (player.getScore() > settings.getMaxScore()) {
                stopGame = true;
                loosers.add(player);
            }
        }

        for (int i=0; i<7; i++) {
            if ( i < players.size()) {
                Player currPlayer = players.get(i);

                JLabel playerNameLabel = new JLabel(currPlayer.getName());

                JLabel playerScoreLable = new JLabel("" + currPlayer.getScore());
                playerScoreLable.setHorizontalAlignment(SwingConstants.CENTER);

                JProgressBar playerProgress = new JProgressBar(0, settings.getMaxScore());
                playerProgress.setValue(currPlayer.getScore());
                playerProgress.setStringPainted(true);

                JButton playerWinButton = new JButton("WIN");
                playerWinButton.addActionListener(new WinClickListener());
                playerWinButton.setName("" + i);

                if(stopGame) {
                    playerWinButton.setEnabled(false);
                    playerProgress.setEnabled(false);
                }

                playerInfo = new JPanel(new GridLayout(1,4));
                playerInfo.add(playerNameLabel);
                playerInfo.add(playerScoreLable);
                playerInfo.add(playerProgress);
                playerInfo.add(playerWinButton);

                gmPanel.add(playerInfo);
            } else {
                gmPanel.add(new JPanel());
            }
        }
        gmPanel.setBorder(new EmptyBorder(10,10,10,10));

        return gmPanel;
    }

    public void exit() {
        System.exit(0);
    }

    public ImageIcon getImageIcon(String path) {
        URL imgURL = GameCalc.class.getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("File not found " + path);
            return null;
        }
    }

    public void updateFrame() {
        mainPanel.updateUI();
        frame.validate();
    }

    public class SettingsClickListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {

            mainPanel.removeAll();

            mainPanel.add(BorderLayout.CENTER, settingsPanel);
            mainPanel.add(BorderLayout.EAST, menuPanel);

            updateFrame();
        }
    }

    public class ResetClickListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {

            for(Player player : players) {
                player.setScore(0);
            }

            mainPanel.removeAll();

            gamePanel = createGamePanel();

            mainPanel.add(BorderLayout.CENTER, gamePanel);
            mainPanel.add(BorderLayout.EAST, menuPanel);

            updateFrame();
        }
    }

    public class ExitClickListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            exit();
        }
    }

    public class SettingsApplyListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {

            //Changing names:
            for (int i=0; i<players.size(); i++)
            {
                players.get(i).setName(nameFields.get(i).getText());
            }

            int maxScore = Integer.parseInt(scoreField.getText());

            settings.setMaxScore(maxScore);

            mainPanel.removeAll();

            gamePanel = createGamePanel();

            mainPanel.add(BorderLayout.CENTER, gamePanel);
            mainPanel.add(BorderLayout.EAST, menuPanel);

            updateFrame();
        }
    }

    public class WinApplyListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {

            //Changing players score
            for (int i=0; i<scoreFields.size(); i++)
            {
                Player currPlayer = players.get(i);
                int bonusScore;
                try {
                    bonusScore = Integer.parseInt(scoreFields.get(i).getText());
                } catch (Exception e) {
                    bonusScore = 0;
                }

                int totalScore = currPlayer.getScore() + bonusScore;
                if (totalScore == settings.getMaxScore())
                    totalScore = 0;

                currPlayer.setScore(totalScore);
            }

            mainPanel.removeAll();

            gamePanel = createGamePanel();

            mainPanel.add(BorderLayout.CENTER, gamePanel);
            mainPanel.add(BorderLayout.EAST, menuPanel);

            updateFrame();

            if (stopGame) {
                checkLoosers();
            }
        }
    }

    public class WinClickListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            mainPanel.removeAll();

            winPanel = createWinPanel();

            mainPanel.add(BorderLayout.CENTER, winPanel);
            mainPanel.add(BorderLayout.EAST, menuPanel);

            updateFrame();
        }
    }

    public class CountChangedListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            int num = Integer.parseInt((String)comboBox.getSelectedItem());

            if (num >= players.size())
            {
                for (int i = players.size(); i < num; i++) {

                    Player player = new Player( "Игрок " + (i+1) , 0);
                    players.add(player);
                }
            } else {
                for (int i = players.size(); i > num; i--) {
                    players.remove(i - 1);
                }
            }
            settings.setPlayersCount(num);

            settingsPanel.removeAll();

            midSetPanel = new JPanel(new GridLayout(6,2));
            nameFields.clear();
            for (int i=0; i<6; i++) {
                JLabel playerName = new JLabel("Игрок" + (i+1) + " :");
                JTextField nameField = new JTextField();

                if ( i < settings.getPlayersCount() ) {
                    nameField.setEditable(true);
                    nameField.setText(players.get(i).getName());
                    nameFields.add(nameField);
                } else {
                    nameField.setEditable(false);
                }

                midSetPanel.add(playerName);
                midSetPanel.add(nameField);
            }

            settingsPanel.add(BorderLayout.NORTH, topSetPanel);
            settingsPanel.add(BorderLayout.CENTER, midSetPanel);
            settingsPanel.add(BorderLayout.SOUTH, botSetPanel);

            updateFrame();
        }
    }

    public void checkLoosers() {

        ImageIcon icon = getImageIcon("icon.png");
        String msg = "Проигравшие игроки:";

        JLabel iconLabel = new JLabel(icon);
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.add(iconLabel);

        JPanel textPanel = new JPanel(new GridLayout(0, 1));

        Font smallFont = new Font("arial", Font.BOLD, 8);
        Font boldFont = new Font("arial", Font.BOLD, 12);

        JLabel webLable = new JLabel("GameCalc v1.0  |  www.miet-developers.com");
        JLabel titleLable = new JLabel(msg);

        webLable.setFont(smallFont);
        titleLable.setFont(boldFont);

        textPanel.add(new JLabel(" "));
        textPanel.add(titleLable);
        textPanel.add(new JLabel(" "));

        for(int i=0; i<6; i++) {
            if (i < loosers.size()) {
                Player looser = loosers.get(i);
                textPanel.add(new JLabel("   " + looser.getName()));
            } else {
                textPanel.add(new JLabel(" "));
            }
        }

        textPanel.add(new JLabel("_______________________"));
        textPanel.add(webLable);

        JPanel aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.add(BorderLayout.WEST, iconPanel);
        aboutPanel.add(BorderLayout.CENTER, textPanel);

        JOptionPane.showMessageDialog(
                null,
                aboutPanel,
                "Игра окончена",
                JOptionPane.PLAIN_MESSAGE);
    }
}