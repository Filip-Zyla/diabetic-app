package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.enums.InsulinType;
import com.filipzyla.diabeticapp.backend.enums.MeasurementType;
import com.filipzyla.diabeticapp.backend.enums.SugarType;
import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import com.filipzyla.diabeticapp.backend.models.Insulin;
import com.filipzyla.diabeticapp.backend.models.Sugar;
import com.filipzyla.diabeticapp.backend.models.User;
import com.filipzyla.diabeticapp.backend.security.SecurityService;
import com.filipzyla.diabeticapp.backend.service.InsulinService;
import com.filipzyla.diabeticapp.backend.service.SugarService;
import com.filipzyla.diabeticapp.backend.service.UserService;
import com.filipzyla.diabeticapp.ui.components.TopMenuBar;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.time.LocalDateTime;

@Route("add")
public class AddMeasurementsView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;

    private final VerticalLayout insulinLayout = new VerticalLayout();
    private final VerticalLayout sugarLayout = new VerticalLayout();

    public AddMeasurementsView(SugarService sugarService, InsulinService insulinService, SecurityService securityService, UserService userService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());


        ComboBox<MeasurementType> comboBoxMeasurementType = new ComboBox("What do you want to add?");
        comboBoxMeasurementType.setItems(MeasurementType.values());
        comboBoxMeasurementType.setWidth("200px");

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
        add(new TopMenuBar(securityService), comboBoxMeasurementType);
    }

    private Component addSugarLayout() {
        NumberField numField = new NumberField("Sugar");
        numField.setStep(1);
        numField.setWidth("140px");
        ComboBox<SugarType> comboBoxType = new ComboBox("Type");
        comboBoxType.setItems(SugarType.values());
        comboBoxType.setWidth("200px");
        comboBoxType.setItemLabelGenerator(SugarType::getMsg);
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setStep(Duration.ofMinutes(1));
        dateTimePicker.setWidth("400px");
        TextArea textAreaNote = new TextArea("Note");
        textAreaNote.setWidth("400px");
        textAreaNote.setMaxLength(255);
        textAreaNote.setValueChangeMode(ValueChangeMode.EAGER);
        textAreaNote.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });

        Button buttonSave = new Button("Save", save -> {
            Sugar sugar = new Sugar(numField.getValue().intValue(), comboBoxType.getValue(), dateTimePicker.getValue(), textAreaNote.getValue(), user);
            sugarService.save(sugar);
            Notification.show("Saved").setPosition(Notification.Position.MIDDLE);
            numField.setValue(null);
            comboBoxType.setValue(null);
            dateTimePicker.setValue(LocalDateTime.now());
            textAreaNote.setValue("");
        });

        HorizontalLayout sugarWithUnitsLayout = new HorizontalLayout();
        sugarWithUnitsLayout.add(numField, new H5(SugarUnits.MILLI_GRAM.getMsg()));
        sugarWithUnitsLayout.setWidth("200px");
        sugarWithUnitsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        sugarWithUnitsLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        sugarLayout.setAlignItems(Alignment.CENTER);
        sugarLayout.add(sugarWithUnitsLayout, comboBoxType, dateTimePicker, textAreaNote, buttonSave);
        return sugarLayout;
    }

    private Component addInsulinLayout() {
        NumberField numField = new NumberField("Insulin");
        numField.setStep(1);
        numField.setWidth("200px");
        ComboBox<InsulinType> comboBoxType = new ComboBox("Type");
        comboBoxType.setWidth("200px");
        comboBoxType.setItems(InsulinType.values());
        comboBoxType.setItemLabelGenerator(InsulinType::getMsg);
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setStep(Duration.ofMinutes(1));
        dateTimePicker.setWidth("400px");
        TextArea textAreaNote = new TextArea("Note");
        textAreaNote.setWidth("400px");
        textAreaNote.setMaxLength(255);
        textAreaNote.setValueChangeMode(ValueChangeMode.EAGER);
        textAreaNote.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });

        Button buttonSave = new Button("Save", save -> {
            Insulin insulin = new Insulin(numField.getValue().intValue(), comboBoxType.getValue(), dateTimePicker.getValue(), textAreaNote.getValue(), user);
            insulinService.save(insulin);
            Notification.show("Saved").setPosition(Notification.Position.MIDDLE);
            numField.setValue(null);
            comboBoxType.setValue(null);
            dateTimePicker.setValue(LocalDateTime.now());
            textAreaNote.setValue("");
        });

        insulinLayout.setAlignItems(Alignment.CENTER);
        insulinLayout.add(numField, comboBoxType, dateTimePicker, textAreaNote, buttonSave);
        return insulinLayout;
    }
}