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
import com.filipzyla.diabeticapp.backend.utility.CustomDateTimeFormatter;
import com.filipzyla.diabeticapp.backend.utility.Validators;
import com.filipzyla.diabeticapp.ui.utility.TopMenuBar;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Route("home")
public class MainUserView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;
    public final ResourceBundle langResources;

    private final VerticalLayout lastSugarLayout;
    private final VerticalLayout lastInsulinLayout;
    private final VerticalLayout sugarLayout;
    private final VerticalLayout insulinLayout;

    @Autowired
    public MainUserView(SecurityService securityService, UserService userService, SugarService sugarService, InsulinService insulinService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");

        lastSugarLayout = new VerticalLayout();
        lastInsulinLayout = new VerticalLayout();
        sugarLayout = new VerticalLayout();
        insulinLayout = new VerticalLayout();

        setAlignItems(Alignment.CENTER);
        add(new TopMenuBar(securityService), addLastMeasurements(), addMeasurementLayout());
    }

    private Component addLastMeasurements() {
        lastInsulin();
        lastSugar();

        HorizontalLayout layoutLastMeasurements = new HorizontalLayout();
        layoutLastMeasurements.setSizeFull();
        layoutLastMeasurements.add(lastSugarLayout, lastInsulinLayout);
        return layoutLastMeasurements;
    }

    private void lastSugar() {
        lastSugarLayout.setWidth("50%");
        lastSugarLayout.setAlignItems(Alignment.CENTER);

        Optional<Sugar> sugarOpt = Optional.ofNullable(sugarService.findFirstByOrderByTimeAsc(user.getUserId()));
        if (sugarOpt.isPresent()) {
            SugarUnits userUnits = user.getUnits();

            H3 labelSugarMain = new H3(langResources.getString("last_sugar"));
            H5 labelSugar = new H5(sugarOpt.get().getSugar() + " " + userUnits.getMsg());
            H5 labelTypeSug = new H5(langResources.getString(sugarOpt.get().getType().getMsg()));
            H5 labelTimeSug = new H5(sugarOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            lastSugarLayout.add(labelSugarMain, labelSugar, labelTypeSug, labelTimeSug);
        }
    }

    private void lastInsulin() {
        lastInsulinLayout.setWidth("50%");
        lastInsulinLayout.setAlignItems(Alignment.CENTER);

        Optional<Insulin> insulinOpt = Optional.ofNullable(insulinService.findFirstByOrderByTimeAsc(user.getUserId()));
        if (insulinOpt.isPresent()) {
            H3 labelInsulinMain = new H3(langResources.getString("last_insulin"));
            H5 labelInsulin = new H5(insulinOpt.get().getInsulin().toString() + " " + langResources.getString("units").toLowerCase());
            H5 labelTypeIns = new H5(langResources.getString(insulinOpt.get().getType().getMsg()));
            H5 labelTimeIns = new H5(insulinOpt.get().getTime().format(CustomDateTimeFormatter.formatter));
            lastInsulinLayout.add(labelInsulinMain, labelInsulin, labelTypeIns, labelTimeIns);
        }
    }

    private Component addMeasurementLayout() {
        ComboBox<MeasurementType> comboBoxMeasurementType = new ComboBox(langResources.getString("what_add"));
        comboBoxMeasurementType.setItems(MeasurementType.values());
        comboBoxMeasurementType.setWidth("200px");
        comboBoxMeasurementType.setItemLabelGenerator(type -> langResources.getString(type.getMsg()));

        comboBoxMeasurementType.addValueChangeListener(event -> {
            insulinLayout.removeAll();
            sugarLayout.removeAll();
            remove(insulinLayout, sugarLayout);
            if (event.getValue() == MeasurementType.SUGAR) {
                addSugarLayout();
                add(sugarLayout);
            }
            else if (event.getValue() == MeasurementType.INSULIN) {
                addInsulinLayout();
                add(insulinLayout);
            }
        });

        setAlignItems(Alignment.CENTER);
        return comboBoxMeasurementType;
    }

    private void addSugarLayout() {
        NumberField numField = new NumberField(langResources.getString("sugar"));
        numField.setStep(1);
        numField.setWidth("140px");
        ComboBox<SugarType> comboBoxType = new ComboBox(langResources.getString("type"));
        comboBoxType.setItems(SugarType.values());
        comboBoxType.setWidth("200px");
        comboBoxType.setItemLabelGenerator(sugarType -> langResources.getString(sugarType.getMsg()));
        DateTimePicker dateTimePicker = new DateTimePicker(langResources.getString("time"));
        dateTimePicker.setLocale(new Locale("pl", "PL"));
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
                lastSugarLayout.removeAll();
                lastSugar();
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
    }

    private void addInsulinLayout() {
        NumberField numField = new NumberField(langResources.getString("insulin"));
        numField.setStep(1);
        numField.setWidth("200px");
        ComboBox<InsulinType> comboBoxType = new ComboBox(langResources.getString("type"));
        comboBoxType.setWidth("200px");
        comboBoxType.setItems(InsulinType.values());
        comboBoxType.setItemLabelGenerator(insulinType -> langResources.getString(insulinType.getMsg()));
        DateTimePicker dateTimePicker = new DateTimePicker(langResources.getString("time"));
        dateTimePicker.setLocale(new Locale("pl", "PL"));
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
                lastInsulinLayout.removeAll();
                lastInsulin();
            }
            else {
                Notification.show(langResources.getString("values_between") + Validators.INSULIN_RANGE).setPosition(Notification.Position.MIDDLE);
            }
        });

        insulinLayout.setAlignItems(Alignment.CENTER);
        insulinLayout.add(numField, comboBoxType, dateTimePicker, textAreaNote, buttonSave);
    }
}