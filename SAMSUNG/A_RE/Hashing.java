package A_RE;

public class Hashing {

	public static void main(String[] args) {
		
		String[] arr = {"A","AZZZ", "B", "AA", "AAA", "AAB", "AAC", "BAA", "AAAA", "ZZZ", "ZZZZ"};
		
		for (String s : arr) {
			int hash = getHash(s);
			System.out.println(s + " : " + hash);
			System.out.println(decodeHash(hash));
		}
		System.out.println("--------------------------");
		for (String s : arr) {
			int hashAsc = getHashDicAsc(s);
			System.out.println(s + " : " + hashAsc);
			System.out.println(decodeHashDicAsc(hashAsc));
		}
	}

	// 전체 문자열 해싱 처리
	static int getHash(String s) {
		int hash = 0;
		for (int i = 0; i < s.length(); i++) {
			hash = (hash * 27) + (s.charAt(i) - 'A' + 1);
		}
		return hash;
	}
	static String decodeHash(int hash) {
		StringBuilder sb = new StringBuilder();
		
		while (hash > 0) {
			int val = hash % 27;
			sb.append((char)('A' + val - 1));
			hash /= 27;
		}
		return sb.reverse().toString();
	}
	
	// 전체 문자열 해싱 처리 : 사전 순 오름차순으로 // 외울거면 이걸 외워라
	static int getHashDicAsc(String s) {
		int hash = 0;
		int p = 27 * 27 * 27; // 현재 4자리까지
		for (int i = 0; i < s.length(); i++) {
			hash += (s.charAt(i) - 'A' + 1) * p;
			p /= 27;
		}
		return hash;
	}
	
	static String decodeHashDicAsc(int hash) {
		StringBuilder sb = new StringBuilder();
		int p = 27 * 27 * 27; // 현재 4자리 까지
		
		while (p > 0) {
			int val = hash / p;
			if (val > 0) {
				sb.append((char)('A' + val - 1));
			}
			hash %= p;
			p /= 27;
		}
		
		return sb.toString();
	}
}
