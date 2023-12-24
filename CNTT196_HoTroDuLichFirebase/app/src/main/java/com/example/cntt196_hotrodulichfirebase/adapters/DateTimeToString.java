package com.example.cntt196_hotrodulichfirebase.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeToString {
    public static String Format(LocalDateTime localDateTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        return  formattedDateTime;
    }
    public static String GenarateID(String IdNguoiDung)
    {
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = localDateTime.format(formatter);
        return  IdNguoiDung + formattedDateTime;
    }
    public static String GenarateID()
    {
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = localDateTime.format(formatter);
        return  formattedDateTime;
    }
    public static String FormatVND(double ChiPhi)
    {
        NumberFormat numberFormat = DecimalFormat.getCurrencyInstance( new Locale("vi", "VN"));
        return numberFormat.format(ChiPhi / 1.0);
    }
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@gmail.com$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    public static boolean isNoneNumber(String strInput)
    {
        String strRegex = ".*\\d.*";

        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(strInput);
        return matcher.matches();
    }
}
