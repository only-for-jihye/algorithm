package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class E_착한친구나쁜친구이상한친구 {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());

		HashMap<String, Integer> good_hm = new HashMap<>();
		HashMap<String, Integer> bad_hm = new HashMap<>();
		
		HashSet<String> name = new HashSet<>();
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String good = st.nextToken();
			name.add(good);
			String bad = st.nextToken();
			name.add(bad);
			if (good_hm.containsKey(good)) {
				int v = good_hm.get(good);
				good_hm.put(good, v + 1);
			} else {
				good_hm.put(good, 1);
			}
			if (bad_hm.containsKey(bad)) {
				int v = bad_hm.get(bad);
				bad_hm.put(bad, v + 1);
			} else {
				bad_hm.put(bad, 1);
			}
		}
		
		int v_good = 0;
		int v_bad = 0;
		int v_esanghan = 0;
		
		Iterator<String> it = name.iterator();
		while (it.hasNext()) {
			String next = it.next();
			int good_v = 0;
			int bad_v = 0;
			if (good_hm.containsKey(next)) {
				good_v = good_hm.get(next);
			}
			if (bad_hm.containsKey(next)) {
				bad_v = bad_hm.get(next);
			}
			if (good_v > bad_v) {
				v_good++;
			} else if (good_v < bad_v) {
				v_bad++;
			} else {
				v_esanghan++;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(v_good);
		sb.append(" ");
		sb.append(v_bad);
		sb.append(" ");
		sb.append(v_esanghan);
		System.out.println(sb);
	}		
}
