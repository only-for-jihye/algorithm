package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class J_경력직추천 {
	
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		HashMap<Integer, ArrayList<String>> recommendHm = new HashMap<>();
		
		HashMap<String, ArrayList<Integer>> whoHm = new HashMap<>();
		
		
		for (int t = 0; t < N; t++) {
			st = new StringTokenizer(br.readLine());
			String cmd = st.nextToken();
			
			if (cmd.equals("recommand")) {
				int memberNum = Integer.parseInt(st.nextToken());
				String candidate = st.nextToken();
				if (!recommendHm.containsKey(memberNum)) {
					ArrayList<String> al = new ArrayList<>();
					al.add(candidate);
					recommendHm.put(memberNum, al);
					System.out.println(recommendHm.get(memberNum).size());
				} else {
					ArrayList<String> al = recommendHm.get(memberNum);
					al.add(candidate);
					System.out.println(al.size());
				}
				if (!whoHm.containsKey(candidate)) {
					ArrayList<Integer> al = new ArrayList<>();
					al.add(memberNum);
					whoHm.put(candidate, al);
				} else {
					ArrayList<Integer> al = whoHm.get(candidate);
					al.add(memberNum);
				}
			} else if (cmd.equals("print")) {
				int memberNum = Integer.parseInt(st.nextToken());
				int size = 0;
				if (recommendHm.containsKey(memberNum)) {
					size = recommendHm.get(memberNum).size();
				}
				if (size > 0) {
					String result = String.join(" ", recommendHm.get(memberNum));
					System.out.println(result);
				} else {
					System.out.println("none");
				}
			} else if (cmd.equals("whois")) {
				String candidate = st.nextToken();
				if (whoHm.containsKey(candidate)) {
					ArrayList<Integer> al = whoHm.get(candidate);
					Collections.sort(al);
					for (int i = 0; i < al.size(); i++) {
						System.out.print(al.get(i)+" ");
					}
					System.out.println();
				} else {
					System.out.println("none");
				}
			}
		}
	}		
}
