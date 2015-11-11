// Software: Multi-Layer Perceptron Library in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c) Hy Truong Son. All rights reserved. Only use for academic purposes.

package MyLib;

public class SmoothGrayImage {
    private static final int mask[][] = { 
        {1, 2, 1} , 
        {2, 4, 2} , 
        {1, 2, 1} 
    };
    private static final int maskFactor = 16;
    
    private static int widthImage, heightImage;
	
    private static int get(int f[][], int x, int y) {
        if ((x < 0) || (x >= widthImage) || (y < 0) || (y >= heightImage)) return 0;
	return f[x][y];
    }
	
    private static int calculate(int f[][], int x, int y) {
        int res = 0;
	for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j){
                int u = x + i;
                int v = y + j;
		res += mask[1 + i][1 + j] * get(f, u, v);
            }
        }
        return res / maskFactor;
    }
	
    public static void process(int input[][], int output[][], int widthImage, int heightImage) {
        SmoothGrayImage.widthImage = widthImage;
        SmoothGrayImage.heightImage = heightImage;
        for (int x = 0; x < widthImage; ++x) {
            for (int y = 0; y < heightImage; ++y) {
                output[x][y] = calculate(input, x, y);
            }
        }
    }
	
    public static void process(int grayImage[][], int widthImage, int heightImage){
        SmoothGrayImage.widthImage = widthImage;
        SmoothGrayImage.heightImage = heightImage;
	int temp[][] = new int [widthImage][heightImage];
	for (int x = 0; x < widthImage; ++x) {
            for (int y = 0; y < heightImage; ++y) {
                temp[x][y] = calculate(grayImage, x, y);
            }
        }
	for (int x = 0; x < widthImage; ++x) {
            for (int y = 0; y < heightImage; ++y) {
                grayImage[x][y] = temp[x][y];
            }
	}
    }

}
