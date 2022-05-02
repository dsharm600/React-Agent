package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.AgencyRepository;
import learn.field_agent.models.Agency;
import learn.field_agent.models.AgencyAgent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final AgencyAgentRepository agencyAgentRepository;

    public AgencyService(AgencyRepository agencyRepository, AgencyAgentRepository agencyAgentRepository) {
        this.agencyRepository = agencyRepository;
        this.agencyAgentRepository = agencyAgentRepository;
    }

    public List<Agency> findAll() {
        return agencyRepository.findAll();
    }

    public Agency findById(int agencyId) {
        return agencyRepository.findById(agencyId);
    }

    public Result<Agency> add(Agency agency) {
        Result<Agency> result = validate(agency);
        if (!result.isSuccess()) {
            return result;
        }

        if (agency.getAgencyId() != 0) {
            result.addMessage("agencyId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        agency = agencyRepository.add(agency);
        result.setPayload(agency);
        return result;
    }

    public Result<Agency> update(Agency agency) {
        Result<Agency> result = validate(agency);
        if (!result.isSuccess()) {
            return result;
        }

        if (agency.getAgencyId() <= 0) {
            result.addMessage("agencyId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!agencyRepository.update(agency)) {
            String msg = String.format("agencyId: %s, not found", agency.getAgencyId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int agencyId) {
        return agencyRepository.deleteById(agencyId);
    }

    public Result<Void> addAgent(AgencyAgent agencyAgent) {
        Result<Void> result = validate(agencyAgent);
        if (!result.isSuccess()) {
            return result;
        }

        if (!agencyAgentRepository.add(agencyAgent)) {
            result.addMessage("agent not added", ResultType.INVALID);
        }

        return result;
    }

    public Result<Void> updateAgent(AgencyAgent agencyAgent) {
        Result<Void> result = validate(agencyAgent);
        if (!result.isSuccess()) {
            return result;
        }

        if (!agencyAgentRepository.update(agencyAgent)) {
            String msg = String.format("update failed for agency id %s, agent id %s: not found",
                    agencyAgent.getAgencyId(),
                    agencyAgent.getAgent().getAgentId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteAgentByKey(int agencyId, int agentId) {
        return agencyAgentRepository.deleteByKey(agencyId, agentId);
    }

    private Result<Agency> validate(Agency agency) {
        Result<Agency> result = new Result<>();
        if (agency == null) {
            result.addMessage("agency cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(agency.getShortName())) {
            result.addMessage("shortName is required", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(agency.getLongName())) {
            result.addMessage("longName is required", ResultType.INVALID);
        }

        return result;
    }

    private Result<Void> validate(AgencyAgent agencyAgent) {
        Result<Void> result = new Result<>();
        if (agencyAgent == null) {
            result.addMessage("agencyAgent cannot be null", ResultType.INVALID);
            return result;
        }

        if (agencyAgent.getAgent() == null) {
            result.addMessage("agent cannot be null", ResultType.INVALID);
        }

        if (agencyAgent.getSecurityClearance() == null) {
            result.addMessage("securityClearance cannot be null", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(agencyAgent.getIdentifier())) {
            result.addMessage("identifier is required", ResultType.INVALID);
        }

        if (agencyAgent.getActivationDate() == null) {
            result.addMessage("activationDate is required", ResultType.INVALID);
        }

        return result;
    }
}
