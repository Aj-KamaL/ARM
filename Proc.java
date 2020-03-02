import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.spi.CurrencyNameProvider;


class Instruction_Word{

	String Field2;			//Instruction
	String Operand1;		//the value stored in the Rn in String
	String operand2;		//the value stored in the Rs in String
	String result;			//the output to be stored in the Rd in String
	String Rn,Rs,Rd;		//register number containnig Operand1 operand2 result in string in bin
	
	public Instruction_Word(String string2) {

		Field2=string2;
		}
	@Override
	public String toString() {
		return "Instruction_Word [Field2=" + Field2 + ", Operand1=" + Operand1 + ", operand2="
				+ operand2 + ", result=" + result + ", Rn=" + Rn + ", Rs=" + Rs + ", Rd=" + Rd + "]";
	}
	static String hexToBin(String s) {
		//System.out.println("string s "+s);
		  return new BigInteger(s, 16).toString(2);
		}
	static String BintoHex(String s) {
		  return new BigInteger(s, 2).toString(16);
		}
	
	
}
public class Proc {

	int[] Registers;
	Map<String,String> Memory;
	int[] memory;
	int k=1;
	Stack<Integer> Stac;
	Instruction_Word currnt;
	int Z=0, N=0;
	ArrayList<String> use_to_store;
	public Proc(){
		this.Registers=new int[16];
		this.Memory=new HashMap<String,String>(4000);
		this.Stac=new Stack<>();
		use_to_store = new ArrayList<String>();
		
	}
	public void resetproc(){
		for(int i=0;i<this.Registers.length;i++){
			this.Registers[i]=0;
		}
		this.memory = new int[(int)1e6];
		this.Memory.clear();
		
	}
	public void load_program(String s) throws FileNotFoundException{
		Scanner inn = new Scanner(new BufferedInputStream(new FileInputStream(s)));
		
		
		int h=1;
		String first_address=" ";
		while (inn.hasNext()) { // IOException
			String[] k=inn.nextLine().split(" ");
			k[0] = k[0].substring(2, k[0].length());
			k[1] = k[1].substring(2, k[1].length());
			Instruction_Word instr=new Instruction_Word(k[1]);
			System.out.println("0x"+k[0]+" 0x"+k[1]);
			
			if(h==1){
				first_address=k[0];
				h=2;
			}
//			System.out.println(Integer.parseInt(k[0],10));
			System.out.println(Long.parseLong(instr.Field2, 16));
//			System.out.println(Integer.decode(instr.Field2));
			
			memory[(int)Long.parseLong(k[0],16)] = (int)Long.parseLong(instr.Field2, 16);
//			this.Memory.put(k[0],instr.Field2);
			
			
			/* Memory address that is key of map is a hex string*/
			
			
		}
		this.Registers[13]=21504;
		this.Registers[15]=0;
		this.Registers[15]=(int)Long.parseLong(first_address,16);// Program Counter start of the memory could be 15 or 0 or anything
		
	}
	public static void main(String[] args) throws IOException{
		Proc AMD_7=new Proc();
		AMD_7.resetproc();
		AMD_7.load_program("1.txt");
		AMD_7.simulate();
		 
	}
	public void simulate() throws IOException {
		
		while(k==1 || !currnt.Field2.equals("ef000011")){//updt termination condition
		fetch();
		decode();
		execute();
		memory();
		write();
		System.out.println("***************************************************");
		}
		
		 FileWriter fw=new FileWriter("Maa_ki_chu.txt");/////////////////////
		 
		 for(int i=0;i<use_to_store.size()-1;i=i+2)
		 {
		
         fw.write(use_to_store.get(i)+" "+ use_to_store.get(i+1));    
		 }
         fw.close(); 
		
	}
	private void write() {
		System.out.println("Stage:  COMMIT ");
		String bin_to_decod = Instruction_Word.hexToBin(currnt.Field2);//substring begin right from  the very first index required in the resultant string
		if(bin_to_decod.length()<32){
			int rem_bits = 32-bin_to_decod.length();
			for(int i=0; i<rem_bits; i++)
				bin_to_decod = "0"+bin_to_decod;
		}
		/*****************************************************************/
		String typ_of_instruction;
		typ_of_instruction=bin_to_decod.substring(4,6);							//Data Instruction
		if(typ_of_instruction.equals("00")==true){
			if(Integer.parseInt(currnt.Rd,2)!=0){
				System.out.println("	Committing value "+currnt.result+ " to Register Number "+ Integer.parseInt(currnt.Rd,2));
				Registers[Integer.parseInt(currnt.Rd,2)]=Integer.parseInt(currnt.result);
				//DBG
//				System.out.println(currnt.result+ " currnt.result");
				
			}
			else{
				System.out.println("	Nothing to Commit");
			}
		}
		/*****************************************************************/
		typ_of_instruction=bin_to_decod.substring(4,7);							// Branch type Instruction
		if(typ_of_instruction.equals("101")==true){
			System.out.println("	Nothing to Commit");
		}
		/****************************************************************/
		typ_of_instruction=bin_to_decod.substring(4,6);							//  Load/Store
		if(typ_of_instruction.equals("01")==true){
			if(bin_to_decod.substring(11,12).equals("1")){								// Load
				System.out.println("	Committing value "+currnt.Operand1+ " to Register Number "+ Integer.parseInt(currnt.Rd,2));
				int t = this.Registers[Integer.parseInt(currnt.Operand1)];
				System.out.println(t+" t");
				System.out.println(Integer.toHexString(21460)+ " hex value");
				
				
//				if(!Memory.containsKey(g))
//					Memory.put(g, "0");
			//	System.out.println(Integer.parseInt(Memory.get(g)+" val in map="+memory[Integer.parseInt(g)]));
				System.out.println(" val in map="+memory[t]);

//				Registers[Integer.parseInt(currnt.Rd,2)]=Integer.parseInt(Memory.get(g), 16);
				Registers[Integer.parseInt(currnt.Rd,2)]=(memory[t]);
			}
			else if(bin_to_decod.substring(11,12).equals("0")){																// Store
				System.out.println("	Nothing to Commit");
			}
		}
		
	}
	private void memory() {
		System.out.println("Stage:	MEMEORY ACCESS");
		String bin_to_decod = Instruction_Word.hexToBin(currnt.Field2);//substring begin right from  the very first index required in the resultant string
		if(bin_to_decod.length()<32){
			int rem_bits = 32-bin_to_decod.length();
			for(int i=0; i<rem_bits; i++)
				bin_to_decod = "0"+bin_to_decod;
		}
		String typ_of_instruction;
		typ_of_instruction=bin_to_decod.substring(4,6);							//Data Instruction
		if(typ_of_instruction.equals("00")==true){
			System.out.println("	Memory Access Not Required");
		}
		/*******************************************************************************/
		typ_of_instruction=bin_to_decod.substring(4,6);
		if(typ_of_instruction.equals("01")==true){	
			
			if(bin_to_decod.substring(11,12).equals("0")){						//Store  
//				System.out.println(currnt.Operand1+ " before write instruction"+ currnt.Rd);
				write_instruction(currnt.Operand1,Integer.toString(this.Registers[Integer.parseInt(currnt.Rd,2)]));		// we are storing the value in memory in binary
				
				//System.out.println(Memory.get(key));
				System.out.println(" Store data " + this.Registers[Integer.parseInt(currnt.Rd,2)]  +" at addrress "+currnt.Operand1 );

			}
			if(bin_to_decod.substring(11,12).equals("1")){ 						//Load 
			      String data = read_instruction(Integer.parseInt(currnt.Operand1)).Field2;							//unsigned check reading op12 in decode load store
//			      currnt.Operand1 = Integer.toString(Integer.parseInt(currnt.Operand1)+4);
			      int tp = this.Registers[Integer.parseInt(currnt.Operand1)]+4;

			      
			      System.out.println("  Load data " + data +" from addrress "+currnt.Operand1 +" in memory "+memory[tp] );
			      currnt.Rn=Integer.toBinaryString(tp);
			}
		}
		 
		/*****************************************************************************************************/
		typ_of_instruction=bin_to_decod.substring(4,7);							//Branch
		if(typ_of_instruction.equals("101")==true){
			System.out.println("	Memory Access Not Required");

		}
	}
	
/*********************************************************************************************************************************************************************************************/
	private void execute() {
		System.out.println("Stage:	Execute");
		
		
		
		
		
		
//-------------------------------------------------------------------------
		String bin_to_decod = Instruction_Word.hexToBin(currnt.Field2);//substring begin right from  the very first index required in the resultant string
		if(bin_to_decod.length()<32){
			int rem_bits = 32-bin_to_decod.length();
			for(int i=0; i<rem_bits; i++)
				bin_to_decod = "0"+bin_to_decod;
		}
		String typ_of_instruction;

		typ_of_instruction=bin_to_decod.substring(4,6);
		if(typ_of_instruction.equals("00")==true){											//Data Instructions		

			String Opcode=bin_to_decod.substring(7,11);	

				if(Opcode.equals("0000")==true){								//AND
					System.out.println("	AND "+currnt.Operand1+" and "+currnt.operand2);
					int temp = Integer.parseInt(currnt.Operand1) & Integer.parseInt(currnt.operand2);
					currnt.result = Integer.toString(temp);
				}
				else if(Opcode.equals("0010")==true){							//SUB
					System.out.println("	SUB "+currnt.Operand1+" and "+currnt.operand2);
					int temp = Integer.parseInt(currnt.Operand1) - Integer.parseInt(currnt.operand2);
					currnt.result = Integer.toString(temp);
				}
				else if(Opcode.equals("0100")==true){							//ADD
					System.out.println("	ADD "+currnt.Operand1+" and "+currnt.operand2);
					int temp = Integer.parseInt(currnt.Operand1) + Integer.parseInt(currnt.operand2);
					
					//System.out.println(temp+"Manish fuckr bhosdika mc bc");
					currnt.result= Integer.toString(temp);
				}
				else if(Opcode.equals("1101")==true){							//MOV
					System.out.println("	MOV");
					currnt.result= currnt.operand2;
					
				}
				else if(Opcode.equals("1010")==true){							//CMP as SUB, but result is not written
					System.out.println("	CMP "+currnt.Operand1+" and "+currnt.operand2);
					Z=0;
					N=0;
					if((Integer.parseInt(currnt.Operand1) - Integer.parseInt(currnt.operand2)) == 0){
						Z = 1;
						System.out.println("	Z updated to 1");
					}
					if(Integer.parseInt(currnt.Operand1) - Integer.parseInt(currnt.operand2) < 0){
						N = 0;
						System.out.println("	N updated to 1");
					}
				}
				else if(Opcode.equals("1100")==true){							//ORR
					System.out.println("	OR "+currnt.Operand1+" and "+currnt.operand2);
					int temp = Integer.parseInt(currnt.Operand1) | Integer.parseInt(currnt.operand2);
					currnt.result = Integer.toString(temp, 2);
				}
				else if(Opcode.equals("0101")==true){							//ADC  Operand1 + operand2 + carry
					
				}
				else if(Opcode.equals("0110")==true){							//SBC	Operand1 - operand2 + carry - 1

				}
				else if(Opcode.equals("0111")==true){							//RSC 	operand2 - Operand1 + carry - 1
					
				}
			}
		
		typ_of_instruction=bin_to_decod.substring(4,7);

		if(typ_of_instruction.equals("101")==true){										//Branch conditioned instruction
			String Opcode=bin_to_decod.substring(7,11);
			System.out.println("Decode: Instruction is Brach type if");
			String Condition=bin_to_decod.substring(0,4);
			String offset =bin_to_decod.substring(8,32);
			currnt.Operand1 = "0";
			if(Condition.equals("0000")==true){					// 	equal	EQ
				
					if(Z==1){
						currnt.Operand1 = "1";
					}
					System.out.println("mdsakhfkjshfkjh");
			}
			
			else if(Condition.equals("0001")==true){			// not equal  NE
				if(Z==0){
					currnt.Operand1 = "1";
				}
			}
			else if(Condition.equals("1010")==true){			//Greater or equal
				if(N==0 && Z!=0){
					currnt.Operand1 = "1";
				}
					
			}
			else if(Condition.equals("1011")==true){			//less than
				if(N!=0){
					currnt.Operand1 = Integer.toString(1);//Integer.parseInt("0001", 2);
				}
			}
			else if(Condition.equals("1100")==true){			//Greater than
				if(N==0){
					currnt.Operand1 = "1";
				}
			}
			else if(Condition.equals("1101")==true){			//Less than or equal
				if(N!=0 || Z!=0){
					currnt.Operand1 = "1";
				}
			}
			else if(Condition.equals("1110")==true){			//AL
				currnt.Operand1 = "1";
				
			}
			if(currnt.Operand1.equals("1")){
				if(currnt.Operand1.equals("1")){
					int intOperand2 = Integer.parseInt(bin_to_decod.substring(8,32),2);
					int s = (intOperand2 & 0x800000)<<1;
					for(int j=0; j<8; j++, s<<=1)
					{
						intOperand2+=s;
					}
					intOperand2<<=2;
					Registers[15] += intOperand2;
					Registers[15]+=4;
					currnt.operand2 = Integer.toString(intOperand2);
					System.out.println("	Updating PC to 0x"+Registers[15]);
				}
			}
		}
		
		typ_of_instruction=bin_to_decod.substring(4,6);										//Load/Store
		if(typ_of_instruction.equals("01")==true){
			System.out.println("	No execute operation");
		}
//----------------------------------------------------------------
		
		
		
	}
/*********************************************************************************************************************************************************************************************/
	private void decode() {
		System.out.println("Stage:  Decode ");
		
		String bin_to_decod = Instruction_Word.hexToBin(currnt.Field2);//substring begin right from  the very first index required in the resultant string
		if(bin_to_decod.length()<32){
			int rem_bits = 32-bin_to_decod.length();
			for(int i=0; i<rem_bits; i++)
				bin_to_decod = "0"+bin_to_decod;
		}
		
		String typ_of_instruction;
		/*************************************************************************************/
		//Checks the opcodes possible in 2 4 bits
		//System.out.println(bin_to_decod.substring(4,6)+" possible type "+bin_to_decod.substring(4,8));
		typ_of_instruction=bin_to_decod.substring(4,6);
		if(typ_of_instruction.equals("00")==true){											//Data Instructions		
			System.out.println("	Instruction is Data Instruction type");
			String Opcode=bin_to_decod.substring(7,11);	
				if(Opcode.equals("0000")==true){								//AND
					System.out.println("	Operation : AND");
				}
				else if(Opcode.equals("0010")==true){							//SUB
					System.out.println("	Operation : SUB");
				}
				else if(Opcode.equals("0100")==true){							//ADD
					System.out.println("	Operation : ADD");
				}
				else if(Opcode.equals("1101")==true){							//MOV
					System.out.println("	Operation : MOV");
				}
				else if(Opcode.equals("1010")==true){							//CMP as SUB, but result is not written
					System.out.println("	Operation : CMP");
				}
				else if(Opcode.equals("1100")==true){							//ORR
					System.out.println("	Operation : ORR");
				}
				else if(Opcode.equals("0101")==true){							//ADC  Operand1 + operand2 + carry
					
				}
				else if(Opcode.equals("0110")==true){							//SBC	Operand1 - operand2 + carry - 1
					
				}
				else if(Opcode.equals("0111")==true){							//RSC 	operand2 - Operand1 + carry - 1
					
				}
				/*
				 * The arithmetic operations (SUB, ADD, CMP) treat each
					operand as a 32 bit integer (either unsigned or 2’s complement signed, the two are
					equivalent). If the S bit is set (and Rd is not R15) the V flag in the CPSR will be set if
					an overflow occurs into bit 31 of the result; this may be ignored if the operands were
					considered unsigned, but warns of a possible error if the operands were 2’s
				 */
				  currnt.Rn = bin_to_decod.substring(12,16);		//Register number where the 1st operand is stored in the register in hex
//				  System.out.println(this.Registers[Integer.parseInt(currnt.Rn,2)]+" vfr");
				  currnt.Rd = bin_to_decod.substring(16,20);		//Register number where the result is to be put in hex
				  currnt.Operand1 =Integer.toString(this.Registers[Integer.parseInt(currnt.Rn,2)]);	//the first operand in string

				  //DBG
//				  System.out.println(Integer.parseInt(currnt.Rs,2)+ " currnt.rs");
//				  System.out.println(Integer.parseInt(currnt.Rn,2)+ " currnt.Rn");
//				  System.out.println(Integer.parseInt(currnt.Rd,2)+ " currnt.Rd");
				  
				  //??
				  if(Integer.parseInt(currnt.Rn,2)!=0){							
					  System.out.println("	Ist OPERAND is in Register Number "+Integer.parseInt(currnt.Rn,2));
				  }
				  
				  															//register number in string
				  String Immediate_Operand=bin_to_decod.substring(6,7);
//				  System.out.println(bin_to_decod);
//				  System.out.println(Immediate_Operand+" immediate op");
				  //??
				  //Checks if the instruction Rs is immediate value or Register
				  if(Immediate_Operand.equals("0")){
					  currnt.Rs = bin_to_decod.substring(28,32);
//					  System.out.println(bin_to_decod.substring(20, 28)+" Rm shift value");
					  //Checks if the instruction is single source based or two sources based if below compare true then single else two source
					  //?? is this the right way for judging the above comparison
					  if(Integer.parseInt(currnt.Rn,2)==0){						
				    	  System.out.print("	OPERAND "+this.Registers[Integer.parseInt(currnt.Rs,2)]+ " is stored in Register Number (without shift applied) "+ Integer.parseInt(currnt.Rs,2));
				      }
				      else {
				    	  System.out.println("	IInd OPERAND is stored in Register Number (without shift applied)  "+ Integer.parseInt(currnt.Rs,2));
				      }
//				      System.out.println(Integer.parseInt(currnt.Rs,2)+ " currnt.rs");
//				      System.out.println(this.Registers[Integer.parseInt(currnt.Rs,2)]+ "value at rs");
				      
				      currnt.operand2 = Integer.toString(this.Registers[Integer.parseInt(currnt.Rs,2)]);
				  }
				  else{			  
					  //Checks if the instruction is single source based or two sources based if below compare true then single else two source
					  //??
					  String operand_direct=bin_to_decod.substring(24,32);
//					  System.out.println(bin_to_decod.substring(20,24)+" Imm shift value");
					  if(Integer.parseInt(currnt.Rn,2)==0){		
						  
				    	  System.out.println("	OPERAND is (without rotate applied)  "+ Integer.parseInt(operand_direct,2));
				      }					  
					  else {
				    	  System.out.println("	OPERAND is (without rotate applied)  "+ Integer.parseInt(operand_direct,2));
				      }
				      currnt.operand2 = Integer.toString(Integer.parseInt(operand_direct,2));
				  }
				  
				  //??
				  if(Integer.parseInt(currnt.Rd,2)!=0){						
					  System.out.println("	Destination Register Number is "+Integer.parseInt(currnt.Rd,2)); 
				  }
				  
				  //Reading the registers to obtain the respective operands if Rn is involved or Rs is a register because if Rn is not involved than no need to read it as well as if Rs is not immediate only than its needed to be read else its already read
				  //??
				  if(Integer.parseInt(currnt.Rn,2)!=0||Immediate_Operand.equals("0") ){
					  System.out.print("	The operands are : ");
				  }
				  //??
				  if(Integer.parseInt(currnt.Rn,2)!=0){	
				      System.out.println("\n		Rn Operand is "+ currnt.Operand1);
				      	
				    }
				  if(Immediate_Operand.equals("0")){
				      System.out.println("		Rs Operand is "+ currnt.operand2);

				  }
//				  System.out.println();
			}
		/*************************************************************************************/
		typ_of_instruction=bin_to_decod.substring(4,7);
		if(typ_of_instruction.equals("101")==true){											//Branch conditioned instruction
			System.out.println("Decode: Instruction is Brach type");
			String Condition=bin_to_decod.substring(0,4);
			String offset =bin_to_decod.substring(8,32);	

			if(Condition.equals("0000")==true){					// 	equal	EQ
				System.out.println("	The Condition bit is Equal");
			}
			else if(Condition.equals("0001")==true){			// not equal  NE
				System.out.println("	The Condition bit is Not Equal");
			}
			else if(Condition.equals("1010")==true){			//Greater or equal
				System.out.println("	The Condition bit is Greater or Equal");
			}
			else if(Condition.equals("1011")==true){			//less than
				System.out.println("	The Condition bit is Less Than");
			}
			else if(Condition.equals("1100")==true){			//Greater than
				System.out.println("	The Condition bit is Graeter Than");				
			}
			else if(Condition.equals("1101")==true){			//Less than or equal
				System.out.println("	The Condition bit is Less than or Equal");

			}
			else if(Condition.equals("1110")==true){
				System.out.println("	The Condition bit is Always True");
			}
			System.out.println("	of Offset : "+ Instruction_Word.BintoHex(offset));
		}
		/*************************************************************************************/
		
		typ_of_instruction=bin_to_decod.substring(4,6);										//Load/Store
		if(typ_of_instruction.equals("01")==true){
			System.out.println("	Instruction is Single Data Transfer type");
				
			currnt.Rd=bin_to_decod.substring(16,20);
			currnt.Rn=bin_to_decod.substring(12,16);										//base register
			
			if(bin_to_decod.substring(7,8).equals("0"))										
			{
				System.out.println("	Indexing : Post");												//P
			}
			if(bin_to_decod.substring(7,8).equals("1"))
			{
				System.out.println("	Indexing : Pre");												//P
			}
			if(bin_to_decod.substring(11,12).equals("0")){						//Store  
				System.out.println("	Transfer : Store");
				
			}
			if(bin_to_decod.substring(11,12).equals("1")){						//Load 
				System.out.println("	Transfer: Load");

			}
			//??
			if(Integer.parseInt(currnt.Rn,2)!=0){
				System.out.println("	Base Register Number is "+Integer.parseInt(currnt.Rn,2));
				System.out.println("	Base Address in Base Register is "+this.Registers[Integer.parseInt(currnt.Rn,2)]);
//				System.out.println(bin_to_decod.substring(20,28)+" Rm shift value");
				System.out.println();
				currnt.Operand1 =Integer.toString(Integer.parseInt(currnt.Rn,2));	//the first operand in string
//				System.out.println(currnt.Operand1+" jak"); 
				//operand 1 is storing the address to be used for main memory from Register array at index Rn(is the binary register in str instruction)

			}
			//??
			if(Integer.parseInt(currnt.Rd,2)!=0){System.out.println("	S_D Destination Register Number is "+Integer.parseInt(currnt.Rd,2));}
			System.out.println(bin_to_decod+" instruction");
			if(bin_to_decod.substring(6,7).equals("0")){						//I offset is immediate value
				System.out.println("	Immediate value of Offset "+Instruction_Word.BintoHex(bin_to_decod.substring(20,32)));
			}
			if(bin_to_decod.substring(6,7).equals("1")){						//I offset is in Rs 
				currnt.Rs=bin_to_decod.substring(28,32);						
				System.out.println(bin_to_decod+" bin to decode");
				System.out.println("	The Offset is in Register Number(without Shift applied)  "+Integer.parseInt(currnt.Rs,2)+" whose immediat value is " +this.Registers[Integer.parseInt(currnt.Rs,2)]);
				
			}
			//Reading the register
			
				 
			
			
			
			
			
			//use Write back bit and up down bit in the execute cycle of this  
			
		}}
				
				
			
		
		
	
	private void fetch() {
		currnt=read_instruction(15); k=2;
		System.out.println("Stage:  Fetch"); 
		System.out.println("	Current instruction is "+ currnt.Field2+"  from address " +Integer.toHexString(this.Registers[15]));
		this.Registers[15]=this.Registers[15]+4;//update need more details i.e norm +4 if the instruction already is not telling where to go

	}
	private	void write_instruction(String k,String s) {
//		System.out.println(k+" before "+ s);
		
//		System.out.println(this.Registers[13]+"dsvd");
//		if(s.length()<32){
//			int rem_bits = 32-s.length();
//			for(int i=0; i<rem_bits; i++)
//				s = "0"+s;
//		}
//		this.Memory.put(k,s);
		memory[this.Registers[Integer.parseInt(k)]] = Integer.parseInt(s);
//		if() 
//		System.out.println(Memory.get(k)+" sadkfjllksadjflksadjflkajsdlkfjsadlk"+" kkkkkk "+ k);
		use_to_store.add(k);
//		use_to_store.add(Integer.toHexString(Integer.parseInt(s,2)));
//		System.out.println(Integer.parseInt(s,2));
//		System.out.println(k+" dcajgsvjd" +s);
		//put s at k

		
		
	}
	private	Instruction_Word read_instruction(int Reg_no) {
		System.out.println(Reg_no+ " reg no");
		 
		
//		System.out.println(this.Registers[13]+"dsvd");
	
//		String present=this.Memory.get(str);
		int present = this.memory[this.Registers[Reg_no]];
//		System.out.println(" present "+present+" str "+str);
		
		Instruction_Word temp=new Instruction_Word(Integer.toHexString(present));
//		System.out.println(temp+"vsf");
		
		return temp;
	}

}
//negative  Base Address in Base Register is -4 /????/
//load store address///
