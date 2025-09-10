/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author S2026080
 */
public class CountryUtil {
    
      public static List<String> getDisplayCountryNames() {
        String[] locales = Locale.getISOCountries();
       // Locale.
        List<String> countryNames = new ArrayList<>();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryNames.add(obj.getDisplayCountry());
        }
        return countryNames;
    }
    
    public static Map<String, String> getCoutryCodes() {
        Map<String, String> countryCodes = new HashMap<>();
        String[] locales = Locale.getISOCountries();
        List<String> countryNames = new ArrayList<>();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryNames.add(obj.getDisplayCountry());
            countryCodes.put(obj.getDisplayCountry(), obj.getISO3Country());
        }
        return countryCodes;
    }
}
