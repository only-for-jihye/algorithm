package 마라톤;

//Main.java

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main {

//  private static UserSolution usersolution = new UserSolution();
  private static UserSolution2 usersolution = new UserSolution2();

  private final static int CMD_INIT   = 100;
  private final static int CMD_ADD	= 200;
	private final static int CMD_REMOVE	= 300;
	private final static int CMD_GETLEN	= 400;
	
  private static boolean run(BufferedReader br) throws Exception
  {
  	int id[] = new int[10];
  	int sa[] = new int[10];
  	int sb[] = new int[10];
  	int len[] = new int[10];
		String strTmp;

      boolean ok = false;

      StringTokenizer st = new StringTokenizer(br.readLine(), " ");
      int Q = Integer.parseInt(st.nextToken());

      for (int q = 0; q < Q; q++) {
      	st = new StringTokenizer(br.readLine());
          int cmd = Integer.parseInt(st.nextToken());

          if (cmd == CMD_INIT) {
				int N = Integer.parseInt(st.nextToken());
				usersolution.init(N);
              ok = true;
          } else if (cmd == CMD_ADD) {
				strTmp = st.nextToken();
				int K = Integer.parseInt(st.nextToken());
				for (int i = 0; i < K; i++) {
					st = new StringTokenizer(br.readLine());
					strTmp = st.nextToken();
					id[i] = Integer.parseInt(st.nextToken());
					strTmp = st.nextToken();
					sa[i] = Integer.parseInt(st.nextToken());
					strTmp = st.nextToken();
					sb[i] = Integer.parseInt(st.nextToken());
					strTmp = st.nextToken();
					len[i] = Integer.parseInt(st.nextToken());
				}
				usersolution.addRoad(K, id, sa, sb, len);
			} else if (cmd == CMD_REMOVE) {
				strTmp = st.nextToken();
				id[0] = Integer.parseInt(st.nextToken());
          	usersolution.removeRoad(id[0]);
			} 
          else if (cmd == CMD_GETLEN) {
				strTmp = st.nextToken();
          	id[0] = Integer.parseInt(st.nextToken());
      		int ret = usersolution.getLength(id[0]);
				strTmp = st.nextToken();
      		int ans = Integer.parseInt(st.nextToken());
          	if (ret != ans) {
              	ok = false;
              }
			}
			else ok = false;
      }
      return ok;
  }

  public static void main(String[] args) throws Exception {

      System.setIn(new java.io.FileInputStream("SAMSUNG/marathon/sample_input.txt"));
      
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " "); 

      int T = Integer.parseInt(st.nextToken());
      int MARK = Integer.parseInt(st.nextToken());

      for (int tc = 1; tc <= T; tc++) {
          int score = run(br) ? MARK : 0;
          System.out.println("#" + tc + " " + score);
      }

      br.close();
  }
}