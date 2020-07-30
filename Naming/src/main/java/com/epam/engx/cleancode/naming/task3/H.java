package com.epam.engx.cleancode.naming.task3;

public class HarshadNumbersWithinLimit {

	public static void main(String[] args) {
		long const LIMIT = 1000;
		for (int digit = 1; digit <= LIMIT; digit++) {
			if (digit % summationOfDigits(digit) == 0) {
				System.out.println(digit);
			}
		}
	}

	private static int summationOfDigits(int digit) {
		int sum = 0;
		while (digit != 0) {
            sum += digit % 10;
            digit = digit / 10;
        }
		return sum;
	}

}
