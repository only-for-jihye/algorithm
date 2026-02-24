package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class N_징검다리건너기 {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int[] array_A = new int[N];
		int M = Integer.parseInt(st.nextToken());
		int[] array_B = new int[M];
		int K = Integer.parseInt(st.nextToken());
		int[] array_C = new int[K];
		int L = Integer.parseInt(st.nextToken());
		int[] array_D = new int[L];
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			array_A[i] = Integer.parseInt(st.nextToken());
		}
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			array_B[i] = Integer.parseInt(st.nextToken());
		}
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < K; i++) {
			array_C[i] = Integer.parseInt(st.nextToken());
		}
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < L; i++) {
			array_D[i] = Integer.parseInt(st.nextToken());
		}
		
		// 계산
		// A,B를 묶어서 일단 하나의 Hash로 구함
		HashMap<Integer, Integer> hm = new HashMap<>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				int sum_AB = array_A[i] + array_B[j];
				hm.put(sum_AB, hm.getOrDefault(sum_AB, 0) + 1);
			}
		}
		
		int count = 0;
		
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < L; j++) {
				int sum_CD = array_C[i] + array_D[j];
				int minus_sum_AB = -sum_CD;
				
				if (hm.containsKey(minus_sum_AB)) {
					count += hm.get(minus_sum_AB);
				}
			}			
		}
		
		System.out.println(count);
	}
	
}
