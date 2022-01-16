package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.enums.InsulinType;
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
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Route("history")
public class HistoryView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;

    private final VerticalLayout layoutSugar = new VerticalLayout();
    private final VerticalLayout layoutInsulin = new VerticalLayout();
    private DatePicker datePickerFrom, datePickerTo;

    public final ResourceBundle langResources;

    public HistoryView(SugarService sugarService, InsulinService insulinService, SecurityService securityService, UserService userService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");

        Button buttonShowHistory = new Button(langResources.getString("show"), event -> refreshHistoryGrid());
        setAlignItems(Alignment.CENTER);

        add(new TopMenuBar(securityService), new H2(langResources.getString("history")), datePeriodSelector(), buttonShowHistory, historyGrid());
    }

    private Component datePeriodSelector() {
        HorizontalLayout layoutDatePeriod = new HorizontalLayout();
        datePickerFrom = new DatePicker(langResources.getString("from"));
        datePickerFrom.setLocale(new Locale("pl", "PL"));
        datePickerFrom.setValue(LocalDate.now().minusDays(14));

        datePickerTo = new DatePicker(langResources.getString("to"));
        datePickerTo.setLocale(new Locale("pl", "PL"));
        datePickerTo.setValue(LocalDate.now());

        layoutDatePeriod.add(datePickerFrom, datePickerTo);
        return layoutDatePeriod;
    }

    private Component historyGrid() {
        HorizontalLayout layoutTabs = new HorizontalLayout();
        layoutTabs.add(sugarGrid(), insulinGrid());
        return layoutTabs;
    }

    private Component sugarGrid() {
        Grid<Sugar> gridSugar = new Grid(Sugar.class, false);
        gridSugar.addColumn(sugar -> sugar.getSugar() + " " + sugar.getUnits().getMsg()).setHeader(langResources.getString("sugar"));
        gridSugar.addColumn(sugar -> langResources.getString(sugar.getType().getMsg())).setHeader(langResources.getString("type"));
        gridSugar.addColumn(sugar -> sugar.getTime().format(CustomDateTimeFormatter.formatter)).setHeader(langResources.getString("time"));
        gridSugar.addColumn(
                new ComponentRenderer<>(Button::new, (button, sugar) -> {
                    button.addClickListener(e -> modifySugar(sugar));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader(langResources.getString("modify"));


        Optional<List<Sugar>> sugarOpt = Optional.ofNullable(sugarService.findAllOrderByTimeBetweenDates(user.getUserId(), datePickerFrom.getValue(), datePickerTo.getValue().plusDays(1)));
        SugarUnits userUnits = user.getUnits();
        sugarOpt.ifPresent(gridSugar::setItems);
        gridSugar.setWidth(700, Unit.PIXELS);
        gridSugar.setHeight(500, Unit.PIXELS);
        layoutSugar.setAlignItems(Alignment.CENTER);
        layoutSugar.add(new H4(langResources.getString("sugar")), gridSugar);
        return layoutSugar;
    }

    private Component insulinGrid() {
        Grid<Insulin> gridInsulin = new Grid(Insulin.class, false);
        gridInsulin.addColumn(Insulin::getInsulin).setHeader(langResources.getString("units"));
        gridInsulin.addColumn(insulin -> langResources.getString(insulin.getType().getMsg())).setHeader(langResources.getString("type"));
        gridInsulin.addColumn(insulin -> insulin.getTime().format(CustomDateTimeFormatter.formatter)).setHeader(langResources.getString("time"));
        gridInsulin.addColumn(
                new ComponentRenderer<>(Button::new, (button, insulin) -> {
                    button.addClickListener(e -> modifyInsulin(insulin));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader(langResources.getString("modify"));

        Optional<List<Insulin>> sugarOpt = Optional.ofNullable(insulinService.findAllOrderByTimeBetweenDates(user.getUserId(), datePickerFrom.getValue(), datePickerTo.getValue().plusDays(1)));
        sugarOpt.ifPresent(gridInsulin::setItems);
        gridInsulin.setWidth(700, Unit.PIXELS);
        gridInsulin.setHeight(500, Unit.PIXELS);
        layoutInsulin.setAlignItems(Alignment.CENTER);
        layoutInsulin.add(new H4(langResources.getString("insulin")), gridInsulin);
        return layoutInsulin;
    }

    private void modifySugar(Sugar sugar) {
        Dialog dialog = new Dialog();
        dialog.open();

        NumberField numField = new NumberField(langResources.getString("sugar"));
        numField.setStep(1);
        ComboBox<SugarType> comboBoxType = new ComboBox(langResources.getString("type"));
        comboBoxType.setItems(SugarType.values());
        comboBoxType.setItemLabelGenerator(sugarType -> langResources.getString(sugarType.getMsg()));
        DateTimePicker dateTimePicker = new DateTimePicker(langResources.getString("time"));
        dateTimePicker.setLocale(new Locale("pl", "PL"));
        dateTimePicker.setStep(Duration.ofMinutes(1));
        TextArea textAreaNote = new TextArea(langResources.getString("note"));
        textAreaNote.setWidth(300, Unit.PIXELS);
        textAreaNote.setMaxLength(255);
        textAreaNote.setValueChangeMode(ValueChangeMode.EAGER);
        textAreaNote.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });

        numField.setValue(Double.valueOf(sugar.getSugar()));
        comboBoxType.setValue(sugar.getType());
        dateTimePicker.setValue(sugar.getTime());
        textAreaNote.setValue(sugar.getNote());

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(numField, comboBoxType, dateTimePicker, textAreaNote);

        Button buttonCommit = new Button(langResources.getString("save"), save -> {
            if (Validators.validateSugar(numField.getValue().intValue())) {
                sugar.setSugar(numField.getValue().intValue());
                sugar.setType(comboBoxType.getValue());
                sugar.setTime(dateTimePicker.getValue());
                sugar.setNote(textAreaNote.getValue());
                sugarService.save(sugar);
                dialog.close();
                refreshHistoryGrid();
            }
            else {
                Notification.show(langResources.getString("values_between") + Validators.SUGAR_RANGE).setPosition(Notification.Position.MIDDLE);
            }
        });
        Button buttonDelete = new Button(langResources.getString("delete"), delete -> {
            sugarService.delete(sugar);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonClose = new Button(langResources.getString("close"), close -> {
            dialog.close();
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(buttonCommit, buttonDelete, buttonClose);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        dialog.add(dialogLayout, buttonLayout);
        add(dialog);
    }

    private void modifyInsulin(Insulin insulin) {
        Dialog dialog = new Dialog();
        dialog.open();

        NumberField numField = new NumberField(langResources.getString("insulin"));
        numField.setStep(1);
        ComboBox<InsulinType> comboBox = new ComboBox(langResources.getString("type"));
        comboBox.setItems(InsulinType.values());
        comboBox.setItemLabelGenerator(insulinType -> langResources.getString(insulinType.getMsg()));
        DateTimePicker dateTimePicker = new DateTimePicker(langResources.getString("time"));
        dateTimePicker.setLocale(new Locale("pl", "PL"));
        dateTimePicker.setStep(Duration.ofMinutes(1));
        TextArea textAreaNote = new TextArea(langResources.getString("note"));
        textAreaNote.setWidth(300, Unit.PIXELS);
        textAreaNote.setMaxLength(255);
        textAreaNote.setValueChangeMode(ValueChangeMode.EAGER);
        textAreaNote.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });

        numField.setValue(Double.valueOf(insulin.getInsulin()));
        comboBox.setValue(insulin.getType());
        dateTimePicker.setValue(insulin.getTime());
        textAreaNote.setValue(insulin.getNote());

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(numField, comboBox, dateTimePicker, textAreaNote);

        Button buttonCommit = new Button(langResources.getString("save"), save -> {
            if (Validators.validateInsulin(numField.getValue().intValue())) {
                insulin.setInsulin(numField.getValue().intValue());
                insulin.setType(comboBox.getValue());
                insulin.setTime(dateTimePicker.getValue());
                insulin.setNote(textAreaNote.getValue());
                insulinService.save(insulin);
                dialog.close();
                refreshHistoryGrid();
            }
            else {
                Notification.show(langResources.getString("values_between") + Validators.INSULIN_RANGE).setPosition(Notification.Position.MIDDLE);
            }
        });
        Button buttonDelete = new Button(langResources.getString("delete"), delete -> {
            insulinService.delete(insulin);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonClose = new Button(langResources.getString("close"), close -> {
            dialog.close();
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(buttonCommit, buttonDelete, buttonClose);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        dialog.add(dialogLayout, buttonLayout);
        add(dialog);
    }

    private void refreshHistoryGrid() {
        layoutSugar.removeAll();
        layoutInsulin.removeAll();
        sugarGrid();
        insulinGrid();
    }
}