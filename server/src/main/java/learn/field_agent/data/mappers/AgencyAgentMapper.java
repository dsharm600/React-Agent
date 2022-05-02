package learn.field_agent.data.mappers;

import learn.field_agent.models.AgencyAgent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgencyAgentMapper implements RowMapper<AgencyAgent> {

    @Override
    public AgencyAgent mapRow(ResultSet resultSet, int i) throws SQLException {

        AgencyAgent agencyAgent = new AgencyAgent();
        agencyAgent.setAgencyId(resultSet.getInt("agency_id"));
        agencyAgent.setIdentifier(resultSet.getString("identifier"));
        agencyAgent.setActivationDate(resultSet.getDate("activation_date").toLocalDate());
        agencyAgent.setActive(resultSet.getBoolean("is_active"));

        SecurityClearanceMapper securityClearanceMapper = new SecurityClearanceMapper();
        agencyAgent.setSecurityClearance(securityClearanceMapper.mapRow(resultSet, i));

        AgentMapper agentMapper = new AgentMapper();
        agencyAgent.setAgent(agentMapper.mapRow(resultSet, i));

        return agencyAgent;
    }
}
