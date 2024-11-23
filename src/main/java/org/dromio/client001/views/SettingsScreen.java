package org.dromio.client001.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.dromio.client001.models.service.SettingService;

@PageTitle("Settings")
@Route("/settings")
@Menu( title = "App Settings")
public class SettingsScreen extends VerticalLayout {

    private final SettingService settingService;
    HorizontalLayout individualSetting;

    public SettingsScreen(SettingService settingService) {
        this.settingService = settingService;

        initSettingsTogglers();

        add(

        );
    }

    private void initSettingsTogglers() {
        individualSetting = new HorizontalLayout();
        var settingName_ = "Receive Item";
        Text settingName = new Text(settingName_);

        Button settingToggler = new Button();

        if (settingService.isEnabled(settingName_)) {
            settingToggler.setText("on");
        }
        else {
            settingToggler.setText("off");
        }

        settingToggler.addClickListener( event -> {
            if ( !settingService.isEnabled(settingName_) ) {
                settingService.setEnabled(settingName_);
                settingToggler.setText("on");
            }
            else {
                settingService.setDisabled(settingName_);
                settingToggler.setText("off");
            }
        });

        individualSetting.add(
                settingName,
                settingToggler
        );
    }

}
