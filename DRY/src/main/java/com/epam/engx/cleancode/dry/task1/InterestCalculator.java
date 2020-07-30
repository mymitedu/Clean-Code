package com.epam.engx.cleancode.dry.task1;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InterestCalculator {

    private static final int AGE = 60;
    private static final double ANNUAL_INTEREST_RATE_FOR_MIDDLE_AGE = 4.5d;
    private static final double ANNUAL_INTEREST_RATE_FOR_SENIOR_AGE = 5.5d;
    private static final int BONUS_AGE = 13;


    public BigDecimal calculateInterest(AccountDetails accountDetails) {
        if (isAccountStartedAfterBonusAge(accountDetails)) {
            return interest(accountDetails);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private boolean isAccountStartedAfterBonusAge(AccountDetails accountDetails) {
        return isDurationBetweenDatesInYearsGreaterThanBonusAge(accountDetails);
    }

	private boolean isDurationBetweenDatesInYearsGreaterThanBonusAge(AccountDetails accountDetails) {
		return durationBetweenDatesInYears(accountDetails.getBirth(), accountDetails.getStartDate()) > BONUS_AGE;
	}

    private int durationBetweenDatesInYears(Date from, Date to) {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(from);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(to);

        return getDifferenceInYears(startCalendar, endCalendar);
    }

	private int getDifferenceInYears(Calendar startCalendar, Calendar endCalendar) {
		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        if (endCalendar.get(Calendar.DAY_OF_YEAR) < startCalendar.get(Calendar.DAY_OF_YEAR))
            return diffYear - 1;
        return diffYear;
	}

    private BigDecimal interest(AccountDetails accountDetails) {
        double interest = 0;
        if (isAccountStartedAfterBonusAge(accountDetails)) {
            interest = interestForSeniorAndMiddleAge(accountDetails);
        }
        return BigDecimal.valueOf(interest);
    }

	private double interestForSeniorAndMiddleAge(AccountDetails accountDetails) {
		double interest;
		if (AGE <= accountDetails.getAge()) {             
		    interest = getPrincipleAmount(accountDetails)
		            * getDurationInYears(accountDetails) * ANNUAL_INTEREST_RATE_FOR_SENIOR_AGE / 100;
		} else {
		    interest = getPrincipleAmount(accountDetails)
		            * getDurationInYears(accountDetails) * ANNUAL_INTEREST_RATE_FOR_MIDDLE_AGE / 100;
		}
		return interest;
	}

	private int getDurationInYears(AccountDetails accountDetails) {
		return durationSinceStartDateInYears(accountDetails.getStartDate());
	}

	private double getPrincipleAmount(AccountDetails accountDetails) {
		return accountDetails.getBalance().doubleValue();
	}

    private int durationSinceStartDateInYears(Date startDate) {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(new Date());

        return getDifferenceInYears(startCalendar, endCalendar);

    }
}
