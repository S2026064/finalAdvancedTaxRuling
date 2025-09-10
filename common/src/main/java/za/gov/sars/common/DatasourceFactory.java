/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2028398
 */
public class DatasourceFactory {

    public static DatasourceService getDatabase(ConnectionType connectionType) {
        if (connectionType.equals(ConnectionType.EMPLOYEE_DATABASE)) {
            return new EmployeeDatasourceService();
        }
        return null;
    }
}
