package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class I_회원관리시스템 {
	
	static class Member {
		String id;
		int password;
		boolean isLogin;
		boolean isDeleted;
		public Member(String id, int password) {
			super();
			this.id = id;
			this.password = password;
			this.isLogin = false;
			this.isDeleted = false;
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		HashMap<String, Member> hm = new HashMap<>();
		
		int loginCnt = 0;
		int totalCnt = 0;
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String cmd = st.nextToken();
			
			if (cmd.equals("reg")) {
				String id = st.nextToken();
				int password = Integer.parseInt(st.nextToken());
				
				if (hm.containsKey(id)) {
					if (hm.get(id).isDeleted) {
						Member newMember = new Member(id, password);
						hm.put(id, newMember);
						totalCnt++;
						System.out.println("welcome " + totalCnt);
					} else {
						System.out.println("reg fail");
					}
				} else {
					Member newMember = new Member(id, password);
					hm.put(id, newMember);
					totalCnt++;
					System.out.println("welcome " + totalCnt);
				}
			} else if (cmd.equals("login")) {
				String id = st.nextToken();
				int password = Integer.parseInt(st.nextToken());
				
				if (hm.containsKey(id)) {
					if (hm.get(id).password == password) {
						if (hm.get(id).isDeleted) {
							System.out.println("login fail");
							continue;
						}
						if (hm.get(id).isLogin) {
							System.out.println("login fail");
						} else {
							hm.get(id).isLogin = true;
							loginCnt++;
							System.out.println("login " + loginCnt);
						}
					} else {
						System.out.println("login fail");
					}
				} else {
					System.out.println("login fail");
				}
			} else if (cmd.equals("change")) {
				String id = st.nextToken();
				int oldPassword = Integer.parseInt(st.nextToken());
				int newPassword = Integer.parseInt(st.nextToken());
				
				if (hm.containsKey(id)) {
					if (hm.get(id).isDeleted) {
						System.out.println("change fail");
					} else {
						if (hm.get(id).password == oldPassword) {
							hm.get(id).password = newPassword;
							System.out.println("success");
						} else {
							System.out.println("change fail");							
						}
					}
				} else {
					System.out.println("change fail");
				}
			} else if (cmd.equals("logout")) {
				String id = st.nextToken();
				if (hm.containsKey(id)) {
					if (hm.get(id).isDeleted) {
						System.out.println("logout fail");
						continue;
					}
					if (hm.get(id).isLogin) {
						hm.get(id).isLogin = false;
						loginCnt--;
						System.out.println("logout " + loginCnt);
					} else {
						System.out.println("logout fail");
					}
				} else {
					System.out.println("logout fail");
				}
			} else if (cmd.equals("bye")) {
				String id = st.nextToken();
				if (hm.containsKey(id)) {
					if (hm.get(id).isLogin) {
						hm.get(id).isLogin = false;
						hm.get(id).isDeleted  = true;
						loginCnt--;
						totalCnt--;
						System.out.println("bye " + totalCnt);
					} else {
						System.out.println("bye fail");
					}
				} else {
					System.out.println("bye fail");
				}
			}
		}
		
	}		
}
