package org.sidis.lendings.lendingmanagement.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LendingConfiguration {

    @Value("${lending.maxDaysWithoutFine}")
    private int maxDaysWithoutFine;

    @Value("${lending.finePerDay}")
    private int finePerDay;

    public int getMaxDaysWithoutFine() {
        return maxDaysWithoutFine;
    }

    public int getFinePerDay() {
        return finePerDay;
    }
}
