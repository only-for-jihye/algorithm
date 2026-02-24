package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class Q_암호메시지 {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String s1 = br.readLine();
		String s2 = br.readLine();
		
		int n1 = s1.length();
		int n2 = s2.length();
		int maxL = Math.min(n1, n2);
		
		// 가장 긴 길이부터 거꾸로 탐색
		for (int len = maxL; len >= 1; len--) {
			if (hasCommonComposition(s1, s2, len)) {
				System.out.println(len);
				return;
			}
		}
		System.out.println(0);
	}

	// 특정 길이(len)에서 구성이 같은 구간이 있는지 확인
	private static boolean hasCommonComposition(String s1, String s2, int len) {
		Set<String> set = new HashSet<>();
		
		int[] freq = new int[26];
		
		// 1. s1의 모든 len 구간의 빈도수를 해쉬셋에 저장
		for (int i = 0; i < s1.length(); i++) {
			freq[s1.charAt(i) - 'a']++;
			if (i >= len) {
				freq[s1.charAt(i - len) - 'a']--; // 윈도우 이동
			}
			if (i >= len - 1) {
				set.add(Arrays.toString(freq)); // 배열의 상태를 문자열로 변환하여 해시 키로 사용
			}
		}
		
		// 2. s2의 모든 len 구간을 탐색하며 셋에 있는지 확인
		int[] freq2 = new int[26];
		for (int i = 0; i < s2.length(); i++) {
			freq2[s2.charAt(i) - 'a']++;
			if (i >= len) {
				freq2[s2.charAt(i - len) - 'a']--;
			}
			if (i >= len - 1) {
				if (set.contains(Arrays.toString(freq2))) {
					return true;
				}
			}
		}
		return false;
	}
	
}
