package OTT;

import java.io.*;
import java.util.*;

class UserSolution3 {
	
	class Movie implements Comparable<Movie> {
		int seq;
		int id;
		int genre;
		int total;
		boolean isDeleted;
		public Movie(int seq, int id, int genre, int total) {
			super();
			this.seq = seq;
			this.id = id;
			this.genre = genre;
			this.total = total;
			this.isDeleted = false;
		}
		@Override
		public int compareTo(Movie o) {
			if (this.total != o.total) return Integer.compare(o.total, this.total);
			if (this.seq != o.seq) return Integer.compare(o.seq, this.seq);
			return 0;
		}
	}
	
	class User {
		int id;
		// 시청목록
		ArrayList<Record> history;
		HashSet<Integer> alreadyWatchingMovie;
		public User(int id) {
			super();
			this.id = id;
			this.history = new ArrayList<>();
			this.alreadyWatchingMovie = new HashSet<>();
		}
	}
	
	class Record {
		int seq;
		Movie movie;
		int score;
		public Record(int seq, Movie movie, int score) {
			super();
			this.seq = seq;
			this.movie = movie;
			this.score = score;
		}
	}
	
	int seq;
	User[] users;
	HashMap<Integer, Movie> movieMap;
	TreeSet<Movie>[] genreSet;
	int movieSeq;
	int watchSeq;
	
	public void init(int N) {
		seq = 0;
		movieSeq = 0;
		watchSeq = 0;
		users = new User[N + 1];
		for (int i = 1; i <= N; i++) {
			users[i] = new User(i);
		}
		movieMap = new HashMap<>();
		genreSet = new TreeSet[6];
		for (int i = 1; i <= 5; i++) {
			genreSet[i] = new TreeSet<>();
		}
	}

	public int add(int mID, int mGenre, int mTotal) {
		// 등록에 실패한 경우는 같은 ID를 가진 영화가 이미 등록된 경우
		if (movieMap.containsKey(mID)) return 0;
		Movie newMovie = new Movie(++movieSeq, mID, mGenre, mTotal);
		movieMap.put(mID, newMovie);
		genreSet[mGenre].add(newMovie);
	    return 1;
	}

	public int erase(int mID) {
		// 삭제에 실패한 경우는 ID가 mID인 영화가 등록된 경우가 없거나 이미 삭제된 경우이다.
		if (!movieMap.containsKey(mID)) return 0;
		if (movieMap.get(mID).isDeleted) return 0;
		
		Movie eraseMovie = movieMap.get(mID);
		genreSet[eraseMovie.genre].remove(eraseMovie);
		eraseMovie.isDeleted = true;
	    return 1;
	}

	public int watch(int uID, int mID, int mRating) {
		// 시청에 실패한 경우는 ID가 mID인 영화가 등록된 경우가 없거나
		if (!movieMap.containsKey(mID)) return 0;
		// 삭제되었거나
		if (movieMap.get(mID).isDeleted) return 0;
		// 사용자 uID가 이미 시청한 영화인 경우이다.
		if (users[uID].alreadyWatchingMovie.contains(mID)) return 0;
		
		Movie watchedMovie = movieMap.get(mID);
		genreSet[watchedMovie.genre].remove(watchedMovie);
		watchedMovie.total += mRating;
		genreSet[watchedMovie.genre].add(watchedMovie);
		Record record = new Record(++watchSeq, watchedMovie, mRating);
		users[uID].history.add(record);
		users[uID].alreadyWatchingMovie.add(mID);
	    return 1;
	}

	public Main.RESULT suggest(int uID) {
	    Main.RESULT res = new Main.RESULT();
	    User user = users[uID];
	    // 시청 목록에 있는 가장 최근 시청한 최대 5개의 영화들 중, 사용자가 준 평점이 가장 높은 영화와 같은 장르의 영화만 추천
	    // 가장 높은 평점을 준 영화가 여러 개면 그 중에서 가장 최근에 시청한 여화와 같은 장르의 영화만 추천
	    
	    // 최근 5개 영화 찾기
	    int count = 0;
	    int maxRating = -1;
	    int maxWatching = -1;
	    int recommendGenre = 0;
	    for (int i = user.history.size() - 1; i >= 0; i--) {
	    	Record record = user.history.get(i);
	    	if (record.movie.isDeleted) continue; // 삭제된 영화 제외
	    	// 점수가 가장 높은 영화 찾기
	    	if (record.score > maxRating) {
	    		maxRating = record.score;
	    		recommendGenre = record.movie.genre;
	    		if (record.seq > maxWatching) {
	    			maxWatching = record.seq;
	    			recommendGenre = record.movie.genre;
	    		}
	    	}
	    	count++;
	    	if (count == 5) break;
	    }
	    // 시청 목록이 없음 즉, 최근 영화가 없음
	    if (count == 0) {
	    	recommendGenre = -1;
	    }
	    
	    if (recommendGenre != -1) { // 시청 목록이 있음, 같은 장르의 영화 추천
	    	Iterator<Movie> mit = genreSet[recommendGenre].iterator();
	    	int whileCount = 0;
	    	
	    	while (mit.hasNext() && whileCount < 5) {
	    		int recommendMid = mit.next().id;
	    		// 만약 이미 시청한 영화라면 패스
	    		if (user.alreadyWatchingMovie.contains(recommendMid)) continue;
	    		// 삭제된 영화도 패스
	    		if (movieMap.get(recommendMid).isDeleted) continue;
	    		res.IDs[whileCount++] = recommendMid;
	    	}
	    	res.cnt = whileCount;
	    } else { // 시청 목록이 없음, 전체 추천
	    	// 장르별로 나누어진 영화를 장르 구분 없이 하나로 합침
	    	TreeSet<Movie> allMovies = new TreeSet<>();
	    	for (int i = 1; i <= 5; i++) {
	    		Iterator<Movie> mv = genreSet[i].iterator();
	    		while (mv.hasNext()) {
	    			Movie current = mv.next();
	    			// 삭제된 영화도 패스
	    			if (current.isDeleted) continue;
	    			// 시청목록이 없기 때문에 시청목록은 조회하지 않음
	    			allMovies.add(current);
	    		}
	    	}
	    	// 순차적으로 5개 뺌
	    	Iterator<Movie> av = allMovies.iterator();
	    	int whileCount = 0;
	    	while (av.hasNext() && whileCount < 5) {
	    		int recommendMid = av.next().id;
	    		res.IDs[whileCount++] = recommendMid;
	    	}
	    	res.cnt = whileCount;
	    }
	    
	    return res;
	}
}

