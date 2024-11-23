package org.dromio.client001.models.repository;

import org.dromio.client001.models.data.Setting;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends ListCrudRepository<Setting, String> {

    Setting findByName(String name);

}
