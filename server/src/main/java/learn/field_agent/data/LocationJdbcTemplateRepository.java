package learn.field_agent.data;

import learn.field_agent.data.mappers.LocationMapper;
import learn.field_agent.models.Location;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class LocationJdbcTemplateRepository implements LocationRepository {

    private final JdbcTemplate jdbcTemplate;

    public LocationJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Location findById(int locationId) {

        final String sql = "select location_id, name, address, city, region, country_code, postal_code, agency_id "
                + "from location "
                + "where location_id = ?;";

        return jdbcTemplate.query(sql, new LocationMapper(), locationId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Location add(Location location) {

        final String sql = "insert into location (name, address, city, region, country_code, postal_code, agency_id)"
                + "values (?,?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, location.getName());
            ps.setString(2, location.getAddress());
            ps.setString(3, location.getCity());
            ps.setString(4, location.getRegion());
            ps.setString(5, location.getCountryCode());
            ps.setString(6, location.getPostalCode());
            ps.setInt(7, location.getAgencyId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        location.setLocationId(keyHolder.getKey().intValue());
        return location;
    }

    @Override
    public boolean update(Location location) {

        // don't allow agency_id updates for now
        final String sql = "update location set "
                + "name = ?, "
                + "address = ?, "
                + "city = ?, "
                + "region = ?, "
                + "country_code = ?, "
                + "postal_code = ? "
                + "where location_id = ?;";

        return jdbcTemplate.update(sql,
                location.getName(),
                location.getAddress(),
                location.getCity(),
                location.getRegion(),
                location.getCountryCode(),
                location.getPostalCode(),
                location.getLocationId()) > 0;
    }

    @Override
    public boolean deleteById(int locationId) {
        return jdbcTemplate.update(
                "delete from location where location_id = ?", locationId) > 0;
    }
}
