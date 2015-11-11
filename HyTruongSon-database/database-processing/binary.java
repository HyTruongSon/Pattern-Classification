// Software: Multi-Layer Perceptrons in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c), all rights reserved. Only use for academic purposes.

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class binary {
    
    private final static String magic_line = "Database: Hy Truong Son's handwriting database. Hy Truong Son and Hy Thi Hong Nhung are the authors of this database.";
    
    private static ArrayList<Integer> getBytes(int number) {
        ArrayList<Integer> ret = new ArrayList<>();
        while (number > 0) {
            ret.add(number % (1 << 8));
            number /= (1 << 8);
        }
        return ret;
    }
    
    public static int RGB(int red, int green, int blue) {
		return (0xff000000) | (red << 16) | (green << 8) | blue;
	}
    
    private static void convert_image(String listFileName, String binaryFileName) throws IOException {
        BufferedReader list = new BufferedReader(new FileReader(listFileName));
        BufferedOutputStream bin = new BufferedOutputStream(new FileOutputStream(binaryFileName));
        
        ArrayList<Integer> bytes;
        
        bytes = getBytes(magic_line.length());
        for (int i = 0; i < bytes.size(); ++i) {
            bin.write((char)((int)(bytes.get(i)))); 
        }
        if (bytes.size() == 1) {
            bin.write((char)(0));
        }
        for (int i = 0; i < magic_line.length(); ++i) {
            bin.write(magic_line.charAt(i));
        }
        
        int nSamples = 0;
        try {
            nSamples = Integer.parseInt(list.readLine());
        } catch (NumberFormatException exc) {
            System.err.println(exc.toString());
        } 
        
        bytes = getBytes(nSamples);
        for (int i = 0; i < bytes.size(); ++i) {
            bin.write((char)((int)(bytes.get(i)))); 
        }
        for (int i = bytes.size() + 1; i <= 4; ++i) {
            bin.write(0);
        }
        
        int red[][];
        int green[][];
        int blue[][];
        
        for (int sample = 0; sample < nSamples; ++sample) {
            String imageFileName = list.readLine();
            BufferedImage image = ImageIO.read(new File(imageFileName));
            
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            
            red = new int [width][height];
            green = new int [width][height];
            blue = new int [width][height];
            
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    int rgb = image.getRGB(x, y);
                    red[x][y] = (rgb & 0x00ff0000) >> 16;
				    green[x][y] = (rgb & 0x0000ff00) >> 8;
				    blue[x][y] = rgb & 0x000000ff;
                }
            }
            
            bytes = getBytes(height);
            for (int i = 0; i < bytes.size(); ++i) {
                bin.write((char)((int)(bytes.get(i)))); 
            }
            for (int i = bytes.size() + 1; i <= 2; ++i) {
                bin.write(0);
            }
            
            bytes = getBytes(width);
            for (int i = 0; i < bytes.size(); ++i) {
                bin.write((char)((int)(bytes.get(i)))); 
            }
            for (int i = bytes.size() + 1; i <= 2; ++i) {
                bin.write(0);
            }
            
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    bin.write((char)(red[x][y]));
                }
            }
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    bin.write((char)(green[x][y]));
                }
            }
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    bin.write((char)(blue[x][y]));
                }
            }
            
            // Writing the image to check the file reading correctness
            /*
            BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		    for (int x = 0; x < width; ++x) {
			    for (int y = 0; y < height; ++y) {
				    image2.setRGB(x, y, RGB(red[x][y], green[x][y], blue[x][y]));
			    }
		    }
		    ImageIO.write(image2, "png", new File(Integer.toString(sample) + ".png"));
		    */
        }
        
        list.close();
        bin.close();
    }
    
    private static void convert_label(String listFileName, String binaryFileName) throws IOException {
        BufferedReader list = new BufferedReader(new FileReader(listFileName));
        BufferedOutputStream bin = new BufferedOutputStream(new FileOutputStream(binaryFileName));
        
        ArrayList<Integer> bytes;
        
        bytes = getBytes(magic_line.length());
        for (int i = 0; i < bytes.size(); ++i) {
            bin.write((char)((int)(bytes.get(i)))); 
        }
        if (bytes.size() == 1) {
            bin.write((char)(0));
        }
        for (int i = 0; i < magic_line.length(); ++i) {
            bin.write(magic_line.charAt(i));
        }
        
        int nSamples = 0;
        try {
            nSamples = Integer.parseInt(list.readLine());
        } catch (NumberFormatException exc) {
            System.err.println(exc.toString());
        } 
        
        bytes = getBytes(nSamples);
        for (int i = 0; i < bytes.size(); ++i) {
            bin.write((char)((int)(bytes.get(i)))); 
        }
        for (int i = bytes.size() + 1; i <= 4; ++i) {
            bin.write(0);
        }
        
        for (int i = 0; i < nSamples; ++i) {
            String str = list.readLine();
            char label = str.charAt(0);
            bin.write(label);
        }
        
        list.close();
        bin.close();
    }
    
    public static void main(String args[]) {
        try {
            convert_image("lower.image", "lower.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing lower.image to binary file has some problem!");
        }
        
        try {
            convert_label("lower.label", "lower.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing lower.label to binary file has some problem!");
        }

        try {
            convert_image("lower.train.image", "lower.train.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing lower.train.image to binary file has some problem!");
        }
        
        try {
            convert_label("lower.train.label", "lower.train.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing lower.train.label to binary file has some problem!");
        }
        
        try {
            convert_image("lower.test.image", "lower.test.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing lower.image to binary file has some problem!");
        }
        
        try {
            convert_label("lower.test.label", "lower.test.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing lower.label to binary file has some problem!");
        }
        
        try {
            convert_image("upper.image", "upper.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing upper.image to binary file has some problem!");
        }
        
        try {
            convert_label("upper.label", "upper.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing upper.label to binary file has some problem!");
        }
        
        try {
            convert_image("upper.train.image", "upper.train.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing upper.train.image to binary file has some problem!");
        }
        
        try {
            convert_label("upper.train.label", "upper.train.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing upper.train.label to binary file has some problem!");
        }
        
        try {
            convert_image("upper.test.image", "upper.test.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing upper.test.image to binary file has some problem!");
        }
        
        try {
            convert_label("upper.test.label", "upper.test.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing upper.test.label to binary file has some problem!");
        }
    }    
    
}
