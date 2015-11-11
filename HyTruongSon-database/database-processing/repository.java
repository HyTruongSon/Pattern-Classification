// Software: Multi-Layer Perceptrons in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c), all rights reserved. Only use for academic purposes.

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class repository {
    
    private final static String lowerFolder = "lower-case/";
    private final static String upperFolder = "upper-case/";
    private final static int trainingPercent = 60;
    
    private static ArrayList<String> lowerImage;
    private static ArrayList<String> lowerLabel;
    private static ArrayList<String> upperImage;
    private static ArrayList<String> upperLabel;
    
    private static int lowerOrder[];
    private static int lowerTrainingOrder[];
    private static int lowerTestingOrder[];
    
    private static int upperOrder[];
    private static int upperTrainingOrder[];
    private static int upperTestingOrder[];
    
    private static ArrayList<String> make(String folderName) {
        File folder = new File(folderName);
        File listFiles[] = folder.listFiles();
        
        ArrayList<String> list = new ArrayList<>();
        
        for (int i = 0; i < listFiles.length; ++i) {
            File file = listFiles[i];
            if (file.isFile()) {
                list.add(folderName + file.getName());
            }
        }
        
        return list;
    }
    
    private static void save(int order[], ArrayList<String> image, ArrayList<String> label, String imageFileName, String labelFileName) throws IOException {
        PrintWriter imageFile = new PrintWriter(new FileWriter(imageFileName));
        PrintWriter labelFile = new PrintWriter(new FileWriter(labelFileName));
        
        imageFile.println(order.length);
        labelFile.println(order.length);
        
        for (int i = 0; i < order.length; ++i) {
            imageFile.println(image.get(order[i]));
            labelFile.println(label.get(order[i]));
        }
        
        imageFile.close();
        labelFile.close();
    } 
    
    public static void reorder() {
        Random rand = new Random();
    
        int nLower = lowerImage.size();
        lowerOrder = new int [nLower];
        for (int i = 0; i < nLower; ++i) {
            lowerOrder[i] = i;
        }
        
        for (int i = 0; i < nLower; ++i) {
            int j = Math.abs(rand.nextInt()) % nLower;
            int temp = lowerOrder[i];
            lowerOrder[i] = lowerOrder[j];
            lowerOrder[j] = temp;
        }
        
        int lowerTraining = nLower * trainingPercent / 100;
        int lowerTesting = nLower - lowerTraining;
        
        lowerTrainingOrder = new int [lowerTraining];
        lowerTestingOrder = new int [lowerTesting];
        
        for (int i = 0; i < lowerTraining; ++i) {
            lowerTrainingOrder[i] = lowerOrder[i];
        }
        
        for (int i = lowerTraining; i < nLower; ++i) {
            lowerTestingOrder[i - lowerTraining] = lowerOrder[i]; 
        }
        
        int nUpper = upperImage.size();
        upperOrder = new int [nUpper];
        for (int i = 0; i < nUpper; ++i) {
            upperOrder[i] = i;
        }
        
        for (int i = 0; i < nUpper; ++i) {
            int j = Math.abs(rand.nextInt()) % nUpper;
            int temp = upperOrder[i];
            upperOrder[i] = upperOrder[j];
            upperOrder[j] = temp;
        }
        
        int upperTraining = nUpper * trainingPercent / 100;
        int upperTesting = nUpper - upperTraining;
        
        upperTrainingOrder = new int [upperTraining];
        upperTestingOrder = new int [upperTesting];
        
        for (int i = 0; i < upperTraining; ++i) {
            upperTrainingOrder[i] = upperOrder[i];
        }
        
        for (int i = upperTraining; i < nUpper; ++i) {
            upperTestingOrder[i - upperTraining] = upperOrder[i];
        }
    }
    
    public static void main(String args[]) {
        lowerImage = new ArrayList<String>();
        lowerLabel = new ArrayList<String>();
        
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            ArrayList<String> path = make(lowerFolder + ch + "/");
            for (int i = 0; i < path.size(); ++i) {
                lowerImage.add(path.get(i));
                String str = "" +  ch;
                lowerLabel.add(str);
            }
        }
        
        upperImage = new ArrayList<String>();
        upperLabel = new ArrayList<String>();
        
        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            ArrayList<String> path = make(upperFolder + ch + "/");
            for (int i = 0; i < path.size(); ++i) {
                upperImage.add(path.get(i));
                String str = "" + ch;
                upperLabel.add(str);
            }
        }
        
        reorder();
        
        try {
            save(lowerOrder, lowerImage, lowerLabel, "lower.image", "lower.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file lower.image or lower.label");
        }
        
        try {
            save(lowerTrainingOrder, lowerImage, lowerLabel, "lower.train.image", "lower.train.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file lower.train.image or lower.train.label");
        }
        
        try {
            save(lowerTestingOrder, lowerImage, lowerLabel, "lower.test.image", "lower.test.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file lower.test.image or lower.test.label");
        }
        
        try {
            save(upperOrder, upperImage, upperLabel, "upper.image", "upper.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file upper.image or upper.label");
        }
        
        try {
            save(upperTrainingOrder, upperImage, upperLabel, "upper.train.image", "upper.train.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file upper.train.image or upper.train.label");
        }
        
        try {
            save(upperTestingOrder, upperImage, upperLabel, "upper.test.image", "upper.test.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file upper.test.image or upper.test.label");
        }
    }
    
}
