package model;

import java.util.function.Predicate;

public class Password {
	
	
	/*might be better to do form validation on client side*/
	
	private static final int LENGTH = 6;
	private static final int MIN_UPPER = 1;
	private static final int MIN_LOWER = 1;
	private static final int MIN_DIGITS = 1;
	
	public static final Predicate<String> lengthCheck = password -> password.length() >= LENGTH;
	
	public static Predicate<String> upperCaseCheck = password -> {
		int totalUpper = 0;
		for(char curr: password.toCharArray()) {
			if(Character.isUpperCase(curr)) totalUpper++;
			else if(totalUpper >= MIN_UPPER) return true;
		}
		
		return totalUpper >=MIN_UPPER;
	};
	
	
	public static Predicate<String> lowerCaseCheck = password -> {
		int totalLower = 0;
		for(char curr: password.toCharArray()) {
			if(Character.isLowerCase(curr)) totalLower++;
			else if(totalLower >= MIN_LOWER) return true;
		}
		
		return totalLower >=MIN_LOWER;
	};
	
	public static Predicate<String> caseCheck = password ->  lowerCaseCheck.test(password) && upperCaseCheck.test(password);
	
	
	public static Predicate<String> digitCheck = password -> {
		int min_digits = 0;
		for(char curr: password.toCharArray()) {
			if(Character.isDigit(curr)) min_digits++;
			else if(min_digits >= MIN_DIGITS) return true;
		}
		
		return min_digits >= MIN_DIGITS;
	};
	
	
	private Password() {}
	
	public static boolean validatePassword(String password) {
		return validatePassword(password,lengthCheck,caseCheck,digitCheck);
	}
	
	public static boolean validatePassword(String password,Predicate<String>... testConditions) {
		
		for(Predicate<String> condition: testConditions) {
			
			if(!condition.test(password)) return false;
		}
		
		return true;
	}
}
