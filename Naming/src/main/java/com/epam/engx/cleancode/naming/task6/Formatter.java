package com.epam.engx.cleancode.naming.task6;

public class Formatter {

    private static final String PLUS = "+";
    private static final String PIPE = "|";
    private static final String MINUS = "-";
    private static final String UNDERSCORE = " _ ";

    public static void main(String[] args) {
        System.out.println(formatKeyValue("enable", "true"));
        System.out.println(formatKeyValue("name", "Bob"));
    }

    private static String formatKeyValue(String key, String value) {
        String content = key + UNDERSCORE + value;
        String minuses = repeat(MINUS, content.length());
        String finalString = formateString(content, minuses);
        return finalString;
    }

	private static String formateString(String content, String minuses) {
		String finalString = PLUS +  minuses + PLUS + "\n"
        + PIPE + content + PIPE + "\n" +
        PLUS + minuses + PLUS + "\n";
		return finalString;
	}

    private static String repeat(String symbol, int times) {
        String result = "";
        for (int i = 0; i < times; i++)
            result += symbol;
        return result;
    }
}
