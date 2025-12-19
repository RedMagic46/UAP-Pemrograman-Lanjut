package laundry.util;

public class CsvUtil {
    public static String[] parseLine(String line) {
        return line.split(",");
    }
    public static String escape(String value) {
        if (value == null) return "";
        return value.replace(",", ";").trim();
    }
}
