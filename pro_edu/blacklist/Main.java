package pro_edu.blacklist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int height = Integer.parseInt(st.nextToken());
		int width = Integer.parseInt(st.nextToken());
		int apart[][] = new int[height][width];
		for (int i = 0; i < height; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < width; j++) {
				int value = Integer.parseInt(st.nextToken());
				apart[i][j] = value;
			}
		}
		st = new StringTokenizer(br.readLine());
		int bheight = Integer.parseInt(st.nextToken());
		int bwidth = Integer.parseInt(st.nextToken());
		boolean[] isBlack = new boolean[100001];
		for (int i = 0; i < bheight; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < bwidth; j++) {
				int value = Integer.parseInt(st.nextToken());
				isBlack[value] = true;
			}
		}
		int black = 0;
		int normal = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int value = apart[i][j];
				if (isBlack[value]) {
					black++;
				} else {
					normal++;
				}
			}
		}
		System.out.println(black);
		System.out.println(normal);
	}
	
}