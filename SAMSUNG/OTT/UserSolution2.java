package OTT;

import java.util.*;

public class UserSolution2 {

	// 영화
	class Movie implements Comparable<Movie> {
		int id;
		int genre; // 1~5
		int total;
		int regTime;
		boolean isDeleted;
		public Movie(int id, int genre, int total, int regTime) {
			super();
			this.id = id;
			this.genre = genre;
			this.total = total;
			this.regTime = regTime;
			this.isDeleted = false;
		}
		@Override
		public int compareTo(Movie other) {
			// 2. 시청 목록에 있는 가장 최근 시청한 최대 5개의 영화들 중에서 사용자가 준 평점이 가장 높은 영화와 같은 장르의 영화만 추천
			if (this.total != other.total) return Integer.compare(other.total, this.total);
			//    가장 높은 평점을 준 영화가 여러 개이면 그 중에서 가장 최근에 시청한 영화와 같은 장르의 영화만 추천
			if (this.regTime != other.regTime) return Integer.compare(other.regTime, this.regTime);
			return Integer.compare(this.id, other.id);
		}
	}
	
	// 영화 추천
	// 1. 사용자가 이미 시청했거나 삭제된 영화는 제외

	// 3. 만약, 사용자의 시청 목록에 어떤 영화도 없는 경우 장르에 상관없이 모든 영화에 대해서 추천
	// 4. 위 조건을 만족하는 영화들 중에서 총점이 가장 높은 순으로 최대 5개를 추천
	//    만약 총점이 같은 경우 더 최신에 등록한 영화가 우선 순위가 더 높다.
	
	class User {
		int id;
		ArrayList<Record> history;
		HashSet<Integer> alreadyWatchMovies;
		public User(int id) {
			super();
			this.id = id;
			this.history = new ArrayList<>();
			this.alreadyWatchMovies = new HashSet<>();
		}
	}
	
	// 시청 목록을 관리해야한다.
	class Record {
		Movie movie;
		int rating;
		int watchTime;
		public Record(Movie movie, int rating, int watchTime) {
			super();
			this.movie = movie;
			this.rating = rating;
			this.watchTime = watchTime;
		}
	}

	// 유저번호 ID로 사용
	int userSeq;
	User[] users;
	// 영화 목록
	HashMap<Integer, Movie> movieMap;
	TreeSet<Movie>[] genreSet;
	int movieSeq;
	int watchSeq;
	
	public void init(int N) {
		userSeq = N;
		users = new User[N + 1];
		for (int i = 1; i <= N; i++) {
			users[i] = new User(i);
		}
		movieMap = new HashMap<>();
		genreSet = new TreeSet[6];
		for (int i = 1; i <= 5; i++) {
			genreSet[i] = new TreeSet<>();
		}
		movieSeq = 0;
		watchSeq = 0;
	}

	// 최대 10,000
	public int add(int mID, int mGenre, int mTotal) {
		if (movieMap.containsKey(mID)) {
			return 0;
		}
		movieSeq++;
		Movie movie = new Movie(mID, mGenre, mTotal, movieSeq);
		movieMap.put(mID, movie);
		genreSet[mGenre].add(movie);
	    return 1;
	}

	// 최대 1,000
	public int erase(int mID) {
		if (!movieMap.containsKey(mID)) {
			return 0;
		}
		Movie movie = movieMap.get(mID);
		if (movie.isDeleted) {
			return 0;
		}
		// 순서가 상관이 있는가?
		movie.isDeleted = true;
		genreSet[movie.genre].remove(movie);
		
	    return 1;
	}

	// 최대 30,000
	public int watch(int uID, int mID, int mRating) {
		// 영화가 없을 경우
		if (!movieMap.containsKey(mID)) {
			return 0;
		}
		Movie movie = movieMap.get(mID);
		// 영화가 삭제된 경우
		if (movie.isDeleted) {
			return 0;
		}
		// 이미 시청한 영화일 경우
		if (users[uID].alreadyWatchMovies.contains(mID)) {
			return 0;
		}
		// 장르 순위 업데이트
		genreSet[movie.genre].remove(movie);
		movie.total += mRating;
		genreSet[movie.genre].add(movie);
		watchSeq++;
		Record rc = new Record(movie, mRating, watchSeq);
		users[uID].history.add(rc);
		users[uID].alreadyWatchMovies.add(mID);
	    return 1;
	}

	// 최대 5,000
	public Main.RESULT suggest(int uID) {
		Main.RESULT res = new Main.RESULT();
//		ⓐ 사용자가 이미 시청했거나 삭제된 영화는 제외한다.

//		ⓒ 만약, 사용자의 시청 목록에 어떤 영화도 없는 경우 장르에 상관없이 모든 영화에 대해서 추천한다.
//		ⓓ 위 조건을 만족하는 영화들 중에서 총점이 가장 높은 순으로 최대 5개를 추천한다.
//		     만약 총점이 같은 경우 더 최신에 등록한 영화가 우선 순위가 더 높다.
		User user = users[uID];
		ArrayList<Record> record = user.history;
		int maxRating = -1;
		int recommendGenre = 0;
		int watchTime = -1;
		int count = 0;
//		ⓑ 시청 목록에 있는 가장 최근 시청한 최대 5개의 영화들 중에서 사용자가 준 평점이 가장 높은 영화와 같은 장르의 영화만 추천한다.
//	     가장 높은 평점을 준 영화가 여러 개이면 그 중에서 가장 최근에 시청한 영화와 같은 장르의 영화만 추천한다.
		for (int i = record.size() - 1; i >= 0; i--) {
			Record current = record.get(i);
			if (current.movie.isDeleted) continue; // 삭제된 영화 제외
			if (current.rating > maxRating) {
				maxRating = current.rating;
				recommendGenre = current.movie.genre;
				if (current.watchTime >= watchTime) {
					watchTime = current.watchTime;
					recommendGenre = current.movie.genre;
				}
			}
			count++;
			if (count == 5) break;
		}
		if (count == 0) {
			recommendGenre = -1;
		}
		// 영화 추천 5개
		// 시청 목록이 있는 경우
		if (recommendGenre != -1) {
			Iterator<Movie> it = genreSet[recommendGenre].iterator(); // 같은 장르 가져오기
			while (it.hasNext() && res.cnt < 5) {
				Movie movie = it.next();
				// 이미 본 영화 ㄴㄴ
				if (user.alreadyWatchMovies.contains(movie.id)) continue;
				// 안본 영화 ㅇㅇ
				res.IDs[res.cnt++] = movie.id;
//				if (res.cnt == 4) break;
			}
		} else {
			// 시청 목록이 없는 경우, 전체 추천
			// 모든 영화 목록 중, 총점이 가장 높은 순으로 최대 5개
			// 총점이 같은 경우 더 최신에 등록한 영화가 우선 순위가 높음
			TreeSet<Movie> allMovie = new TreeSet<>(); 
			for (int i = 1; i <= 5; i++) {
				Iterator<Movie> it = genreSet[i].iterator();
				while (it.hasNext()) {
					Movie movie = it.next();
					allMovie.add(movie);
				}
			}
			ArrayList<Integer> recommendList = new ArrayList<>();
			int recommendCnt = 0;
			Iterator<Movie> it = allMovie.iterator();
			while (it.hasNext()) {
				Movie movie = it.next();
				recommendList.add(movie.id);
				res.IDs[recommendCnt] = movie.id;
				recommendCnt++;
				res.cnt++;
				if (recommendCnt == 5) break;
			}
			if (recommendCnt == 0) {
				res.cnt = 0;
			}
		}
		
	    return res;
	}
}
