package pro_edu.hasing;

import java.io.*;
import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		HashMap<Integer, Integer> hm = new HashMap<>();
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			int key = Integer.parseInt(st.nextToken());
			if (!hm.containsKey(key)) {
				hm.put(key, 1);
			} else {
				int value = hm.get(key) + 1;
				hm.put(key, value);
			}
		}
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			int key = Integer.parseInt(st.nextToken());
			if (!hm.containsKey(key)) {
				System.out.print(0 + " ");
			} else {
				System.out.print(hm.get(key) + " ");
			}
		}
	}
}
