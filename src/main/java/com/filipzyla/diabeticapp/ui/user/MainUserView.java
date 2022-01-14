package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import com.filipzyla.diabeticapp.backend.models.Insulin;
import com.filipzyla.diabeticapp.backend.models.Sugar;
import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.backend.service.InsulinService;
import com.filipzyla.diabeticapp.backend.service.SugarService;
import com.filipzyla.diabeticapp.backend.service.UserService;
import com.filipzyla.diabeticapp.backend.utility.CustomDateTimeFormatter;
import com.filipzyla.diabeticapp.ui.utility.TopMenuBar;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.ResourceBundle;

@Route("home")
public class MainUserView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;

    public final ResourceBundle langResources;

    public MainUserView(SugarService sugarService, InsulinService insulinService, SecurityService securityService, UserService userService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");

        final Button button = new Button(langResources.getString("add_new"), new Icon(VaadinIcon.PLUS), event -> UI.getCurrent().navigate("add"));

        setAlignItems(Alignment.CENTER);
        add(new TopMenuBar(securityService), addLastMeasurements(), button);
    }

    private Component addLastMeasurements() {
        HorizontalLayout layoutLastMeasurements = new HorizontalLayout();
        layoutLastMeasurements.setSizeFull();
        layoutLastMeasurements.add(addLastSugar(), addLastInsulin());
        return layoutLastMeasurements;
    }

    private Component addLastSugar() {
        VerticalLayout layoutLastSugar = new VerticalLayout();
        layoutLastSugar.setWidth("50%");
        layoutLastSugar.setAlignItems(Alignment.CENTER);

        Optional<Sugar> sugarOpt = Optional.ofNullable(sugarService.findFirstByOrderByTimeAsc(user.getUserId()));
        if (sugarOpt.isPresent()) {
            SugarUnits userUnits = user.getUnits();

            H3 labelSugarMain = new H3(langResources.getString("last_sugar"));
            H5 labelSugar = new H5(sugarOpt.get().getSugar() + " " + userUnits.getMsg());
            H5 labelTypeSug = new H5(langResources.getString(sugarOpt.get().getType().getMsg()));
            H5 labelTimeSug = new H5(sugarOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            layoutLastSugar.add(labelSugarMain, labelSugar, labelTypeSug, labelTimeSug);
        }
        return layoutLastSugar;
    }

    private Component addLastInsulin() {
        VerticalLayout layoutLastInsulin = new VerticalLayout();
        layoutLastInsulin.setWidth("50%");
        layoutLastInsulin.setAlignItems(Alignment.CENTER);

        Optional<Insulin> insulinOpt = Optional.ofNullable(insulinService.findFirstByOrderByTimeAsc(user.getUserId()));
        if (insulinOpt.isPresent()) {
            H3 labelInsulinMain = new H3(langResources.getString("last_insulin"));
            H5 labelInsulin = new H5(insulinOpt.get().getInsulin().toString() + " " + langResources.getString("units").toLowerCase());
            H5 labelTypeIns = new H5(langResources.getString(insulinOpt.get().getType().getMsg()));
            H5 labelTimeIns = new H5(insulinOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            layoutLastInsulin.add(labelInsulinMain, labelInsulin, labelTypeIns, labelTimeIns);
        }
        return layoutLastInsulin;
    }
}