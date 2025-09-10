/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.listener;

import org.hibernate.envers.RevisionListener;
import za.gov.sars.bridge.UserContextBridge;

import za.gov.sars.domain.audit.CustomRevisionEntity;

/**
 *
 * @author S2026080
 */
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;
        revision.setUsername(UserContextBridge.getCurrentUser());

    }

}
