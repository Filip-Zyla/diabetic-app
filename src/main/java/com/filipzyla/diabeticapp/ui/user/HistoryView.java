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
import com.filipzyla.diabeticapp.ui.components.TopMenuBar;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Route("history")
public class HistoryView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;

    private final VerticalLayout layoutSugar = new VerticalLayout();
    private final VerticalLayout layoutInsulin = new VerticalLayout();
    private DatePicker datePickerFrom, datePickerTo;

    public HistoryView(SugarService sugarService, InsulinService insulinService, SecurityService securityService, UserService userService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());
        
        Button buttonShowHistory = new Button("Show", event -> refreshHistoryGrid());
        setAlignItems(Alignment.CENTER);

        add(new TopMenuBar(securityService), new H2("History"), datePeriodSelector(), buttonShowHistory, historyGrid());
    }

    private Component datePeriodSelector() {
        HorizontalLayout layoutDatePeriod = new HorizontalLayout();
        datePickerFrom = new DatePicker("From");
        datePickerFrom.setValue(LocalDate.now().minusDays(14));
        datePickerTo = new DatePicker("To");
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
        gridSugar.addColumn(sugar -> sugar.getSugar() + " " + sugar.getUnits().getMsg()).setHeader("Sugar");
        gridSugar.addColumn(sugar -> sugar.getType().getMsg()).setHeader("Type");
        gridSugar.addColumn(sugar -> sugar.getTime().format(CustomDateTimeFormatter.formatter)).setHeader("Time");
        gridSugar.addColumn(
                new ComponentRenderer<>(Button::new, (button, sugar) -> {
                    button.addClickListener(e -> modifySugar(sugar));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader("Modify");


        Optional<List<Sugar>> sugarOpt = Optional.ofNullable(sugarService.findAllOrderByTimeBetweenDates(user.getUserId(), datePickerFrom.getValue(), datePickerTo.getValue().plusDays(1)));
        SugarUnits userUnits = user.getUnits();
        sugarOpt.ifPresent(gridSugar::setItems);
        gridSugar.setWidth(700, Unit.PIXELS);
        gridSugar.setHeight(500, Unit.PIXELS);
        layoutSugar.setAlignItems(Alignment.CENTER);
        layoutSugar.add(new H4("Sugar"), gridSugar);
        return layoutSugar;
    }

    private Component insulinGrid() {
        Grid<Insulin> gridInsulin = new Grid(Insulin.class, false);
        gridInsulin.addColumn(Insulin::getInsulin).setHeader("Units");
        gridInsulin.addColumn(insulin -> insulin.getType().getMsg()).setHeader("Type");
        gridInsulin.addColumn(insulin -> insulin.getTime().format(CustomDateTimeFormatter.formatter)).setHeader("Time");
        gridInsulin.addColumn(
                new ComponentRenderer<>(Button::new, (button, insulin) -> {
                    button.addClickListener(e -> modifyInsulin(insulin));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader("Modify");

        Optional<List<Insulin>> sugarOpt = Optional.ofNullable(insulinService.findAllOrderByTimeBetweenDates(user.getUserId(), datePickerFrom.getValue(), datePickerTo.getValue().plusDays(1)));
        sugarOpt.ifPresent(gridInsulin::setItems);
        gridInsulin.setWidth(700, Unit.PIXELS);
        gridInsulin.setHeight(500, Unit.PIXELS);
        layoutInsulin.setAlignItems(Alignment.CENTER);
        layoutInsulin.add(new H4("Insulin"), gridInsulin);
        return layoutInsulin;
    }

    private void modifySugar(Sugar sugar) {
        Dialog dialog = new Dialog();
        dialog.open();

        NumberField numField = new NumberField("Sugar");
        numField.setStep(1);
        ComboBox<SugarType> comboBoxType = new ComboBox("Type");
        comboBoxType.setItems(SugarType.values());
        comboBoxType.setItemLabelGenerator(SugarType::getMsg);
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setStep(Duration.ofMinutes(1));
        TextArea textAreaNote = new TextArea("Note");
        textAreaNote.setWidth(300, Unit.PIXELS);
        textAreaNote.setMaxLength(200);

        numField.setValue(Double.valueOf(sugar.getSugar()));
        comboBoxType.setValue(sugar.getType());
        dateTimePicker.setValue(sugar.getTime());
        textAreaNote.setValue(sugar.getNote());

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(numField, comboBoxType, dateTimePicker, textAreaNote);

        Button buttonCommit = new Button("Save", save -> {
            sugar.setSugar(numField.getValue().intValue());
            sugar.setType(comboBoxType.getValue());
            sugar.setTime(dateTimePicker.getValue());
            sugar.setNote(textAreaNote.getValue());
            sugarService.save(sugar);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonDelete = new Button("Delete", delete -> {
            sugarService.delete(sugar);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonClose = new Button("Close", close -> {
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

        NumberField numField = new NumberField("Insulin");
        numField.setStep(1);
        ComboBox<InsulinType> comboBox = new ComboBox("Type");
        comboBox.setItems(InsulinType.values());
        comboBox.setItemLabelGenerator(InsulinType::getMsg);
        DateTimePicker dateTimePicker = new DateTimePicker("Time");
        dateTimePicker.setStep(Duration.ofMinutes(1));
        TextArea textAreaNote = new TextArea("Note");
        textAreaNote.setWidth(300, Unit.PIXELS);
        textAreaNote.setMaxLength(200);

        numField.setValue(Double.valueOf(insulin.getInsulin()));
        comboBox.setValue(insulin.getType());
        dateTimePicker.setValue(insulin.getTime());
        textAreaNote.setValue(insulin.getNote());

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(numField, comboBox, dateTimePicker, textAreaNote);

        Button buttonCommit = new Button("Save", save -> {
            insulin.setInsulin(numField.getValue().intValue());
            insulin.setType(comboBox.getValue());
            insulin.setTime(dateTimePicker.getValue());
            insulin.setNote(textAreaNote.getValue());
            insulinService.save(insulin);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonDelete = new Button("Delete", delete -> {
            insulinService.delete(insulin);
            dialog.close();
            refreshHistoryGrid();
        });
        Button buttonClose = new Button("Close", close -> {
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