///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package za.gov.sars.config;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import org.hibernate.envers.AuditReader;
//import org.hibernate.envers.AuditReaderFactory;
//import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.inject.Produces;
//
///**
// *
// * @author S2026080
// */
//@ApplicationScoped
//public class AuditReaderProducer {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Produces
//    public AuditReader createAuditReader() {
//        return AuditReaderFactory.get(entityManager);
//    }
//}
