package com;

import javax.swing.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class GUI extends JFrame implements ActionListener {
    JMenuBar menuBar;
    JButton shuffle;
    JPanel contents;
    ArrayList<JButton> Tiles = new ArrayList<>();
    int emptyTile;
    int size = 4;

    GUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Tiles");
        this.setSize(500, 500);
        this.setLayout(new GridLayout());
        this.setLocationRelativeTo(null);

        shuffle = new JButton("Shuffle");
        menuBar = new JMenuBar();
        menuBar.add(shuffle);
        shuffle.addActionListener(this);
        contents = new JPanel(new GridLayout(size, size));

        for (int i = 0; i < size * size; i++) {
            JButton button = new JButton(Integer.toString(i));
            Tiles.add(button);
            var sansbold14 = new Font("Console",Font.BOLD,50);
            button.setFont(sansbold14);
            button.setBackground(new Color(194, 154, 222));
            button.addActionListener(e ->
            {
                if (checkNeighbours(Tiles.indexOf(button)))
                    swapText(Tiles.indexOf(button),emptyTile);
            });
        }

        Tiles.add(Tiles.remove(0));
        for (var b : Tiles) {
            contents.add(b);
            setColor(b);
        }

        shuffle.setFocusable(false);
        this.setJMenuBar(menuBar);
        this.add(contents);
        this.setVisible(true);
        this.setFocusable(true);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.isControlDown()){
                    if (keyDirection.containsKey(e.getKeyCode())) {
                        int tempEmptyTile = -1;
                        while (tempEmptyTile != emptyTile) {
                            tempEmptyTile = emptyTile;
                            move(keyDirection.get(e.getKeyCode()));
                        }
                    }
                } else if (keyDirection.containsKey(e.getKeyCode()))
                    move(keyDirection.get(e.getKeyCode()));
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        checkZero();
    }

    void shuffle() {
        Random rnd = new Random();
        for(int i = 0; i < Tiles.size(); i++) {
            swapText(i, rnd.nextInt(i + 1));
        }
    }

    void checkZero() {
        for (int i = 0; i < Tiles.size(); i++) {
            if(!Objects.equals(Tiles.get(i).getText(), "0")) {
                Tiles.get(i).setVisible(true);
            } else {
                Tiles.get(i).setVisible(false);
                emptyTile = i;
            }
        }
    }

    void swapText(int i, int j) {
        String str = Tiles.get(j).getText();
        Tiles.get(j).setText(Tiles.get(i).getText());
        Tiles.get(i).setText(str);
        setColor(i);
        setColor(j);
        checkZero();
    }

    boolean checkNeighbours(int position) {
        int i1 = position / size;
        int j1 = position % size;
        int i2 = emptyTile / size;
        int j2 = emptyTile % size;
        return (Math.abs(i2 - i1) + Math.abs(j1 - j2)) == 1;
    }

    void setColor(JButton button) {
        setColor(Tiles.indexOf(button));
    }

    void setColor(int i) {
        var button = Tiles.get(i);
        if (i + 1 != Integer.parseInt(button.getText())) {
            button.setForeground(new Color(243, 234, 61));
        } else {
            button.setForeground(Color.black);
        }
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    void move(Direction d) {
        switch (d) {
            case UP:
                if (emptyTile < (size - 1) * size)
                    swapText(emptyTile,emptyTile + size);
                break;
            case DOWN:
                if (emptyTile >= size)
                    swapText(emptyTile,emptyTile - size);
                break;
            case LEFT:
                if (emptyTile % size != size - 1)
                    swapText(emptyTile,emptyTile + 1);
                break;
            case RIGHT:
                if (emptyTile % size != 0)
                    swapText(emptyTile,emptyTile - 1);
                break;

        }
    }

    static final Map<Integer, Direction> keyDirection = Map.ofEntries(
            Map.entry(KeyEvent.VK_UP, Direction.UP),
            Map.entry(KeyEvent.VK_DOWN, Direction.DOWN),
            Map.entry(KeyEvent.VK_LEFT, Direction.LEFT),
            Map.entry(KeyEvent.VK_RIGHT, Direction.RIGHT)
    );

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == shuffle){
            shuffle();
            checkZero();
        }
    }
}
