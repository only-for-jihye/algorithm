package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class Main {
	
	static char[][] grid;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int H = Integer.parseInt(st.nextToken());
		int W = Integer.parseInt(st.nextToken());
		
		grid = new char[H][W];
		
		for (int i = 0; i < H; i++) {
			st = new StringTokenizer(br.readLine());
			String str = st.nextToken();
			char[] array = str.toCharArray();
			for (int j = 0; j < W; j++) {
				grid[i][j] = array[j];
			}
		}
		
	}
}
