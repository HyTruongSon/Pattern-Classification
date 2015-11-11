// Software: Multi-Layer Perceptrons in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c), all rights reserved. Only use for academic purposes.

package MLP.HTS.upper;

import MyLib.EnhanceGrayImage;
import MyLib.MLP;
import MyLib.Normalization;
import MyLib.SmoothGrayImage;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Training extends JFrame {
    
    private final String databaseFolder = "HyTruongSon-database/";
    private final String imagesFileName = "upper.train.image.bin";
    private final String labelsFileName = "upper.train.label.bin";
    
    private final int nTraining;
    private final int width = 50;
    private final int height = 50;
    private int sample = 0;
    private int iteration = 0;
    
    private final double Momentum = 0.9;
    private final double Epsilon = 1e-3;
    
    private MLP myNet;
    private double inp[];
    private double out[];
    private double predict[];
    private int nInput, nOutput;
    
    private class AnImage {
        public int widthImage, heightImage;
        public char red[][];
        public char green[][];
        public char blue[][];
    }
    private AnImage input[];
    private int processed[][][]; 
    private char label[];
    
    private final String titleFrame = "MLP - Training - Hy Truong Son's upper case handwriting database";
    private final int widthFrame = 800;
    private final int heightFrame = 600;
    private final int marginFrame = 20;
    private final int widthLabel = 150;
    private final int heightComponent = 30;
    private final int zoomPara = 2;
    
    private final ArrayList<Integer> neurons;
    private final int nIterations;
    private final double learningRate;
    private final String modelFileName;
    private final String prevModelFileName;
    
    private JLabel statusLabel;
    private JTextField statusText;
    private JLabel progressLabel;
    private final JProgressBar progressBar = new JProgressBar();
    private JLabel sampleLabel;
    private JButton nextSample;
    private JButton allSamples;
    private JLabel iterationLabel;
    private JButton nextIteration;
    private JButton allIterations;
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
        System.out.println("Number of iterations: " + Integer.toString(nIterations));
        System.out.println("Learing rate: " + Double.toString(learningRate));
        System.out.println("Model file name: " + modelFileName);
        System.out.println("Previous model file name: " + prevModelFileName);
    }
    
    public Training(int nTraining, ArrayList<Integer> neurons, int nIterations, double learningRate, 
            String modelFileName, String prevModelFileName) {
        this.nTraining = nTraining;
        this.neurons = new ArrayList<>();
        for (int i = 0; i < neurons.size(); ++i) {
            this.neurons.add(neurons.get(i));
        }
        this.nIterations = nIterations;
        this.learningRate = learningRate;
        this.modelFileName = modelFileName;
        this.prevModelFileName = prevModelFileName;
        
        input = new AnImage [this.nTraining];
        processed = new int [this.nTraining][width][height]; 
        label = new char [this.nTraining];
        
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
        
        myNet.setEpochs(nIterations);
	myNet.setLearningRate(learningRate);
	myNet.setMomentum(Momentum);
	myNet.setEpsilon(Epsilon);
        
        if ((prevModelFileName != null) && (prevModelFileName.length() > 0)) {
            try {
                myNet.setWeights(prevModelFileName);
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(null, "Cannot load the previous trained model file!");
            }
        }
    }
    
    private void vectorizeInputOutput(int sample) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                inp[x + y * width] = (double)(processed[sample][x][y]) / 256;
            }
        }
        
        for (int i = 0; i < nOutput; ++i) {
            out[i] = 0.0;
        }
        out[(int)(label[sample]) - (int)('A')] = 1.0;
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
            nextIteration.setEnabled(true);
            allIterations.setEnabled(true);
            iteration = 0;
            
            if (sample >= nTraining) {
                return;
            }
            
            sample++;
            
            vectorizeInputOutput(sample - 1);
            final double err = myNet.Predict(inp, out, predict);
            final int p = findBest(predict); 
            
            final int s = sample;
            final int rate = sample * 100 / nTraining;
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setValue(rate);
                            statusText.setText("Training sample " + Integer.toString(s) + " / " + Integer.toString(nTraining));
                            sampleLabel.setText("Sample: " + Integer.toString(s) + " / " + Integer.toString(nTraining));
                            outputLabel.setText("Output: " + label[s - 1]);
                            predictLabel.setText("Predict: " + (char)('A' + p));
                            errorLabel.setText("Error: " + Double.toString(err));
                            iterationLabel.setText("Iteration: 0 / " + Integer.toString(nIterations));
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
                    nextIteration.setEnabled(false);
                    allIterations.setEnabled(false);
                    allSamples.setEnabled(false);

                    while (sample < nTraining) {
                        if (sample % 100 == 0) {
                            statusText.setText("Saving model to file " + modelFileName);
                            try {
                                myNet.writeWeights(modelFileName);
                            } catch (IOException exc) {
                                JOptionPane.showMessageDialog(null, "Could not save the model to file!");
                            }
                        }
                        sample++;

                        vectorizeInputOutput(sample - 1);
                        myNet.StochasticLearning(inp, out);
                        final double err = myNet.Predict(inp, out, predict);
                        final int p = findBest(predict); 
                        
                        final int s = sample;
                        final int rate = sample * 100 / nTraining;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setValue(rate);
                                        statusText.setText("Training sample " + Integer.toString(s) + " / " + Integer.toString(nTraining));
                                        sampleLabel.setText("Sample: " + Integer.toString(s) + " / " + Integer.toString(nTraining));
                                        outputLabel.setText("Output: " + label[s - 1]);
                                        predictLabel.setText("Predict: " + (char)('A' + p));
                                        errorLabel.setText("Error: " + Double.toString(err));
                                        iterationLabel.setText("Iteration: " + Integer.toString(nIterations) +
                                                " / " + Integer.toString(nIterations));
                                        drawInput(s - 1);
                                    }
                                });
                            }
                        }).start();
                    }

                    nextSample.setEnabled(true);
                    nextIteration.setEnabled(true);
                    allIterations.setEnabled(true);
                    allSamples.setEnabled(true);
                    
                    statusText.setText("Saving model to file " + modelFileName);
                    try {
                        myNet.writeWeights(modelFileName);
                    } catch (IOException exc) {
                        JOptionPane.showMessageDialog(null, "Could not save the model to file!");
                    }
                }
            }).start(); 
        }
    };
    
    private ActionListener nextIterationAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (iteration == nIterations) {
                return;
            }
            iteration++;
            
            final double err = myNet.StochasticLearning(inp, out, 1);
            myNet.Predict(inp, predict);
            final int p = findBest(predict); 
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            predictLabel.setText("Predict: " + (char)('A' + p));
                            errorLabel.setText("Error: " + Double.toString(err));
                            iterationLabel.setText("Iteration: " + Integer.toString(iteration) + 
                                    " / " + Integer.toString(nIterations));
                        }
                    });
                }
            }).start();
        }
    };
    
    private ActionListener allIterationsAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (iteration == nIterations) {
                return;
            }
            iteration = nIterations;
            
            final double err = myNet.StochasticLearning(inp, out);
            myNet.Predict(inp, predict);
            final int p = findBest(predict); 

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            predictLabel.setText("Predict: " + (char)('A' + p));
                            errorLabel.setText("Error: " + Double.toString(err));
                            iterationLabel.setText("Iteration: " + Integer.toString(iteration) + 
                                        " / " + Integer.toString(nIterations));
                        }
                    });
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
        
        int nMagic;
        
        nMagic = (int)(imagesFile.read());
        nMagic += (int)(imagesFile.read()) * (1 << 8);
        for (int i = 0; i < nMagic; ++i) {
            imagesFile.read();
        }
        for (int i = 0; i < 4; ++i) {
            imagesFile.read();
        }

        nMagic = (int)(labelsFile.read());
        nMagic += (int)(labelsFile.read()) * (1 << 8);
        for (int i = 0; i < nMagic; ++i) {
            labelsFile.read();
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
                                nextIteration.setEnabled(false);
                                allIterations.setEnabled(false);
                            }
                        });
                    }
                }).start();
                
                for (int i = 0; i < nTraining; ++i) {
                    try {
                        label[i] = (char)(labelsFile.read());
                    } catch (IOException exc) {
                    }
                    
                    int heightImage = 0;
                    try {
                        heightImage = (int)(imagesFile.read());
                        heightImage += (int)(imagesFile.read()) * (1 << 8);
                    } catch (IOException exc) {
                    }
                    
                    int widthImage = 0;
                    try {
                        widthImage = (int)(imagesFile.read());
                        widthImage += (int)(imagesFile.read()) * (1 << 8);
                    } catch (IOException exc) {
                    }
                        
                    input[i] = new AnImage();
                    input[i].widthImage = widthImage;
                    input[i].heightImage = heightImage;
                    input[i].red = new char [widthImage][heightImage];
                    input[i].green = new char [widthImage][heightImage];
                    input[i].blue = new char [widthImage][heightImage];
                    int gray[][] = new int [widthImage][heightImage];
                    
                    for (int y = 0; y < heightImage; ++y) {
                        for (int x = 0; x < widthImage; ++x) {
                            try {
                                input[i].red[x][y] = (char)(imagesFile.read());
                            } catch (IOException exc) {
                            }
                        }
                    }
                    for (int y = 0; y < heightImage; ++y) {
                        for (int x = 0; x < widthImage; ++x) {
                            try {
                                input[i].green[x][y] = (char)(imagesFile.read());
                            } catch (IOException exc) {
                            }
                        }
                    }
                    for (int y = 0; y < heightImage; ++y) {
                        for (int x = 0; x < widthImage; ++x) {
                            try {
                                input[i].blue[x][y] = (char)(imagesFile.read());
                            } catch (IOException exc) {
                            }
                        }
                    }
                    
                    for (int x = 0; x < widthImage; ++x) {
                        for (int y = 0; y < heightImage; ++y) {
                            int red = (int)(input[i].red[x][y]);
                            int green = (int)(input[i].green[x][y]);
                            int blue = (int)(input[i].blue[x][y]);
                            gray[x][y] = (red * 299 + green * 587 + blue * 114) / 1000;
                        }
                    }
                    
                    Normalization.Standardize(gray, processed[i], widthImage, heightImage, width, height);
                    // SmoothGrayImage.process(processed[i], width, height);
                    // EnhanceGrayImage.process(processed[i], width, height);
                    
                    final int rate = (i + 1) * 100 / nTraining;
                    final int j = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setValue(rate);
                                    statusText.setText("Reading sample " + Integer.toString(j + 1) + " / " + Integer.toString(nTraining));
                                    sampleLabel.setText("Sample: " + Integer.toString(j + 1) + " / " + Integer.toString(nTraining));
                                    outputLabel.setText("Output: " + label[j]);
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
                                statusText.setText("Training phase");
                                sampleLabel.setText("Sample: 0 / " + Integer.toString(nTraining));
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
        final int w1 = zoomPara * input[index].widthImage;
        final int h1 = zoomPara * input[index].heightImage;
        
        final int w2 = zoomPara * width;
        final int h2 = zoomPara * height;
        
        final BufferedImage inputImage = new BufferedImage(w1, h1, BufferedImage.TYPE_INT_RGB);
        final BufferedImage processedImage = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < w1; ++x) {
            for (int y = 0; y < h1; ++y) {
                int r = input[index].red[x / zoomPara][y / zoomPara];
                int g = input[index].green[x / zoomPara][y / zoomPara];
                int b = input[index].blue[x / zoomPara][y / zoomPara];
                int rgb = RGB(r, g, b);
                inputImage.setRGB(x, y, rgb);
            }
        }
        
        for (int x = 0; x < w2; ++x) {
            for (int y = 0; y < h2; ++y) {
                int gray = processed[index][x / zoomPara][y / zoomPara];
                int rgb = RGB(gray, gray, gray);
                processedImage.setRGB(x, y, rgb);
            }
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        inputButton.setSize(w1, h1);
                        inputButton.setIcon(new ImageIcon(inputImage));
                        
                        processedInputButton.setSize(w2, h2);
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
        
        iterationLabel = new JLabel();
        iterationLabel.setText("Iteration:");
        iterationLabel.setBounds(x, y, length, heightComponent);
        add(iterationLabel);
        
        nextIteration = new JButton("Next iteration");
        nextIteration.setBounds(x + length, y, length, heightComponent);
        nextIteration.addActionListener(nextIterationAction);
        add(nextIteration);
        
        allIterations = new JButton("All iterations (non-stop)");
        allIterations.setBounds(x + 2 * length + marginFrame, y, length, heightComponent);
        allIterations.addActionListener(allIterationsAction);
        add(allIterations);
        
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
