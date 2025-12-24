package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class Main {
	
	static class Grid {
		int y;
		int x;
		public Grid(int y, int x) {
			super();
			this.y = y;
			this.x = x;
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				Grid grid = new Grid(j, i);
				
			}
		}
	}
	
}
