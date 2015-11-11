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
    
    static ArrayList<String> robotTrain;
    static ArrayList<String> nonRobotTrain;
    static ArrayList<String> robotTest;
    static ArrayList<String> nonRobotTest;
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
    
    private static void merge_train(ArrayList<String> robot, ArrayList<String> nonRobot, String imageFileName, String labelFileName) throws IOException {
        int nRobot = robot.size();
        int nSamples = nRobot + nonRobot.size();
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
            if (order[i] < nRobot) {
                imageFile.println(robot.get(order[i]));
                labelFile.println("1");
            } else {
                imageFile.println(nonRobot.get(order[i] - nRobot));
                labelFile.println("0");
            }
        }
        imageFile.close();
        labelFile.close();
    }
    
    private static void merge_test(ArrayList<String> robot, ArrayList<String> nonRobot, String imageFileName, String labelFileName) throws IOException {
        int nSamples = robot.size() + nonRobot.size();
        PrintWriter imageFile = new PrintWriter(new FileWriter(imageFileName));
        PrintWriter labelFile = new PrintWriter(new FileWriter(labelFileName));
        imageFile.println(nSamples);
        labelFile.println(nSamples);
        for (int i = 0; i < robot.size(); ++i) {
            imageFile.println(robot.get(i));
            labelFile.println("1");
        }
        for (int i = 0; i < nonRobot.size(); ++i) {
            imageFile.println(nonRobot.get(i));
            labelFile.println("0");
        }
        imageFile.close();
        labelFile.close();
    }
    
    public static void main(String args[]) {
        robotTrain = make("train/robot/");
        nonRobotTrain = make("train/non-robot/");
        robotTest = make("test/robot/");
        nonRobotTest = make("test/non-robot/");
        
        try {
            save(robotTrain, "robot-train.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file robot-train.list");
        }
        
        try {
            save(nonRobotTrain, "non-robot-train.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file non-robot-train.list");
        }
        
        try {
            save(robotTest, "robot-test.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file robot-test.list");
        }
        
        try {
            save(nonRobotTest, "non-robot-test.list");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file non-robot-test.list");
        }
        
        try {
            merge_train(robotTrain, nonRobotTrain, "train.image", "train.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file train.image or train.label");
        }
        
        try {
            merge_test(robotTest, nonRobotTest, "test.image", "test.label");
        } catch (IOException exc) {
            System.err.println("Problem with writing to file test.image or test.label");
        }
    }
    
}
