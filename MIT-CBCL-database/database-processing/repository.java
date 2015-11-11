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
    
    static ArrayList<String> faceTrain;
    static ArrayList<String> nonFaceTrain;
    static ArrayList<String> faceTest;
    static ArrayList<String> nonFaceTest;
    static ArrayList<String> train;
    static ArrayList<String> test;
    
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
    
    private static void save(ArrayList<String> list, String fileName) throws IOException {
        PrintWriter file = new PrintWriter(new FileWriter(fileName));
        file.println(list.size());
        for (int i = 0; i < list.size(); ++i) {
            file.println(list.get(i));
        }
        file.close();
    } 
    
    private static void merge_train(ArrayList<String> face, ArrayList<String> nonFace, String imageFileName, String labelFileName) throws IOException {
        int nFace = face.size();
        int nSamples = nFace + nonFace.size();
        int order[] = new int [nSamples];
        for (int i = 0; i < nSamples; ++i) {
            order[i] = i;
        }
        Random rand = new Random();
        for (int i = 0; i < nSamples; ++i) {
            int j = Math.abs(rand.nextInt()) % nSamples;
            int temp = order[i];
            order[i] = order[j];
            order[j] = temp;
        }
        PrintWriter imageFile = new PrintWriter(new FileWriter(imageFileName));
        PrintWriter labelFile = new PrintWriter(new FileWriter(labelFileName));
        imageFile.println(nSamples);
        labelFile.println(nSamples);
        for (int i = 0; i < nSamples; ++i) {
            if (order[i] < nFace) {
                imageFile.println(face.get(order[i]));
                labelFile.println("1");
            } else {
                imageFile.println(nonFace.get(order[i] - nFace));
                labelFile.println("0");
            }
        }
        imageFile.close();
        labelFile.close();
    }
    
    private static void merge_test(ArrayList<String> face, ArrayList<String> nonFace, String imageFileName, String labelFileName) throws IOException {
        int nSamples = face.size() + nonFace.size();
        PrintWriter imageFile = new PrintWriter(new FileWriter(imageFileName));
        PrintWriter labelFile = new PrintWriter(new FileWriter(labelFileName));
        imageFile.println(nSamples);
        labelFile.println(nSamples);
        for (int i = 0; i < face.size(); ++i) {
            imageFile.println(face.get(i));
            labelFile.println("1");
        }
        for (int i = 0; i < nonFace.size(); ++i) {
            imageFile.println(nonFace.get(i));
            labelFile.println("0");
        }
        imageFile.close();
        labelFile.close();
    }
    
    public static void main(String args[]) {
        faceTrain = make("train/face/");
        nonFaceTrain = make("train/non-face/");
        faceTest = make("test/face/");
        nonFaceTest = make("test/non-face/");
        
        try {
            save(faceTrain, "face-train.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file face-train.list");
        }
        
        try {
            save(nonFaceTrain, "non-face-train.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file non-face-train.list");
        }
        
        try {
            save(faceTest, "face-test.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file face-test.list");
        }
        
        try {
            save(nonFaceTest, "non-face-test.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file non-face-test.list");
        }
        
        try {
            merge_train(faceTrain, nonFaceTrain, "train.image", "train.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file train.image or train.label");
        }
        
        try {
            merge_test(faceTest, nonFaceTest, "test.image", "test.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file test.image or test.label");
        }
    }
    
}
