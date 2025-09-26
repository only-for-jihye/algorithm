package partTimeJob;

import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		
		int[] arr = new int[N + 1];

		for (int i = 1; i <= N; i++) {
			int human = Integer.parseInt(br.readLine());
			arr[i] = human;
		}
		
        int[] dp = new int[N + 1];
        
        for (int i = 1; i <= N; i++) {
            int currentMaxAbility = 0;
            
            for (int j = 1; j <= K; j++) {
                int startIdx = i - j;
                
                if (startIdx < 0) {
                    break;
                }
                currentMaxAbility = Math.max(currentMaxAbility, arr[i - j + 1]);
                
                int nextTeamSum = j * currentMaxAbility;
                
                dp[i] = Math.max(dp[i], dp[startIdx] + nextTeamSum);
            }
        }

        System.out.println(dp[N]);
    }
	
}