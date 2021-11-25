package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.enums.InsulinType;
import com.filipzyla.diabeticapp.backend.enums.MeasurementType;
import com.filipzyla.diabeticapp.backend.enums.SugarType;
import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import com.filipzyla.diabeticapp.backend.models.Insulin;
import com.filipzyla.diabeticapp.backend.models.Sugar;
import com.filipzyla.diabeticapp.backend.service.InsulinService;
import com.filipzyla.diabeticapp.backend.service.SugarService;
import com.filipzyla.diabeticapp.ui.components.TopMenuBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.time.LocalDateTime;

@Route("add")
public class AddMeasurementsView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final ComboBox<MeasurementType> comboBoxMeasurementType;
    private final NumberField numFieldSugar;
    private final ComboBox<SugarType> comboBoxSugarType;
    private final ComboBox<SugarUnits> comboBoxSugarUnitsType;
    private final DateTimePicker dateTimePicker;
    private final Button buttonCommitSugar;
    private final NumberField numFieldInsulin;
    private final ComboBox<InsulinType> comboBoxInsulinType;
    private final Button buttonCommitInsulin;

    public AddMeasurementsView(SugarService sugarService, InsulinService insulinService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;
        
        comboBoxMeasurementType = new ComboBox("What do you want to add?");
        comboBoxMeasurementType.setItems(MeasurementType.values());

        dateTimePicker = new DateTimePicker("Time");

        numFieldSugar = new NumberField("Sugar");
        comboBoxSugarType = new ComboBox("Type");
        comboBoxSugarUnitsType = new ComboBox("Units");
        buttonCommitSugar = new Button("Save", save -> saveSugar());

        numFieldInsulin = new NumberField("Insulin");
        comboBoxInsulinType = new ComboBox("Type");
        buttonCommitInsulin = new Button("Save", save -> saveInsulin());

        comboBoxMeasurementType.addValueChangeListener(event -> {
            remove(numFieldSugar, comboBoxSugarType, comboBoxSugarUnitsType, dateTimePicker,
                    buttonCommitSugar, numFieldInsulin, comboBoxInsulinType, buttonCommitInsulin);
            if (event.getValue() == MeasurementType.SUGAR) {
                numFieldSugar.setStep(1);
                comboBoxSugarType.setItems(SugarType.values());
                comboBoxSugarType.setItemLabelGenerator(SugarType::getMsg);
                comboBoxSugarUnitsType.setItems(SugarUnits.values());
                comboBoxSugarUnitsType.setItemLabelGenerator(SugarUnits::getMsg);
                dateTimePicker.setStep(Duration.ofMinutes(1));
                dateTimePicker.setValue(LocalDateTime.now());

                add(numFieldSugar, comboBoxSugarUnitsType, comboBoxSugarType, dateTimePicker, buttonCommitSugar);
            }
            else if (event.getValue() == MeasurementType.INSULIN) {
                numFieldInsulin.setStep(1);
                comboBoxInsulinType.setItems(InsulinType.values());
                comboBoxInsulinType.setItemLabelGenerator(InsulinType::getMsg);
                dateTimePicker.setStep(Duration.ofMinutes(1));
                dateTimePicker.setValue(LocalDateTime.now());

                add(numFieldInsulin, comboBoxInsulinType, dateTimePicker, buttonCommitInsulin);
            }
        });

        setAlignItems(Alignment.CENTER);
        add(new TopMenuBar(), comboBoxMeasurementType);


    }

    private void saveSugar() {
        Sugar sugar = new Sugar(numFieldSugar.getValue(), comboBoxSugarType.getValue(), comboBoxSugarUnitsType.getValue(), dateTimePicker.getValue());
        sugarService.save(sugar);
        remove(numFieldSugar, comboBoxSugarUnitsType, comboBoxSugarType, dateTimePicker, buttonCommitSugar);
        comboBoxMeasurementType.setValue(null);
        Notification.show("Saved").setPosition(Notification.Position.MIDDLE);
    }

    //TODO set to nulls fields
    private void saveInsulin() {
        Insulin insulin = new Insulin(numFieldInsulin.getValue().intValue(), comboBoxInsulinType.getValue(), dateTimePicker.getValue());
        insulinService.save(insulin);
        remove(numFieldInsulin, comboBoxInsulinType, dateTimePicker, buttonCommitInsulin);
        comboBoxMeasurementType.setValue(null);
        Notification.show("Saved").setPosition(Notification.Position.MIDDLE);
    }
}