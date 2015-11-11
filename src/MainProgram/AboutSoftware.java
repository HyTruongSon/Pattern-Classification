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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AboutSoftware extends JFrame {
    
    private final String titleFrame = "About the software";
    private final int widthFrame = 800;
    private final int heightFrame = 600;
    private final int marginFrame = 20;
    private final int heightComponent = 30;
    
    private String info[] = {
        "Name:",
        "Multi-Layer Perceptrons and Applications in Pattern Recognition and Classification", 
        "Author:",
        "Hy Truong Son",
        "Purpose:",
        "For the class Application Development, Eotvos Lorand University, Fall 2015.",
        "Content:",
        "Training and testing of Multi-Layer Perceptrons (MLP) over different image databases.",
        "Note:",
        "MLP is a kind of Feed-forward Artificial Neural Network.",
        "Development tool:",
        "Java JDK 8, and NetBeans IDE 8.0.2",
        "Email:",
        "sonpascal93@gmail.com",
        "Website:",
        "http://people.inf.elte.hu/hytruongson/",
        "Acknowledgement:",
        "Copyright 2015 (c) Hy Truong Son. All rights reserved."
    };
    
    public AboutSoftware() {
        setTitle(titleFrame);
        setSize(widthFrame, heightFrame);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        int x = marginFrame;
        int y = marginFrame;
        
        int i = 0;
        while (i < info.length) {
            JLabel label = new JLabel();
            label.setForeground(Color.blue);
            label.setBounds(x, y, widthFrame - 2 * marginFrame, heightComponent);
            label.setText(info[i]);
            add(label);
            
            ++i;
            y += heightComponent;
            
            JTextField text = new JTextField();
            text.setBounds(x, y, widthFrame - 2 * marginFrame, heightComponent);
            text.setText(info[i]);
            text.setEditable(false);
            add(text);
            
            ++i;
            y += heightComponent;
        }
        
        setVisible(true);
    }
    
}
