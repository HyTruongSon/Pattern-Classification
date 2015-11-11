// Software: Multi-Layer Perceptrons in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c), all rights reserved. Only use for academic purposes.

package MainProgram;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrame extends JFrame {
    
    private final String titleFrame = "Multi-Layer Perceptrons in Java";
    private final int widthFrame = 500;
    private final int heightFrame = 500;
    private final int marginFrame = 20;
    private final int widthComponent = widthFrame - 2 * marginFrame;
    private final int heightComponent = 30;
    
    private JLabel modelLabel;
    private JComboBox  modelBox;
    private JLabel processLabel;
    private JComboBox processBox;
    private JLabel databaseLabel;
    private JComboBox databaseBox;
    private JButton aboutSoftwareButton;
    private JButton aboutDatabaseButton;
    private JButton aboutAuthorButton;
    private JButton nextButton;
    
    private final String modelOptions[] = {
        "Multi-Layer Perceptrons"
    };
    
    // Including Autoencoders
    /*
    private final String modelOptions[] = {
        "Multi-Layer Perceptrons",
        "Autoencoder"
    };
    */
    
    private final String processOptions[] = {
        "Training",
        "Testing"
    };
    
    public final String databaseOptions[] = {
        "MNIST - Digit handwriting database - Yann LeCun",
        "Lower character handwriting database - Hy Truong Son",
        "Upper character handwriting database - Hy Truong Son",
        "CBCL Face Database - MIT",
        "NAO robot image database - University of Bonn"
    };
    
    public MainFrame() {
        setTitle(titleFrame);
        setSize(widthFrame, heightFrame);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        
        initGUI();
        initActionListeners();
        
        setVisible(true);
    }
    
    private void initGUI() {
        int x = marginFrame;
        int y = marginFrame;
        
        modelLabel = new JLabel("Model:");
        modelLabel.setForeground(Color.blue);
        modelLabel.setBounds(x, y, widthComponent, heightComponent);
        add(modelLabel);
        
        y += heightComponent;
        
        modelBox = new JComboBox(modelOptions);
        modelBox.setBounds(x, y, widthComponent, heightComponent);
        add(modelBox);
        
        y += heightComponent + marginFrame;
        
        processLabel = new JLabel("Process:");
        processLabel.setForeground(Color.blue);
        processLabel.setBounds(x, y, widthComponent, heightComponent);
        add(processLabel);
        
        y += heightComponent;
        
        processBox = new JComboBox(processOptions);
        processBox.setBounds(x, y, widthComponent, heightComponent);
        add(processBox);
        
        y += heightComponent + marginFrame;
        
        databaseLabel = new JLabel("Database:");
        databaseLabel.setForeground(Color.blue);
        databaseLabel.setBounds(x, y, widthComponent, heightComponent);
        add(databaseLabel);
        
        y += heightComponent;
        
        databaseBox = new JComboBox(databaseOptions);
        databaseBox.setBounds(x, y, widthComponent, heightComponent);
        add(databaseBox);
        
        y += heightComponent + 2 * marginFrame;
        
        aboutDatabaseButton = new JButton("About the databases");
        aboutDatabaseButton.setBounds(x, y, widthComponent, heightComponent);
        add(aboutDatabaseButton);
        
        y += heightComponent + marginFrame;
        
        aboutSoftwareButton = new JButton("About the software");
        aboutSoftwareButton.setBounds(x, y, widthComponent, heightComponent);
        add(aboutSoftwareButton);
        
        y += heightComponent + marginFrame;
        
        aboutAuthorButton = new JButton("About the author Hy Truong Son");
        aboutAuthorButton.setBounds(x, y, widthComponent, heightComponent);
        add(aboutAuthorButton);
        
        y += heightComponent + 2 * marginFrame;
        
        nextButton = new JButton("NEXT");
        nextButton.setForeground(Color.blue);
        nextButton.setBounds(x, y, widthComponent, heightComponent);
        add(nextButton);
    }
    
    private void initActionListeners() {
        aboutDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MainProgram.aboutDatabase == null) {
                    MainProgram.aboutDatabase = new AboutDatabase();
                } else {
                    MainProgram.aboutDatabase.setVisible(true);
                }
            }
        });
        
        aboutSoftwareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MainProgram.aboutSoftware == null) {
                    MainProgram.aboutSoftware = new AboutSoftware();
                } else {
                    MainProgram.aboutSoftware.setVisible(true);
                }
            }
        });
        
        aboutAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // MainProgram.aboutAuthor = new AboutAuthor();
                
                if (MainProgram.aboutAuthor == null) {
                    MainProgram.aboutAuthor = new AboutAuthor();
                } else {
                    MainProgram.aboutAuthor.setVisible(true);
                }
                
            }
        });
        
        nextButton.addActionListener(nextAction);
    }
    
    private final ActionListener nextAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int model = modelBox.getSelectedIndex();
            int process = processBox.getSelectedIndex();
            int database = databaseBox.getSelectedIndex();
            
            // MLP
            if (model == 0) {
                // Training
                if (process == 0) {
                    switch (database){
                        case 0: 
                            new MLP.MNIST.Parameters(true);
                            break;
                        case 1:
                            new MLP.HTS.lower.Parameters(true);
                            break;
                        case 2:
                            new MLP.HTS.upper.Parameters(true);
                            break;
                        case 3:
                            new MLP.MIT.Parameters(true);
                            break;
                        case 4:
                            new MLP.UniBonn.Parameters(true);
                            break;
                    }
                    return;
                }

                // Testing
                if (process == 1) {
                    switch (database){
                        case 0: 
                            new MLP.MNIST.Parameters(false);
                            break;
                        case 1:
                            new MLP.HTS.lower.Parameters(false);
                            break;
                        case 2:
                            new MLP.HTS.upper.Parameters(false);
                            break;
                        case 3:
                            new MLP.MIT.Parameters(false);
                            break;
                        case 4:
                            new MLP.UniBonn.Parameters(false);
                            break;
                    }
                }
            }
        }
    };
    
}
