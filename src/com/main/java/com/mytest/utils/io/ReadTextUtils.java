package com.mytest.utils.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * io流读文件
 * 
 * @author da.zhang
 * 
 */
public class ReadTextUtils {

	public static final void readF1(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));

		for (String line = br.readLine(); line != null; line = br.readLine()) {
			System.out.println(line);
		}
		br.close();

	}

	public static final void readF2(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader bufferedreader = new BufferedReader(fr);
		String instring;
		while ((instring = bufferedreader.readLine().trim()) != null) {
			if (0 != instring.length()) {
				System.out.println(instring);
			}
		}
		fr.close();
	}

}
