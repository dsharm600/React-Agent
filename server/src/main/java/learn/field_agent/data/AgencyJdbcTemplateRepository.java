package learn.field_agent.data;

import learn.field_agent.data.mappers.AgencyAgentMapper;
import learn.field_agent.data.mappers.AgencyMapper;
import learn.field_agent.data.mappers.LocationMapper;
import learn.field_agent.models.Agency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AgencyJdbcTemplateRepository implements AgencyRepository {

    private final JdbcTemplate jdbcTemplate;

    public AgencyJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Agency> findAll() {
        // limit until we develop a paging solution
        final String sql = "select agency_id, short_name, long_name from agency limit 1000;";
        return jdbcTemplate.query(sql, new AgencyMapper());
    }

    @Override
    @Transactional
    public Agency findById(int agencyId) {

        final String sql = "select agency_id, short_name, long_name "
                + "from agency "
                + "where agency_id = ?;";

        Agency result = jdbcTemplate.query(sql, new AgencyMapper(), agencyId).stream()
                .findAny().orElse(null);

        if (result != null) {
            addLocations(result);
            addAgents(result);
        }

        return result;
    }

    @Override
    public Agency add(Agency agency) {

        final String sql = "insert into agency (short_name, long_name) values (?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, agency.getShortName());
            ps.setString(2, agency.getLongName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        agency.setAgencyId(keyHolder.getKey().intValue());
        return agency;
    }

    @Override
    public boolean update(Agency agency) {

        final String sql = "update agency set "
                + "short_name = ?, "
                + "long_name = ? "
                + "where agency_id = ?";

        return jdbcTemplate.update(sql, agency.getShortName(), agency.getLongName(), agency.getAgencyId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int agencyId) {
        jdbcTemplate.update("delete from location where agency_id = ?", agencyId);
        jdbcTemplate.update("delete from agency_agent where agency_id = ?", agencyId);
        return jdbcTemplate.update("delete from agency where agency_id = ?", agencyId) > 0;
    }

    private void addLocations(Agency agency) {

        final String sql = "select location_id, name, address, city, region, "
                + "country_code, postal_code, agency_id "
                + "from location "
                + "where agency_id = ?";

        var locations = jdbcTemplate.query(sql, new LocationMapper(), agency.getAgencyId());
        agency.setLocations(locations);
    }

    private void addAgents(Agency agency) {

        final String sql = "select aa.agency_id, aa.agent_id, aa.identifier, aa.activation_date, aa.is_active, "
                + "sc.security_clearance_id, sc.name security_clearance_name, "
                + "a.first_name, a.middle_name, a.last_name, a.dob, a.height_in_inches "
                + "from agency_agent aa "
                + "inner join agent a on aa.agent_id = a.agent_id "
                + "inner join security_clearance sc on aa.security_clearance_id = sc.security_clearance_id "
                + "where aa.agency_id = ?";

        var agencyAgents = jdbcTemplate.query(sql, new AgencyAgentMapper(), agency.getAgencyId());
        agency.setAgents(agencyAgents);
    }

}
