package com.user.getweatherusingspeechapp.utils;

public class CommonUtilities {

    public String changeTemp(String x) {
        Double celsius = Double.parseDouble(x) - 273.0;
        Integer i = celsius.intValue();
        return String.valueOf(i);
    }
}
