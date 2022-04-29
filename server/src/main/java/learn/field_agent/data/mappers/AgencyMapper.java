package learn.field_agent.data.mappers;

import learn.field_agent.models.Agency;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgencyMapper implements RowMapper<Agency> {

    @Override
    public Agency mapRow(ResultSet resultSet, int i) throws SQLException {
        Agency agency = new Agency();
        agency.setAgencyId(resultSet.getInt("agency_id"));
        agency.setShortName(resultSet.getString("short_name"));
        agency.setLongName(resultSet.getString("long_name"));
        return agency;
    }
}
