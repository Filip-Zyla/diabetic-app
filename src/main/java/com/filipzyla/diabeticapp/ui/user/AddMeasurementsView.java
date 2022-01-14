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
import com.filipzyla.diabeticapp.backend.utility.Validators;
import com.filipzyla.diabeticapp.ui.utility.TopMenuBar;
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
import java.util.ResourceBundle;

@Route("add")
public class AddMeasurementsView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;

    private final VerticalLayout insulinLayout = new VerticalLayout();
    private final VerticalLayout sugarLayout = new VerticalLayout();

    public final ResourceBundle langResources;

    public AddMeasurementsView(SugarService sugarService, InsulinService insulinService, SecurityService securityService, UserService userService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");

        ComboBox<MeasurementType> comboBoxMeasurementType = new ComboBox(langResources.getString("what_add"));
        comboBoxMeasurementType.setItems(MeasurementType.values());
        comboBoxMeasurementType.setWidth("200px");
        comboBoxMeasurementType.setItemLabelGenerator(type -> langResources.getString(type.getMsg()));

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
        NumberField numField = new NumberField(langResources.getString("sugar"));
        numField.setStep(1);
        numField.setWidth("140px");
        ComboBox<SugarType> comboBoxType = new ComboBox(langResources.getString("type"));
        comboBoxType.setItems(SugarType.values());
        comboBoxType.setWidth("200px");
        comboBoxType.setItemLabelGenerator(sugarType -> langResources.getString(sugarType.getMsg()));
        DateTimePicker dateTimePicker = new DateTimePicker(langResources.getString("time"));
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setStep(Duration.ofMinutes(1));
        dateTimePicker.setWidth("400px");
        TextArea textAreaNote = new TextArea(langResources.getString("note"));
        textAreaNote.setWidth("400px");
        textAreaNote.setMaxLength(255);
        textAreaNote.setValueChangeMode(ValueChangeMode.EAGER);
        textAreaNote.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });

        Button buttonSave = new Button(langResources.getString("save"), save -> {
            if (numField.isEmpty() || comboBoxType.isEmpty()) {
                Notification.show(langResources.getString("empty_fields")).setPosition(Notification.Position.MIDDLE);
            }
            else if (Validators.validateSugar(numField.getValue().intValue())) {
                Sugar sugar = new Sugar(numField.getValue().intValue(), comboBoxType.getValue(), dateTimePicker.getValue(), textAreaNote.getValue(), user);
                sugarService.save(sugar);
                Notification.show(langResources.getString("saved")).setPosition(Notification.Position.MIDDLE);
                numField.setValue(null);
                comboBoxType.setValue(null);
                dateTimePicker.setValue(LocalDateTime.now());
                textAreaNote.setValue("");
            }
            else {
                Notification.show(langResources.getString("values_between") + Validators.SUGAR_RANGE).setPosition(Notification.Position.MIDDLE);
            }
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
        NumberField numField = new NumberField(langResources.getString("insulin"));
        numField.setStep(1);
        numField.setWidth("200px");
        ComboBox<InsulinType> comboBoxType = new ComboBox(langResources.getString("type"));
        comboBoxType.setWidth("200px");
        comboBoxType.setItems(InsulinType.values());
        comboBoxType.setItemLabelGenerator(insulinType -> langResources.getString(insulinType.getMsg()));
        DateTimePicker dateTimePicker = new DateTimePicker(langResources.getString("time"));
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setStep(Duration.ofMinutes(1));
        dateTimePicker.setWidth("400px");
        TextArea textAreaNote = new TextArea(langResources.getString("note"));
        textAreaNote.setWidth("400px");
        textAreaNote.setMaxLength(255);
        textAreaNote.setValueChangeMode(ValueChangeMode.EAGER);
        textAreaNote.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });

        Button buttonSave = new Button(langResources.getString("save"), save -> {
            if (numField.isEmpty() || comboBoxType.isEmpty()) {
                Notification.show(langResources.getString("empty_fields")).setPosition(Notification.Position.MIDDLE);
            }
            else if (Validators.validateInsulin(numField.getValue().intValue())) {
                Insulin insulin = new Insulin(numField.getValue().intValue(), comboBoxType.getValue(), dateTimePicker.getValue(), textAreaNote.getValue(), user);
                insulinService.save(insulin);
                Notification.show(langResources.getString("saved")).setPosition(Notification.Position.MIDDLE);
                numField.setValue(null);
                comboBoxType.setValue(null);
                dateTimePicker.setValue(LocalDateTime.now());
                textAreaNote.setValue("");
            }
            else {
                Notification.show(langResources.getString("values_between") + Validators.INSULIN_RANGE).setPosition(Notification.Position.MIDDLE);
            }
        });

        insulinLayout.setAlignItems(Alignment.CENTER);
        insulinLayout.add(numField, comboBoxType, dateTimePicker, textAreaNote, buttonSave);
        return insulinLayout;
    }
}