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
import com.vaadin.flow.component.Component;
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
    private final VerticalLayout insulinLayout = new VerticalLayout();
    private final VerticalLayout sugarLayout = new VerticalLayout();

    public AddMeasurementsView(SugarService sugarService, InsulinService insulinService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        ComboBox<MeasurementType> comboBoxMeasurementType = new ComboBox("What do you want to add?");
        comboBoxMeasurementType.setItems(MeasurementType.values());

        comboBoxMeasurementType.addValueChangeListener(event -> {
            insulinLayout.removeAll();
            sugarLayout.removeAll();
            remove(insulinLayout, sugarLayout);
            if (event.getValue() == MeasurementType.SUGAR)
                add(addSugarLayout());
            else if (event.getValue() == MeasurementType.INSULIN)
                add(addInsulinLayout());
        });

        setAlignItems(Alignment.CENTER);
        add(new TopMenuBar(), comboBoxMeasurementType);
    }

    private Component addSugarLayout() {
        NumberField numField = new NumberField("Sugar");
        numField.setStep(0.1);
        ComboBox<SugarUnits> comboBoxUnits = new ComboBox("Units");
        comboBoxUnits.setItems(SugarUnits.values());
        comboBoxUnits.setItemLabelGenerator(SugarUnits::getMsg);
        ComboBox<SugarType> comboBoxType = new ComboBox("Type");
        comboBoxType.setItems(SugarType.values());
        comboBoxType.setItemLabelGenerator(SugarType::getMsg);
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setStep(Duration.ofMinutes(1));

        Button buttonSave = new Button("Save", save -> {
            Sugar sugar = new Sugar(numField.getValue(), comboBoxType.getValue(), comboBoxUnits.getValue(), dateTimePicker.getValue());
            sugarService.save(sugar);
            Notification.show("Saved").setPosition(Notification.Position.MIDDLE);
            numField.setValue(null);
            comboBoxType.setValue(null);
            comboBoxUnits.setValue(null);
            dateTimePicker.setValue(LocalDateTime.now());
        });

        sugarLayout.setAlignItems(Alignment.CENTER);
        sugarLayout.add(numField, comboBoxUnits, comboBoxType, dateTimePicker, buttonSave);
        return sugarLayout;
    }

    private Component addInsulinLayout() {
        NumberField numField = new NumberField("Insulin");
        numField.setStep(1);
        ComboBox<InsulinType> comboBoxType = new ComboBox("Type");
        comboBoxType.setItems(InsulinType.values());
        comboBoxType.setItemLabelGenerator(InsulinType::getMsg);
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setStep(Duration.ofMinutes(1));

        Button buttonSave = new Button("Save", save -> {
            Insulin insulin = new Insulin(numField.getValue().intValue(), comboBoxType.getValue(), dateTimePicker.getValue());
            insulinService.save(insulin);
            Notification.show("Saved").setPosition(Notification.Position.MIDDLE);
            numField.setValue(null);
            comboBoxType.setValue(null);
            dateTimePicker.setValue(LocalDateTime.now());
        });

        insulinLayout.setAlignItems(Alignment.CENTER);
        insulinLayout.add(numField, comboBoxType, dateTimePicker, buttonSave);
        return insulinLayout;
    }
}