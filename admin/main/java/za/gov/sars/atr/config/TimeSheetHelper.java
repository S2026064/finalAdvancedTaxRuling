/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb.util;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import za.gov.sars.domain.Activity;

/**
 *
 * @author S2028398
 */
@Getter
@Setter
public class TimeSheetHelper {

    private String refNumber;
    private String companyName;
    private Date date;
    private double hours;
    private String resource;
    private Activity activity;
    private String activityStatus;
    private String activitytype;
    private String description;
}
