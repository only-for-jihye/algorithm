package base.trie;

import java.util.HashMap;
import java.util.Map;

// 1. 카운트 메타데이터가 탑재된 트라이 노드
class PSTrieNode {
	PSTrieNode[] child = new PSTrieNode[26]; // 소문자만 사용한다고 가정 시
	
	// 이 노드를 거쳐간 단어의 총 개수 (prefix 계산용)
	int passCount = 0;
	// 이 노드에서 정확히 끝나는 단어의 개수 (순위 계산 시, 나보다 짧은 단어 파악용)
	int endCount = 0;
}

public class AdvancedTrie {

	private PSTrieNode root;
	
	public AdvancedTrie() {
		root = new PSTrieNode();
	}
	
	// insert : 단어를 트라이에 추가하며 passCount와 endCount를 갱신
	public void insert(String word) {
		PSTrieNode cur = root;
		for (int i = 0; i < word.length(); i++) {
			int idx = word.charAt(i) -'a'; // 소문자 알파벳을 0~25 인덱스로 변환
			
			if (cur.child[idx] == null) {
				cur.child[idx] = new PSTrieNode();
			}
			cur = cur.child[idx];
			cur.passCount++; // 이 노드를 지나가는 단어가 추가되었으므로 +1
		}
		cur.endCount++; // 단어의 마지막 노드에 도달했으므로 +1
	}
	
	// prefix 개수 찾기
	public int getPrefixCount(String prefix) {
		PSTrieNode cur = root;
		for (int i = 0; i < prefix.length(); i++) {
			int idx = prefix.charAt(i) - 'a';
			
			if (cur.child[idx] == null) return 0;
			cur = cur.child[idx];
		}
		// 무사히 접두사 끝에 도달했다면 이 노드를 거쳐간 단어의 수가 정답
		return cur.passCount;
	}
	
	// 사전순 순위, 찾고자 하는 단어가 사전 순으로 몇번째에 위치하는지 찾기
	public int getDictionaryRank(String word) {
		PSTrieNode cur = root;
		int rank = 1; // 1등 부터 시작
		
		for (int i = 0; i < word.length(); i++) {
			int targetIdx = word.charAt(i) - 'a';
			
			// 핵심 1 : 현재 노드에서 이미 끝난 단어들은 나보다 길이가 짧으므로 무조건 사전 순으로 앞섬
			rank += cur.endCount;
			
			// 핵심 2 : 현재 가야할 알파벳보다 '앞선 알파벳'을 가진 형제 노드들의 단어 개수를 모두 더함
			for (int j = 0; j < targetIdx; j++) {
				if (cur.child[j] != null) {
					rank += cur.child[j].passCount;
				}
			}
			
			cur = cur.child[targetIdx];
			
			// 만약 트라이에 존재하지 않는 단어를 검색했다면, 현재까지 누적된 랭크를 반환하거나 예외 던짐
			if (cur == null) return -1;
		}
		return rank;
	}
	
	// delete
	public boolean delete(String word) {
		return delete(root, word, 0);
	}

	private boolean delete(PSTrieNode cur, String word, int depth) {
		// 기저 조건 : 단어의 끝 (마지막 글자)에 도달했을 때
		if (depth == word.length())	{
			// 해당 단어가 실제로 등록되어 있다면 endCount 감소
			if (cur.endCount > 0) {
				cur.endCount--;
				return true; // 삭제 성공 신호르 부모에게 보냄
			}
			return false; // 등록되어 있지 않은 단어라면 삭제 실패
		}
		
		int idx = word.charAt(depth) - 'a';
		
		// 지우려는 단어가 트라이에 존재하지 않음
		if (cur.child[idx] == null) {
			return false;
		}
		
		// 재귀적으로 다음 글자를 향해 파고들어감
		boolean isDeleted = delete(cur.child[idx], word, depth + 1);
		
		// 깊은 곳에서 성공적으로 단어를 하나 지우고 돌아왔다면 ?
		if (isDeleted) {
			cur.child[idx].passCount--; // 거쳐온 길의 passCount를 1 빼줌
			// 핵심 : 만약 passCount가 0이 되었다면 더이상 이 경로를 쓰는 단어가 없으므로 제거
			if (cur.child[idx].passCount == 0) {
				cur.child[idx] = null; // gc 처리
			}
		}
		
		return isDeleted;
	}
	
	// move
	public boolean move(String oldWord, String newWord) {
		// 기존 단어를 성공적으로 삭제하고나서 새 단어 추가
		if (delete(oldWord)) {
			insert(newWord);
			return true;
		}
		return false;
	}
}




