package compile3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public  class Semantics {
	//	private static int UTCTemp;
//	private static int UFCTemp;
	Upro Utemp;
	private static boolean JVarible=true;
	private static boolean isPrint=false;
	private static Stack<String> valueStack=new Stack<>();
	private static String tempVar;
	private static String tempVar1="";
	private static String priviousTempVar;
	private static Stack<Integer> NXQForWhile=new Stack<>();
	private static Upro UTemp=new Upro();
	private static StatementFor statefor=new StatementFor();
	private static boolean isFirst=true;
	private static boolean symbolStart=false;//���Ϊtrue��������ifǶ��
	private static Stack<List> stackForIf=new Stack<>();
	private static int aFC;//Ϊ��ȥ��һ����Ԫʽ�õ��ĸ�����������¼Ҫȥ������Ԫʽ��ַ
	private static Stack<Integer> listForIf=new Stack<>();
	private static Stack<Integer> listForIfTemp=new Stack<>();
	//private static List<Integer> listForIfTemp=new ArrayList<>();
	private static Xpro X=new Xpro();
	private static Stack<Upro> stacku=new Stack<>();
	//private static Stack<Upro> stackuTemp=new Stack<>();
	//private static Upro U=new Upro();
	private static boolean ifIndex=false; //�����if�����
	private static Stack<Boolean> stackif=new Stack<>();
	private static Stack<Boolean> stackifTemp=new Stack<>();
	private static boolean existFor=false;
	private static boolean elseIndex=false; //�����else�����
	private static Stack<Boolean> stackelse=new Stack<>();
	private static Integer addrStart;//һ���������Ԫʽ��ʼλ��
	private static Integer addrStartTemp;//һ���������Ԫʽ��ʼλ��
	private static Stack<Integer> stackAddrStart=new Stack<>();
	private static Stack<Integer> stackAddrStartTemp=new Stack<>();
	private static Integer addrEnd;//һ���������Ԫʽ����λ��
	//	private static Stack<Integer> stackAddrEnd=new Stack<>();
	private static String VOP;//�Ƚ������
	private static Integer NXQ=1;//��һ����Ԫʽ��ַ����
	private static Integer NXQTemp=1;//��һ����Ԫʽ��ַ����
	private static Iden temp;//רΪ��87����ʽ��Ƶ���ʱ�洢����
	private static int indexOpr=0;//��¼���������ʽ�����˶��ٴ�����
	private static List<String> addrs=new ArrayList<>();//��Ԫʽ��
	private static List<String> addrsTemp=new ArrayList<>();//��Ԫʽ�ݴ��
	private static List<Iden> ids=new ArrayList<>();//������
	private static Ipro I=new Ipro();
	private static Fpro F=new Fpro();
	private static Tpro T=new Tpro();
	private static Epro E=new Epro("","");
	private static Epro anotherE=new Epro("","");
	//private static Cal cal=new Cal();
	//private static String var="";
	private static Stack<String> stackt=new Stack<>();//Ϊ�������ʽ+-ʹ��
	private static Stack<String> stackv=new Stack<>();//Ϊ�������ʽ+-ʹ��\

	private static Stack<String> stackt1=new Stack<>();//Ϊ�������ʽ*/ʹ��
	private static Stack<String> stackv1=new Stack<>();//Ϊ�������ʽ*/ʹ��
	/**
	 * רΪ�������ʽ��Ƶ���
	 * @author ��ĺ�ջ�
	 *
	 */
//	static class Cal{
//		//String type;//�洢�������ʽ������������ߵ�����
//		int index=0;//��¼���������ʽ�����˶��ٴ�����
//		int i=-1;//ָ����������������һ��ָ��ĩβ
//		List<Tpro> digits=new ArrayList<>();//�洢�ȴ����������
//		private  void init() {
//			i=-1;
//			digits.clear();
//		}
//	}
	/**
	 * Ϊforѭ����Ƶ���
	 * @author ��ĺ�ջ�
	 *
	 */
	static class StatementFor{
		boolean ifindex=false;
		boolean[] sym=new boolean[2];
		String var;//�����
		String varValue;//�����ֵ
		String varTemp;
		int addr;//��¼for������ʼ��ַ
		//int addr1;//��¼D++/D--����Ԫʽ��ַ
		//String var1;
		boolean[] flag=new boolean[3];//���
		StatementFor(){
			var=varValue=varTemp="";
			sym[0]=sym[1]=flag[0]=flag[1]=flag[2]=false;
			addr=0;
		}
		void init() {
			flag[0]=flag[1]=flag[2]=false;
		}
	}
	/**
	 * �Զ����ʶ������
	 * @author ��ĺ�ջ�
	 *
	 */
	static class Iden{
		String name;//��ʶ������
		String type;//��ʶ����������

		boolean flag1;//��Ǹñ�ʶ���Ƿ����ڵȴ�������Ĭ��Ϊtrue,רΪ����������
		boolean flag2;//��Ǹñ�ʶ���Ƿ����ڵȴ�������Ĭ��Ϊfalse��רΪ������ֵ���
		boolean flag3;//��Ǹñ�ʶ���ǽ��ܱ߶���߸�ֵ����Ĭ��Ϊfalse��רΪ�������ͬʱ��ֵ���
		public Iden(String name, String type) {
			super();
			this.name = name;
			this.type = type;

			this.flag1=true;
			this.flag2=false;
			this.flag3=false;
		}
		public Iden() {
			super();
		}
		@Override
		public String toString() {
			return "Iden [name=" + name + ", type=" + type + ", flag1=" + flag1 + ", flag2=" + flag2 + ", flag3="
					+ flag3 + "]";
		}





	}
	static class Upro{
		Integer TC=0;
		Integer FC=0;
	}
	static class Xpro{
		Integer TC=0;
		Integer FC=0;
	}
	static class Ipro{
		String type;

		public Ipro(String type) {
			super();
			this.type = type;
		}

		public Ipro() {
			super();
		}

	}
	static class Fpro{
		String type;
		String value;
		public Fpro(String type, String value) {
			super();
			this.type = type;
			this.value = value;
		}
		public Fpro() {
			super();
		}


	}
	static class Tpro{
		String type;
		String value;
		public Tpro(String type, String value) {
			super();
			this.type = type;
			this.value = value;
		}
		public Tpro() {
			super();
		}

	}
	static class Epro{
		String type;
		String value;
		public Epro(String type, String value) {
			super();
			this.type = type;
			this.value = value;
		}
		public Epro() {
			super();
		}

	}

	public static void print() {
		for(String addr:addrs) {
			System.out.println(addr);
		}
	}

	/**
	 * �����Ԫʽ��op��reg1��reg2��result�����ļ�
	 * @param bw
	 * @param op
	 * @param reg1
	 * @param reg2
	 * @param result
	 * @throws IOException
	 */
	private static void gen(String op,String reg1,String reg2,String result,int index) throws IOException {
		//System.out.println(index+" "+"("+op+","+reg1+","+reg2+","+result+")");

		addrs.add(index+" "+"("+op+","+reg1+","+reg2+","+result+")");
		//bw.write(index+" "+"("+op+","+reg1+","+reg2+","+result+")");
//		System.out.println(addrs);
		NXQ++;
	}
	/**
	 * �ж��Ƿ�����ʽת��
	 * @param left =�����
	 * @param right =���ұ�
	 */
	private static boolean transform(String left,String right) {
		if(left .equals("double")&&(right.equals("int")||right.equals("int26")||right.equals("float or double"))) {
			return true;
		}else if(left .equals("float")&&right.equals("int")||left .equals("float")&&right.equals("int26")) {
			return true;
		}else if(left .equals("int")&&right.equals("int26")||left .equals("int26")&&right.equals("int")) {
			return true;
		}else {
			return false;

		}
	}
	private static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	/**
	 * ��������ids����µ�id��ǰ����id.name��δ���ڣ�������ڣ�����Ϊ��������id�ȴ�������
	 * @param id
	 */
	private static void addTo(Iden id) {

		for(Iden i:ids) {

			if(id.name.equals(i.name)&&i.flag1==false&&i.flag2==false) {
				i.flag2=true;
				temp=i;
				return;
			}else if(id.name.equals(i.name)) {
				temp=i;
				return;
			}

//			else if(id.name.equals(i.name)) {
//				//System.out.println(i.name+"   "+id.name+id.flag1);
//				System.out.println("������������ͬ��");
//				//System.out.println(ids);
//				System.exit(-1);
//			}
		}
		ids.add(id);
		//System.out.println(ids);
	}
	/**
	 * ����Ƿ���ͬ������
	 */
//	private static void check() {
//		System.out.println(ids.size());
//		System.out.println(ids);
//		List<String> idNames=new ArrayList<>();//�������ֱ�
//		for(Iden id:ids) {
//			idNames.add(id.name);
//
//		}
//		for(String name:idNames) {
//			if(idNames.indexOf(name)!=idNames.lastIndexOf(name)) {
//				System.out.println("������������ͬ��:"+name);
//				System.exit(-1);
//			}
//		}
//
//	}
	private static String highType(String type1,String type2) {

		if(type1.equals("int")&&type2.equals("int")) {
			return "int";
		}
		return "float or double";
	}
	/**
	 * ͨ������ʽ��ŵ��øò���ʽ���������
	 * @param index ��Լ�Ĳ���ʽ��ţ�
	 * @param to id��digit�����ֵ�������ǿմ�
	 * @throws IOException
	 */
	public static void sems(int index,String to) throws IOException {
//		System.out.println("�������"+to);
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("src/SemanticOutput.txt")));
		switch (index) {
			case 19:
				JVarible=true;//
				boolean newFlag=false;
				if(!to.equals("")&&!to.equals("main")) {
					for(Iden id:ids) {
						if(id.type.equals("int26")&&id.flag1==false&&id.flag2==true&&id.flag3==false) {
							Iden aId=new Iden(to,"int26");
							aId.flag1=aId.flag2=aId.flag3=false;
							ids.add(aId);
							for(int i=0;i<ids.size()-1;i++) {
								if(ids.get(i).name.equals(aId.name)) {
									ids.remove(ids.size()-1);
									break;
								}
							}

							temp=aId;
							newFlag=true;
							break;
						}
					}
					if(newFlag) {
						break;
					}
					if(isPrint) {
						gen("print", "/", "/", to, NXQ);
						isPrint=false;
					}
					priviousTempVar=tempVar;
					tempVar=to;
					addTo(new Iden(to,""));
					statefor.var=to;
					if(statefor.flag[1]==false) {
						statefor.flag[0]=true;
					}else {
						statefor.flag[1]=true;
					}

					//System.out.println("----------"+statefor.flag[0]+statefor.flag[1]+statefor.flag[2]);
					//System.out.println("----------"+statefor.varValue);
//					System.out.println("----------"+ids);
					if(statefor.sym[0]&&!statefor.sym[1]) {
						//ids.clear();
						statefor.ifindex=true;
						ids.remove(ids.size()-1);
						statefor.sym[1]=true;
						//String result="T"+Integer.valueOf(++indexOpr).toString();
						statefor.varTemp=to;
						//gen("",statefor.var , "1", result, NXQ);
						//statefor.addr1=NXQ-1;
					}

				}
				break;
			case 20:
				JVarible=true;//
				ids.remove(ids.size()-1);
				//System.out.println("----------"+Integer.valueOf(E.value));
				if(isInteger(E.value)) {
					for(int i=0;i<Integer.valueOf(E.value);i++) {
						String var=tempVar+"["+i+"]";
						addTo(new Iden(var,""));
					}
				}else {
					//ids.remove(ids.size()-1);
					tempVar1=priviousTempVar;
					tempVar1+="["+E.value+"]";
					addTo(new Iden(tempVar1,""));
					ids.get(ids.size()-1).flag1=false;
					ids.get(ids.size()-1).type="int";
					//sems(87, tempVar1);

				}


				break;
			case 24:
				JVarible=true;//
				I.type="int";
				break;
			case 25:
				JVarible=true;//
				I.type="float";
				break;
			case 26:
				JVarible=true;//
				I.type="double";
				break;
			case 27:
				JVarible=true;//
				I.type="int26";
				break;
			case 28:
//				System.out.println("-----zaizheli:"+stacku+"if������"+stackif.size());
//				symbolStart=false;
//				addrsTemp.clear();
//				stackifTemp.clear();
//				listForIfTemp.clear();
//				stackAddrStartTemp.clear();
//
//				addrsTemp.addAll(0, addrs);
//				stackifTemp.addAll(0, stackif);
//				listForIfTemp.addAll(0, listForIf);
//				if(stacku.size()!=0) {
//					UTemp.TC=stacku.get(stacku.size()-1).TC;
//					UTemp.FC=stacku.get(stacku.size()-1).FC;
//				}
//
//				stackAddrStartTemp.addAll(0, stackAddrStart);
//				NXQTemp=NXQ;

//				if(statefor.ifindex) {
//					ifIndex=true;
//				}
//				System.out.println("������1��"+ifIndex+stackif.isEmpty()+"if������"+stackif.size());
				if(JVarible==false) {
					break;
				}
				if(!stackif.isEmpty()) {
					ifIndex=stackif.pop();
				}

				if(ifIndex&&!stackAddrStart.isEmpty()&&!stacku.isEmpty()) {

//					System.out.println("������2��"+ifIndex+stackif.isEmpty());
					if(ifIndex) {
						// gen("j","/","/",Integer.valueOf(statefor.addr).toString(),NXQ);

						gen("j", "/", "/", Integer.valueOf(NXQ+1).toString(), NXQ);

						listForIf.push(NXQ-2);
						addrEnd=NXQ;
						Upro U=stacku.pop();
						UTemp=new Upro();
						UTemp.TC=U.TC;
						UTemp.FC=U.FC;
//						System.out.println("������3��"+U.TC+"  "+U.FC);
						int addr=U.TC;
						addrStart=stackAddrStart.pop();
						addrStartTemp=addrStart;
						U.TC=addrStart;
						U.FC=addrEnd;
						String str=addrs.get(addr-1);
						str=str.replaceAll("[,]\\d+[)]", ","+U.TC.toString()+")");//�޸�����ڵ�ַ

						addrs.set(addr-1, str);
						str=addrs.get(addr);
						str=str.replaceAll("[,]\\d+[)]", ","+U.FC.toString()+")");//�޸ļٳ��ڵ�ַ
						addrs.set(addr, str);
//						aFC=addr;
						ifIndex=false;
						JVarible=false;
						//System.out.println("listForIf:"+listForIf);


						listForIfTemp.push(listForIf.pop());
//						if(elem==addrs.size()) {
//							elem=NXQ-2;
//						}
//						String str1=addrs.get(elem);
//						str1=str1.replaceAll("[,]\\d+[)]", ","+NXQ+")");
//						addrs.set(elem, str1);
					}
				}
				break;
			case 36:
				JVarible=true;//
				for(Iden id:ids) {
					if(id.flag1==true&&id.flag2==false) {
						if(id.type.equals("")) {
							id.type=I.type;
						}else if(I.type.equals(id.type)||id.type.contains(I.type)||transform(I.type,id.type)) {
							id.type=I.type;
						}else {
							System.out.println("�������Ͳ���:"+id.name);
							System.exit(-1);
						}
						id.flag1=false;
//						check();
					}
				}
				break;
			case 38:
				JVarible=true;//
				statefor.init();
				break;
			case 39:
				JVarible=true;//
				statefor.init();
				break;
			case 41:
				JVarible=true;//
//				if(cal.digits.size()>0) {
//					E.type=highType(E.type, cal.type);
//					//E.value=cal.digits.get(0);
//				}
				statefor.init();
				for(Iden id:ids) {
					if(id.flag2==true&&id.flag1==false) {//ֻ�Ǹ�ֵ
//						System.out.println(E.type+"  "+id.type);
//						if(E.type.equals(id.type)||E.type.contains(id.type)||transform(id.type,E.type)) {

						id.flag2=false;
						gen("=", E.value, "/", id.name,NXQ);
						indexOpr=0;
						stackt.clear();
						stackv.clear();
						stackt1.clear();
						stackv1.clear();
						//E.value=E.type="";

//							id.value=E.value;
//							id.flag2=false;
//							gen(bw, "=", id.value, "/", id.name);
//							E.value=E.type="";
//						}else {
//							System.out.println("�������Ͳ���:"+id.name);
//							System.exit(-1);
//						}
						//id.flag2=false;
					}else if(id.flag2==false&&id.flag1==true&&id.flag3==false) {//�߶���߸�ֵ
						id.type=E.type;
						//id.value=E.value;
						id.flag3=true;
						gen("=", E.value, "/", id.name,NXQ);
						indexOpr=0;
						stackt.clear();
						stackv.clear();
						stackt1.clear();
						stackv1.clear();
						//E.value=E.type="";
						//System.out.println(ids);
					}

				}

				break;
			case 42:
//				System.out.println(valueStack);
				JVarible=true;//
				statefor.init();
				for(Iden id:ids) {
					if(id.flag2==false&&id.flag1==true&&id.flag3==false) {//�߶���߸�ֵ
						id.flag3=true;
						E.value=valueStack.pop();
						gen("=", E.value, "/", id.name,NXQ);
						indexOpr=0;
						stackt.clear();
						stackv.clear();
						stackt1.clear();
						stackv1.clear();
						//E.value=E.type="";
						//System.out.println(ids);
					}
				}
				break;
			case 43:
				JVarible=true;//
				String resultTemp="T"+Integer.valueOf(++indexOpr).toString();
				gen("+", tempVar, "1", resultTemp, NXQ);
				gen("=", resultTemp, "/", tempVar, NXQ);
				break;
			case 49:
				JVarible=true;//
				valueStack.push(to);

				break;
			case 50:
				JVarible=true;//
				valueStack.push(to);

				break;

			case 52:
				JVarible=true;//
				isPrint=true;

				break;

			case 53:
				JVarible=true;//
				isPrint=true;
				break;
			case 57:
				JVarible=true;//
				System.out.println("NXQForWhile--------:"+NXQForWhile);
				addrs.remove(NXQ-2);
				NXQ--;
				int addrUpd=NXQForWhile.pop();
				gen("j", "/", "/", Integer.valueOf(addrUpd).toString(), NXQ);
				addrs.set(addrUpd,++addrUpd+" (j,/,/,"+NXQ+")");
				break;
			case 58:
				JVarible=true;//
				addrs.remove(NXQ-2);
				NXQ--;
				String result="T"+Integer.valueOf(++indexOpr).toString();
				gen("+",statefor.varTemp , "1", result, NXQ);
				gen("=", result, "/", statefor.varTemp, NXQ);
				gen("j","/","/",Integer.valueOf(statefor.addr).toString(),NXQ);

				String string=addrs.get(statefor.addr);
				string=string.replaceAll("[,]\\d+[)]", ","+NXQ.toString()+")");
				addrs.set(statefor.addr, string);
				statefor=new StatementFor();
				break;
			case 59:
				JVarible=true;//
				addrs.remove(NXQ-2);
				NXQ--;
				String resultx="T"+Integer.valueOf(++indexOpr).toString();
				gen("-",statefor.varTemp , "1", resultx, NXQ);
				gen("=", resultx, "/", statefor.varTemp, NXQ);
				gen("j","/","/",Integer.valueOf(statefor.addr).toString(),NXQ);
				String string1=addrs.get(statefor.addr);
				string1=string1.replaceAll("[,]\\d+[)]", ","+NXQ.toString()+")");
				addrs.set(statefor.addr, string1);
				statefor=new StatementFor();
				break;
			case 60:
				JVarible=true;//
				NXQForWhile.pop();

				break;
			case 61:
				JVarible=true;//


				int elem=listForIfTemp.pop();
				if(elem==addrs.size()) {
					elem=NXQ-2;
				}
				String str1=addrs.get(elem);
				str1=str1.replaceAll("[,]\\d+[)]", ","+NXQ+")");
				addrs.set(elem, str1);

//				if(!stackelse.isEmpty()) {
//					elseIndex=stackelse.pop();
//				}
//				if(elseIndex) {
////					System.out.println(stackForIf.size()+"-------"+stackForIf+"----"+listForIf+"----"+addrs);
//
////					int elem=listForIf.pop();
////					if(elem==addrs.size()) {
////						elem=NXQ-2;
////					}
////					String str=addrs.get(elem);
////					str=str.replaceAll("[,]\\d+[)]", ","+NXQ+")");
////					addrs.set(elem, str);
////
//
////					listForIf.clear();
////					if(!stackForIf.isEmpty()) {
////						listForIf=stackForIf.pop();
////					}
//					elseIndex=false;
//					ifIndex=true;
//				}

				break;
			case 62:
				JVarible=true;//                                                                                                                                                           addrs.remove(NXQ-2);
				listForIfTemp.pop();
//				stackelse.push(true);
				stackAddrStart.push(addrStartTemp);
				stacku.push(UTemp);
				stackif.push(true);
//				String str=addrs.get(aFC);
//				str=str.replaceAll("[,]\\d+[)]", ","+NXQ+")");
//				addrs.set(aFC, str);
				//System.out.println(listForIf);

//				int elem=listForIf.pop();
//				if(elem==addrs.size()) {
//					elem=NXQ-2;
//				}
//				String str1=addrs.get(elem);
//				str1=str1.replaceAll("[,]\\d+[)]", ","+NXQ+")");
//				addrs.set(elem, str1);
//

//				listForIf.clear();
//				if(!stackForIf.isEmpty()) {
//					listForIf=stackForIf.pop();
//				}
				//ifIndex=true;
				break;
			case 63:
				JVarible=true;//
				if(stackif.isEmpty()&&listForIfTemp.get(listForIfTemp.size()-1)!=NXQ-2) {
					System.out.println("����");
					break;
				}
				System.out.println("-----------------"+listForIfTemp);
				listForIfTemp.pop();
//				stackelse.push(true);
				stackAddrStart.push(addrStartTemp);
				stacku.push(UTemp);
				stackif.push(true);

//				addrs.clear();
//				stackif.clear();
//				listForIf.clear();
//				stackAddrStart.clear();
////				System.out.println("-----zaizheli--123:"+stacku+"IF������"+stackifTemp.size());
//				addrs.addAll(0, addrsTemp);
//				stackif.addAll(0, stackifTemp);
//				listForIf.addAll(0, listForIfTemp);
//				Upro UTemp1=new Upro();
//				UTemp1.TC=UTemp.TC;
//				UTemp1.FC=UTemp.FC;
//				stacku.push(UTemp1);
//				stackAddrStart.addAll(0, stackAddrStartTemp);
//				NXQ=NXQTemp;
//				System.out.println("-----zaizheli--456:"+stacku+"IF������"+stackif.size());
				//elseIndex=true;
				ifIndex=true;
				break;
			case 65:
				JVarible=true;//
//				if(isFirst) {
//					ifIndex=true;
//					isFirst=false;
//				}
				//addrStart=NXQ;
				stackAddrStart.push(NXQ.intValue());
				ifIndex=true;
				stackif.push(ifIndex);
//				System.out.println("if������"+stackif.size());
				//


//				if(symbolStart==true) {
//
//					List list=new ArrayList<>();
//					list=listForIf;
//					stackForIf.push(list);
//					listForIf.clear();
//				}
//
//				symbolStart=true;

				break;
			case 67:
				JVarible=true;//

				Upro U=new Upro();
				U.TC=X.TC;
				U.FC=X.FC;
				stacku.push(U);

				if(statefor.sym[0]&&!statefor.sym[1]) {

					sems(65, "");

				}

				break;
			case 71:
				JVarible=true;//
				if(statefor.flag[0]&&statefor.flag[1]&&statefor.flag[2]&&!statefor.sym[0]&&existFor) {
					statefor.sym[0]=true;

					gen("=", statefor.varValue, "/", statefor.var, NXQ);
					statefor.addr=NXQ;
				}
				X.TC=NXQ;
				X.FC=NXQ+1;
				NXQForWhile.push(NXQ);
				System.out.println("----"+E.value);
				gen("j"+VOP,anotherE.value,E.value,"0",NXQ);
				gen("j","/","/","0",NXQ);
				break;
			case 73:
				System.out.println("----"+E.value);
				JVarible=true;//
				anotherE.value=E.value;
				VOP=">=";
				break;
			case 74:
				System.out.println("----"+E.value);
				JVarible=true;//
				anotherE.value=E.value;
				VOP="<=";
				break;
			case 75:
				System.out.println("----"+E.value);
				JVarible=true;//
				anotherE.value=E.value;
				VOP="<";
				break;
			case 76:
				System.out.println("----"+E.value);
				JVarible=true;//
				anotherE.value=E.value;
				VOP="==";
				break;
			case 77:
				System.out.println("----"+E.value);
				JVarible=true;//
				anotherE.value=E.value;
				VOP="!=";
				break;
			case 78:
				System.out.println("----"+E.value);
				JVarible=true;//
				anotherE.value=E.value;
				VOP=">";
				break;

			case 79:
				JVarible=true;//
//				cal.index++;
//				String result1="T"+Integer.valueOf(cal.index).toString();
//				T=cal.digits.get(cal.i);
//				cal.i--;
//				gen(bw, "+", T.value, E.value, result1);
//				E.type=highType(E.type, T.type);
//				E.value=result1;
				stackt.pop();
				T.type=stackt.pop();
				stackv.pop();
				T.value=stackv.pop();
				E.type=highType(E.type,T.type);
				indexOpr++;
				String result1="T"+Integer.valueOf(indexOpr).toString();
				gen("+", T.value, E.value, result1,NXQ);

				E.value=result1;
				if(!(statefor.flag[0]&&statefor.flag[1]&&statefor.flag[2])) {
					statefor.varValue=E.value;
				}

				if(statefor.flag[0]&&statefor.flag[1]) {
					statefor.flag[2]=true;
				}

				stackt.push(E.type);
				stackv.push(E.value);

				break;
			case 80:
				JVarible=true;//
				stackt.pop();
				T.type=stackt.pop();
				stackv.pop();
				T.value=stackv.pop();
				E.type=highType(E.type,T.type);
				indexOpr++;
				String result2="T"+Integer.valueOf(indexOpr).toString();
				gen("-", T.value, E.value, result2,NXQ);
				E.value=result2;
				if(!(statefor.flag[0]&&statefor.flag[1]&&statefor.flag[2])) {
					statefor.varValue=E.value;
				}
				if(statefor.flag[0]&&statefor.flag[1]) {
					statefor.flag[2]=true;
				}
				stackt.push(E.type);
				stackv.push(E.value);
				break;
			case 81:
				JVarible=true;//
				E.type=T.type;
				E.value=T.value;

				if(!(statefor.flag[0]&&statefor.flag[1]&&statefor.flag[2])) {
					statefor.varValue=E.value;
				}
				if(statefor.flag[0]&&statefor.flag[1]) {
					statefor.flag[2]=true;
				}
				break;
			case 82:
				JVarible=true;//
				stackt.pop();
				stackv.pop();
				F.type=stackt1.pop();
				F.value=stackv1.pop();
				T.type=highType(T.type, F.type);
				indexOpr++;
				String result3="T"+Integer.valueOf(indexOpr).toString();
				gen("*", F.value, T.value, result3,NXQ);

				T.value=result3;
				stackt.push(T.type);
				stackv.push(T.value);
				break;
			case 83:
				JVarible=true;//
				stackt.pop();
				stackv.pop();
				F.type=stackt1.pop();
				F.value=stackv1.pop();
				T.type=highType(T.type, F.type);
				indexOpr++;
				String result4="T"+Integer.valueOf(indexOpr).toString();
				gen("/", F.value, T.value, result4,NXQ);
				T.value=result4;
				stackt.push(T.type);
				stackv.push(T.value);
				break;

			case 84:

				JVarible=true;//
				T.type=F.type;
				T.value=F.value;
				stackt.push(T.type);
				stackv.push(T.value);


				stackt1.pop();
				stackv1.pop();
				break;
			case 85:
				JVarible=true;//
				F.value=E.value;
				F.type=E.type;
				stackt.pop();
				stackv.pop();
				stackt1.push(F.type);
				stackv1.push(F.value);
				break;
			case 86:
				JVarible=true;//
				if(to.contains(".")&&to.length()>1) {
					F.type="float or double";
//				}else if(!to.contains(".")&&to.length()==1&&!Character.isDigit(to.charAt(0))){
//					F.type="char";
				}else {
					F.type="int";
					if(!Character.isDigit(to.charAt(0))) {
						F.type="int26";
					}
				}
				F.value=to;


				stackt1.push(F.type);
				stackv1.push(F.value);
				if(statefor.flag[0]==true&&statefor.flag[1]==false) {
					statefor.flag[1]=true;
				}
				break;
			case 87:
				JVarible=true;//
				if(!tempVar1.equals("")) {
					//System.out.println("----------------------------------------------------------"+tempVar1);
					F.type="int";
					F.value=tempVar1;
					tempVar1="";
				}else {
					//System.out.println(temp);
					F.type=temp.type;
					F.value=temp.name;

				}
				stackt1.push(F.type);
				stackv1.push(F.value);
				for(Iden i:ids) {
					if(F.value.equals(i.name)) {
						i.flag2=false;
					}
				}
				break;
			default:
				JVarible=true;//
				break;

		}
//		System.out.println(ids);
		for(String str:addrs) {
			bw.write(str);
			bw.flush();
			bw.newLine();
		}




		for(Iden id:ids) {
			if(id.type.equals("int26")) {
				bw.write(id.name+" "+id.type);
				bw.flush();
				bw.newLine();
			}
		}
		System.out.println(addrs);
		bw.close();
	}
}
