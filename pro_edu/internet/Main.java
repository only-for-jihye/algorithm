package pro_edu.internet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
	
	private static ArrayDeque<String> backdeq;
	private static ArrayDeque<String> frontdeq;

	public static void main(String[] args) throws Exception {
//		System.setIn(new java.io.FileInputStream("src/pro/SEP15/Internet/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int Q = Integer.parseInt(st.nextToken());
		
		backdeq = new ArrayDeque<String>();
		frontdeq = new ArrayDeque<String>();
		String current = null;
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			String work = st.nextToken();
			if (work.equals("B")) { // 뒤로가기
				if (!backdeq.isEmpty()) {
					frontdeq.addFirst(current);
					current = backdeq.pollLast();
//					current = backdeq.removeLast();
				}
			} else if (work.equals("F")) {
				if (!frontdeq.isEmpty()) {
					backdeq.addLast(current);
					current = frontdeq.pollFirst();
				}
			} else if (work.equals("A")) {
				frontdeq.clear();
				if (current != null) {
					backdeq.addLast(current);
				}
				current = st.nextToken();
			} else if (work.equals("C")) {
				// 새로운 deque를 만들어서 추가
				ArrayDeque<String> tempdeq = new ArrayDeque<>();
				for (String s : backdeq) {
					if (!tempdeq.isEmpty() && s.equals(tempdeq.getLast())) continue;
					tempdeq.add(s);
//					System.out.println(s);
				}
				backdeq = tempdeq;
			}
		}
		System.out.println(current);
//		while(!backdeq.isEmpty()) {
//			System.out.print(backdeq.poll() + " ");
//		}
//		System.out.println();
//		if (frontdeq.isEmpty()) System.out.println("none"); 
//		else {
//			while(!frontdeq.isEmpty()) {
//				System.out.print(frontdeq.poll() + " ");
//			}
//		}
		if (!backdeq.isEmpty()) {
			Iterator<String> it = backdeq.descendingIterator();
			while (it.hasNext()) {
				String name = it.next();
				System.out.print(name + " ");
			}
			System.out.println();
		} else {
			System.out.println("none");
		}
		if (!frontdeq.isEmpty()) {
			Iterator<String> it = frontdeq.iterator();
			while (it.hasNext()) {
				String name = it.next();
				System.out.print(name + " ");
			}
			System.out.println();
		} else {
			System.out.println("none");
		}
	}
	
	// Stack ??
}