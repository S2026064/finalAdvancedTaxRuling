/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb.util;

import lombok.Getter;
import lombok.Setter;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.TimeSheetActivity;

/**
 *
 * @author S2028398
 */
@Getter
@Setter
public class ProgressTrackingDaysHelper {

    private ATRApplication aTRApplication;
    private int numDays;
    private TimeSheetActivity timeSheetActivity;

}
