package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;

public interface SecurityClearanceRepository {
    SecurityClearance findById(int securityClearanceId);
}
