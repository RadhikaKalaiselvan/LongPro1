
// Starter code for lp1.

// Change following line to your group number
package cs6301.g23;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author radhikakalaiselvan
 *
 */
public class Num {

    static int defaultBase = 10;  // This can be changed to what you want it to be.
    int base = defaultBase;  // Change as needed
    int numOfDigits=String.valueOf(base-1).length();
    boolean isNeg=false;
    LinkedList<Long> numList=null;
    static Num one = new Num(1);
    static Num two = new Num(2);
    /* Start of Level 1 */
    Num(String s) throws Exception {
    	//uses default base
    	this(s,defaultBase);
    }

    Num(long x) {
    	//uses default base
    	this(x,defaultBase);
    }
    Num(String s,int base) throws Exception {
    	numList= new LinkedList<Long>();
    	this.base=base;
    	this.numOfDigits=String.valueOf(base-1).length();
    	this.numList=convertBaseAndStore(s,base);
    }

    Num(long x,int base) {
    	numList= new LinkedList<Long>();
    	this.base=base;
    	this.numOfDigits=String.valueOf(base-1).length();
    	this.convertBaseAndStore(x);
    }
    
    Num(LinkedList<Long> numList,int base){
    	this.numList=numList;
    	this.base=base;
    	this.numOfDigits=String.valueOf(base-1).length();
    	
    }
    Num(LinkedList<Long> numList,int base,boolean isNeg){
    	this(numList,base);
    	this.isNeg=isNeg;
    }
    Num(Num a) {
		// TODO Auto-generated constructor stub
    	this.base = a.base();
    	this.numOfDigits = a.numOfDigits;
    	this.isNeg = a.isNeg;
    	this.numList = new LinkedList<Long>();
    	this.numList = a.numList;
    	
	}
    Num(){
    	numList=new LinkedList<Long>();
    }
    
    LinkedList<Long> convertBaseAndStore(String s,int b) throws Exception{
    	char[] c=s.toCharArray();
    	Num base=new Num(10,b);
    	Num x=new Num(Long.parseLong(String.valueOf(c[0])),b);
    	for(int i=1;i<c.length;i++){
    		Num d=new Num(Long.parseLong(String.valueOf(c[i])),b);
    	    x=Num.add(Num.product(x,base),d);
    	}
    	return x.numList;
    }
    
    void convertBaseAndStore(long num){
    	if(num<0){
    		num=num*(-1L);
    		this.isNeg=true;
    	}
    	while(num>=this.base){
    	long mod=num % this.base;
    	this.numList.addLast(mod); //O(1)
    	num=(long)Math.floor((double)num/this.base);	       
    	}
    	this.numList.add(num);
    }
      
    static long next(Iterator<Long> it){
    	return it.hasNext()?(long) it.next():0;
    }
    
    static boolean isSameSign(Num a,Num b){
    	boolean sameSign=false;
    	if(a.isNeg == b.isNeg){
    		sameSign=true;
    	}
    	return sameSign;
    }
    
   
    public static int compareWithoutSign(Num a,Num b){
    	Num.stripNum(a);
    	Num.stripNum(b);
    	if(a.numList.size()>b.numList.size()){
    		return 1;
    	} else if(a.numList.size()<b.numList.size()){
    		return -1;
    	} else {
    		//same size
    		for(int i=a.numList.size()-1;i>=0;i--){
    			int comp=a.numList.get(i).compareTo(b.numList.get(i));
    			if(comp>0){
    				return 1;
    			} else if(comp<0) {
    				return -1;
    			} 
    		}
    	}
    	return 0;
    }
    
    public static Num add(Num a ,Num b){
    	Num.stripNum(a);
    	Num.stripNum(b);
    	Num result=new Num(0,a.base);
    	if(Num.isSameSign(a,b)){
    		result=Num.addition(a, b);
    		result.isNeg=a.isNeg;
    	} else {
    		int comp=Num.compareWithoutSign(a,b);
    		if(comp>0){
    			result=Num.subtraction(a, b);
    			result.isNeg=a.isNeg;
    		} else if(comp<0) {
    			result=Num.subtraction(b, a);
    			result.isNeg=b.isNeg;
    		} 
    		//else will return result which is already set to new Num(0)
    	}
    	return result;
    }
    
    static Num addition(Num a, Num b) {
    	Num.stripNum(a);
    	Num.stripNum(b);
    	if(Num.checkZero(a)){
    		return b;
    	}
    	if(Num.checkZero(b)){
    		return a;
    	}
    	LinkedList<Long> resultList=new LinkedList<Long>();
    	long carry=0;
    	Iterator<Long> numAit=a.numList.iterator();
    	Iterator<Long> numBit=b.numList.iterator();
    	while(numAit.hasNext() || numBit.hasNext()){
    		long sum=next(numAit)+next(numBit)+carry;
    		resultList.add(sum%a.base);
    		carry=sum/a.base;
    	}
    	if(carry!=0){
    		resultList.add(carry);
    	}
	return new Num(resultList,a.base,a.isNeg);
    }
    
   
static Num subtract(Num a, Num b){
	Num.stripNum(a);
	Num.stripNum(b);
	Num result=new Num(0,a.base);
	if(Num.isSameSign(a,b)){
		if(a.isNeg){
			//both are negative  
			int comp=Num.compareWithoutSign(a,b);
			if(comp>0){
				//(-30) - (-10)
				result=Num.subtraction(a, b);
				result.isNeg=true;
			} else if(comp<0){
				// (-10) - (-30)
				result=Num.subtraction(b, a);
				result.isNeg=false;
			}
		} else {
			
			//both positive 
			int comp=Num.compareWithoutSign(a,b);
		//	System.out.println("Both positive  "+comp);
			if(comp>0){
				// a > b 30 -10
				//System.out.println("subtract ");
				result=Num.subtraction(a, b);
			} else if(comp<0){
				//b > a 10 -30
				result=Num.subtraction(b, a);
				result.isNeg=true;
			}
		}
	} else {
		//any one of the numbers is negative
		if(b.isNeg){
			//second number is negative 10 - (-30)
			Num bNum=new Num(b.numList,b.base);
			bNum.isNeg=false;
			result=Num.add(a, bNum);
		} else {
			//first number is negative (-10) - 30
			Num bNum=new Num(b.numList,b.base);
			bNum.isNeg=true;
			result=Num.add(a, bNum);
		}
	}
	return result;
}
    static Num subtraction(Num a, Num b) {
    	LinkedList<Long> resultList=new LinkedList<Long>();
    	Iterator<Long> numAit=a.numList.iterator();
    	Iterator<Long> numBit=b.numList.iterator();
    	boolean borrowed=false;
    	while(numAit.hasNext() || numBit.hasNext()){
    		long numA=next(numAit);
    		long numB=next(numBit);
    		if(borrowed){
    			numA=numA-1;
    			borrowed=false;
    		}
    		
    		long diff=numA-numB;
    		if(diff>=0){
    			resultList.add(diff);
    		}else{
    			long newA=numA+a.base;
    			resultList.add(newA-numB);
    			borrowed=true;
    			
    		}
    	}
	return new Num(resultList,a.base);
    }
    
    static Num getHalfOfNumAsList(Num a,boolean secondHalf){
    	LinkedList<Long> list=new LinkedList<Long>();
    	int i=0,size=a.numList.size();
    	int end=size/2;
    			if(!secondHalf){
    				i=end;
    				end=size;
    			}
    	for(;i<end;i++){
    		list.add(a.numList.get(i));
    	}
    	return new Num(list,a.base);
    }
    
    static void appendZerosToFront(Num a,int expectedLength){
    	for(int i=a.numList.size();i<expectedLength;i++){
    		a.numList.add(0L);
    	}
    }
    
  static void checkEqualLengthAndPadZeros(Num a, Num b){
	  int aSize=a.numList.size();
	  int bSize=b.numList.size();
	  if(aSize>bSize){
		  if(aSize%2!=0)
			  {
				  Num.appendZerosToFront(a,aSize+1);
				  aSize=aSize+1;
			  }
		  Num.appendZerosToFront(b,aSize);
	  }else if(bSize>aSize){
		     if(bSize%2!=0)
			  {
				  Num.appendZerosToFront(b,bSize+1);
				  bSize=bSize+1;
			  }
		  Num.appendZerosToFront(a,bSize);
		  }else {
			  if(aSize%2!=0 && aSize!=1){
				  Num.appendZerosToFront(a, aSize+1);
				  Num.appendZerosToFront(b, bSize+1);
			  }
		  }
  }
  
  
  static boolean checkZero(Num a){
	  boolean containsOnlyZero=true;
	  for(int i=0;i<a.numList.size();i++){
		
		  if(a.numList.get(i)!=0){
			 containsOnlyZero=false;
			 break;
		  }
	  }
	  return containsOnlyZero;
	  
  }
  
  static void stripNum(Num a){
	  if(Num.checkZero(a)){
		  return ;
	  }
	  int n = a.numList.size();
	  if(n==1){
		  return;
	  }
	  while(a.numList.getLast()==0){
		  a.numList.removeLast();
	  }
  }
   
    static Num product(Num a, Num b) throws Exception{
    	//System.out.println("Product called a and b "+a.numList.size()+" "+b.numList.size());
    	/*
    	 * Suppose we want to multiply two 2-digit base-m numbers: x2 m + x1 and y2 m + y1:

compute x2 · y2, call the result F
compute x1 · y1, call the result G
compute (x2 + x1) · (y2 + y1), call the result H
compute H − F − G, call the result K; this number is equal to x2 · y1 + x1 · y2
compute F · m2 + K · m + G.
    	 */
    	
    
    	if(a.numList.size()==0 ||  b.numList.size()==0){
    	throw new Exception("size zero");
    	}
    	Num.checkEqualLengthAndPadZeros(a, b);
    	if(checkZero(a) || checkZero(b)){
    		return new Num(0,a.base);
    	}
    	int aSize=a.numList.size();
    	if(aSize==1 ||  b.numList.size()==1){
    		long result=a.numList.peek()*b.numList.peek();
           return new Num(result,a.base);
    	}

    	Num x1=Num.getHalfOfNumAsList(a,true);
    	Num x2=Num.getHalfOfNumAsList(a,false);
    	Num y1=Num.getHalfOfNumAsList(b, true);
    	Num y2=Num.getHalfOfNumAsList(b, false);
    	Num F=Num.product(x2, y2);
    	Num G=Num.product(x1,y1);
        Num H=Num.product(Num.add(x2, x1),Num.add(y2,y1));
    	
    	Num K=Num.subtract(H, F);
    	K=Num.subtract(K, G);
  
    	Num k=Num.padZeros(K,a.numList.size()/2);
   
    	Num z=Num.padZeros(F,a.numList.size());
  	
    	Num finalResult=Num.add(k,z);
  		
    	finalResult=Num.add(finalResult, G);
  	
    	return finalResult;
    }
    
    static Num padZeros(Num a,int n){
    	//assert n%10==0
    	if(Num.checkZero(a)){
    		return new Num(0,a.base);
    	}
    	
    	LinkedList<Long> k=new LinkedList<Long>(a.numList);
    		while(n>0){
    			k.addFirst(new Long(0));
    			n--;
    		}
    	return new Num(k,a.base);
    }
 
    // Use divide and conquer
    static Num power(Num a, long n) throws Exception {
    	Num.stripNum(a);
    	Num result, temp;
    	if(n == 0)
    		return new Num(1,a.base);
    	else if(n ==1)
    		return a;
    	else if(n%2 == 0){
    		result = product(power(a,n/2),power(a,n/2));
    	}
    	else
    	{
    		temp = product(power(a,(n-1)/2), power(a,(n-1)/2));
    		result = product(temp, a);
    	}
    	return result;
    }
    /* End of Level 1 */

    /* Start of Level 2 */
   
    
    static Num divide(Num a, Num b) throws Exception {
    	try{
    		checkZero(b);  		
    	}catch(ArithmeticException e){
    		System.out.println("Cannot divide by 0");
    	}
    	Num.stripNum(a);
    	Num.stripNum(b);
    	Num quotient = new Num(1);
    	Num dividend = new Num(a);
    	Num divisor = new Num(b);
    	Num pro = Num.product(divisor, quotient);
    	//System.out.println(Num.compareWithoutSign(pro,dividend));
    	//System.out.println("flag"+dividend.numList.size());
    	//System.out.println(pro.numList);
    	//System.out.println(dividend.numList);
    	while(Num.compareWithoutSign(pro,dividend)<=0){
    		//System.out.println("flag");
    		//System.out.println(pro + "-" + dividend + "-" + Num.compareWithoutSign(pro,dividend));
    		quotient = add(quotient,one);
    		pro = Num.product(divisor, quotient);
    		//System.out.println(pro);
    	}
    	//System.out.println(pro + "->" + dividend + "->" + Num.compareWithoutSign(pro,dividend));
    	quotient = subtract(quotient,one);
    	return quotient;
    }

    static Num mod(Num a, Num b) throws Exception {
    	Num.stripNum(a);
    	Num.stripNum(b);
    	Num dividend = new Num(a);
    	Num divisor = new Num(b);
    	Num remainder = new Num();
    	Num quotient = Num.divide(dividend, divisor);
    	Num pro = Num.product(quotient,b);
    	remainder = Num.subtract(a,pro);
    	return remainder;  	
    }

    // Use divide and conquer
    static Num power(Num a, Num n) throws Exception {
    	if(Num.checkZero(a)){
    		return new Num(0,a.base);
    	}
    	if(Num.checkZero(n)){
    		return new Num(1);
    	}
    	Num.stripNum(a);
    	Num.stripNum(n);
    	Num temp = new Num();
    	//System.out.println("->"+ a.numList);
    	//System.out.println("->"+ divide(n,two).numList);
    	temp = power(a,divide(n,two));
    	if(Num.checkZero(Num.mod(n,two))){
    		Num product = Num.product(temp,temp);
    	//	System.out.println("look "+ product.numList);
    		return product;
    	}
    	else{
    		Num pro = Num.product(temp,temp);
    //		System.out.println("pro = " + pro.numList);
    //		System.out.println("pro = " + a.numList);
    		Num product = Num.product(a,pro);
    //		System.out.println("see"+product.numList);
    		return product;
    	}
    }

    static Num squareRoot(Num a) throws Exception {
    	Num.stripNum(a);
    	Num x = new Num(1);
    	Num number = new Num(a);
    	Num pro = Num.product(x,x);
    	while(Num.compareWithoutSign(pro,number)<=0){
    	//	System.out.println(pro + "-" + dividend + "-" + Num.compareWithoutSign(pro,dividend));
    		x = add(x,one);
    		pro = Num.product(x,x);
    //		System.out.println(x+ " " + pro + " " + number);
    	}
    	//System.out.println(pro + "-" + dividend + "-" + Num.compareWithoutSign(pro,dividend));
    	x = subtract(x,one);
    	return x;
    }
    /* End of Level 2 */
  
    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    void printList() {
    	StringBuilder num=new StringBuilder("");
    	for(long i:numList){
    		num.append(i+" ");
    	}
    	System.out.println(this.base+" : "+num);
    }
    
    // Return number to a string in base 10
    public String toString() {
    	Num base=new Num(this.base,10);
    	Num d=new Num(0,10);
    	ListIterator<Long> li = this.numList.listIterator(this.numList.size());
    	while(li.hasPrevious()){
    		
    		Num x=new Num(li.previous(),10);
    		try {
				d=Num.add(Num.product(d,base),x);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
    	}
    	StringBuilder st=new StringBuilder("");
    	for(Long val: d.numList){
    		st.insert(0,val);
    	}
    	if(isNeg){
    		st.insert(0,"-");
    	}
    return st.toString();
    }

    public int base() { return base; }
}