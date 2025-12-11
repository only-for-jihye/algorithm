package 단어장;

//25.11.08 단어장
//@admin_deukwha
//trie + memory pool (java)

class UserSolution
{
	String[] words = new String[70001]; 
	int idCnt;
	int curPage;
	
	class Node {
		int idx; 
		int maxImportance; // 최대 importance 
		int maxIdx;        // 최대 importance의 index;
		int[] cnt;         // 이 노드 앞에 몇개의 단어가 존재하는가
		Node[] childs;
		Node(){
			this.idx = -1;
			this.maxImportance = - 1;
			this.maxIdx = -1; 
			this.childs = new Node[26]; 
			this.cnt = new int[26]; 
		}
	}
	
	class Trie {
		Node root;
		Trie() {
			this.root = newNode(); // memory pool
		}
		int insert(String str, int importance) {
			Node cur = root;
			words[++idCnt] = str; 
			int cnt = 0; 
			// str에 존재하는 모든 문자들 'a' -> 'b' -> 'c'
			for(int i  = 0; i < str.length(); i++) {
				if(importance > cur.maxImportance) {
					cur.maxImportance = importance;
					cur.maxIdx = idCnt;
				}
				int ch = str.charAt(i) - 'a';
				if(cur.childs[ch] == null) cur.childs[ch] = newNode();
				cur.cnt[ch]++; // cur.cnt에 한개 추가
				for(int j = 0; j < ch; j++) cnt += cur.cnt[j]; // 'a'~ (ch-1)까지의 개수 추가
				if(cur.idx != -1) cnt++; // 만약 새롭게 생긴 노드가 아니면 개수 추가
				cur =  cur.childs[ch];
			}
			cur.idx = idCnt;
			return cnt; 
		}	
		int move(int idx) {
			Node cur = root;
			while(true) {
				if(cur.idx != -1) idx--;
				if(idx == 0) 
					return cur.idx;
				for(int i  = 0; i < 26; i++) {
					if(idx <= cur.cnt[i]) {
						cur = cur.childs[i];
						break;
					}
					idx -= cur.cnt[i];
				}
			}
		}
		int search(String str) {
			Node cur = root; 
			for(int i = 0; i < str.length(); i++) {
				int ch = str.charAt(i) - 'a';
				if(cur.childs[ch] == null) return -1;  // i번째 문자가 없다면 실패
				cur = cur.childs[ch];
			}
			if(cur.idx == -1) return cur.maxIdx; 
			return cur.idx;
		}
		int findPage(int idx) {
			int pnum = 0;
			Node cur = root;
			for(int i = 0; i < words[idx].length(); i++) {
				int ch = words[idx].charAt(i) - 'a';
				for(int j = 0; j < ch; j++) pnum += cur.cnt[j];
				if(cur.idx != -1) pnum++;
				cur = cur.childs[ch];
			}
			return pnum;
		}
	}
	
	// ** Memory Pool** 
	Node[] pool = new Node[420001]; // 70000 x 6
	int pCnt; 
	
	Node newNode() {
		Node node = pool[pCnt]; 
		if(node == null) {
			node = new Node();
			pool[pCnt] = node; 
		}
		else {
			node.idx = -1;
			node.maxIdx = -1;
			node.maxImportance = -1;
			for(int i = 0; i < 26; i++) {
				node.childs[i] = null;
				node.cnt[i] = 0; 
			}
		}
		pCnt++; 
		return node; 
	}
	
	Trie trie; 
	
	public void init()
	{
		pCnt = 0; // pool 초기화
		trie = new Trie();
		idCnt = 0;
		trie.root.idx = 0;
		curPage = trie.insert("a", 1); // "a", importance=1인 값이 하나 존재하는 상태로 시작
		return;
	}

	// 70,000
	public Main.PAGE add(String mWord, int mImportance)
	{
		Main.PAGE res = new Main.PAGE();
		curPage = trie.insert(mWord, mImportance);
		res.no =  curPage;
		res.word = mWord;
		return res;
	}

	// 100,000
	public Main.PAGE move(int mDir)
	{
		Main.PAGE res = new Main.PAGE();
		if(mDir < 0) curPage--;
		else curPage++;
		res.no = curPage;
		res.word = words[trie.move(curPage+1)]; 
		return res;
	}

	// 100,000
	public Main.PAGE search(String mStr)
	{
		Main.PAGE res = new Main.PAGE();
		res.no =  -1;
		int idx = trie.search(mStr);
		if(idx == -1) res.no = -1;    // 시작하는 단어가 없거나 일치하지 않
		else {
			curPage = trie.findPage(idx);
			res.no = curPage;
			res.word = words[idx]; 
		}
		return res;
	}

	// 100,000
	public Main.PAGE go(int mNo)
	{
		Main.PAGE res = new Main.PAGE();
		res.no =  mNo;
		curPage = mNo; 
		res.word = words[trie.move(curPage+1)];
		return res;
	}
}