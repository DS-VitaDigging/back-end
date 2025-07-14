package com.example.VitaDigging.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeCalculator {
    public static String calculateKoreanAndInternationalAge(int birthYear, int birthMonth, int birthDay) {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);

        // 만 나이
        int internationalAge = Period.between(birthDate, today).getYears();

        // 한국식 나이: 현재 연도 - 출생연도 + 1
        int koreanAge = today.getYear() - birthYear + 1;

        return koreanAge + "세(만 " + internationalAge + "세)";
    }

    // 생년월일이 "1999-07-14" 형식으로 들어올 경우 처리
    public static String calculateFromBirthString(String birthDateStr) {
        LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return calculateKoreanAndInternationalAge(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
    }
}
