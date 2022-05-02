package learn.field_agent.data;

import learn.field_agent.models.Location;

public interface LocationRepository {
    Location findById(int locationId);

    Location add(Location location);

    boolean update(Location location);

    boolean deleteById(int locationId);
}
