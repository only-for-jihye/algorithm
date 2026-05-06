package base.trie;

import java.util.HashMap;
import java.util.Map;

public class BasicTrie {
	
	/**
	 * Tip : 
	 * 	만약 소문자 알파벳으로만 구성이 된다라고 한다면
	 * 	-> HashMap을 사용하기 보다 배열을 26개 선언하는게 더 빨라짐
	 *  -> 방법 :
	 *  	int idx = word.charAt(i) - 'a';
	 */

	// 1. 트라이의 각 노드를 표현하는 클래스
	class TrieNode {
		// 자식 노드들을 저장할 맵 (글자 -> 다음 노드)
		Map<Character, TrieNode> childNodes = new HashMap<>();
		
		// 이 노드에서 단어가 끝나는지 여부 (예 : 'app'과 'apple' 구분용)
		boolean isLastChar;
	}
	
	// 2. 트라이 자료구조 본체
	public class Trie {
		// 트리의 루트 노드 (문자를 담지 않고 시작점 역할만 함)
		private TrieNode rootNode;
		
		public Trie() {
			rootNode = new TrieNode();
		}
		
		// insert
		public void insert(String word) {
			TrieNode thisNode = this.rootNode;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				thisNode = thisNode.childNodes.computeIfAbsent(c, key -> new TrieNode());
			}
			// 단어의 마지막임을 표시
			thisNode.isLastChar = true;
		}
		
		// search : 완벽하게 똑같은 단어 찾기
		public boolean search(String word) {
			TrieNode thisNode = this.rootNode;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				TrieNode node = thisNode.childNodes.get(c);
				// 가고자 하는 자식 노드가 없다면 단어가 없음
				if (node == null) return false;
				thisNode = node; // 다음 노드로 계속 이동
			}
			// 마지막까지 왔을 때, 단어의 끝이 맞는지 확인
			return thisNode.isLastChar;
		}
		
		// search : prefix로 찾기
		public boolean startsWith(String prefix) {
			TrieNode thisNode = this.rootNode;
			for (int i = 0; i < prefix.length(); i++) {
				char c = prefix.charAt(i);
				TrieNode node = thisNode.childNodes.get(c);
				if (node == null) return false;
				thisNode = node;
			}
			// 끝까지 도달했다면 그 뒤에 글자가 더 있든 없든 접두사는 있는 것
			return true;
		}
		
		// delete // 재귀
		public void delete(String word) {
			delete(this.rootNode, word, 0);
		}

		private boolean delete(TrieNode thisNode, String word, int idx) {
			// 기저조건 : 단어의 끝 (마지막 글자)에 도달했을 때
			if (idx == word.length()) {
				// 해당 단어가 트라이에 등록되어 있지 않다면 지울 수없음
				if (!thisNode.isLastChar) {
					return false;
				}
				// 등록된 단어라면 끝점 표시를 해제
				thisNode.isLastChar = false;
				
				// 자식 노드가 비어있다면 (다른 단어의 접두사가 아니라면) 삭제 대상이 됨
				return thisNode.childNodes.isEmpty();
			}
			
			char c = word.charAt(idx);
			TrieNode node = thisNode.childNodes.get(c);
			
			// 지우려는 단어의 경로가 존재하지 않음
			if (node == null) return false;
			
			// 재귀적으로 다음 글자로 넘어가며 삭제 가능 여부를 판단
			boolean shouldDeleteChildNode = delete(node, word, idx + 1);
			
			// 만약 자식 노드를 지워도 된다고 판명 났다면 (true라면 )
			if (shouldDeleteChildNode) {
				thisNode.childNodes.remove(c); // 현재 노드의 맵에서 해당 자식 글자를 제거
				
				// 자식을 지운 후, 현재 노드도 자식이 없고, 다른 단어의 끝점이 아니라면 함께 연쇄 삭제
				return thisNode.childNodes.isEmpty() && !thisNode.isLastChar;
			}
			
			return false;
		}
		
		// move
		public boolean move(String oldWord, String newWord) {
			// 1. 기존 단어가 존재하는지 확인
			if (search(oldWord)) {
				// 2. 존재한다면 삭제 후 새단어 삽입
				delete(oldWord);
				insert(newWord);
				return true;
			}
			// 기존 단어가 없었다면 실패 처리
			return false;
		}
	}
}
