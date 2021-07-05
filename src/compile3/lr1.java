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
	private static ArrayList<String> ids=new ArrayList<>();;//�����ı�ʶ��
	private static ArrayList<String> nums=new ArrayList<>();;//����������
	private static int temp;
	private static boolean isFirst=true;
	private static Integer index = 0; // ����
	private static Integer row = 1; // ִ�е�����������
	private static Integer column = 0; // ִ�е�����������
	private static Integer inputIndex = 0; // ִ�е�����������
	private static Integer GrammarIndex = 0; // ��Լ�Ĳ���ʽ��index
	private static String NowGrammar = ""; // ��ǰ����ʽ��ʽ��
	private static String NowGrammarLeft = ""; // ��ǰ����ʽ��ߵ�ʽ��
	private static String NowGrammarRight = ""; // ��ǰ����ʽ�ұߵ�ʽ��
	private static Stack<Integer> stateStack = new Stack<>(); // ״̬ջ
	private static Stack<String> markStack = new Stack<>(); // ����ջ
	private static ArrayList<InputArr> inputArr = new ArrayList<>(); // ���봮
	private static Integer action = 1;// 1--�ƽ� 2--��Լ 0--����
	private static ArrayList<String> Grammar = new ArrayList<>(); // ����ʽ
	private static ArrayList<String> GrammarLeft = new ArrayList<>(); // ����ʽ��ߵ�ʽ�Ӽ���
	private static ArrayList<String> GrammarRight = new ArrayList<>(); // ����ʽ�ұߵ�ʽ�Ӽ���
	private static String[][] map = new String[1000][1000]; // goto��ӳ��

	/**
	 * ���봮���͵��ࣨΪ��ʵ��id��digit�ܶ�Ӧ�����ֵ������һ�������Ƿ��ţ��ڶ������������Ӧ�ľ���ֵ
	 * @author ��ĺ�ջ�
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

		// ��ȡoutput.txt�ĵ��еĵ��ʴ���inputArr������
		try {
			String filePath = "src/LexerOutput.txt";
			FileInputStream fin = new FileInputStream(filePath);
			InputStreamReader reader = new InputStreamReader(fin);
			BufferedReader buffReader = new BufferedReader(reader);
			String strTmp = "";
			while ((strTmp = buffReader.readLine()) != null) {
				String[] string = strTmp.split("`");//��"'"�ָ��ַ���
				String left=string[strTmp.split("`").length - 2];//����"'"�ָ��ַ����ĵ����ڶ����ַ�������left
				String right=string[strTmp.split("`").length - 1];//����"'"�ָ��ַ����ĵ�����һ���ַ�������right
				InputArr arr;
				if(left.equals("22")) {
					arr=new InputArr("id",right);
					inputArr.add(arr);
				}else if(left.equals("23")) {
					arr=new InputArr("digit",right);
					inputArr.add(arr);
				}else {
					arr=new InputArr(right,"");
					inputArr.add(arr);//��right�ַ�������inputArr������
				}
				
			}
			inputArr.add(new InputArr("$",""));
			buffReader.close();
		} catch (IOException e1) {
		}
		
		// ��ȡgrammar.xls�еĲ���ʽ����Grammar�����У�������ʽ��ߵĶ�������GrammarLeft��������ʽ�ұߵĶ�������GrammarRight
		FileInputStream fs;
		try {
			fs = new FileInputStream("src/grammar.xls");
			POIFSFileSystem ps = new POIFSFileSystem(fs); // ʹ��POI�ṩ�ķ����õ�excel����Ϣ
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0); // ��ȡ����������Ϊһ��excel�����ж��������
			int rows = sheet.getPhysicalNumberOfRows();
			// ��ȡ����ʽ
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
			String[] strs = Grammar.get(i).split(" -> ");//��"->"�ָ��ַ���
			int len = strs.length;
			GrammarLeft.add(strs[0]);//����"->"�ָ��ַ����ĵ�һ���ַ�������GrammarLeft��
			GrammarRight.add(strs[Grammar.get(i).split(" -> ").length - 1]);//����"->"�ָ��ַ��������һ�����ַ�������GrammarRight��
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

		// ��ȡgoto.xls����ӳ�䵽��ά����map��
		try {
			fs = new FileInputStream("src/lr1.xls");
			POIFSFileSystem ps = new POIFSFileSystem(fs); // ʹ��POI�ṩ�ķ����õ�excel����Ϣ
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0); // ��ȡ����������Ϊһ��excel�����ж��������
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
						value = cell.getStringCellValue(); // ��Ϊexcel��Ե��++ --���������ǰ�пո���Ϊ������Щֵ�ѿո�ȥ��
						if (value.startsWith(" ")) {
							value = value.substring(1, value.length());
						}
						break;
					default:
						break;
					}
					map[r - 1][c - 1] = value;//��lr1.xls���ݴ��뵽��ά����map��
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
					if ("S".equals(map[row][i].subSequence(0, 1))) { // �ƽ�
						index++; // ����+1
						row = Integer.parseInt(map[row][i].substring(1, map[row][i].length())) + 1; // ��Ϊlr1.xls���Ǵ�-1��ʼ
						stateStack.push(row); // �ı�״̬ջ
						markStack.push(inputArr.get(inputIndex).string); // �ı����ջ
						inputIndex++;
						System.out.println("��" + index + "�����ƽ�����" +"\t"+ "״̬ջΪ��" + stateStack +"\t"+ "����ջΪ��" + markStack);
						System.out.println(inputArr.get(inputIndex).string);
						break;
					} else if ("r".equals(map[row][i].subSequence(0, 1))) {
						int count = 0;
						GrammarIndex = Integer.parseInt(map[row][i].substring(1)) - 1; // ��Ϊgrammar.xls���Ǵ�1��ʼ
						NowGrammar = Grammar.get(GrammarIndex);
						
						NowGrammarLeft = GrammarLeft.get(GrammarIndex);
						NowGrammarRight = GrammarRight.get(GrammarIndex);
						String[] string = NowGrammarRight.split(" "); // �Կո�ֿ������ɸ��ַ�������
						int NowGrammarRightLen = NowGrammarRight.split(" ").length - 1;
						for (int j = NowGrammarRightLen; j >= 0; j--) {
							if (string[j].equals("��")) { // ����ʽ�ұ�Ϊ�գ�����ջ����Ҫ��ջ
								count++;
							} else if (string[j].equals(markStack.pop())) {
								count++;
							}
						}
						if (string.length == count) { // �жϲ���ʽ�ұߵ�ʽ���Ƿ���ڷ���ջ�г�����ʽ��
							markStack.push(NowGrammarLeft);
							if (!string[0].equals("��")) { // ����ʽ�ұ�Ϊ�գ�״̬ջ����Ҫ��ջ
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
							System.out.println("��" + index + "���ǹ�Լ����" + "\t"+"״̬ջΪ��" + stateStack + "\t"+"����ջΪ��" + markStack
									+ "\t"+"����ʽΪ��" + NowGrammar);
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
							System.out.println("����ջ�����Ķ��������ʽ�ұߵĶ�����һ��");
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
