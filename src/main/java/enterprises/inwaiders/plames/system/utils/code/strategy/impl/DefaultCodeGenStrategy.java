package enterprises.inwaiders.plames.system.utils.code.strategy.impl;

import java.util.Random;

import enterprises.inwaiders.plames.system.utils.code.strategy.CodeGenStrategy;

public class DefaultCodeGenStrategy implements CodeGenStrategy {

	private String[] alphabet = null;
	private String[] numbers = null;
	
	private int bigSize = 3;
	private int smallSize = 2;
	
	private Random random = new Random();
	
	public DefaultCodeGenStrategy() {
		
		alphabet = new String[] {"a", "b", "c", "d", "e", "f", "k", "p", "t", "x", "z", "y", "r", "w", "q"};
		numbers = new String[] {"2", "3", "4", "5", "6", "7", "8", "9"};
	}
	
	public String gen() {
		
		String numbersPart = "";
		String alphabetPart = "";
		
		boolean numbersIsBig = random.nextBoolean();
		
		for(int i = 0; i < (numbersIsBig ? bigSize : smallSize); i++) {
			
			numbersPart += numbers[random.nextInt(numbers.length)];
		}
					
		for(int i = 0; i < (!numbersIsBig ? bigSize : smallSize); i++) {
			
			alphabetPart += alphabet[random.nextInt(alphabet.length)];
		}
		
		boolean firstIsNumbers = random.nextBoolean();
		
		return firstIsNumbers ? numbersPart+alphabetPart : alphabetPart+numbersPart;
	}
	
	public long getCombinationsCount() {
		
		long ifNumbersSmall = (long) (Math.pow(numbers.length, smallSize)*2 + Math.pow(alphabet.length, bigSize)*2);
		long ifNumbersBig = (long) (Math.pow(numbers.length, bigSize)*2 + Math.pow(alphabet.length, smallSize)*2);
		
		return ifNumbersSmall+ifNumbersBig;
	}
	
	public void setSmallSize(int size) {
		
		this.smallSize = size;
	}
	
	public int getSmallSize() {
		
		return this.smallSize;
	}

	public void setBigSize(int size) {
		
		this.bigSize = size;
	}
	
	public int getBigSize() {
		
		return this.bigSize;
	}
	
	public void setNumbers(String[] numbers) {
		
		this.numbers = numbers;
	}
	
	public String[] getNumbers() {
		
		return this.numbers;
	}
	
	public void setAlphabet(String[] alphabet) {
		
		this.alphabet = alphabet;
	}
	
	public String[] getAlphabet() {
		
		return this.alphabet;
	}
}
