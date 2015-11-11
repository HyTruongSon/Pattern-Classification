// Software: Multi-Layer Perceptrons in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c), all rights reserved. Only use for academic purposes.

package MLP.MNIST;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Parameters extends JFrame {
    
    private final String titleFrame = "MLP - Parameters - MNIST handwritten database";
    private final int widthFrame = 600;
    private final int heightFrame = 650;
    private final int marginFrame = 10;
    private final int heightComponent = 30;
    private final int widthLabel = 200;
    
    private int nLayers, nHiddens;
    private final int nInput = 784;
    private final int nOutput = 10;
    
    private final boolean isTraining;
    
    private final String nHiddensOptions[] = {
        "0 hidden layer (Logistic Regression)", 
        "1 hidden layer (standard)", 
        "2 hidden layers", 
        "3 hidden layers", 
        "4 hidden layers"
    };
    
    private final String nNeuronsLabelsNames[] = {
        "Input layer:",
        "Hidden layer 1:",
        "Hidden layer 2:",
        "Hidden layer 3:",
        "Hidden layer 4:",
        "Output layer:"
    };
    
    private final int nHiddenNeuronsDefault[] = {
        128, 128, 128, 128
    };
    
    private int nIterations = 256;
    private double learningRate = 1e-3;
    private String modelFileName = "mnist-model.dat";
    private String prevModelFileName;
    
    private int MaxTraining = 60000;
    private int MaxTesting = 10000;
    private int nTraining, nTesting;
    
    private JLabel nTrainingLabel;
    private JTextField nTrainingText;
    private JLabel nTestingLabel;
    private JTextField nTestingText;
    private JLabel nHiddensLabel;
    private JComboBox nHiddensBox;
    private JLabel nNeuronsLabels[] = new JLabel [6];
    private JTextField nNeuronsTexts[] = new JTextField [6];
    private JLabel nIterationsLabel;
    private JTextField nIterationsText;
    private JLabel learningRateLabel;
    private JTextField learningRateText;
    private JButton saveModelButton;
    private JTextField saveModelText;
    private JButton loadModelButton;
    private JTextField loadModelText;
    private JFileChooser fileChooser = new JFileChooser();
    private JButton startButton;
    
    public Parameters() {
        setTitle(titleFrame);
        setSize(widthFrame, heightFrame);
        setResizable(false);
        setLayout(null);
        
        this.isTraining = true;
        initGUI();
        
        setVisible(true);
    }
    
    public Parameters(boolean isTraining) {
        setTitle(titleFrame);
        setSize(widthFrame, heightFrame);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.isTraining = isTraining;
        initGUI();
        
        setVisible(true);
    }
    
    private void initGUI() {
        int x = marginFrame;
        int y = marginFrame;
        int length = widthFrame - 2 * marginFrame - widthLabel;
        
        nTrainingLabel = new JLabel();
        nTrainingLabel.setText("No. training samples:");
        nTrainingLabel.setBounds(x, y, widthLabel, heightComponent);
        add(nTrainingLabel);
        
        nTrainingText = new JTextField();
        nTrainingText.setText(Integer.toString(MaxTraining));
        nTrainingText.setBounds(x + widthLabel, y, length, heightComponent);
        add(nTrainingText);
        
        y += heightComponent + marginFrame;
        
        nTestingLabel = new JLabel();
        nTestingLabel.setText("No. testing samples:");
        nTestingLabel.setBounds(x, y, widthLabel, heightComponent);
        add(nTestingLabel);
        
        nTestingText = new JTextField();
        nTestingText.setText(Integer.toString(MaxTesting));
        nTestingText.setBounds(x + widthLabel, y, length, heightComponent);
        add(nTestingText);
        
        y += heightComponent + marginFrame;
        
        nHiddensLabel = new JLabel();
        nHiddensLabel.setText("Number of hidden layers:");
        nHiddensLabel.setBounds(x, y, widthLabel, heightComponent);
        add(nHiddensLabel);
        
        nHiddensBox = new JComboBox(nHiddensOptions);
        nHiddensBox.setBounds(x + widthLabel, y, length, heightComponent);
        nHiddensBox.setSelectedIndex(1);
        nHiddensBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = nHiddensBox.getSelectedIndex();
                
                for (int i = 1; i <= index; ++i) {
                    nNeuronsTexts[i].setEditable(true);
                    nNeuronsTexts[i].setText(Integer.toString(nHiddenNeuronsDefault[i - 1]));
                }
                
                for (int i = index + 1; i < nNeuronsLabels.length - 1; ++i) {
                    nNeuronsTexts[i].setEditable(false);
                    nNeuronsTexts[i].setText("Not used");
                }
            }
        });
        add(nHiddensBox);
        
        for (int i = 0; i < nNeuronsLabels.length; ++i) {
            y += heightComponent + marginFrame;
            
            nNeuronsLabels[i] = new JLabel();
            nNeuronsLabels[i].setText(nNeuronsLabelsNames[i]);
            nNeuronsLabels[i].setBounds(x, y, widthLabel, heightComponent);
            add(nNeuronsLabels[i]);
            
            nNeuronsTexts[i] = new JTextField();
            nNeuronsTexts[i].setBounds(x + widthLabel, y, length, heightComponent);
            add(nNeuronsTexts[i]);
            
            if ((i == 0) || (i == nNeuronsLabels.length - 1)) {
                nNeuronsTexts[i].setEditable(false);
                nNeuronsLabels[i].setForeground(Color.blue);
                if (i == 0) {
                    nNeuronsTexts[i].setText(Integer.toString(nInput));
                } else {
                    nNeuronsTexts[i].setText(Integer.toString(nOutput));
                }
            } else {
                nNeuronsTexts[i].setEditable(true);
                nNeuronsTexts[i].setText(Integer.toString(nHiddenNeuronsDefault[i - 1]));
            }
        }
        
        int index = nHiddensBox.getSelectedIndex();
        for (int i = index + 1; i < nNeuronsLabels.length - 1; ++i) {
            nNeuronsTexts[i].setEditable(false);
            nNeuronsTexts[i].setText("Not used");
        }
        
        y += heightComponent + marginFrame;
        
        nIterationsLabel = new JLabel();
        nIterationsLabel.setText("Number of iterations:");
        nIterationsLabel.setBounds(x, y, widthLabel, heightComponent);
        add(nIterationsLabel);
        
        nIterationsText = new JTextField();
        nIterationsText.setText(Integer.toString(nIterations));
        nIterationsText.setBounds(x + widthLabel, y, length, heightComponent);
        add(nIterationsText);
        
        y += heightComponent + marginFrame;
        
        learningRateLabel = new JLabel();
        learningRateLabel.setText("Learning rate:");
        learningRateLabel.setBounds(x, y, widthLabel, heightComponent);
        add(learningRateLabel);
        
        learningRateText = new JTextField();
        learningRateText.setText(Double.toString(learningRate));
        learningRateText.setBounds(x + widthLabel, y, length, heightComponent);
        add(learningRateText);
        
        y += heightComponent + marginFrame;
        
        saveModelButton = new JButton("Save model to:");
        saveModelButton.setBounds(x, y, widthLabel - 10, heightComponent);
        saveModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                File workingDirectory = new File(System.getProperty("user.dir"));
                fileChooser.setCurrentDirectory(workingDirectory);
                fileChooser.showSaveDialog(null);
                File file = null;
                try {
                    file = fileChooser.getSelectedFile();
                } catch (Exception exc) {
                    return;
                }
                if (file != null) {
                    saveModelText.setText(file.getPath());
                }
            }
        });
        add(saveModelButton);
        
        saveModelText = new JTextField();
        saveModelText.setBounds(x + widthLabel, y, length, heightComponent);
        saveModelText.setText(modelFileName);
        add(saveModelText);
        
        y += heightComponent + marginFrame;
        
        loadModelButton = new JButton("Load previous model:");
        loadModelButton.setBounds(x, y, widthLabel - 10, heightComponent);
        loadModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                File workingDirectory = new File(System.getProperty("user.dir"));
                fileChooser.setCurrentDirectory(workingDirectory);
                fileChooser.showSaveDialog(null);
                File file = null;
                try {
                    file = fileChooser.getSelectedFile();
                } catch (Exception exc) {
                    return;
                }
                if (file != null) {
                    loadModelText.setText(file.getPath());
                }
            }
        });
        add(loadModelButton);
        
        loadModelText = new JTextField();
        loadModelText.setBounds(x + widthLabel, y, length, heightComponent);
        add(loadModelText);
        
        y += heightComponent + marginFrame;
        
        startButton = new JButton();
        startButton.setText("Start Training");
        startButton.setForeground(Color.blue);
        startButton.setBounds(x, y, widthFrame - 2 * marginFrame, heightComponent);
        add(startButton);
        
        startButton.addActionListener(startAction);
        
        if (!this.isTraining) {
            nTrainingLabel.setEnabled(false);
            nTrainingText.setEditable(false);
            nTrainingText.setText("No need for the testing phase");
            
            nIterationsLabel.setEnabled(false);
            nIterationsText.setEditable(false);
            nIterationsText.setText("No need for the testing phase");
            
            learningRateLabel.setEnabled(false);
            learningRateText.setEditable(false);
            learningRateText.setText("No need for the testing phase");
            
            saveModelButton.setEnabled(false);
            saveModelText.setEditable(false);
            saveModelText.setText("No need for the testing phase");
            
            startButton.setText("Start Testing");
        } else {
            nTestingLabel.setEnabled(false);
            nTestingText.setEditable(false);
            nTestingText.setText("No need for the training phase");
        }
    }
    
    private ActionListener startAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            nHiddens = nHiddensBox.getSelectedIndex();
            nLayers = nHiddens + 2;
            
            ArrayList<Integer> neurons = new ArrayList<>();
            neurons.add(nInput);
            for (int i = 1; i <= nHiddens; ++i) {
                String s = nNeuronsTexts[i].getText();
                int number;
                try {
                    number = Integer.parseInt(s);
                } catch (NumberFormatException exc) {
                    String message = "Number format in Hidden layer " + Integer.toString(i) + " is NOT correct!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }
                if (number <= 0) {
                    String message = "Number of neurons in Hidden layer " + Integer.toString(i) + " must bigger than 0!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }
                neurons.add(number);
            }
            neurons.add(nOutput);
            
            if (isTraining) {
                modelFileName = saveModelText.getText();
                prevModelFileName = loadModelText.getText();
                
                try {
                    nTraining = Integer.parseInt(nTrainingText.getText());
                } catch (NumberFormatException exc) {
                    String message = "No. training samples has NOT correct format!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }

                if (nTraining <= 0) {
                    String message = "No. training samples must be bigger than 0!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }
                
                try {
                    nIterations = Integer.parseInt(nIterationsText.getText());
                } catch (NumberFormatException exc) {
                    String message = "Number of iterations has NOT correct format!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }

                if (nIterations <= 0) {
                    String message = "Number of iterations must be bigger than 0!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }

                try {
                    learningRate = Double.parseDouble(learningRateText.getText());
                } catch (NumberFormatException exc) {
                    String message = "Learning rate has NOT correct format!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }

                if (learningRate <= 0) {
                    String message = "Learning rate must be bigger than 0!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }
            } else {
                prevModelFileName = loadModelText.getText();
                if ((prevModelFileName == null) || (prevModelFileName.length() == 0)) {
                    String message = "You MUST choose a trained model file for testing!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }
                
                try {
                    nTesting = Integer.parseInt(nTestingText.getText());
                } catch (NumberFormatException exc) {
                    String message = "No. testing samples has NOT correct format!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }

                if (nTesting <= 0) {
                    String message = "No. testing samples must be bigger than 0!";
                    JOptionPane.showMessageDialog(null, message);
                    return;
                }
            }
            
            if (isTraining) {
                new Training(nTraining, neurons, nIterations, learningRate, modelFileName, prevModelFileName);
            } else {
                new Testing(nTesting, neurons, prevModelFileName);
            }
        }
    };
}
