package org.dromio.client001.utility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyFormatter {

    public static String formatMoney(Double amount) {
        if (amount == null) {
            return "0.00 Tsh"; // Default value when amount is null
        }

        // Create a DecimalFormat with custom symbols for grouping and decimal separators
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator(','); // Set ',' as the grouping separator
        symbols.setDecimalSeparator('.');  // Set '.' as the decimal separator

        DecimalFormat format = new DecimalFormat("#,###.00", symbols);

        // Return the formatted string
        return format.format(amount) + " Tsh";
    }

}
