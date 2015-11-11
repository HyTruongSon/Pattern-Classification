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
    
    final private static int widthImage = 19;
    final private static int heightImage = 19;
    
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
        
        int nSamples = 0;
        try {
            nSamples = Integer.parseInt(list.readLine());
        } catch (NumberFormatException exc) {
            System.err.println(exc.toString());
        } 
        
        ArrayList<Integer> bytes = getBytes(nSamples);
        for (int i = 0; i < bytes.size(); ++i) {
            bin.write((char)((int)(bytes.get(i)))); 
        }
        for (int i = bytes.size() + 1; i <= 4; ++i) {
            bin.write(0);
        }
        bin.write((char)(widthImage));
        bin.write((char)(heightImage));
        
        int gray[][] = new int [widthImage][heightImage];
        
        for (int i = 0; i < nSamples; ++i) {
            String imageFileName = list.readLine();
            
            // PGM 256 image reading
            PGM256.getGrayImage(imageFileName, gray, widthImage, heightImage);
            
            // Normal image reading
            /*
            int rgb[][] = new int [widthImage][heightImage];
            BufferedImage image = ImageIO.read(new File(imageFileName));
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            if ((width != widthImage) || (height != heightImage)) {
                System.err.println("Sizes conflict!");
            }
            for (int x = 0; x < widthImage; ++x) {
                for (int y = 0; y < heightImage; ++y) {
                    rgb[x][y] = image.getRGB(x, y);
                    int red = (rgb[x][y] & 0x00ff0000) >> 16;
				    int green = (rgb[x][y] & 0x0000ff00) >> 8;
				    int blue = rgb[x][y] & 0x000000ff;
				    gray[x][y]= (red * 299 + green * 587 + blue * 114) / 1000;
                }
            }
            */
            
            for (int y = 0; y < heightImage; ++y) {
                for (int x = 0; x < widthImage; ++x) {
                    bin.write((char)(gray[x][y]));
                }
            }
            
            // Writing the image to check the file reading correctness
            /*
            BufferedImage image = new BufferedImage(widthImage, heightImage, BufferedImage.TYPE_INT_RGB);
		    for (int x = 0; x < widthImage; ++x) {
			    for (int y = 0; y < heightImage; ++y) {
				    image.setRGB(x, y, RGB(gray[x][y], gray[x][y], gray[x][y]));
			    }
		    }
		    ImageIO.write(image, "png", new File(Integer.toString(i) + ".png"));
		    */
        }
        
        list.close();
        bin.close();
    }
    
    private static void convert_label(String listFileName, String binaryFileName) throws IOException {
        BufferedReader list = new BufferedReader(new FileReader(listFileName));
        BufferedOutputStream bin = new BufferedOutputStream(new FileOutputStream(binaryFileName));
        
        int nSamples = 0;
        try {
            nSamples = Integer.parseInt(list.readLine());
        } catch (NumberFormatException exc) {
            System.err.println(exc.toString());
        } 
        
        ArrayList<Integer> bytes = getBytes(nSamples);
        for (int i = 0; i < bytes.size(); ++i) {
            bin.write((char)((int)(bytes.get(i)))); 
        }
        for (int i = bytes.size() + 1; i <= 4; ++i) {
            bin.write(0);
        }
        
        for (int i = 0; i < nSamples; ++i) {
            int label = 0;
            try {
                label = Integer.parseInt(list.readLine());
            } catch (NumberFormatException exc) {
                System.err.println(exc.toString());
            }
            bin.write((char)(label));
        }
        
        list.close();
        bin.close();
    }
    
    public static void main(String args[]) {
        try {
            convert_image("train.image", "train.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing train-image to binary file has some problem!");
        }
        
        try {
            convert_label("train.label", "train.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing train-label to binary file has some problem!");  
        }
        
        try {
            convert_image("test.image", "test.image.bin");
        } catch (IOException exc) {
            System.err.println("Writing test-image to binary file has some problem!");
        }
        
        try {
            convert_label("test.label", "test.label.bin");
        } catch (IOException exc) {
            System.err.println("Writing test-label to binary file has some problem!");
        }
    }    
    
}
