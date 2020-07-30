package com.epam.engx.cleancode.comments.task1;

import com.epam.engx.cleancode.comments.task1.thirdpartyjar.InvalidInputException;

public class MortgageInstallmentCalculator {

	public static double calculateMonthlyPayment(
            int principleAmount, int termOfMontageInYears, double rateOfInterest) {

        if (principleAmount < 0 ||  termOfMontageInYears<= 0 || rateOfInterest < 0) {
            throw new InvalidInputException("Negative values are not allowed");
        }
        
        rateOfInterest /= 100.0;
        
        double termOfMontageInMonths = termOfMontageInYears * 12;
        
        if(rateOfInterest==0)
            return  principleAmount/termOfMontageInMonths;

        double monthlyRate = rateOfInterest / 12.0;

        double monthlyPayment = (principleAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termOfMontageInMonths));

        return monthlyPayment;
    }


}
