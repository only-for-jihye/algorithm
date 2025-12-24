package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class A_HashTable {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int T = Integer.parseInt(st.nextToken());
		
		HashMap<Integer, Integer> hm = new HashMap<>();
		
		for (int i = 0; i < T; i++) {
			st = new StringTokenizer(br.readLine());
			String cmd = st.nextToken();
			
			if (cmd.equals("insert")) {
				int k = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());
				//
				if (!hm.containsKey(k)) {
					hm.put(k, v);
				} else {
					int value = hm.get(k);
					hm.put(k, value + v);
				}
			} else if (cmd.equals("get")) {
				int k = Integer.parseInt(st.nextToken());
				//
				if (!hm.containsKey(k)) {
					System.out.println(0);
				} else {
					int value = hm.get(k);
					System.out.println(value);
				}
			} else if (cmd.equals("remove")) {
				int k = Integer.parseInt(st.nextToken());
				//
				if (!hm.containsKey(k)) {
					
				} else {
					hm.remove(k);
				}
			} else if (cmd.equals("size")) {
				System.out.println(hm.size());
			} else if (cmd.equals("empty")) {
				if (!hm.isEmpty()) {
					System.out.println(0);
				} else {
					System.out.println(1);
				}
			}
		}
	}
	
}
