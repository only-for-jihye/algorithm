package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class F_점수조회서비스 {

	static class Info {
		int id;
		String name;
		int language;
		int math;
		int science;
		int sociology;
		public Info(int id, String name, int language, int math, int science, int sociology) {
			super();
			this.id = id;
			this.name = name;
			this.language = language;
			this.math = math;
			this.science = science;
			this.sociology = sociology;
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());

		HashMap<Integer, Info> hm = new HashMap<>();
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			String name = st.nextToken();
			int lang = Integer.parseInt(st.nextToken());
			int math = Integer.parseInt(st.nextToken());
			int science = Integer.parseInt(st.nextToken());
			int socio = Integer.parseInt(st.nextToken());
			Info info = new Info(id, name, lang, math, science, socio);
			hm.put(id, info);
		}
		
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			String subject = st.nextToken();
			
			Info info = hm.get(id);
			StringBuilder sb = new StringBuilder();
			sb.append(info.name);
			sb.append(" ");
			if (subject.equals("language")) {
				sb.append(info.language);
			} else if (subject.equals("math")) {
				sb.append(info.math);
			} else if (subject.equals("science")) {
				sb.append(info.science);
			} else if (subject.equals("sociology")) {
				sb.append(info.sociology);
			}
			System.out.println(sb);
		}
	}		
}
