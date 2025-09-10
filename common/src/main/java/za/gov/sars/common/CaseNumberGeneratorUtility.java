package za.gov.sars.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author S2024726
 */
public class CaseNumberGeneratorUtility {

    public static String generate(Long referenceId, Date createdDate, String branchOfficeCode) {
        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String currentYear = getYearFormat.format(createdDate);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH); // January = 0
        String formattedMonth = String.format("%02d", month + 1);
        StringBuilder builder = new StringBuilder(branchOfficeCode);
        builder.append(currentYear);
        builder.append(formattedMonth);
        builder.append(referenceId);
        return builder.toString();
    }

}
