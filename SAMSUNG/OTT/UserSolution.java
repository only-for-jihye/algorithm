package OTT;

import java.util.*;

public class UserSolution {
	private static final int MAX_N = 1000;
    private static final int MAX_GENRE = 5;
    
    // 영화 정보를 담는 클래스
    // TreeSet에서 정렬 기준을 만족하기 위해 Comparable 구현
    private static class Movie implements Comparable<Movie> {
        int mID;
        int genre;
        int totalScore;
        int registerTime; // add 호출 순서 (클수록 최신)
        boolean isDeleted;

        public Movie(int mID, int genre, int totalScore, int registerTime) {
            this.mID = mID;
            this.genre = genre;
            this.totalScore = totalScore;
            this.registerTime = registerTime;
            this.isDeleted = false;
        }

        @Override
        public int compareTo(Movie o) {
            // 1. 총점이 높은 순
            if (this.totalScore != o.totalScore) {
                return o.totalScore - this.totalScore;
            }
            // 2. 등록 시간이 더 최신인 순
            if (this.registerTime != o.registerTime) {
                return o.registerTime - this.registerTime;
            }
            // 3. ID 기준 (TreeSet 중복 방지 및 일관성 유지용)
            return this.mID - o.mID;
        }
    }

    // 시청 기록을 담는 클래스
    private static class WatchRecord {
        Movie movie;
        int rating;
        int watchTime; // watch 호출 순서

        public WatchRecord(Movie movie, int rating, int watchTime) {
            this.movie = movie;
            this.rating = rating;
            this.watchTime = watchTime;
        }
    }

    // 사용자 클래스
    private static class User {
        int uID;
        ArrayList<WatchRecord> history; // 시청 이력 (순서 유지)
        HashSet<Integer> watchedMovieIds; // 중복 시청 체크용

        public User(int uID) {
            this.uID = uID;
            this.history = new ArrayList<>();
            this.watchedMovieIds = new HashSet<>();
        }
    }

    // Global Data Structures
    private User[] users;
    private HashMap<Integer, Movie> movieMap; // ID -> Movie 객체
    private TreeSet<Movie>[] genreSets; // 장르별 정렬된 영화 목록
    
    private int addSequence;   // 영화 등록 순서 카운터
    private int watchSequence; // 시청 순서 카운터
    private int userCount;

    // -------------------------------------------------------
    // API 구현
    // -------------------------------------------------------

    @SuppressWarnings("unchecked")
    public void init(int N) {
        userCount = N;
        users = new User[N + 1];
        for (int i = 1; i <= N; i++) {
            users[i] = new User(i);
        }

        movieMap = new HashMap<>();
        
        // 장르는 1~5번
        genreSets = new TreeSet[MAX_GENRE + 1];
        for (int i = 1; i <= MAX_GENRE; i++) {
            genreSets[i] = new TreeSet<>();
        }

        addSequence = 0;
        watchSequence = 0;
    }

    public int add(int mID, int mGenre, int mTotal) {
        if (movieMap.containsKey(mID)) {
            // 이미 존재하는 ID라면 실패 (삭제된 ID라도 재등록 불가하므로 key check로 충분)
            return 0;
        }

        addSequence++;
        Movie newMovie = new Movie(mID, mGenre, mTotal, addSequence);
        
        movieMap.put(mID, newMovie);
        genreSets[mGenre].add(newMovie);

        return 1;
    }

    public int erase(int mID) {
        if (!movieMap.containsKey(mID)) return 0;

        Movie target = movieMap.get(mID);
        if (target.isDeleted) return 0;

        // 1. TreeSet에서 제거
        genreSets[target.genre].remove(target);
        
        // 2. 삭제 플래그 설정 (사용자 히스토리 등에서 나중에 필터링됨)
        target.isDeleted = true;
        
        // Map에서는 삭제하지 않고 객체 상태만 변경 (재등록 방지 확인용)
        
        return 1;
    }

    public int watch(int uID, int mID, int mRating) {
        // 영화 체크
        Movie movie = movieMap.get(mID);
        if (movie == null || movie.isDeleted) return 0;

        // 사용자 체크
        User user = users[uID];
        if (user.watchedMovieIds.contains(mID)) return 0;

        // 1. 영화 총점 업데이트 (TreeSet 갱신 필요)
        genreSets[movie.genre].remove(movie); // 기존 점수 상태로 제거
        movie.totalScore += mRating;          // 점수 변경
        genreSets[movie.genre].add(movie);    // 변경된 점수로 재삽입

        // 2. 사용자 시청 이력 추가
        watchSequence++;
        user.history.add(new WatchRecord(movie, mRating, watchSequence));
        user.watchedMovieIds.add(mID);

        return 1;
    }

    public Main.RESULT suggest(int uID) {
        Main.RESULT res = new Main.RESULT();
        User user = users[uID];

        // -------------------------------------------------
        // 1단계: 추천 기준 장르 선정
        // -------------------------------------------------
        int targetGenre = -1; // -1이면 모든 장르 대상
        
        // 최신 시청 이력부터 탐색하여 유효한(삭제되지 않은) 최근 5개 수집
        // 리스트의 뒤쪽이 최신임
        int validCount = 0;
        int bestRating = -1;
        int bestWatchTime = -1;

        for (int i = user.history.size() - 1; i >= 0; i--) {
            WatchRecord record = user.history.get(i);
            if (record.movie.isDeleted) continue; // 삭제된 영화 건너뜀

            // 후보군 갱신 로직
            // 평점이 더 높거나, 평점이 같으면 더 최근에 본 것
            if (record.rating > bestRating) {
                bestRating = record.rating;
                bestWatchTime = record.watchTime;
                targetGenre = record.movie.genre;
            } else if (record.rating == bestRating) {
                // rating이 같으면 더 최근 것(watchTime이 큰 것)이 우선
                // 역순 탐색 중이고, 같은 rating을 처음 만나는 것이 더 최신일 수밖에 없음?
                // -> 아니오, 역순 탐색 중 max 갱신 로직이므로
                //    현재 record가 기존 best보다 watchTime이 작음.
                //    따라서 rating이 더 클 때만 갱신하고, 같을 땐 갱신 안 하면 자동으로 최신 것이 유지됨.
                //    하지만 명시적으로 조건 비교:
                if (record.watchTime > bestWatchTime) {
                    bestWatchTime = record.watchTime;
                    targetGenre = record.movie.genre;
                }
            }

            validCount++;
            if (validCount == 5) break;
        }

        // 유효한 시청 기록이 하나도 없으면 전체 장르 추천
        if (validCount == 0) {
            targetGenre = -1;
        }

        // -------------------------------------------------
        // 2단계: 영화 추천 (최대 5개)
        // -------------------------------------------------
        ArrayList<Movie> candidates = new ArrayList<>();

        if (targetGenre != -1) {
            // [특정 장르 추천]
            // TreeSet을 순회하며 조건에 맞는 5개 찾기
            Iterator<Movie> it = genreSets[targetGenre].iterator();
            while (it.hasNext() && res.cnt < 5) {
                Movie m = it.next();
                // 이미 본 영화 제외 (삭제된 영화는 erase시 set에서 제거되므로 체크 불필요)
                if (user.watchedMovieIds.contains(m.mID)) continue;
                
                res.IDs[res.cnt++] = m.mID;
            }
        } else {
            // [전체 장르 추천] - 5개 장르의 TreeSet을 병합 정렬하듯 탐색
            // 각 장르별 Iterator 준비
            Iterator<Movie>[] iters = new Iterator[MAX_GENRE + 1];
            Movie[] currentHeads = new Movie[MAX_GENRE + 1];

            // 초기화: 각 장르의 첫 번째 유효한 후보를 로드
            for (int g = 1; g <= MAX_GENRE; g++) {
                iters[g] = genreSets[g].iterator();
                currentHeads[g] = getNextValidMovie(iters[g], user);
            }

            // 5개 뽑을 때까지 반복
            while (res.cnt < 5) {
                int bestGenreIdx = -1;
                Movie bestMovie = null;

                // 5개 장르 중 가장 우선순위 높은 영화 선택
                for (int g = 1; g <= MAX_GENRE; g++) {
                    Movie m = currentHeads[g];
                    if (m == null) continue;

                    if (bestMovie == null) {
                        bestMovie = m;
                        bestGenreIdx = g;
                    } else {
                        if (m.compareTo(bestMovie) < 0) { // compareTo가 오름차순 기준 음수면 m이 더 앞섬(우선)
                            bestMovie = m;
                            bestGenreIdx = g;
                        }
                    }
                }

                if (bestMovie == null) break; // 추천할 영화가 더 이상 없음

                // 결과 저장
                res.IDs[res.cnt++] = bestMovie.mID;

                // 선택된 장르에서 다음 후보 로드
                currentHeads[bestGenreIdx] = getNextValidMovie(iters[bestGenreIdx], user);
            }
        }

        return res;
    }

    // Iterator에서 사용자가 보지 않은 다음 유효 영화를 가져오는 헬퍼 함수
    private Movie getNextValidMovie(Iterator<Movie> it, User user) {
        while (it.hasNext()) {
            Movie m = it.next();
            // 이미 본 영화면 스킵
            if (user.watchedMovieIds.contains(m.mID)) continue;
            return m;
        }
        return null;
    }
}
