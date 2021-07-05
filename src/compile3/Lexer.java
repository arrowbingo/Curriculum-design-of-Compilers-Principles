package compile3;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class LexicalError extends Error {

    public LexicalError(String string) {
    }
}


public class Lexer {
	
    static int i = 0;
    static String keywords[] = {"#include","int","float","int26",
                                "double","void","math.h",
                                "stdio.h","stdlib.h","string.h","malloc.h",
                                "return","for","do","while",
                                "if","else","break","printf","%d","%f"};
    //"stdio.h","math.h","stdlib.h"
    static List<Character> sourceCode=new ArrayList<Character>();
    static String filename="src/LexerOutput.txt";

    public static void getTokenLiteral(char c) {
        //System.out.println("<"+c + ","+"char"+">");
        i++;
    }

    public static void getTokenNumber(char c)  {
        //System.out.println("<"+c + ","+"number"+">");
        i++;
    }

    public static void getTokenOperation(char c)   {
        //System.out.println("<"+c + ","+(keywords.length+11)+">");
        i++;
    }
    public static void getTokenKeywords(String c,int i)   {
        //System.out.println("<"+c + ","+i+">");

    }
    public static void getTokenSymbol(char c) {
        //System.out.println("<"+c + ","+(keywords.length+12)+">");
        i++;
    }
    public static void getTokenIdentifier(String c) {

        //System.out.println("<"+c + ","+(keywords.length+1)+">");
    }
    public static void getTokenSelfadding(String c) {//++
        //System.out.println("<"+c + ","+(keywords.length+3)+">");
        i++;
    }
    public static void getTokenSelfdecrement(String c) {//--
        //System.out.println("<"+c + ","+38+">");
        i++;
    }
    public static void getTokenLogicand(String c) {//&&
        //System.out.println("<"+c + ","+(keywords.length+5)+">");
        i++;
    }
    public static void getTokenLogicor(String c) {
        //System.out.println("<"+c + ","+(keywords.length+6)+">");
        i++;
    }
    public static void getTokenLogicalnon(String c) {
        //System.out.println("<"+c + ","+(keywords.length+7)+">");
        i++;
    }
    public static void getTokenLogicequality(String c) {
        //System.out.println("<"+c + ","+(keywords.length+8)+">");
        i++;
    }
    public static void getTokenGreaterequal(String c) {
        //System.out.println("<"+c + ","+(keywords.length+9)+">");
        i++;
    }
    public static void getTokenLessequal(String c) {
        //System.out.println("<"+c + ","+(keywords.length+10)+">");
        i++;
    }
    public static void getTokenNumber1(String c) {
        //System.out.println("<"+c + ","+(keywords.length+2)+">");
        i++;
    }
    public static void getTokenkexuejishu(String c) {
        //System.out.println("<"+c + ","+(keywords.length+13)+">");
        i++;
    }
    public static void getArray(String c) {
        //System.out.println("<"+c + ","+(keywords.length+14)+">");
        i++;
    }
    public void lexicalAnalysis() throws IOException{
        BufferedWriter writer=new BufferedWriter(new FileWriter(filename));
        String file = "src/input.txt";
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                if (((char) tempchar) != '\r') {
                    sourceCode.add((char) tempchar);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (i < sourceCode.size()) {
            char c = sourceCode.get(i);
            String s = String.valueOf(c);
            String string="";
            boolean flag=true;
            boolean kw=false;
            boolean findoneid=false;
            while(true) {
                if (s.matches("[A-Za-z%#]")) {
                    string=string+s;
                    i++;
                    s=String.valueOf(sourceCode.get(i));
                    while(s.matches("[A-Za-z0-9.]")) {
                        flag=false;
                        string=string+s;
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                    }
                    if(flag) {
                        getTokenIdentifier(string);
                        writer.write(keywords.length+1+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    for (int i=0;i<keywords.length;i++) {
                        if(string.equals(keywords[i])) {
                            getTokenKeywords(string,(i+1));
                            writer.write(i+1+"`"+string);
                            writer.newLine();
                            kw=true;
                            break;
                        }
                    }
                    if(!kw) {
                        getTokenIdentifier(string);
                        writer.write(keywords.length+1+"`"+string);
                        writer.newLine();
                        break;
                    }

                }
                else {
                    break;
                }
            }
            flag=true;
            c = sourceCode.get(i);
            string="";//仅仅是点后面无数字
            boolean singlepoint = false;
            boolean findpoint = false;
            while(true) {
                if(s.matches("[0]")) {
                    string=string+s;
                    i++;
                    s=String.valueOf(sourceCode.get(i));
                    if(s.matches("[0-9]")){
                        writer.close();
                        throw new LexicalError("unexpected char ${c}");
                    }
                    if(s.matches("[.]")) {//是0.
                        singlepoint = true;
                        string=string+s;
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                        while(s.matches("[0-9]")){
                            if(s.matches("[0-9]")) {
                                singlepoint = false;
                                string = string + s;
                                i++;
                                s = String.valueOf(sourceCode.get(i));
                            }
                            else{
                                singlepoint = true;
                            }
                        }
                        if(singlepoint) {//小数点后留空直接写数字以外的抛出异常
                            writer.close();
                            throw new LexicalError("unexpected char ${c}");
                        }else {
                            getTokenNumber1(string);//输出0.xxxx
                            writer.write(keywords.length+2+"`"+string);
                            writer.newLine();
                            i--;
                            break;
                        }
                    }else {//仅仅是0
                        getTokenNumber1(string);
                        writer.write(keywords.length+2+"`"+string);
                        writer.newLine();
                        i--;
                        break;
                    }
                }
                if (s.matches("[1-9]")) {//1-9开头的int或double
                    string=string+s;
                    i++;
                    s=String.valueOf(sourceCode.get(i));
                    while(s.matches("[0-9.]")) {//int
                        flag=false;
                        string=string+s;
                        if(s.matches("[.]")) {
                            findpoint = true;
                            singlepoint = true;
                            break;
                        }
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                    }
                    if(findpoint) {
//						string=string+s;
                        i++;
                        s=String.valueOf(sourceCode.get(i));
                        while(s.matches("[0-9]")) {//double
                            if(s.matches("[0-9]")) {
                                singlepoint = false;
                                string = string + s;
                                i++;
                                s = String.valueOf(sourceCode.get(i));
                            }
                            else{
                                singlepoint = true;
                            }
                        }
                        if(singlepoint) {//小数点后留空直接写数字以外的抛出异常
                            throw new LexicalError("unexpected char ${c}");
                        }else if(singlepoint){
                            getTokenNumber1(string);
                            writer.write(keywords.length+2+"`"+string);
                            writer.newLine();
                            i--;
                            break;
                        }
                    }
                    if(flag) {
                        getTokenNumber1(string);//仅仅是1-9单数字
                        writer.write(keywords.length+2+"`"+string);
                        writer.newLine();
                        i--;
                        findoneid=true;
                        break;
                    }
                    else{
                        getTokenNumber1(string);//int多数字
                        writer.write(keywords.length+2+"`"+string);
                        writer.newLine();
                        i--;
                        break;
                    }
                }
                else {
                    break;
                }
            }
            c = sourceCode.get(i);
            string="";
            while(true) {
                if(c=='+') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='+') {
                        string=string+'+';
                        getTokenSelfadding(string);
                        writer.write(38+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            string="";
            while(true) {//--38
                if(c=='-') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='-') {
                        string=string+'-';
                        getTokenSelfdecrement(string);
                        writer.write(39+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //------------------------
            string="";
            while(true) {//&&34
                if(c=='&') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='&') {
                        string=string+'&';
                        getTokenLogicand(string);
                        writer.write(35+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            string="";
            while(true) {//||35
                if(c=='|') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='|') {
                        string=string+'|';
                        getTokenLogicor(string);
                        writer.write(36+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            string="";
            while(true) {//!=29
                if(c=='!') {
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        getTokenLogicalnon(string);
                        writer.write(30+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            string="";
            while(true) {
                if(c=='=') {//==24
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        getTokenLogicequality(string);
                        writer.write(25+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            string="";
            while(true) {
                if(c=='>') {//>=27
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        getTokenGreaterequal(string);
                        writer.write(28+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            string="";
            while(true) {
                if(c=='<') {//<=28
                    string=string+s;
                    i++;
                    if(sourceCode.get(i)=='=') {
                        string=string+'=';
                        getTokenLessequal(string);
                        writer.write(29+"`"+string);
                        writer.newLine();
                        findoneid=true;
                        break;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                else{
                    break;
                }
            }
            //------------------------case语句测试
            string = "";
            while(sourceCode.get(i)=='\'') {
                i++;
                string = string+'\'';
                writer.write('\'');
                writer.newLine();
                c = sourceCode.get(i);
                switch(c) {
                    case 'Z':{
                        string=string+c;
                        i++;
                        writer.write(c);
                        writer.newLine();
                        break;
                    }
                    default:
                        throw new LexicalError("unexpected char ${c}");
                }
                if(sourceCode.get(i)=='\'') {
                    string+='\'';
                    i++;
                    writer.write('\'');
                    writer.newLine();
                    //System.out.println("<"+string+"`"+"100>");
                    break;
                }
                else {
                    throw new LexicalError("unexpected char ${c}");
                }
            }
            string="";
            //------------------------------
            c = sourceCode.get(i);
            switch (c) {
                case '$':{
                    writer.write('$');
                    writer.newLine();
                    writer.close();
                    return;}
                case '\n':
                case '\t':
                case ' ': {
                    i++;
                    break;
                }
                case '=':{
                    getTokenOperation(c);
                    writer.write(24+"`"+c);
                    writer.newLine();
                    break;
                }
                case '*':
                {
                    getTokenOperation(c);
                    writer.write(33+"`"+c);
                    writer.newLine();
                    break;
                }
                case '+':
                {
                    getTokenOperation(c);
                    writer.write(31+"`"+c);
                    writer.newLine();
                    break;
                }
                case '-':
                {
                    getTokenOperation(c);
                    writer.write(32+"`"+c);
                    writer.newLine();
                    break;
                }
                case '/':
                {
                    getTokenOperation(c);
                    writer.write(34+"`"+c);
                    writer.newLine();
                    break;
                }
                case '^':
                case ':':{
                    break;
                }
                case '[':
                {
                    getTokenOperation(c);
                    writer.write(44+"`"+c);
                    writer.newLine();
                    break;
                }
                case ']':
                {
                    getTokenOperation(c);
                    writer.write(45+"`"+c);
                    writer.newLine();
                    break;
                }
                case '!':
                {
                    getTokenOperation(c);
                    writer.write(37+"`"+c);
                    writer.newLine();
                    break;
                }
                case '<':
                {
                    getTokenOperation(c);
                    writer.write(26+"`"+c);
                    writer.newLine();
                    break;
                }
                case '>':
                {
                    getTokenOperation(c);
                    writer.write(27+"`"+c);
                    writer.newLine();
                    break;
                }
                case '}':
                {
                    getTokenOperation(c);
                    writer.write(42+"`"+c);
                    writer.newLine();
                    break;
                }
                case '{':
                {
                    getTokenOperation(c);
                    writer.write(43+"`"+c);
                    writer.newLine();
                    break;
                }
                case '"':
                {
                    getTokenOperation(c);
                    writer.write(48+"`"+c);
                    writer.newLine();
                    break;
                }
                case '(':
                {
                    getTokenOperation(c);
                    writer.write(40+"`"+c);
                    writer.newLine();
                    break;
                }
                case ')':
                {
                    getTokenOperation(c);
                    writer.write(41+"`"+c);
                    writer.newLine();
                    break;
                }
                case ';':
                {
                    getTokenOperation(c);
                    writer.write(47+"`"+c);
                    writer.newLine();
                    break;
                }
                case ',':
                {
                    getTokenOperation(c);
                    writer.write(46+"`"+c);
                    writer.newLine();
                    break;
                }

                case '&': {
                    getTokenSymbol(c);
                    writer.write(c);
                    //writer.write("<"+c + ","+(keywords.length+12)+">");
                    writer.newLine();
                    break;
                }
                default:
                    if(!findoneid) {
                        throw new LexicalError("unexpected char ${c}");
                    }
                    else {
                        break;
                    }

            }
        }
        writer.close();
    }
    public static void main(String[] args)throws IOException{
        Lexer lexer=new Lexer();
        lexer.lexicalAnalysis();
    }

    public List<String> getWord(String filename) {
        List<String> words = new ArrayList();
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            line = in.readLine();
            words.add(line);
            while(line != null) {
                line = in.readLine();
                if(line == null) {
                    break;
                }
                words.add(line);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return words;
    }















}
