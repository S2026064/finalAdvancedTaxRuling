/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import za.gov.sars.domain.User;

/**
 *
 * @author S2030707
 */
public interface UserInformationServiceLocal {
    
    public User getUserBySid(String sid, String userSid);
    
}
