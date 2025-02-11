package me.gustavo.ghautosell.utils;

import java.text.DecimalFormat;

public class FormatNumbers {

    public static String format(double number) {
        if (number < 1000) {
            return String.valueOf(number);
        }

        String[] suffixes = {"", "k", "m", "b", "t", "q", "qq", "s", "sp"};
        int magnitude = (int) Math.log10(number);
        int suffixIndex = magnitude / 3;

        double shortenedNumber = number / Math.pow(1000, suffixIndex);

        DecimalFormat df = new DecimalFormat("#0.##");

        return df.format(shortenedNumber) + suffixes[suffixIndex];
    }
}
