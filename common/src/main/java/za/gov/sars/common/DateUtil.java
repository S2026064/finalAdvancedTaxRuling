/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author s2028398
 */
public class DateUtil {

    public Date formattedDate(String dateString) {
        SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder builder = new StringBuilder(dateString.substring(0, 4));
        builder.append("-");
        builder.append(dateString.substring(4, 6));
        builder.append("-");
        builder.append(dateString.substring(6, 8));
        try {
            return sdfSource.parse(builder.toString());
        } catch (ParseException ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String convertStringToDate(Date inputDateParam) {
        if (inputDateParam != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            return sdf.format(inputDateParam);
        } else {
            return "";
        }
    }

    public String convertStringToDateMmYyyy(Date inputDateParam) {
        if (inputDateParam != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
            return sdf.format(inputDateParam);
        } else {
            return "";
        }
    }
}
