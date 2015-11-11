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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AboutDatabase extends JFrame {
    
    private final String imagesFolder = "about-databases/";
    
    private final String titleFrame = "About databases";
    private final int widthFrame = 800;
    private final int heightFrame = 600;
    private final int marginFrame = 20;
    private final int heightComponent = 30;
    private final int widthLabel = 100;
    private final int widthBox = 450;
    
    private JLabel databaseLabel;
    private JComboBox databaseBox;
    private JButton viewButton;
    private JLabel labels[];
    private JTextField texts[];
    private JLabel exampleLabel;
    
    private int finalY = 400;
    
    private int nButtons = 10;
    private JButton buttons[] = new JButton [nButtons];
    
    private final String databaseOptions[] = {
        "MNIST - Digit handwriting database - Yann LeCun",
        "Character handwriting database - Hy Truong Son",
        "CBCL Face Database - MIT",
        "NAO robot image database - University of Bonn"
    };
    
    private final String item[] = {
        "Full name:",
        "Author:",
        "Website:",
        "Database size:",
        "Database description:"
    };
    
    private final String info[][] = {
        {
            "The MNIST database of handwritten digits",
            "Yann LeCun (NYU), Corinna Cortes (Google Labs, New York), Christopher J.C. Burges (Microsoft Research, Redmond).",
            "http://yann.lecun.com/exdb/mnist/",
            "60,000 samples for training and 10,000 samples for testing.",
            "A subset of larger set available from NIST. Size-normalized, centered 28 x 28 gray scale images."
        },
        
        {
            "Hy Truong Son's Character handwriting database",
            "Hy Truong Son (the author of this software)",
            "http://people.inf.elte.hu/hytruongson/",
            "Total 33,859 samples.",
            "Handwritten samples from Hy Truong Son and Hy Thi Hong Nhung (sister)."
        },
        
        {
            "CBCL Face Database",
            "MIT Center for biological and computational learning",
            "http://cbcl.mit.edu/software-datasets/FaceData2.html",
            "Training 2,429 faces and 4,548 non-faces. Testing 472 faces and 23,573 non-faces.",
            "19 x 19 Grayscale PGM format images. 27 Megabytes compressed."
        },
        
        {
            "University of Bonn's NAO robot image database",
            "RoboCup team (Robot Soccer Worldcup), University of Bonn (Germany).",
            "http://www.uni-bonn.de",
            "Total 1,909 samples including robot-feet images and non robot-feet ones.",
            "Small size, color images."
        },
    };
    
    private int currentDatabase = 0;
    
    public AboutDatabase() {
        setTitle(titleFrame);
        setSize(widthFrame, heightFrame);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        labels = new JLabel [item.length];
        texts = new JTextField [item.length];
        
        for (int i = 0; i < item.length; ++i) {
            labels[i] = new JLabel();
            add(labels[i]);
            
            texts[i] = new JTextField();
            texts[i].setEditable(false);
            add(texts[i]);
        }
        
        for (int i = 0; i < nButtons; ++i) {
            buttons[i] = new JButton();
            add(buttons[i]);
        }
        
        addComponents();
        
        setVisible(true);
    }
    
    private void reupdate() {
        int x = marginFrame;
        int y = finalY + marginFrame;
        int size = (widthFrame - (nButtons + 1) * marginFrame) / nButtons;
        
        for (int i = 0; i < texts.length; ++i) {
            texts[i].setText(info[currentDatabase][i]);
        }
        
        for (int i = 0; i < nButtons; ++i) {
            String imageName = imagesFolder + "/" + Integer.toString(currentDatabase) + "/" +
                    Integer.toString(i) + ".jpg";
            
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File(imageName));
            } catch (IOException exc) {
                System.err.println(exc.toString());
            }
            
            int w1 = image.getWidth(null);
            int h1 = image.getHeight(null);
            double ratio = (double)(size) / w1;
            int w2 = (int)(ratio * w1);
            int h2 = (int)(ratio * h1);
            
            buttons[i].setIcon(new ImageIcon(image.getScaledInstance(w2, h2, Image.SCALE_DEFAULT)));
            buttons[i].setBounds(x, y, w2, h2);
            x += w2 + marginFrame;
        }
    }
    
    private void addComponents() {
        int x = marginFrame;
        int y = marginFrame;
        
        databaseLabel = new JLabel("Database:");
        databaseLabel.setBounds(x, y, widthLabel, heightComponent);
        databaseLabel.setForeground(Color.blue);
        add(databaseLabel);
        
        x += widthLabel;
        
        databaseBox = new JComboBox(databaseOptions);
        databaseBox.setBounds(x, y, widthBox, heightComponent);
        add(databaseBox);
        
        /*
        databaseBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        */
        
        x += widthBox + marginFrame;
        
        viewButton = new JButton("View Information");
        viewButton.setBounds(x, y, widthFrame - x - marginFrame, heightComponent);
        add(viewButton);
        
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentDatabase = databaseBox.getSelectedIndex();
                reupdate();
            }
        });
        
        x = marginFrame;
        y += heightComponent + marginFrame;
        
        for (int i = 0; i < labels.length; ++i) {
            labels[i].setText(item[i]);
            labels[i].setBounds(x, y, widthFrame - 2 * marginFrame, heightComponent);
            labels[i].setForeground(Color.blue);
            
            y += heightComponent;
            
            texts[i].setBounds(x, y, widthFrame - 2 * marginFrame, heightComponent);
            
            y += heightComponent;
        }
        
        exampleLabel = new JLabel("Examples from the database:");
        exampleLabel.setBounds(x, y, widthFrame - 2 * marginFrame, heightComponent);
        exampleLabel.setForeground(Color.blue);
        add(exampleLabel);
        
        finalY = y + heightComponent;
        
        reupdate();
    }
    
}
