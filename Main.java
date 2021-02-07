package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class test {

    int turn = 1; // 1 for x, -1 for O
    int tilesCompleted = 0;
    JFrame frame;

    public void buttonPressed(JButton button) {
        if (turn == 1) button.setText("X");
        else button.setText("O");
        turn *= -1;
        button.setEnabled(false);
        tilesCompleted++;
    }

    public void checkWinning(JButton[] buttons) {
        if (buttons[0].getText().equals(buttons[1].getText()) && buttons[1].getText().equals(buttons[2].getText()) && buttons[0].getText() != "") {
            frame.hide();
            JOptionPane.showMessageDialog(someoneFrame, "You had to play alone because you wanted to have \n2 dates in the same day. \nThis will make you be lonely.\nPlease chose another day!");
        } else if (buttons[3].getText().equals(buttons[4].getText()) && buttons[4].getText().equals(buttons[5].getText()) && buttons[3].getText() != "") {
            frame.hide();
            JOptionPane.showMessageDialog(someoneFrame, "You had to play alone because you wanted to have \n2 dates in the same day. \nThis will make you be lonely.\nPlease chose another day!");
        } else if (buttons[6].getText().equals(buttons[7].getText()) && buttons[7].getText().equals(buttons[8].getText()) && buttons[6].getText() != "") {
            frame.hide();
            JOptionPane.showMessageDialog(someoneFrame, "You had to play alone because you wanted to have \n2 dates in the same day. \nThis will make you be lonely.\nPlease chose another day!");
        } else if (buttons[0].getText().equals(buttons[3].getText()) && buttons[3].getText().equals(buttons[6].getText()) && buttons[0].getText() != "") {
            frame.hide();
            JOptionPane.showMessageDialog(someoneFrame, "You had to play alone because you wanted to have \n2 dates in the same day. \nThis will make you be lonely.\nPlease chose another day!");
        } else if (buttons[1].getText().equals(buttons[4].getText()) && buttons[4].getText().equals(buttons[7].getText()) && buttons[1].getText() != "") {
            frame.hide();
            JOptionPane.showMessageDialog(someoneFrame, "You had to play alone because you wanted to have \n2 dates in the same day. \nThis will make you be lonely.\nPlease chose another day!");
        } else if (buttons[2].getText().equals(buttons[5].getText()) && buttons[5].getText().equals(buttons[8].getText()) && buttons[2].getText() != "") {
            frame.hide();
            JOptionPane.showMessageDialog(someoneFrame, "You had to play alone because you wanted to have \n2 dates in the same day. \nThis will make you be lonely.\nPlease chose another day!");
        } else if (tilesCompleted == 9) {
            frame.hide();
            JOptionPane.showMessageDialog(someoneFrame, "You had to play alone because you wanted to have \n2 dates in the same day. \nThis will make you be lonely.\nPlease chose another day!");
        }
    }

    public void TicTacToe() {
        frame = new JFrame("Tic Tac Toe");
        frame.setSize(new Dimension(400, 400));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3));
        JButton[] buttons = new JButton[10];
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            //buttons[i].setText(String.valueOf(i));
            frame.add(buttons[i]);
        }
        frame.repaint();
        frame.revalidate();

        for (int i = 0; i < 9; i++) {
            int finalI = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    buttonPressed(buttons[finalI]);
                    checkWinning(buttons);
                }
            });
        }
    }

    JTextArea dateLine;
    int whichLine = 1;
    HashMap<String, Integer> picku = new HashMap<>();
    HashMap<String, String> dates = new HashMap<>();
    JLabel ans;

    public static int distance(String wordToBeAnalyzed, String wordToAnalyzeWith) {
        wordToBeAnalyzed = wordToBeAnalyzed.toLowerCase();
        wordToAnalyzeWith = wordToAnalyzeWith.toLowerCase();
        // i == 0
        int[] costs = new int[wordToAnalyzeWith.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= wordToBeAnalyzed.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= wordToAnalyzeWith.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), wordToBeAnalyzed.charAt(i - 1) == wordToAnalyzeWith.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[wordToAnalyzeWith.length()];
    }

    public void PickUpLines() throws FileNotFoundException {
        File reader = new File("picklines.txt");
        Scanner s = new Scanner(reader);
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] pul = line.split("@");
            picku.put(pul[0], Integer.parseInt(pul[1]));
        }
    }

    public String topRatedLine() throws FileNotFoundException {
        PickUpLines();
        String bestline = null;
        Integer topScore = 0;
        for (Map.Entry<String, Integer> e : picku.entrySet()) {
            if (e.getValue() > topScore)
                bestline = e.getKey();
        }
        return bestline;
    }

    int oldRating;
    String key;

    public void ratePickUpLines(int i) throws IOException {
        PickUpLines();
        int pos = 1;
        key = null;
        oldRating = 0;
        for (Map.Entry<String, Integer> e : picku.entrySet()) {
            if (pos == i) {
                dateLine.setText(e.getKey());
                key = e.getKey();
                oldRating = e.getValue();
            }
            pos++;
        }
    }

    public void updateDB(int rate) throws IOException {
        if (rate != 0) {
            int currentRating = rate;
            if (oldRating != 0)
                currentRating = (currentRating + oldRating) / 2;
            picku.put(key, currentRating);
        }
        FileWriter fw = new FileWriter("picklines.txt");
        for (Map.Entry<String, Integer> e : picku.entrySet()) {
            fw.write(e.getKey() + "@" + e.getValue() + "\n");
        }
        fw.close();
    }

    public void checkPickUpLine(String line) throws IOException {
        PickUpLines();
        int smallestDist = 1000;
        String lineSmallestDist = null;
        for (HashMap.Entry<String, Integer> e : picku.entrySet()) {
            if (distance(line, e.getKey()) < smallestDist) {
                smallestDist = distance(line, e.getKey());
                lineSmallestDist = e.getKey();
            }
        }
        System.out.println(smallestDist + " " + lineSmallestDist);
        if (smallestDist < line.length() / 3) {
            ans.setText("The most similar pick-up line is: " + lineSmallestDist + "\nThe rating for it is: " + smallestDist);
        } else {
            ans.setText("We don't have your pick-up line in our database. \nI'll add it and people will have the chance to rate it. \nUntil then you have a 10 from me!");
            picku.put(line, 10);
            updateDB(0);
        }

    }

    JTextField pickUp, name, date;
    JButton vday, someone, boy, girl, submit, rateSubmit, dateSubmit, show;
    JFrame main, vdayFrame, someoneFrame;
    JPanel guyPanel, girlPanel;
    JLabel guyText, girlText, nameText, dateText;
    JSlider rate;


    public void createComp() {
        vday = new JButton("Pick-Up Lines");
        someone = new JButton("Date Manager");
        someone.setSize(new Dimension(30, 30));
        vday.setSize(new Dimension(10, 10));
        main = new JFrame("Vday Helper");
        main.setLayout(new FlowLayout());
        main.setSize(new Dimension(380, 300));
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setVisible(true);
        main.setResizable(false);
        main.add(vday);
        main.add(someone);
        ArrayList<String> greetings;
        String[] tempGreetings = new String[]{"","Hello","Hi","Hey","Heya","Hi there","Hi you","Good day", "Hii"};
        greetings = new ArrayList<>();
        greetings.addAll(Arrays.asList(tempGreetings));
        Random i = new Random();
        JOptionPane.showMessageDialog(main,greetings.get(i.nextInt(7)) + "! I am Dari. \nPatricia and Horia built me during HackBU.");
        vdayFrame = new JFrame("VDAY CHAT");

        // aici o sa punem jocul cu pickup lines.
        vday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent vdayClick) {
                //main.hide();

                vdayFrame.setLayout(new GridBagLayout());
                vdayFrame.setSize(new Dimension(400, 400));
                //vdayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                vdayFrame.setVisible(true);
                vdayFrame.setResizable(false);
                GridBagConstraints i = new GridBagConstraints();


                boy = new JButton("Guys");
                girl = new JButton("Girls");
                boy.setSize(400, 10);
                girl.setSize(400, 10);
                i.gridx = 0;
                i.gridy = 0;
                i.weighty = 60;
                i.weightx = 100;
                vdayFrame.add(boy, i);

                i.gridx = 1;
                i.gridy = 0;
                i.weighty = 60;
                i.weightx = 100;
                vdayFrame.add(girl, i);


                guyPanel = new JPanel();
                guyPanel.setVisible(false);
                guyPanel.setLayout(new BoxLayout(guyPanel, BoxLayout.PAGE_AXIS));
                guyPanel.setSize(new Dimension(400, 100));
                girlPanel = new JPanel();
                girlPanel.setVisible(false);


                guyText = new JLabel("show me you pickup line");
                girlText = new JLabel("Pick up line to date:");

                i.gridx = 0;
                i.gridy = 1;
                i.gridwidth = 2;
                i.ipady = 80;
                guyPanel.add(guyText, i);
                girlPanel.add(girlText, i);
                vdayFrame.add(guyPanel, i);
                vdayFrame.add(girlPanel, i);
                pickUp = new JTextField(20);
                pickUp.setMaximumSize(new Dimension(3000, 20));

                submit = new JButton("Submit");
                submit.setSize(20, 10);

                guyPanel.add(pickUp, i);

                guyPanel.add(submit, i);


                boy.addActionListener((new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        guyPanel.setVisible(true);
                        girlPanel.setVisible(false);

                        ans = new JLabel();
                        submit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    checkPickUpLine(pickUp.getText());
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                i.gridx = 0;
                                i.gridy = 4;
                                guyPanel.add(ans, i);
                                vdayFrame.repaint();
                                vdayFrame.revalidate();

                            }
                        });
                    }
                }));
                rateSubmit = new JButton("Submit");
                dateLine = new JTextArea();
                dateLine.setMaximumSize(new Dimension(3000, 20));
                dateLine.setEditable(false);
                girlPanel.add(dateLine, i);
                rate = new JSlider();
                girlPanel.add(rate);
                girlPanel.add(rateSubmit);

                girl.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //whichLine++;
                        girlPanel.setVisible(true);
                        guyPanel.setVisible(false);


                        try {
                            ratePickUpLines(whichLine);
                            whichLine++;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }


                        rate.setMaximum(10);
                        rate.setMinimum(0);
                        rate.setMajorTickSpacing(25);
                        rate.setMinorTickSpacing(10);
                        rate.setPaintTicks(true);
                        rate.setPaintLabels(true);
                        Hashtable position = new Hashtable();
                        position.put(0, new JLabel("0"));
                        position.put(1, new JLabel("1"));
                        position.put(2, new JLabel("2"));
                        position.put(3, new JLabel("3"));
                        position.put(4, new JLabel("4"));
                        position.put(5, new JLabel("5"));
                        position.put(6, new JLabel("6"));
                        position.put(7, new JLabel("7"));
                        position.put(8, new JLabel("8"));
                        position.put(9, new JLabel("9"));
                        position.put(10, new JLabel("10"));
                        rate.setLabelTable(position);


                        rateSubmit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    updateDB(rate.getValue());
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                try {
                                    ratePickUpLines(whichLine);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                whichLine++;
                            }
                        });


                    }
                });
            }
        });


        //de aici incepe date manager


        someone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent someoneClick) {
                //main.hide();
                someoneFrame = new JFrame("Date Manager");
                someoneFrame.setLayout(new GridBagLayout());
                someoneFrame.setSize(new Dimension(400, 400));
                //someoneFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                someoneFrame.setVisible(true);
                GridBagConstraints c = new GridBagConstraints();

                nameText = new JLabel("Type your date Name:");
                dateText = new JLabel("The date for your date:");

                name = new JTextField(20);
                date = new JTextField(20);

                show = new JButton("List your dates");
                show.setSize(20, 20);
                dateSubmit = new JButton("Submit new date");
                dateSubmit.setSize(20, 20);

                c.gridx = 0;
                c.gridy = 0;
                someoneFrame.add(nameText, c);
                c.gridx = 1;
                c.gridy = 0;
                someoneFrame.add(name, c);
                c.gridx = 0;
                c.gridy = 1;
                someoneFrame.add(dateText, c);
                c.gridx = 1;
                c.gridy = 1;
                someoneFrame.add(date, c);
                c.gridx = 0;
                c.gridy = 2;
                someoneFrame.add(show, c);
                c.gridx = 1;
                c.gridy = 2;
                someoneFrame.add(dateSubmit, c);
                JLabel listDates = new JLabel("");
                someoneFrame.add(listDates);
                dateSubmit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (dates.containsValue(date.getText()) == true) {
                            TicTacToe();
                        } else {
                            dates.put(name.getText(), date.getText());
                            name.setText("");
                            date.setText("");
                            JOptionPane.showMessageDialog(someoneFrame, "Succesfully added your date!\nI'll give you my best pick-up line for your date!");
                            try {
                                JOptionPane.showMessageDialog(someoneFrame, topRatedLine());
                            } catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }
                    }
                });
                show.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String all = null;
                        for (Map.Entry<String, String> d : dates.entrySet()) {
                            if (all == null)
                                all = d.getKey() + " " + d.getValue() + "\n";
                            else
                                all += d.getKey() + " " + d.getValue() + "\n";
                        }
                        JOptionPane.showMessageDialog(someoneFrame, all);
                    }
                });

            }
        });
    }
}

public class Main {
    public static void main(String[] args) {
        test t = new test();
        t.createComp();
    }
}