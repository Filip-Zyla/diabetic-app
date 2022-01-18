package com.filipzyla.diabeticapp.ui.user;

import com.filipzyla.diabeticapp.backend.enums.InsulinType;
import com.filipzyla.diabeticapp.backend.enums.SugarType;
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
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Route("history")
public class HistoryView extends VerticalLayout {

    private final SugarService sugarService;
    private final InsulinService insulinService;

    private final User user;
    public final ResourceBundle langResources;

    private final VerticalLayout layoutSugar;
    private final VerticalLayout layoutInsulin;

    private DatePicker datePickerFrom, datePickerTo;

    private List<Insulin> insulinList;
    private List<Sugar> sugarList;

    public HistoryView(SugarService sugarService, InsulinService insulinService, SecurityService securityService, UserService userService) {
        this.sugarService = sugarService;
        this.insulinService = insulinService;

        user = userService.findByUsername(securityService.getAuthenticatedUser());
        langResources = ResourceBundle.getBundle("lang.res");

        layoutSugar = new VerticalLayout();
        layoutInsulin = new VerticalLayout();
        setAlignItems(Alignment.CENTER);

        add(new TopMenuBar(securityService), new H2(langResources.getString("history")), datePeriodSelector(), historyGrid(), importExportButtons());
    }

    private Component datePeriodSelector() {
        HorizontalLayout layoutDatePeriod = new HorizontalLayout();
        datePickerFrom = new DatePicker(langResources.getString("from"));
        datePickerFrom.setLocale(new Locale("pl", "PL"));
        datePickerFrom.setValue(LocalDate.now().minusDays(14));

        datePickerTo = new DatePicker(langResources.getString("to"));
        datePickerTo.setLocale(new Locale("pl", "PL"));
        datePickerTo.setValue(LocalDate.now());

        Button buttonShowHistory = new Button(langResources.getString("show"), event -> refreshHistoryGrid());

        layoutDatePeriod.add(datePickerFrom, buttonShowHistory, datePickerTo);
        layoutDatePeriod.setAlignItems(Alignment.END);
        return layoutDatePeriod;
    }

    private Component historyGrid() {
        HorizontalLayout layoutTabs = new HorizontalLayout();
        sugarGrid();
        insulinGrid();
        layoutTabs.add(layoutSugar, layoutInsulin);
        return layoutTabs;
    }

    private void sugarGrid() {
        Grid<Sugar> gridSugar = new Grid(Sugar.class, false);
        gridSugar.addColumn(sugar -> sugar.getSugar() + " " + sugar.getUnits().getMsg()).setHeader(langResources.getString("sugar"));
        gridSugar.addColumn(sugar -> langResources.getString(sugar.getType().getMsg())).setHeader(langResources.getString("type"));
        gridSugar.addColumn(sugar -> sugar.getTime().format(CustomDateTimeFormatter.formatter)).setHeader(langResources.getString("time"));
        gridSugar.addColumn(
                new ComponentRenderer<>(Button::new, (button, sugar) -> {
                    button.addClickListener(e -> modifySugar(sugar));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader(langResources.getString("modify"));


        sugarList = sugarService.findAllOrderByTimeBetweenDates(user.getUserId(), datePickerFrom.getValue(), datePickerTo.getValue().plusDays(1));
        gridSugar.setItems(sugarList);
        gridSugar.setWidth(700, Unit.PIXELS);
        gridSugar.setHeight(500, Unit.PIXELS);
        layoutSugar.setAlignItems(Alignment.CENTER);
        layoutSugar.add(new H4(langResources.getString("sugar")), gridSugar);
    }

    private void insulinGrid() {
        Grid<Insulin> gridInsulin = new Grid(Insulin.class, false);
        gridInsulin.addColumn(Insulin::getInsulin).setHeader(langResources.getString("units"));
        gridInsulin.addColumn(insulin -> langResources.getString(insulin.getType().getMsg())).setHeader(langResources.getString("type"));
        gridInsulin.addColumn(insulin -> insulin.getTime().format(CustomDateTimeFormatter.formatter)).setHeader(langResources.getString("time"));
        gridInsulin.addColumn(
                new ComponentRenderer<>(Button::new, (button, insulin) -> {
                    button.addClickListener(e -> modifyInsulin(insulin));
                    button.setIcon(new Icon(VaadinIcon.MENU));
                })).setHeader(langResources.getString("modify"));

        insulinList = insulinService.findAllOrderByTimeBetweenDates(user.getUserId(), datePickerFrom.getValue(), datePickerTo.getValue().plusDays(1));
        gridInsulin.setItems(insulinList);
        gridInsulin.setWidth(700, Unit.PIXELS);
        gridInsulin.setHeight(500, Unit.PIXELS);
        layoutInsulin.setAlignItems(Alignment.CENTER);
        layoutInsulin.add(new H4(langResources.getString("insulin")), gridInsulin);
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

    private Component importExportButtons() {
        Button importButton = new Button(langResources.getString("import_file"), event -> {
            uploadFile();
        });
        Button exportButton = new Button(langResources.getString("export_file"), event -> {
            downloadFile();
        });

        HorizontalLayout importExportLayout = new HorizontalLayout();
        importExportLayout.add(importButton, exportButton);
        return importExportLayout;
    }

    private void uploadFile() {
        Dialog uploadWindow = new Dialog();

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".txt", ".csv");
        upload.setMaxFileSize(1024 * 1024 * 10);
        UploadI18N i18n = new UploadI18N();
        i18n.setAddFiles(new UploadI18N.AddFiles());
        i18n.getAddFiles().setOne(langResources.getString("upload"));
        i18n.setDropFiles(new UploadI18N.DropFiles());
        i18n.getDropFiles().setOne(langResources.getString("drop_file"));
        i18n.setError(new UploadI18N.Error());
        i18n.getError().setIncorrectFileType(langResources.getString("file_req"));
        upload.setI18n(i18n);

        Button saveButton = new Button(langResources.getString("save"), new Icon(VaadinIcon.UPLOAD), event -> {
            if (!upload.isUploading()) {
                InputStream inputStream = buffer.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVReader csvReader = new CSVReader(inputStreamReader);
                try {
                    processFile(csvReader);
                    csvReader.close();
                    inputStreamReader.close();
                    inputStream.close();
                    uploadWindow.close();
                    Notification.show(langResources.getString("data_save")).setPosition(Notification.Position.MIDDLE);
                } catch (IOException | CsvException e) {
                    System.err.println("File reading fail");
                    Notification.show(langResources.getString("file_read_prob")).setPosition(Notification.Position.MIDDLE);
                }
            }
        });

        uploadWindow.open();
        uploadWindow.add(upload, saveButton);
    }

    private void processFile(CSVReader csvReader) throws IOException, CsvException {
        for (String[] row : csvReader.readAll()) {
            if (row[0].equals("sugar")) {
                Integer value = Integer.parseInt(row[1]);
                SugarType type = SugarType.valueOf(row[2]);
                LocalDateTime time = LocalDateTime.parse(row[3], CustomDateTimeFormatter.formatter);
                String note = row[4];
                Sugar sugar = new Sugar(value, type, time, note, user);
                sugarService.save(sugar);
            }
            else if (row[0].equals("insulin")) {
                Integer value = Integer.parseInt(row[1]);
                InsulinType type = InsulinType.valueOf(row[2]);
                LocalDateTime time = LocalDateTime.parse(row[3], CustomDateTimeFormatter.formatter);
                String note = row[4];
                Insulin insulin = new Insulin(value, type, time, note, user);
                insulinService.save(insulin);
            }
            else throw new IOException("Wrong format");
        }
    }

    private void downloadFile() {
        Dialog downloadWindow = new Dialog();

        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setLabel(langResources.getString("export_data"));
        checkboxGroup.setItems(langResources.getString("sugar"), langResources.getString("insulin"));
        checkboxGroup.deselectAll();
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        Anchor anchor = new Anchor(new StreamResource("import_data.csv", () -> {
            boolean sugar = checkboxGroup.isSelected("Sugar");
            boolean insulin = checkboxGroup.isSelected("Insulin");
            return measurementsToInputStream(sugar, insulin);
        }), "");
        anchor.getElement().setAttribute("download", true);
        anchor.add(new Button(langResources.getString("save"), new Icon(VaadinIcon.DOWNLOAD), event -> downloadWindow.close()));

        VerticalLayout downloadLayout = new VerticalLayout();
        downloadLayout.add(checkboxGroup, anchor);
        downloadWindow.open();
        downloadWindow.add(downloadLayout);
    }

    private InputStream measurementsToInputStream(boolean sugar, boolean insulin) {
        InputStream sugarStream = InputStream.nullInputStream();
        if (sugar) {
            String[] sugarCsv = sugarList
                    .stream()
                    .map(s -> "sugar" + "," + s.getSugar().toString() + "," + s.getType().toString() + ","
                            + s.getTime().format(CustomDateTimeFormatter.formatter) + "," + s.getNote() + "\n")
                    .toArray(String[]::new);
            sugarStream = new ByteArrayInputStream(String.join("", Arrays.asList(sugarCsv)).getBytes(StandardCharsets.UTF_8));
        }

        InputStream insulinStream = InputStream.nullInputStream();
        if (insulin) {
            String[] insulinCsv = insulinList
                    .stream()
                    .map(i -> "insulin" + "," + i.getInsulin().toString() + "," + i.getType().toString() + ","
                            + i.getTime().format(CustomDateTimeFormatter.formatter) + "," + i.getNote() + "\n")
                    .toArray(String[]::new);
            insulinStream = new ByteArrayInputStream(String.join("", Arrays.asList(insulinCsv)).getBytes(StandardCharsets.UTF_8));
        }

        return new SequenceInputStream(sugarStream, insulinStream);
    }
}