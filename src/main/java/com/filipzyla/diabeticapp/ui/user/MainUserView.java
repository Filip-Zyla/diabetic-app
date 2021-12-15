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
import com.filipzyla.diabeticapp.ui.components.TopMenuBar;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.text.DecimalFormat;
import java.util.Optional;

@Route("home")
public class MainUserView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;

    public MainUserView(SugarService sugarService, InsulinService insulinService, SecurityService securityService, UserService userService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());

        add(new TopMenuBar(securityService), addLastMeasurements(), lowerButtons());
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
            DecimalFormat f = new DecimalFormat("0.#");

            H3 labelSugarMain = new H3("Last sugar");
            H5 labelSugar;
            H5 labelTypeSug;
            if (sugarOpt.get().getUnits() != userUnits) {
                Double s = sugarOpt.get().getSugar() * sugarOpt.get().getUnits().getConversion();
                labelSugar = new H5(f.format(s) + " " + userUnits.getMsg());
            }
            else {
                labelSugar = new H5(f.format(sugarOpt.get().getSugar()) + " " + sugarOpt.get().getUnits().getMsg());
            }
            labelTypeSug = new H5(sugarOpt.get().getType().getMsg());
            H5 labelTimeSug = new H5(sugarOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            Label labelNote = new Label(sugarOpt.get().getNote());
            layoutLastSugar.add(labelSugarMain, labelSugar, labelTypeSug, labelTimeSug, labelNote);
        }
        return layoutLastSugar;
    }

    private Component addLastInsulin() {
        VerticalLayout layoutLastInsulin = new VerticalLayout();
        layoutLastInsulin.setWidth("50%");
        layoutLastInsulin.setAlignItems(Alignment.CENTER);

        Optional<Insulin> insulinOpt = Optional.ofNullable(insulinService.findFirstByOrderByTimeAsc(user.getUserId()));
        if (insulinOpt.isPresent()) {
            H3 labelInsulinMain = new H3("Last insulin");
            H5 labelInsulin = new H5(insulinOpt.get().getInsulin().toString() + " units");
            H5 labelTypeIns = new H5(insulinOpt.get().getType().getMsg());
            H5 labelTimeIns = new H5(insulinOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            Label labelNote = new Label(insulinOpt.get().getNote());
            layoutLastInsulin.add(labelInsulinMain, labelInsulin, labelTypeIns, labelTimeIns, labelNote);
        }
        return layoutLastInsulin;
    }

    private Component lowerButtons() {
        Button buttonAddNewMeasurement = new Button("Add new", new Icon(VaadinIcon.PLUS), event -> UI.getCurrent().navigate("add"));
        Button buttonShowHistory = new Button("History", new Icon(VaadinIcon.ARCHIVE), event -> UI.getCurrent().navigate("history"));

        HorizontalLayout layoutLowerButtons = new HorizontalLayout();
        layoutLowerButtons.setWidthFull();
        layoutLowerButtons.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutLowerButtons.setAlignSelf(Alignment.STRETCH);
        layoutLowerButtons.add(buttonAddNewMeasurement, buttonShowHistory);

        return layoutLowerButtons;
    }
}