package B_TOP.Hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class D_제로경매 {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		HashMap<Long, Integer> hm = new HashMap<>();
		int total = 0;
		st = new StringTokenizer(br.readLine());
		
		for (int i = 0; i < N; i++) {
			long num = Integer.parseInt(st.nextToken());
			if (hm.containsKey(num)) {
				int v = hm.get(num);
				hm.put(num, v + 1);
			} else {
				hm.put(num, 1);
			}
		}
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			long num = Long.parseLong(st.nextToken()) * -1;
			if (hm.containsKey(num)) {
				hm.remove(num);
				total++;
			}
		}
		System.out.println(total);
	}
}
