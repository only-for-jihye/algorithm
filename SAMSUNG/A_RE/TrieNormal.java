package A_RE;

public class TrieNormal {

	// 1. 트라이의 각 지점(노드)을 정의하는 클래스
	class TrieNode {
		// 알파벳 26개  0 : a ~ 25 : z
		// 자식 노드들을 저장할 배열
		TrieNode[] children = new TrieNode[26];
		// end Flag
		boolean isEndOfWord;
		
		public TrieNode() {
			isEndOfWord = false;
			// children 배열은 처음에 모두 null로 초기화
		}
	}
	
	// 2. 트라이 자료구조 클래스
	class Trie {
		private TrieNode root;
		
		public Trie() {
			root = new TrieNode(); // root 노드는 비어있는 상태로 시작
		}
		
		// insert
		public void insert(String word) {
			TrieNode current = root;
			word = word.toLowerCase(); // 소문자로 통일
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				int index = c - 'a';
				
				// 위 문자에 대한 자식 노드가 없으면 만듦
				if (current.children[index] == null) {
					current.children[index] = new TrieNode();
				}
				// 자식 노드로 이동
				current = current.children[index];
			}
			
			// 단어의 끝이므로 end flag
			current.isEndOfWord = true;
		}
		
		// search
		public boolean search(String word) {
			TrieNode current = root; // 루트부터 시작
			word = word.toLowerCase();
			
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				int index = c - 'a';
				
				// 단어가 없으면 false 리턴
				if (current.children[index] == null) {
					return false;
				}
				current = current.children[index];
			}
			// 단어의 끝으로 표기되어야 진짜 단어임
			return current.isEndOfWord;
		}
		
		// move
		public int move(int index) {
			TrieNode current = root;
			while (true) {
				if (current.)
			}
		}
	}
}
