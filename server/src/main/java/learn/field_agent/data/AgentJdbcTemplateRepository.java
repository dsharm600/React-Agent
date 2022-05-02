package learn.field_agent.data;

import learn.field_agent.data.mappers.AgentAgencyMapper;
import learn.field_agent.data.mappers.AgentMapper;
import learn.field_agent.models.Agent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AgentJdbcTemplateRepository implements AgentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AgentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Agent> findAll() {
        final String sql = "select agent_id, first_name, middle_name, last_name, dob, height_in_inches "
                + "from agent limit 1000;";
        return jdbcTemplate.query(sql, new AgentMapper());
    }

    @Override
    @Transactional
    public Agent findById(int agentId) {

        final String sql = "select agent_id, first_name, middle_name, last_name, dob, height_in_inches "
                + "from agent "
                + "where agent_id = ?;";

        Agent agent = jdbcTemplate.query(sql, new AgentMapper(), agentId).stream()
                .findFirst().orElse(null);

        if (agent != null) {
            addAgencies(agent);
        }

        return agent;
    }

    @Override
    public Agent add(Agent agent) {

        final String sql = "insert into agent (first_name, middle_name, last_name, dob, height_in_inches) "
                + " values (?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, agent.getFirstName());
            ps.setString(2, agent.getMiddleName());
            ps.setString(3, agent.getLastName());
            ps.setDate(4, agent.getDob() == null ? null : Date.valueOf(agent.getDob()));
            ps.setInt(5, agent.getHeightInInches());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        agent.setAgentId(keyHolder.getKey().intValue());
        return agent;
    }

    @Override
    public boolean update(Agent agent) {

        final String sql = "update agent set "
                + "first_name = ?, "
                + "middle_name = ?, "
                + "last_name = ?, "
                + "dob = ?, "
                + "height_in_inches = ? "
                + "where agent_id = ?;";

        return jdbcTemplate.update(sql,
                agent.getFirstName(),
                agent.getMiddleName(),
                agent.getLastName(),
                agent.getDob(),
                agent.getHeightInInches(),
                agent.getAgentId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int agentId) {
        jdbcTemplate.update("delete from agency_agent where agent_id = ?;", agentId);
        return jdbcTemplate.update("delete from agent where agent_id = ?;", agentId) > 0;
    }

    private void addAgencies(Agent agent) {

        final String sql = "select aa.agency_id, aa.agent_id, aa.identifier, aa.activation_date, aa.is_active, "
                + "sc.security_clearance_id, sc.name security_clearance_name, "
                + "a.short_name, a.long_name "
                + "from agency_agent aa "
                + "inner join agency a on aa.agency_id = a.agency_id "
                + "inner join security_clearance sc on aa.security_clearance_id = sc.security_clearance_id "
                + "where aa.agent_id = ?;";

        var agentAgencies = jdbcTemplate.query(sql, new AgentAgencyMapper(), agent.getAgentId());
        agent.setAgencies(agentAgencies);
    }
}
