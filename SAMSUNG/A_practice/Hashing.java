package A_practice;

import java.util.List;

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

// 재은프로님꺼
class UserSolution {

    // 해시가 담을 수 있는 최대 문자열 길이
    static final int MAX_WORD_LEN = 10;

    /*
     * 문자를 숫자로 해싱
     * C++의 char* str -> Java의 String str (또는 char[] str)
     * C++의 long long -> Java의 long (64bit)
     */
    public long hashFunction(String str) {
        long hash = 0;

        // 시작 비트 위치 (최대 길이 10 기준, 0~9번 인덱스 -> 비트 45부터 시작)
        int start_bit = (MAX_WORD_LEN - 1) * 5; // 45

        // 입력 문자열 길이와 최대 길이 제한 확인
        // Java에서는 str의 null 문자('\0') 체크 대신 length()를 사용합니다.
        int len = str.length();
        
        for (int i = start_bit, j = 0; i >= 0 && j < len; i -= 5, j++) {
            // 'a'(97) -> 1, 'b' -> 2 ... 로 변환
            // long으로 캐스팅 후 시프트 연산
            hash |= ((long)(str.charAt(j) - 96) << i);
        }
        return hash;
    }

    /*
     * hashFunction함수의 역연산 함수
     * Java에서는 문자열 연결 효율을 위해 StringBuilder 사용
     */
    public String getStr(long hash) {
        StringBuilder sb = new StringBuilder();

        // 시작 비트 위치
        int start_bit = (MAX_WORD_LEN - 1) * 5; // 45

        for (int i = start_bit; i >= 0; i -= 5) {
            // 해당 위치의 5비트 추출
            int val = (int)((hash >> i) & 31); // 0b11111
            
            if (val == 0) break; // 0 값이 나오면 문자열의 끝 (패딩된 부분)
            
            sb.append((char)(val + 96)); // 숫자를 다시 문자로 변환하여 추가
        }
        return sb.toString();
    }

    /*
     * 실제 배열에 들어갈 index를 탐색함 (Lower Bound)
     * C++ vector<long long> -> Java List<Long> (또는 ArrayList<Long>)
     */
    public int binarySearch(long word, List<Long> vec) {
        int left = 0;
        int right = vec.size() - 1;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            
            // Java List의 get 메서드 사용
            if (vec.get(mid) < word) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left; // word 이상인 값이 처음 나오는 위치 (삽입 위치)
    }
}

