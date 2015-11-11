// Software: Multi-Layer Perceptrons in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c), all rights reserved. Only use for academic purposes.

package MLP.UniBonn;

import MyLib.ColorHSV;
import MyLib.MLP;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Testing extends JFrame {
    
    private final String databaseFolder = "NAO-UniBonn-database/";
    private final String imagesFileName = "test.image.bin";
    private final String labelsFileName = "test.label.bin";
    
    private final int nTesting;
    private final int width = 20;
    private final int height = 20;
    private int sample = 0;
    private int correct = 0;
    
    private MLP myNet;
    private double inp[];
    private double out[];
    private double predict[];
    private int nInput, nOutput;
    
    private int red[][][];
    private int green[][][];
    private int blue[][][];
    private int processed[][][]; 
    private int label[];
    
    private final String titleFrame = "MLP - Testing - NAO Robot Database";
    private final int widthFrame = 800;
    private final int heightFrame = 600;
    private final int marginFrame = 20;
    private final int widthLabel = 150;
    private final int heightComponent = 30;
    private final int zoomPara = 4;
    
    private final ArrayList<Integer> neurons;
    private final String prevModelFileName;
    
    private JLabel statusLabel;
    private JTextField statusText;
    private JLabel progressLabel;
    private final JProgressBar progressBar = new JProgressBar();
    private JLabel sampleLabel;
    private JButton nextSample;
    private JButton allSamples;
    private JLabel correctLabel;
    private JLabel accuracyLabel;
    private JLabel outputLabel;
    private JLabel predictLabel;
    private JLabel errorLabel;
    private JLabel inputLabel;
    private JButton inputButton;
    private JLabel processedInputLabel;
    private JButton processedInputButton;
    
    private void about() {
        System.out.println("Number of layers: " + Integer.toString(neurons.size()));
        for (int i = 0; i < neurons.size(); ++i) {
            System.out.println("Layer " + Integer.toString(i + 1) + ": " + 
                    Integer.toString(neurons.get(i)) + " neurons");
        }

        System.out.println("Previous model file name: " + prevModelFileName);
    }
    
    public Testing(int nTesting, ArrayList<Integer> neurons, String prevModelFileName) {
        this.nTesting = nTesting;
        this.neurons = new ArrayList<>();
        for (int i = 0; i < neurons.size(); ++i) {
            this.neurons.add(neurons.get(i));
        }
        this.prevModelFileName = prevModelFileName;
        
        red = new int [this.nTesting][width][height];
        green = new int [this.nTesting][width][height];
        blue = new int [this.nTesting][width][height];
        processed = new int [this.nTesting][width][height]; 
        label = new int [this.nTesting];
        
        about();
        
        setTitle(titleFrame);
        setSize(widthFrame, heightFrame);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initGUI();
        
        setVisible(true);
        
        initMLP();
        
        try {
            loadDatabase();
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(null, exc.toString());
        }
    }
    
    private void initMLP() {
        System.out.println("Initialize Multi-Layer Perceptrons");
        
        nInput = neurons.get(0);
        nOutput = neurons.get(neurons.size() - 1);
        
        inp = new double [nInput];
        out = new double [nOutput];
        predict = new double [nOutput];
        
        switch (neurons.size()) {
            case 2:
                myNet = new MLP(neurons.get(0), neurons.get(1));
                break;
            case 3:
                myNet = new MLP(neurons.get(0), neurons.get(1), neurons.get(2));
                break;
            case 4:
                myNet = new MLP(neurons.get(0), neurons.get(1), neurons.get(2), neurons.get(3));
                break;
            case 5:
                myNet = new MLP(neurons.get(0), neurons.get(1), neurons.get(2), neurons.get(3),
                    neurons.get(4));
                break;
            case 6:
                myNet = new MLP(neurons.get(0), neurons.get(1), neurons.get(2), neurons.get(3),
                    neurons.get(4), neurons.get(5));
                break;
        }
        
        
        try {
            myNet.setWeights(prevModelFileName);
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(null, "Cannot load the previous trained model file!");
        }
    }
    
    private void vectorizeInputOutput(int sample) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                inp[x + y * width] = (double)(processed[sample][x][y]) / ColorHSV.nColors;
            }
        }
        
        for (int i = 0; i < nOutput; ++i) {
            out[i] = 0.0;
        }
        out[label[sample]] = 1.0;
    }
    
    private int findBest(double predict[]) {
        int p = 0;
        for (int i = 1; i < nOutput; ++i) {
            if (predict[i] > predict[p]) {
                p = i;
            }
        }
        return p;
    }
    
    private ActionListener nextSampleAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {            
            if (sample >= nTesting) {
                return;
            }
            
            sample++;
            vectorizeInputOutput(sample - 1);
            
            final double err = myNet.Predict(inp, out, predict);
            
            final int p = findBest(predict);
            if (p == label[sample - 1]) {
                correct++;
            }
            
            final int s = sample;
            final int c = correct;
            final int rate = sample * 100 / nTesting;
            
            DecimalFormat df = new DecimalFormat("#.##");      
            final double accuracy = Double.valueOf(df.format(c * 100.0 / s));
            
            try {
                Thread.sleep(30);
            } catch(InterruptedException exc) {
                Thread.currentThread().interrupt();
            }
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setValue(rate);
                            statusText.setText("Testing sample " + Integer.toString(s) + " / " + Integer.toString(nTesting));
                            sampleLabel.setText("Sample: " + Integer.toString(s) + " / " + Integer.toString(nTesting));
                            correctLabel.setText("No. correct samples: " + Integer.toString(c));
                            accuracyLabel.setText("Accuracy: " + Double.toString(accuracy) + "%");
                            outputLabel.setText("Output: " + Integer.toString(label[s - 1]));
                            predictLabel.setText("Predict: " + Integer.toString(p));
                            errorLabel.setText("Error: " + Double.toString(err));
                            drawInput(s - 1);
                        }
                    });
                }
            }).start();
        }
    };
    
    private ActionListener allSamplesAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    nextSample.setEnabled(false);
                    allSamples.setEnabled(false);

                    while (sample < nTesting) {
                        sample++;
                        vectorizeInputOutput(sample - 1);
                        
                        final double err = myNet.Predict(inp, out, predict);
                        
                        final int p = findBest(predict); 
                        if (p == label[sample - 1]) {
                            correct++;
                        }

                        final int s = sample;
                        final int c = correct;
                        final int rate = sample * 100 / nTesting;

                        DecimalFormat df = new DecimalFormat("#.##");      
                        final double accuracy = Double.valueOf(df.format(c * 100.0 / s));

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setValue(rate);
                                        statusText.setText("Testing sample " + Integer.toString(s) + " / " + Integer.toString(nTesting));
                                        sampleLabel.setText("Sample: " + Integer.toString(s) + " / " + Integer.toString(nTesting));
                                        correctLabel.setText("No. correct samples: " + Integer.toString(c));
                                        accuracyLabel.setText("Accuracy: " + Double.toString(accuracy) + "%");
                                        outputLabel.setText("Output: " + Integer.toString(label[s - 1]));
                                        predictLabel.setText("Predict: " + Integer.toString(p));
                                        errorLabel.setText("Error: " + Double.toString(err));
                                        drawInput(s - 1);
                                    }
                                });
                            }
                        }).start();
                    }

                    nextSample.setEnabled(true);
                    allSamples.setEnabled(true);
                }
            }).start(); 
        }
    };
    
    private void loadDatabase() throws IOException {
        final BufferedInputStream imagesFile;
	final BufferedInputStream labelsFile;
        
        try {
            imagesFile = new BufferedInputStream(new FileInputStream(databaseFolder + imagesFileName));
            labelsFile = new BufferedInputStream(new FileInputStream(databaseFolder + labelsFileName));
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(null, exc.toString());
            return;
        }
        
        for (int i = 0; i < 6; ++i) {
            imagesFile.read();
        }
		
	for (int i = 0; i < 4; ++i) {
            labelsFile.read();
	}
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                nextSample.setEnabled(false);
                                allSamples.setEnabled(false);
                            }
                        });
                    }
                }).start();
                
                for (int i = 0; i < nTesting; ++i) {
                    try {
                        label[i] = labelsFile.read();
                    } catch (IOException exc) {
                    }
                    
                    for (int y = 0; y < height; ++y) {
                        for (int x = 0; x < width; ++x) {
                            try {
                                red[i][x][y] = imagesFile.read();
                            } catch (IOException exc) {
                            }
                        }
                    }
                    for (int y = 0; y < height; ++y) {
                        for (int x = 0; x < width; ++x) {
                            try {
                                green[i][x][y] = imagesFile.read();
                            } catch (IOException exc) {
                            }
                        }
                    }
                    for (int y = 0; y < height; ++y) {
                        for (int x = 0; x < width; ++x) {
                            try {
                                blue[i][x][y] = imagesFile.read();
                            } catch (IOException exc) {
                            }
                        }
                    }
                    
                    for (int x = 0; x < width; ++x) {
                        for (int y = 0; y < height; ++y) {
                            processed[i][x][y] = ColorHSV.classify(red[i][x][y], green[i][x][y], blue[i][x][y]);
                        }
                    }

                    final int rate = (i + 1) * 100 / nTesting;
                    final int j = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setValue(rate);
                                    statusText.setText("Reading sample " + Integer.toString(j + 1) + " / " + Integer.toString(nTesting));
                                    sampleLabel.setText("Sample: " + Integer.toString(j + 1) + " / " + Integer.toString(nTesting));
                                    outputLabel.setText("Output: " + Integer.toString(label[j]));
                                }
                            });
                        }
                    }).start();
                }
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                nextSample.setEnabled(true);
                                allSamples.setEnabled(true);
                                
                                progressBar.setValue(0);
                                statusText.setText("Testing phase");
                                sampleLabel.setText("Sample: 0 / " + Integer.toString(nTesting));
                                outputLabel.setText("Output:");
                            }
                        });
                    }
                }).start();
            }
        }).start();
        
        nextSample.setEnabled(true);
        allSamples.setEnabled(true);
    }
    
    private int RGB(int red, int green, int blue){
        return (0xff000000) | (red << 16) | (green << 8) | blue;
    }
    
    private void drawInput(int index) {
        final int w = zoomPara * width;
        final int h = zoomPara * height;
        
        final BufferedImage inputImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final BufferedImage processedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                int r = red[index][x / zoomPara][y / zoomPara];
                int g = green[index][x / zoomPara][y / zoomPara];
                int b = blue[index][x / zoomPara][y / zoomPara];
                int rgb = RGB(r, g, b);
                inputImage.setRGB(x, y, rgb);
                
                int c = processed[index][x / zoomPara][y / zoomPara];
                processedImage.setRGB(x, y, ColorHSV.int2color(c).getRGB());
            }
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        inputButton.setSize(w, h);
                        inputButton.setIcon(new ImageIcon(inputImage));
                        
                        processedInputButton.setSize(w, h);
                        processedInputButton.setIcon(new ImageIcon(processedImage));
                    }
                });
            }
        }).start();
    }
    
    private void initGUI() {
        int x = marginFrame;
        int y = marginFrame;
        
        statusLabel = new JLabel();
        statusLabel.setText("Status:");
        statusLabel.setBounds(x, y, widthLabel, heightComponent);
        statusLabel.setForeground(Color.blue);
        add(statusLabel);
        
        statusText = new JTextField();
        statusText.setEditable(false);
        statusText.setBounds(x + widthLabel, y, widthFrame - 2 * marginFrame - widthLabel, heightComponent);
        add(statusText);
        
        y += heightComponent + marginFrame;
        
        progressLabel = new JLabel();
        progressLabel.setText("Progress:");
        progressLabel.setBounds(x, y, widthLabel, heightComponent);
        progressLabel.setForeground(Color.blue);
        add(progressLabel);
        
        progressBar.setBounds(x + widthLabel, y, widthFrame - 2 * marginFrame - widthLabel, heightComponent);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        add(progressBar);
        
        y += heightComponent + marginFrame;
        
        int length = (widthFrame - 3 * marginFrame) / 3;
        
        sampleLabel = new JLabel();
        sampleLabel.setText("Sample:");
        sampleLabel.setBounds(x, y, length, heightComponent);
        add(sampleLabel);
        
        nextSample = new JButton("Next sample");
        nextSample.setBounds(x + length, y, length, heightComponent);
        nextSample.addActionListener(nextSampleAction);
        add(nextSample);
        
        allSamples = new JButton("All samples (non-stop)");
        allSamples.setBounds(x + 2 * length + marginFrame, y, length, heightComponent);
        allSamples.addActionListener(allSamplesAction);
        add(allSamples);
        
        y += heightComponent + marginFrame;
        
        correctLabel = new JLabel();
        correctLabel.setText("No. correct samples:");
        correctLabel.setBounds(x, y, length, heightComponent);
        add(correctLabel);
        
        accuracyLabel = new JLabel();
        accuracyLabel.setText("Accuracy:");
        accuracyLabel.setBounds(x + length, y, length, heightComponent);
        add(accuracyLabel);
        
        y += heightComponent + marginFrame;
        
        outputLabel = new JLabel();
        outputLabel.setText("Output:");
        outputLabel.setBounds(x, y, length, heightComponent);
        outputLabel.setForeground(Color.blue);
        add(outputLabel);
        
        predictLabel = new JLabel();
        predictLabel.setText("Predict:");
        predictLabel.setBounds(x + length, y, length, heightComponent);
        predictLabel.setForeground(Color.blue);
        add(predictLabel);
        
        errorLabel = new JLabel();
        errorLabel.setText("Error:");
        errorLabel.setBounds(x + 2 * length, y, length, heightComponent);
        add(errorLabel);
        
        y += heightComponent + marginFrame;
        
        length = (widthFrame - 2 * marginFrame) / 2;
        
        inputLabel = new JLabel();
        inputLabel.setText("Input:");
        inputLabel.setBounds(x, y, length, heightComponent);
        add(inputLabel);
        
        processedInputLabel = new JLabel();
        processedInputLabel.setText("Processed Input:");
        processedInputLabel.setBounds(x + length, y, length, heightComponent);
        add(processedInputLabel);
        
        y += heightComponent + marginFrame;
        
        inputButton = new JButton();
        inputButton.setLocation(x, y);
        add(inputButton);
        
        processedInputButton = new JButton();
        processedInputButton.setLocation(x + length, y);
        add(processedInputButton);
    }
}
