/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import za.gov.sars.common.ConnectionType;
import za.gov.sars.common.DatasourceFactory;
import za.gov.sars.common.DatasourceService;
import za.gov.sars.domain.User;

/**
 *
 * @author S2030707
 */
@Service
public class UserInformationService implements UserInformationServiceLocal {

    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet result = null;

    @Override
    public User getUserBySid(String sid, String userSid) {
        User user = null;
        connection = null;
        stmt = null;
        result = null;

        try {
            DatasourceService dataSourceService = DatasourceFactory.getDatabase(ConnectionType.EMPLOYEE_DATABASE);
            connection = dataSourceService.getDatasourceConnection();
            stmt = connection.prepareStatement("{call  dbo.GetEmployeeBySID_Vetting(?)}");
            stmt.setString(1, sid);
            result = stmt.executeQuery();
            while (result.next()) {
                if (result.getString("costCentreNumber") != null) {
                    user = new User();
                    user.setCreatedBy(userSid);
                    user.setCreatedDate(new Date());
                    user.setSid(sid);
                    user.setFullName(result.getString("fullName"));
                    user.setIdNumber(result.getString("IdNumber"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserInformationService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(UserInformationService.class.getName() + ":" + sid).log(Level.SEVERE, null, e);
            }
        }

        return user;
    }

}
