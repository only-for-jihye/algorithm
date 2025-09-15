package pro_edu.Input_Output;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int T = Integer.parseInt(st.nextToken());
		switch (T) {
		case 1:
			st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			int answer1 = 0;
			long answer2 = 1;
			st = new StringTokenizer(br.readLine());
			for (int i = 0; i < n; i++) {
				int a = Integer.parseInt(st.nextToken());
				answer1 += a;
				answer2 *= a;
			}
			System.out.println(answer1 + " " + answer2);
			break;
		case 2:
			st = new StringTokenizer(br.readLine());
			int m = Integer.parseInt(st.nextToken());
			String[] strArray = new String[m];
			for (int i = 0; i < m; i++) {
				st = new StringTokenizer(br.readLine());
				strArray[i] = st.nextToken();
			}
			int max = 0, min = Integer.MAX_VALUE;
			int longIdx = 0, shortIdx = 0;
			for (int j = 0; j < m; j++) {
				int len = strArray[j].length();
				if (j == 0) {
					max = strArray[0].length();
					min = strArray[0].length();
					continue;
				}
				if (len > max) {
					max = Math.max(max, len);
					longIdx = j;
				}
				if (len < min) {
					min = Math.min(min, len);
					shortIdx = j;
				}
			}
			System.out.println(strArray[longIdx]);
			System.out.println(strArray[shortIdx]);
			break;
		case 3:
			st = new StringTokenizer(br.readLine());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int[][] yx = new int[y][x];
			int arrayMin = Integer.MAX_VALUE;
			int count = 0;
			for (int i = 0; i < y; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < x; j++) {
					yx[i][j] = Integer.parseInt(st.nextToken());
					if (i == 0 && j == 0) {
						arrayMin = yx[0][0];
					}
					if (arrayMin > yx[i][j]) {
						arrayMin = Math.min(arrayMin, yx[i][j]);
						continue;
					}
					if (arrayMin == yx[i][j]) count++;
				}
			}
			System.out.println(arrayMin);
			System.out.println(count + "개");
			break;
		default:
			break;
		}
	}
}