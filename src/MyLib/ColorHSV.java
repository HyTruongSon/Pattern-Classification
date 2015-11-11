// Software: Multi-Layer Perceptron Library in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Final update: October 4th, 2015
// Copyright 2015 (c) Hy Truong Son. All rights reserved. Only use for academic purposes.

package MyLib;

import java.awt.*;

public class ColorHSV {
    
    public final static int nColors = 9; 
    
    private static float H; // Hue
    private static float S; // Saturation
    private static float V; // Value
	
    public static float getH(){
        return H;
    }
	
    public static float getS(){
        return S;
    }
	
    public static float getV(){
        return V;
    }
	
    public static String int2str(int number) {
        switch (number) {
            case 0:
                return "black";
            case 1:
                return "white";
            case 2:
                return "gray";
            case 3:
                return "red";
            case 4:
                return "yellow";
            case 5:
                return "green";
            case 6:
                return "cyan";
            case 7:
                return "blue";
            case 8:
                return "magenta";
        }
        return "red";
    }
    
    public static Color int2color(int number) {
        switch (number) {
            case 0:
                return Color.black;
            case 1:
                return Color.white;
            case 2:
                return Color.gray;
            case 3:
                return Color.red;
            case 4:
                return Color.yellow;
            case 5:
                return Color.green;
            case 6:
                return Color.cyan;
            case 7:
                return Color.blue;
            case 8:
                return Color.magenta;
        }
        return Color.red;
    }
    
    public static void initFromRGB(int red, int green, int blue){
	float[] hsv = new float[3];
	Color.RGBtoHSB(red, green, blue, hsv);
	H = hsv[0];
	S = hsv[1];
	V = hsv[2];
    }
	
    public static void initFromRGB(int rgb){
        int red = (rgb & 0x00ff0000) >> 16;
	int green = (rgb & 0x0000ff00) >> 8;
	int blue = rgb & 0x000000ff;
	initFromRGB(red, green, blue);
    }
	
    public static int classify(float H, float S, float V){
	if (V < 0.2) return 0;
	if (V > 0.8) return 1;

	if (S < 0.25) return 2;

	if (H * 360f < 30)   return 3;
	if (H * 360f < 90)   return 4;
	if (H * 360f < 150)  return 5;
	if (H * 360f < 210)  return 6;
	if (H * 360f < 270)  return 7;
	if (H * 360f < 330)  return 8;
	
        return 3;
    }
    
    public static int classify(int red, int green, int blue) {
        float[] hsv = new float[3];
	Color.RGBtoHSB(red, green, blue, hsv);
	H = hsv[0];
	S = hsv[1];
	V = hsv[2];
        return classify(H, S, V);
    }
    
    public static int classify(int rgb) {
        int red = (rgb & 0x00ff0000) >> 16;
	int green = (rgb & 0x0000ff00) >> 8;
	int blue = rgb & 0x000000ff;
        
        float[] hsv = new float[3];
	Color.RGBtoHSB(red, green, blue, hsv);
	H = hsv[0];
	S = hsv[1];
	V = hsv[2];
        
        return classify(H, S, V);
    }
	
    public static int classify(){
	return classify(H, S, V);
    }	
}