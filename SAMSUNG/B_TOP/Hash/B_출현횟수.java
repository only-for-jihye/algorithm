package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class B_출현횟수 {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		HashMap<Integer, Integer> hm = new HashMap<>();
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			int num = Integer.parseInt(st.nextToken());
			
			if (!hm.containsKey(num)) {
				hm.put(num, 1);
			} else {
				int v = hm.get(num);
				hm.put(num, v + 1);
			}
		}
		
		st = new StringTokenizer(br.readLine());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < M; i++) {
			int search = Integer.parseInt(st.nextToken());
			
			if (!hm.containsKey(search)) {
				sb.append("0").append(" ");
			} else {
				sb.append(hm.get(search)).append(" ");
			}
		}
		System.out.println(sb);
	}
}
