package 메모장프로그램;

import java.io.*;
import java.util.*;

public class UserSolution_Teacher {

	ArrayList<Character> textbox[];
	int Height, Width;
	int cursorRow, cursorCol;
	int cntAlpha[][];
	
	void init(int H, int W, char mStr[])
	{
		Height = H;
		Width = W;
		textbox = new ArrayList[Height];
		for(int i = 0; i < Height; i++)
			textbox[i] = new ArrayList<>();
		cntAlpha = new int[Height][256];
		for(int i = 0; i < mStr.length; i++) {
			textbox[i / Width].add(mStr[i]);
			cntAlpha[i / Width][mStr[i]]++;
		}
		cursorRow = cursorCol = 0;
	}
	
	void insert(char mChar)
	{
		textbox[cursorRow].add(cursorCol, mChar);
		cntAlpha[cursorRow][mChar]++;
		int nowRow = cursorRow;
		while(textbox[nowRow].size() > Width) {
			char ch = textbox[nowRow].remove(textbox[nowRow].size() - 1);
			cntAlpha[nowRow++][ch]--;
			cntAlpha[nowRow][ch]++;
			textbox[nowRow].add(0, ch);
		}
		cursorCol++;
		if(cursorCol >= Width) {
			cursorCol = 0;
			cursorRow++;
		}
	}

	char moveCursor(int mRow, int mCol)
	{
		mRow--; mCol--;
		boolean isOutside = false;
		if(textbox[mRow].size() <= mCol) {
			isOutside = true;
			while(mRow > 0 && textbox[mRow].isEmpty())
				mRow--;
			mCol = textbox[mRow].size();
		}
		cursorRow = mRow;
		cursorCol = mCol;
		Character ret = '$';
		if(!isOutside) {
			ret = textbox[cursorRow].get(cursorCol);
		}
		return ret;
	}

	int countCharacter(char mChar)
	{
		int ret = 0;
		int col = 0;
		for(char ch : textbox[cursorRow]) {
			if(col >= cursorCol && mChar == ch)
				ret++;
			col++;
		}
		for(int row = cursorRow + 1; row < Height; row++)
			ret += cntAlpha[row][mChar];
		return ret;
	}
}