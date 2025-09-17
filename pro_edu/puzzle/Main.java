package pro_edu.puzzle;

import java.io.*;
import java.util.*;

/*
 * 1. 사전 처리 : 어떤 퍼즐이 어디에 있는가?
 * 		int getKey(int row, int col, int map[][]) <- 해당 좌표의 3*3의 퍼즐 Key
 * 		HashMap<Integer, ArrayDeque<좌표>>
 * 
 * 2. 각 퍼즐별 가능한 좌표를 우선 순위대로 매칭
 * 		getKey 함수를 활용 => 퍼즐의 key 값 계산
 * 		해당 퍼즐이 가능한 좌표"들"
 * 		앞에서부터 해당 좌표를 진짜 사용해도 되는가?
 * 		=> boolean isUsed[][]
 * 			-> 해당 좌표가 사용중인가???
 */
public class Main {

	static int[][] board;
	static int puzzleSize = 3;
	static ArrayDeque<Integer> deq;
	// key : 3X3 퍼즐의 정수화 시킨 key 값
	// value : 해당 퍼즐 종류가 어느 좌표에 있는가? -> 여러가지 -> 좌표2개를 1개의 정수값으로.. idx와 같이 저장
	static HashMap<Integer, ArrayDeque<Integer>> hm = new HashMap<>();
	static boolean[][] isUsed;
	
	static boolean isValid(int row, int col) {
		// row, col에서부터 3*3 크기 안에 퍼즈링 사용된 적이 있는가?
		for (int r = row; r < row + puzzleSize; r++) {
			for (int c = col; c < col + puzzleSize; c++) {
				if (isUsed[r][c])
			}
		}
	}
	
	static int getKey(int row, int col, int[][] map) {
		// map의 row, col로부터 3X3의 퍼즐 모양을 정수화 시킨 key
		/*
		 * 3X3 배열 (0, 0) 기준으로...
		 * (0, 0) (0, 1) (0, 2)
		 * (1, 0) (1, 1) (1, 2)
		 * (2, 0) (2, 1) (2, 2)
		 */
		int key = 0;
		for (int r = row; r < row + 3; r++) {
			for (int c = col; c < col + 3; c++) {
				key = key * 10 + (map[r][c] - 1);
			}
		}
		return key;
	}
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		board = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				int val = Integer.parseInt(st.nextToken());
				board[i][j] = val;
			}
		}
		
		// 한 변의 크기 N, 퍼즐 사이즈 S라 가정 시 N-S까지 포함
		// 1. 사전 처리
		for (int row = 0; row <= N - puzzleSize; row++) {
			for (int col = 0; col <= N - puzzleSize; col++) {
				int key = getKey(row, col, board);
				
				ArrayDeque<Integer> coords = hm.get(key);
				if (coords == null) {
					hm.put(key, new ArrayDeque<>()); // 처음이면 deque를 만들어줌
					coords = hm.get(key);
				}
				// 2개의 좌표를 하나의 정수로 만든다. N진수 이상의 수
				coords.add(row * N + col);
				// row : 해당 값 / N
				// col : 해당 값 % N
			}
		}
		
		// 2. 각 퍼즐별 가능한 좌표를 우선 순위대로 매칭
		int result = 0;
		int puzzle[][] = new int[puzzleSize][puzzleSize];
		for (int q = 0; q < M; q++) {
			for (int r = 0; r < puzzleSize; r++) {
				st = new StringTokenizer(br.readLine());
				for (int c = 0; c < puzzleSize; c++) {
					puzzle[r][c] = Integer.parseInt(st.nextToken());
				}
			}
			int key = getKey(0, 0, puzzle);
			ArrayDeque<Integer> puzzleCoordsList = hm.get(key);
			
			// 제일 앞에 있는 좌표 꺼내기
			int coordValue = puzzleCoordsList.pollFirst();
			int row = coordValue / N;
			int col = coordValue % N;
//			if (!isValid(row, col))
			for (int r = row; r < row + puzzleSize; r++) {
				for (int c = col; c < col + puzzleSize; c++) {
					isUsed[r][c] = true; // 이 좌표는 이제 퍼즐로 사용
				}
			}
		}
		
	}	
}