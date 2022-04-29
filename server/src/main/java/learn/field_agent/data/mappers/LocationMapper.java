package learn.field_agent.data.mappers;

import learn.field_agent.models.Location;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationMapper implements RowMapper<Location> {

    @Override
    public Location mapRow(ResultSet resultSet, int i) throws SQLException {
        Location location = new Location();
        location.setLocationId(resultSet.getInt("location_id"));
        location.setName(resultSet.getString("name"));
        location.setAddress(resultSet.getString("address"));
        location.setCity(resultSet.getString("city"));
        location.setRegion(resultSet.getString("region"));
        location.setCountryCode(resultSet.getString("country_code"));
        location.setPostalCode(resultSet.getString("postal_code"));
        location.setAgencyId(resultSet.getInt("agency_id"));
        return location;
    }
}
