package org.dromio.client001.models.service;

import org.dromio.client001.models.data.Setting;
import org.dromio.client001.models.repository.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SettingService {

    private static final Logger log = LoggerFactory.getLogger(SettingService.class);
    private final SettingsRepository settingsRepository;

    public SettingService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public boolean isEnabled(String settingName) {
        // if no such setting is found, return true
        Setting setting = settingsRepository.findByName(settingName);
        if (setting == null) {
            log.error("Failed to fetch Setting with name {} not found", settingName);
            return false;
        }
        return setting.isEnabled();
    }

    public void setEnabled(String settingName) {
        Setting setting = settingsRepository.findByName(settingName);
        if (setting == null) {
            log.error("Failed to enable Setting with name {} not found", settingName);
        }
        else {
            setting.setEnabled(true);
        }
    }

    public void setDisabled(String settingName) {
        Setting setting = settingsRepository.findByName(settingName);
        if (setting == null) {
            log.error("Failed to disable Setting with name {} not found", settingName);
        }
        else {
            setting.setEnabled(false);
        }
    }

    public List<Setting> getAllSettings() {
        List<Setting> settings = settingsRepository.findAll();
        if (settings.isEmpty()) {
            return Collections.emptyList();
        }
        return settings;
    }

    public void addSetting(String settingName, boolean defaultValue) {
        Setting newSetting = new Setting(null, settingName, defaultValue);
        settingsRepository.save(newSetting);
    }

}
