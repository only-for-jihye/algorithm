package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class R_퍼즐맞추기 {
	
	static int[][] board;
	static boolean[][] used;
	static int[][] puzzle;
	static HashMap<Long, ArrayList<Integer>> posMap = new HashMap<>();
	// 해시 값별로 리스트의 어디까지 탐색했는지 저장하는 포인터
    static Map<Long, Integer> pointers = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		board = new int[N][N];
		used = new boolean[N][N];
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				board[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		// hashing
		for (int i = 0; i <= N - 3; i++) {
			for (int j = 0; j <= N - 3; j++) {
				long hash = getHash(i, j, board);
				posMap.computeIfAbsent(hash, k -> new ArrayList<>()).add(i * 1000 + j);
			}
		}
				
		// 맞는 조각 개수 카운트
		int count = 0;
		// puzzle
		for (int m = 0; m < M; m++) {
			int[][] piece = new int[3][3];
			for (int i = 0; i < 3; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < 3; j++) {
					piece[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			long pieceHash = getHash(0, 0, piece);
			if (posMap.containsKey(pieceHash)) {
				List<Integer> list = posMap.get(pieceHash);
				int startIdx = pointers.getOrDefault(pieceHash, 0);
				
				for (int i = startIdx; i < list.size(); i++) {
					int pos = list.get(i);
					int r = pos / 1000;
					int c = pos % 1000;
					
					if (canPlace(r, c)) {
						place(r, c);
						count++;
						pointers.put(pieceHash, i + 1); // 다음 조각은 이 이후부터 탐색
						break;
					}
					// 이미 사용된 곳이라면 다음을 위해 포인터 업데이트
					pointers.put(pieceHash, i + 1);
				}
			}
		}
		System.out.println(count);
	}

	private static long getHash(int r, int c, int[][] target) {
		long hash = 0;
		long power = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				hash += (long) target[r + i][c + j] * power;
				power *= 11;
			}
		}
		return hash;
	}
	
	private static boolean canPlace(int r, int c) {
		for (int i = r; i < r + 3; i++) {
			for (int j = c; j < c + 3; j++) {
				if (used[i][j]) return false;
			}
		}
		return true;
	}
	
	private static void place(int r, int c) {
		for (int i = r; i < r + 3; i++) {
			for (int j = c; j < c + 3; j++) {
				used[i][j] = true;
			}
		}
	}
	
}
