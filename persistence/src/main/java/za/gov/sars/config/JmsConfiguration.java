/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 *
 * @author S2024726
 */
//Please remember to change spring-jms-context-local.xml to spring-jms-context.xml
@Configuration
@ImportResource("classpath*:spring-jms-context-local.xml")
public class JmsConfiguration {
    
}
