package compile3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class lr1 {
	private static ArrayList<String> ids=new ArrayList<>();;//存具体的标识符
	private static ArrayList<String> nums=new ArrayList<>();;//存具体的数字
	private static int temp;
	private static boolean isFirst=true;
	private static Integer index = 0; // 步骤
	private static Integer row = 1; // 执行到步数的行数
	private static Integer column = 0; // 执行到步数的列数
	private static Integer inputIndex = 0; // 执行到步数的列数
	private static Integer GrammarIndex = 0; // 规约的产生式的index
	private static String NowGrammar = ""; // 当前产生式的式子
	private static String NowGrammarLeft = ""; // 当前产生式左边的式子
	private static String NowGrammarRight = ""; // 当前产生式右边的式子
	private static Stack<Integer> stateStack = new Stack<>(); // 状态栈
	private static Stack<String> markStack = new Stack<>(); // 符号栈
	private static ArrayList<InputArr> inputArr = new ArrayList<>(); // 输入串
	private static Integer action = 1;// 1--移进 2--规约 0--接受
	private static ArrayList<String> Grammar = new ArrayList<>(); // 产生式
	private static ArrayList<String> GrammarLeft = new ArrayList<>(); // 产生式左边的式子集合
	private static ArrayList<String> GrammarRight = new ArrayList<>(); // 产生式右边的式子集合
	private static String[][] map = new String[1000][1000]; // goto表映射

	/**
	 * 输入串类型的类（为了实现id和digit能对应具体的值），第一个属性是符号，第二个属性是其对应的具体值
	 * @author 雨暮空华
	 *
	 */
	static class InputArr{
		String string;
		String to;
		public InputArr(String string, String to) {
			super();
			this.string = string;
			this.to = to;
		}
		public InputArr() {
			super();
		}
		@Override
		public String toString() {
			return "InputArr [string=" + string + ", to=" + to + "]";
		}
		
	}
	public static String reverse(String s) {
		return new StringBuffer(s).reverse().toString();

	}

	public static Integer cssLen(String s) {
		int num = 1;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				num++;
			}
		}
		return num;

	}

	public static void main(String[] args) throws IOException {

		// 读取output.txt文档中的单词存入inputArr集合中
		try {
			String filePath = "src/LexerOutput.txt";
			FileInputStream fin = new FileInputStream(filePath);
			InputStreamReader reader = new InputStreamReader(fin);
			BufferedReader buffReader = new BufferedReader(reader);
			String strTmp = "";
			while ((strTmp = buffReader.readLine()) != null) {
				String[] string = strTmp.split("`");//以"'"分割字符串
				String left=string[strTmp.split("`").length - 2];//将以"'"分割字符串的倒数第二个字符串存入left
				String right=string[strTmp.split("`").length - 1];//将以"'"分割字符串的倒数第一个字符串存入right
				InputArr arr;
				if(left.equals("22")) {
					arr=new InputArr("id",right);
					inputArr.add(arr);
				}else if(left.equals("23")) {
					arr=new InputArr("digit",right);
					inputArr.add(arr);
				}else {
					arr=new InputArr(right,"");
					inputArr.add(arr);//将right字符串存入inputArr集合中
				}
				
			}
			inputArr.add(new InputArr("$",""));
			buffReader.close();
		} catch (IOException e1) {
		}
		
		// 读取grammar.xls中的产生式存入Grammar集合中，将产生式左边的东西存入GrammarLeft，将产生式右边的东西存入GrammarRight
		FileInputStream fs;
		try {
			fs = new FileInputStream("src/grammar.xls");
			POIFSFileSystem ps = new POIFSFileSystem(fs); // 使用POI提供的方法得到excel的信息
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0); // 获取到工作表，因为一个excel可能有多个工作表
			int rows = sheet.getPhysicalNumberOfRows();
			// 读取产生式
//			System.out.println(rows);
			Row t = sheet.getRow(0);
			for (int row = 0; row < rows; row++) {
				Row r = sheet.getRow(row);
				Grammar.add(r.getCell(1).getStringCellValue());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		for (int i = 0; i < Grammar.size(); i++) {
			String[] strs = Grammar.get(i).split(" -> ");//以"->"分割字符串
			int len = strs.length;
			GrammarLeft.add(strs[0]);//将以"->"分割字符串的第一个字符串存入GrammarLeft中
			GrammarRight.add(strs[Grammar.get(i).split(" -> ").length - 1]);//将以"->"分割字符串的最后一个个字符串存入GrammarRight中
		}
//		System.out.println("/////////");
//		for (int j = 0; j <GrammarLeft.size(); j++) {
//			System.out.println(GrammarLeft.get(j));
//			
//		}
//		System.out.println("/////////"+GrammarLeft.size());
//		for (int j = 0; j <GrammarRight.size(); j++) {
//			System.out.println(GrammarRight.get(j));
//			
//		}

		// 读取goto.xls数据映射到二维数组map中
		try {
			fs = new FileInputStream("src/lr1.xls");
			POIFSFileSystem ps = new POIFSFileSystem(fs); // 使用POI提供的方法得到excel的信息
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0); // 获取到工作表，因为一个excel可能有多个工作表
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++)
			{
				HSSFRow row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				int cells = row.getPhysicalNumberOfCells();
				for (int c = 1; c < cells; c++)
				{
					HSSFCell cell = row.getCell(c);
					String value = null;
					switch (cell.getCellType())
					{
					case HSSFCell.CELL_TYPE_FORMULA:
						value = cell.getCellFormula();
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						value = String.valueOf(cell.getNumericCellValue());
						break;
					case HSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue(); // 因为excel的缘故++ --等填充内容前有空格，因为遇到这些值把空格去掉
						if (value.startsWith(" ")) {
							value = value.substring(1, value.length());
						}
						break;
					default:
						break;
					}
					map[r - 1][c - 1] = value;//将lr1.xls数据存入到二维数组map中
				}
			}
//			System.out.println(map[5][1]);
//			for (int i = 0; i < 350; i++) {
//				for(int j=0;j<84;j++) {
//					System.out.println(map[i][j]);
//				}
//			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		stateStack.push(1);
		markStack.push("#");
		while (action != 0) {
			for (int i = 0; i < 84; i++) {
				if (map[0][i].equals(inputArr.get(inputIndex).string)) {
					if ("S".equals(map[row][i].subSequence(0, 1))) { // 移进
						index++; // 步骤+1
						row = Integer.parseInt(map[row][i].substring(1, map[row][i].length())) + 1; // 因为lr1.xls中是从-1开始
						stateStack.push(row); // 改变状态栈
						markStack.push(inputArr.get(inputIndex).string); // 改变符号栈
						inputIndex++;
						System.out.println("第" + index + "步是移进操作" +"\t"+ "状态栈为：" + stateStack +"\t"+ "符号栈为：" + markStack);
						System.out.println(inputArr.get(inputIndex).string);
						break;
					} else if ("r".equals(map[row][i].subSequence(0, 1))) {
						int count = 0;
						GrammarIndex = Integer.parseInt(map[row][i].substring(1)) - 1; // 因为grammar.xls中是从1开始
						NowGrammar = Grammar.get(GrammarIndex);
						
						NowGrammarLeft = GrammarLeft.get(GrammarIndex);
						NowGrammarRight = GrammarRight.get(GrammarIndex);
						String[] string = NowGrammarRight.split(" "); // 以空格分开成若干个字符串数组
						int NowGrammarRightLen = NowGrammarRight.split(" ").length - 1;
						for (int j = NowGrammarRightLen; j >= 0; j--) {
							if (string[j].equals("ε")) { // 产生式右边为空，符号栈不需要出栈
								count++;
							} else if (string[j].equals(markStack.pop())) {
								count++;
							}
						}
						if (string.length == count) { // 判断产生式右边的式子是否等于符号栈中出来的式子
							markStack.push(NowGrammarLeft);
							if (!string[0].equals("ε")) { // 产生式右边为空，状态栈不需要出栈
								for (int j = 0; j < cssLen(NowGrammarRight); j++) {
									stateStack.pop();
								}
							}

							row = stateStack.lastElement();
							for (int k = 0; k < 84; k++) {
								if (map[0][k].equals(NowGrammarLeft)) {
									stateStack.push((int) Float.parseFloat(map[row][k]) + 1);
									index++;
									row = (int) Float.parseFloat(map[row][k]) + 1;
									break;

								}
							}
							System.out.println("第" + index + "步是规约操作" + "\t"+"状态栈为：" + stateStack + "\t"+"符号栈为：" + markStack
									+ "\t"+"产生式为：" + NowGrammar);
							//System.out.println("woshishei1:"+inputArr);
							//System.out.println("woshishei2:"+inputArr.get(inputIndex-1).to);
//							if((GrammarIndex+1)==49) {
//								String str;
//								str=inputArr.get(inputIndex-1).to;
//								while(inputArr.get(inputIndex-2).string.equals(",")&&inputArr.get(inputIndex-3).string.equals("digit")) {
//									str+=inputArr.get(inputIndex-3).to;
//								}
//								Semantics.sems(GrammarIndex+1, str);
//							}else {
							//temp=inputIndex;
							if((GrammarIndex+1)==50) {
								if(isFirst) {
									temp=inputIndex-3;
									isFirst=false;
								}else {
									temp-=2;
								}
								
								Semantics.sems(GrammarIndex+1, inputArr.get(temp).to);
							}else {
								Semantics.sems(GrammarIndex+1, inputArr.get(inputIndex-1).to);
							}
								
							//}
							
							
						} else {
							System.out.println("符号栈出来的东西与产生式右边的东西不一样");
						}

						break;
					} else if ("acc".equals(map[row][i])) {
						System.out.println("success!");
						action = 0;
						break;
					}
				}
			}
		}
//		for(InputArr ia:inputArr) {
//			System.out.println(ia.string+"  "+ia.to);
//		}
	}

}
