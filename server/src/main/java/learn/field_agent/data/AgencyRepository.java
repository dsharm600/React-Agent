package learn.field_agent.data;

import learn.field_agent.models.Agency;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AgencyRepository {
    List<Agency> findAll();

    @Transactional
    Agency findById(int agencyId);

    Agency add(Agency agency);

    boolean update(Agency agency);

    @Transactional
    boolean deleteById(int agencyId);
}
