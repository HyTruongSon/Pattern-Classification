// Software: Multi-Layer Perceptron Library in Java
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Copyright 2015 (c) Hy Truong Son. All rights reserved. Only use for academic purposes.

package MyLib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class PGM256 {
    
    public static void getGrayImage(String fileName, int grayImage[][], int widthImage, int heightImage) throws IOException {
        BufferedInputStream file = new BufferedInputStream (new FileInputStream(fileName));
        int count = 0;
        while (true) {
            int value = file.read();
            if ((value == 10) || (value == 32)) {
                ++count;
                if (count == 4) {
                    break;
                }
            }
        }
        for (int y = 0; y < heightImage; ++y) {
            for (int x = 0; x < widthImage; ++x) {
                grayImage[x][y] = file.read();
            }
        }
        file.close();
    }
    
}
