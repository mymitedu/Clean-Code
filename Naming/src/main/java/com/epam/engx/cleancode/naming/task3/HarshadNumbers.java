package com.epam.engx.cleancode.naming.task3;

public class HarshadNumbers {

	public static void main(String[] args) {
		long limitOfHarshedNumbers = 1000; 
		for (int i = 1; i <= limitOfHarshedNumbers; i++) {
			if (i %sum(i) == 0) {
				System.out.println(i);
			}
		}
	}

	private static int sum(int number) {
		int sumOfDigits = 0;
		while (number != 0) {
			sumOfDigits += number % 10;
            number = number / 10;
        }
		return sumOfDigits;
	}

}
