/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;

/**
 *
 * @author S2026095
 */
@DiscriminatorValue("person")
@MappedSuperclass
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Person extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_names")
    private String fullName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "cellphone_num")
    private String cellphone;

    @Column(name = "telephone_num")
    private String telephone;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "income_tax_number")
    private String incomeTaxNum;

    @Column(name = "passport_number")
    private String passPortNumber;

    @Column(name = "personnel_num")
    private String personnelNumber;

    @Column(name = "login_user_session_ind")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean loginUserSessionIndicator = Boolean.FALSE;

    @Column(name = "country_code")
    private String countryCode;

    public Person(String firstName, String lastName, String fullName, String emailAddress, String cellphone, String telephone, String idNumber, String incomeTaxNum, String passPortNumber, String personnelNumber, String countryCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.cellphone = cellphone;
        this.telephone = telephone;
        this.idNumber = idNumber;
        this.incomeTaxNum = incomeTaxNum;
        this.passPortNumber = passPortNumber;
        this.personnelNumber = personnelNumber;
        this.countryCode = countryCode;
    }

   
    public Person() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIncomeTaxNum() {
        return incomeTaxNum;
    }

    public void setIncomeTaxNum(String incomeTaxNum) {
        this.incomeTaxNum = incomeTaxNum;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPassPortNumber() {
        return passPortNumber;
    }

    public void setPassPortNumber(String passPortNumber) {
        this.passPortNumber = passPortNumber;
    }

    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public Boolean getLoginUserSessionIndicator() {
        return loginUserSessionIndicator;
    }

    public void setLoginUserSessionIndicator(Boolean loginUserSessionIndicator) {
        this.loginUserSessionIndicator = loginUserSessionIndicator;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
