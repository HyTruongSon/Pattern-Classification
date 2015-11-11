// Software: Multi-Layer Perceptron Library in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c) Hy Truong Son. All rights reserved. Only use for academic purposes.

package MyLib;

public class EnhanceGrayImage {    
    private static int widthImage, heightImage;
    private static int L = 256;
    private static double cdf[] = new double [L];
    
    public static void process(int input[][], int output[][], int widthImage, int heightImage) {
        EnhanceGrayImage.widthImage = widthImage;
        EnhanceGrayImage.heightImage = heightImage;
        for (int i = 0; i < L; ++i) {
            cdf[i] = 0;
        }
        for (int x = 0; x < widthImage; ++x) {
            for (int y = 0; y < heightImage; ++y) {
                cdf[input[x][y]]++;
            }
        }
        for (int i = 1; i < L; ++i) {
            cdf[i] += cdf[i - 1];
        }
        for (int i = 0; i < L; ++i) {
            cdf[i] /= (widthImage * heightImage);
        }
        for (int x = 0; x < widthImage; ++x) {
            for (int y = 0; y < heightImage; ++y) {
                output[x][y] = (int)((L - 1) * cdf[input[x][y]]);
            }
        }
    }
	
    public static void process(int grayImage[][], int widthImage, int heightImage){
        EnhanceGrayImage.widthImage = widthImage;
        EnhanceGrayImage.heightImage = heightImage;
	int temp[][] = new int [widthImage][heightImage];
	process(grayImage, temp, widthImage, heightImage);
	for (int x = 0; x < widthImage; ++x) {
            for (int y = 0; y < heightImage; ++y) {
                grayImage[x][y] = temp[x][y];
            }
	}
    }

}
