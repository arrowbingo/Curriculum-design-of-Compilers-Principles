package compile3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import compile3.lr1.InputArr;

public class Complie {
	private static String[][] string = new String[100][10]; // 输入串
	private static Integer index=1;
	private static ArrayList<String> output = new ArrayList<>();
	private static Integer[] suspend = new Integer[100];
	private static boolean existBoolean = false;
	private static Map<String, Boolean> map = new HashMap<>();

	public static double computeINT26(String s) {
		String w = "abcdefghijklnmopqrstuvwxyz";
		Integer[] suspend = new Integer[100];
		double Int26Str = 0;
		Map<Character, Integer> map = new HashMap<>();
		for (int i = 0; i < 26; i++) {
			map.put(w.charAt(i), i);
		}
		String aString = s;
		for (int i = 0; i < aString.length(); i++) {
			suspend[i]=map.get(aString.charAt(i));
		}
		for (int i = 0; i < aString.length(); i++) {
			int j =aString.length()-i-1;
			Int26Str += suspend[j]*Math.pow(26, i);
		}
		return Int26Str;

	}

	public static void main(String[] args) throws IOException{
		// 读取output.txt文档中的单词存入inputArr集合中
		try {
			String filePath = "src/SemanticOutput.txt";
			FileInputStream fin = new FileInputStream(filePath);
			InputStreamReader reader = new InputStreamReader(fin);
			BufferedReader buffReader = new BufferedReader(reader);
			StringBuffer sb = new StringBuffer();
			String strTmp = "";
			while ((strTmp = buffReader.readLine()) != null) {
				sb.append(strTmp+"\n");
			}
			String[] a = sb.toString().split("\n");
			int kytong =0;
			for (int i = 0; i < a.length; i++) {
				char temp = a[i].substring(0,1).charAt(0);

				if (!Character.isDigit(temp)) {
					String[] b = a[i].split(" ");
					map.put(b[0], true);
				}
				if(i<9&&Character.isDigit(temp)) {
					a[i] = a[i].substring(3,a[i].length());  //去掉头
					a[i] = a[i].substring(0,a[i].length()-1);  //去掉尾
					kytong++;
				}
				if(i>=9&&Character.isDigit(temp)) {
					a[i] = a[i].substring(4,a[i].length());  //去掉头
					a[i] = a[i].substring(0,a[i].length()-1);  //去掉尾
					kytong++;
				}
			}
			for (int i = 0; i < kytong; i++) {
				String[] c = a[i].split(",");
				for (int j = 0; j < 4; j++) {
					string[i][j]=c[j];
				}
			}
			for (int i = 0; i < suspend.length; i++) {
				suspend[i]=999;
			}
			for (int i = 0; i < kytong; i++) {
				for (int j = 0; j < 4; j++) {
					if (string[i][2].equals("/")&&string[i][0].equals("=")) {    //赋值语句
						suspend[i+1] = index;
						if (map.containsKey(string[i][1])) {
							output.add(index+" mov "+"ax"+","+computeINT26(string[i][1]));
						}else {
							output.add(index+" mov "+"ax"+","+string[i][1]);
						}
						index++;
						output.add(index+" mov "+string[i][3]+","+"ax");
						index++;
						break;
					}
					else if(string[i][0].equals("print")) {      //输出语句
						suspend[i+1] = index;
						output.add(index+" mov ax,"+string[i][3]);index++;
						output.add(index+" stc");index++;
						output.add(index+" call far ptr write");index++;
						break;
					}
					else if(string[i][0].equals("j")) {           //无条件跳转语句
						suspend[i+1] = index;
						output.add(index+" jmp /"+string[i][3]);
						index++;
						break;
					}
					else if(string[i][0].equals("j<")) {          //小于则跳转
						suspend[i+1] = index;
						output.add(index+" mov "+"ax"+","+string[i][1]);
						index++;
						output.add(index+" mov "+"bx"+","+string[i][2]);
						index++;
						output.add(index+" cmp "+"ax"+","+"bx");
						index++;
						output.add(index+" jl /"+string[i][3]);
						index++;
						break;
					}
					else if(string[i][0].equals("j>")) {          //大于则跳转
						suspend[i+1] = index;
						output.add(index+" mov "+"ax"+","+string[i][1]);
						index++;
						output.add(index+" mov "+"bx"+","+string[i][2]);
						index++;
						output.add(index+" cmp "+"ax"+","+"bx");
						index++;
						output.add(index+" jg /"+string[i][3]);
						index++;
						break;
					}
					else if(string[i][0].equals("j<=")) {          //小于等于则跳转
						suspend[i+1] = index;
						output.add(index+" mov "+"ax"+","+string[i][1]);
						index++;
						output.add(index+" mov "+"bx"+","+string[i][2]);
						index++;
						output.add(index+" cmp "+"ax"+","+"bx");
						index++;
						output.add(index+" jle /"+string[i][3]);
						index++;
						break;
					}
					else if(string[i][0].equals("j>=")) {          //大于等于则跳转
						suspend[i+1] = index;
						output.add(index+" mov "+"ax"+","+string[i][1]);
						index++;
					 	output.add(index+" mov "+"bx"+","+string[i][2]);
						index++;
						output.add(index+" cmp "+"ax"+","+"bx");
						index++;
						output.add(index+" jge /"+string[i][3]);
						index++;
						break;
					}
					else if(string[i][0].equals("*")) {  //乘法
						suspend[i+1] = index;
						if (map.containsKey(string[i][1])) {
							output.add(index+" mov "+"al"+","+computeINT26(string[i][1]));
						}else {
							output.add(index+" mov "+"al"+","+string[i][1]);
						}
						index++;
						if (map.containsKey(string[i][2])) {
							output.add(index+" mov "+"bl"+","+computeINT26(string[i][2]));
						}else {
							output.add(index+" mov "+"bl"+","+string[i][2]);
						}
						index++;
						output.add(index+" mul "+"bl");
						index++;
						output.add(index+" mov "+string[i][3]+","+"ax");
						index++;
						break;
					}
					else if(string[i][0].equals("/")) {  //除法
						suspend[i+1] = index;
						if (map.containsKey(string[i][1])) {
							output.add(index+" mov "+"ax"+","+computeINT26(string[i][1]));
						}else {
							output.add(index+" mov "+"ax"+","+string[i][1]);
						}
						index++;
						if (map.containsKey(string[i][2])) {
							output.add(index+" mov "+"bl"+","+computeINT26(string[i][2]));
						}else {
							output.add(index+" mov "+"bl"+","+string[i][2]);
						}
						index++;
						output.add(index+" div "+"bl");
						index++;
						output.add(index+" mov "+string[i][3]+","+"al");
						index++;
						break;
					}
					else if(string[i][0].equals("+")) { //加法
						suspend[i+1] = index;
						if (map.containsKey(string[i][1])) {
							output.add(index+" mov "+"ax"+","+computeINT26(string[i][1]));
						}else {
							output.add(index+" mov "+"ax"+","+string[i][1]);
						}

						index++;
						if (map.containsKey(string[i][2])) {
							output.add(index+" add "+"ax"+","+computeINT26(string[i][2]));
						}else {
							output.add(index+" add "+"ax"+","+string[i][2]);
						}
						index++;
						output.add(index+" mov "+string[i][3]+","+"ax");
						index++;
						break;
					}
					else if(string[i][0].equals("-")) {  //-
						suspend[i+1] = index;
						if (map.containsKey(string[i][1])) {
							output.add(index+" mov "+"ax"+","+computeINT26(string[i][1]));
						}else {
							output.add(index+" mov "+"ax"+","+string[i][1]);
						}
						index++;
						if (map.containsKey(string[i][2])) {
							output.add(index+" sub "+"ax"+","+computeINT26(string[i][2]));
						}else {
							output.add(index+" sub "+"ax"+","+string[i][2]);
						}
						index++;
						output.add(index+" mov "+string[i][3]+","+"ax");
						index++;
						break;
					}
				}
			}
			if (existBoolean) {
//						output.add(index+" disp macro  integer");index++;
//						output.add(index+" mov ax,integer");index++;
//						output.add(index+" stc");index++;
//						output.add(index+" call far ptr write");index++;
//						output.add(index+" endm");
			}
			suspend[kytong+1]= index;
//					for (int j = 0; j < suspend.length; j++) {
//						System.out.println(suspend[j]);
//					}
			for (int j = 0; j < output.size(); j++) {
				if (output.get(j).contains("/")) {
					int temp = output.get(j).indexOf("/")+1;
//							output.get(j)[temp] =
//							System.out.println(output.get(j).substring(temp,output.get(j).length()));
					int w = Integer.parseInt(output.get(j).substring(temp,output.get(j).length()));
//							System.out.println(suspend[w]);
					System.out.println(output.get(j).replaceFirst(output.get(j).substring(temp-1,output.get(j).length()), Integer.toString(suspend[w])));
				}
				else {
					System.out.println(output.get(j));
				}

			}


			buffReader.close();
		} catch (IOException e1) {
		}


	}
}
