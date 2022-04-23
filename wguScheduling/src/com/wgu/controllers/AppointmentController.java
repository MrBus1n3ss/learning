package com.wgu.controllers;

import com.wgu.models.Appointment;
import com.wgu.models.Customer;
import com.wgu.service.impl.AppointmentDaoImpl;
import com.wgu.service.impl.CustomerDaoImpl;
import com.wgu.utility.CheckIfEmpty;
import com.wgu.utility.LoginUser;
import com.wgu.utility.MenuState;
import com.wgu.utility.ClockUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {
    @FXML
    private Label lbl_customer;
    @FXML
    private Label lbl_title;
    @FXML
    private Label lbl_desc;
    @FXML
    private Label lbl_location;
    @FXML
    private Label lbl_contact;
    @FXML
    private Label lbl_type;
    @FXML
    private Label lbl_url;
    @FXML
    private Label lbl_appointment_time;
    @FXML
    private TableView<Appointment> table_appointment;
    @FXML
    private TableColumn<String, Appointment> tbl_col_title;
    @FXML
    private TableColumn<String, Appointment> tbl_col_desc;
    @FXML
    private TableColumn<String, Appointment> tbl_col_location;
    @FXML
    private TableColumn<String, Appointment> tbl_col_contact;
    @FXML
    private TableColumn<String, Appointment> tbl_col_type;
    @FXML
    private TableColumn<String, Appointment> tbl_col_url;
    @FXML
    private TableColumn<String, Appointment> tbl_col_start;
    @FXML
    private TableColumn<String, Appointment> tbl_col_end;
    @FXML
    private ComboBox<Customer> combobox_customer;
    @FXML
    private TextField txt_field_title;
    @FXML
    private TextArea txt_area_desc;
    @FXML
    private TextField txt_field_location;
    @FXML
    private TextField txt_field_contact;
    @FXML
    private TextField txt_field_type;
    @FXML
    private TextField txt_field_url;
    @FXML
    private DatePicker date_picker_start;
    @FXML
    private ComboBox<ClockUtility> combo_box_start_time;
    @FXML
    private ComboBox<ClockUtility> combo_box_end_time;
    @FXML
    private Button btn_add;
    @FXML
    private Label lbl_appointment_id;


    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private ObservableList<ClockUtility> covertTimeList = FXCollections.observableArrayList();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Appointment modifyAppointment;
    private ZoneId systemDefaultZoneId = ZoneId.systemDefault();
    private ZoneId utcDefaultZoneId = ZoneId.of("UTC");
    private CheckIfEmpty checkIfEmpty = new CheckIfEmpty();

    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("MainMenu", actionEvent);
    }

    public void OnActionEdit() {
        try {
            String[] parseStart = table_appointment.getSelectionModel().getSelectedItem().getStart().split(" ");
            String[] parseEnd = table_appointment.getSelectionModel().getSelectedItem().getEnd().split(" ");
            lbl_appointment_id.setText(Integer.toString(table_appointment.getSelectionModel().getSelectedItem().getAppointmentId()));
            System.out.println(table_appointment.getSelectionModel().getSelectedItem().getCustomerId());
            System.out.println(CustomerDaoImpl.getCustomerById(table_appointment.getSelectionModel().getSelectedItem().getCustomerId()));
            combobox_customer.setValue(CustomerDaoImpl.getCustomerById(table_appointment.getSelectionModel().getSelectedItem().getCustomerId()));
            txt_field_title.setText(table_appointment.getSelectionModel().getSelectedItem().getTitle());
            txt_area_desc.setText(table_appointment.getSelectionModel().getSelectedItem().getDescription());
            txt_field_location.setText(table_appointment.getSelectionModel().getSelectedItem().getLocation());
            txt_field_contact.setText(table_appointment.getSelectionModel().getSelectedItem().getContact());
            txt_field_type.setText(table_appointment.getSelectionModel().getSelectedItem().getType());
            txt_field_url.setText(table_appointment.getSelectionModel().getSelectedItem().getUrl());
            combo_box_start_time.setValue(covertTimeList.get(LocalTime.parse(parseStart[1]).getHour()));
            combo_box_end_time.setValue(covertTimeList.get(LocalTime.parse(parseEnd[1]).getHour()));
            date_picker_start.setValue(LocalDate.parse(parseStart[0]));
            modifyAppointment = table_appointment.getSelectionModel().getSelectedItem();
            btn_add.setText("Modify");
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: Please make a selection");
        }
    }


    public void OnActionDelete() {
        try {
            AppointmentDaoImpl.deleteAppointment(table_appointment.getSelectionModel().getSelectedItem());
            allAppointments = AppointmentDaoImpl.getAllAppointments();
            displayTable();
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception, please make a selection in the table");
        }
    }


    // checks if the currentDate is in the future
    private boolean checkAppointmentDate() {
        LocalDate datePicker = date_picker_start.getValue();
        LocalDate currentDate = LocalDate.now();
        if (datePicker.isAfter(currentDate)) {
            return true;
        } else return datePicker.isEqual(currentDate) && checkAppointmentHour();
    }

    private boolean checkAppointmentHour() {
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(combo_box_start_time.getValue().getMilitaryTime(), 0);
        return startTime.isAfter(currentTime);
    }


    // Checks if between 6am to 5pm
    // Checks if start_time < end_time
    private boolean checkWorkingHours() {
        int startTime = combo_box_start_time.getValue().getMilitaryTime();
        int endTime = combo_box_end_time.getValue().getMilitaryTime();
        return startTime >= 6 && endTime <= 17 && startTime < endTime;
    }

    private boolean checkAppointmentTimes() {
        // Parse user selection to local datetime
        LocalDate datePicker = date_picker_start.getValue();
        LocalTime startTime = LocalTime.of(combo_box_start_time.getValue().getMilitaryTime(), 0);
        LocalTime endTime = LocalTime.of(combo_box_end_time.getValue().getMilitaryTime(), 0);
        LocalDateTime currentAppointmentStartDateTime = LocalDateTime.of(datePicker, startTime);
        LocalDateTime currentAppointmentEndDateTime = LocalDateTime.of(datePicker, endTime);
        int modifyAppointmentId = -1;
        if (modifyAppointment != null) {
            modifyAppointmentId = modifyAppointment.getAppointmentId();
        }
        for (Appointment a : allAppointments) {
            // Parse to LocalDateTime
            String[] parseStart = a.getStart().split(" ");
            String[] parseEnd = a.getEnd().split(" ");
            LocalDateTime appointmentStartDateTime = LocalDateTime.of(LocalDate.parse(parseStart[0]), LocalTime.parse(parseStart[1]));
            LocalDateTime appointmentEndDateTime = LocalDateTime.of(LocalDate.parse(parseEnd[0]), LocalTime.parse(parseEnd[1]));
            if (appointmentStartDateTime.getMonth() == currentAppointmentStartDateTime.getMonth() && modifyAppointmentId != a.getAppointmentId()) {
                if (appointmentStartDateTime.getDayOfMonth() == currentAppointmentStartDateTime.getDayOfMonth()) {
                    if (currentAppointmentStartDateTime.getHour() == appointmentEndDateTime.getHour() || currentAppointmentEndDateTime.getHour() == appointmentStartDateTime.getHour()) {
                        System.out.println("Start and end time start at the end/beginning of an appointment");
                    } else {
                        for (int i = appointmentStartDateTime.getHour(); i <= appointmentEndDateTime.getHour(); i++) {
                            for (int j = currentAppointmentStartDateTime.getHour(); j <= currentAppointmentEndDateTime.getHour(); j++) {
                                if (i == j) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private ZonedDateTime convertToUtc(LocalDate date, LocalTime time) {
        ZonedDateTime systemDateTime = ZonedDateTime.of(date, time, systemDefaultZoneId);
        Instant zdtToInstant = systemDateTime.toInstant();
        return zdtToInstant.atZone(utcDefaultZoneId);
    }

    private ZonedDateTime convertToSystemTimezone(LocalDate date, LocalTime time) {
        ZonedDateTime systemDateTime = ZonedDateTime.of(date, time, utcDefaultZoneId);
        Instant zdtToInstant = systemDateTime.toInstant();
        return zdtToInstant.atZone(systemDefaultZoneId);
    }

    private ZonedDateTime parseDateTime(String dateTimeString) {
        String[] parseString = dateTimeString.split(" ");
        return convertToSystemTimezone(LocalDate.parse(parseString[0]), LocalTime.parse(parseString[1]));
    }

    private void checkEmpty(){
        if(combobox_customer.getSelectionModel().isEmpty()) {
            lbl_customer.setTextFill(Color.RED);
        } else {
            lbl_customer.setTextFill(Color.WHITE);
        }
        checkIfEmpty.CheckStringEmpty(lbl_title, txt_field_title.getText());
        checkIfEmpty.CheckStringEmpty(lbl_desc, txt_area_desc.getText());
        checkIfEmpty.CheckStringEmpty(lbl_location, txt_field_location.getText());
        checkIfEmpty.CheckStringEmpty(lbl_contact, txt_field_contact.getText());
        checkIfEmpty.CheckStringEmpty(lbl_type, txt_field_type.getText());
        checkIfEmpty.CheckStringEmpty(lbl_url, txt_field_url.getText());

    }

    public void OnActionAdd() {
        try {
            ZonedDateTime startTime = convertToUtc(date_picker_start.getValue(), LocalTime.of(combo_box_start_time.getValue().getMilitaryTime(), 0));
            ZonedDateTime endTime = convertToUtc(date_picker_start.getValue(), LocalTime.of(combo_box_end_time.getValue().getMilitaryTime(), 0));
            boolean isWorkingHours = checkWorkingHours();
            boolean isAppointmentDate = checkAppointmentDate();
            boolean isAppointmentTime = checkAppointmentTimes();

            if (isWorkingHours) {
                if (isAppointmentDate) {
                    if (isAppointmentTime) {
                        checkEmpty();
                        if (modifyAppointment == null) {
                            AppointmentDaoImpl.addAppointment(
                                    combobox_customer.getValue().getCustomerId(),
                                    LoginUser.getUserId(),
                                    txt_field_title.getText(),
                                    txt_area_desc.getText(),
                                    txt_field_location.getText(),
                                    txt_field_contact.getText(),
                                    txt_field_type.getText(),
                                    txt_field_url.getText(),
                                    dateTimeFormatter.format(startTime),
                                    dateTimeFormatter.format(endTime));
                        } else {
                            modifyAppointment.setCustomerId(combobox_customer.getValue().getCustomerId());
                            modifyAppointment.setUserId(LoginUser.getUserId());
                            modifyAppointment.setTitle(txt_field_title.getText());
                            modifyAppointment.setDescription(txt_area_desc.getText());
                            modifyAppointment.setLocation(txt_field_location.getText());
                            modifyAppointment.setContact(txt_field_contact.getText());
                            modifyAppointment.setType(txt_field_type.getText());
                            modifyAppointment.setUrl(txt_field_url.getText());
                            modifyAppointment.setStart(dateTimeFormatter.format(startTime));
                            modifyAppointment.setEnd(dateTimeFormatter.format(endTime));
                            AppointmentDaoImpl.updateAppointment(modifyAppointment);
                        }
                        displayTable();
                        OnActionClear();
                    } else {
                        System.out.println("Appointment Times conflict");
                    }
                } else {
                    System.out.println("Appointment is before or equal " + LocalDate.now());
                }

            } else {
                System.out.println("Hours are before or after business hours.  Or Start time is before end time.");
                System.out.println("Business hours are 6am to 5pm");
            }

        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception, please fill in the fields");
        }
    }

    public void OnActionClear() {
        lbl_appointment_id.setText("Unknown");
        combobox_customer.setValue(null);
        btn_add.setText("Add");
        txt_field_title.setText("");
        txt_area_desc.setText("");
        txt_field_location.setText("");
        txt_field_contact.setText("");
        txt_field_type.setText("");
        txt_field_url.setText("");
        date_picker_start.setValue(null);
        combo_box_end_time.setValue(null);
        combo_box_start_time.setValue(null);
        modifyAppointment = null;
    }

    private void displayTable() {
        allAppointments = AppointmentDaoImpl.getAllAppointments();
        table_appointment.setItems(allAppointments);
        //convert All appointments to system timezone
        allAppointments.forEach(a -> {
            a.setStart(dateTimeFormatter.format(parseDateTime(a.getStart())));
            a.setEnd(dateTimeFormatter.format(parseDateTime(a.getEnd())));
        });
        tbl_col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tbl_col_desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbl_col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
        tbl_col_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        tbl_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tbl_col_url.setCellValueFactory(new PropertyValueFactory<>("url"));
        tbl_col_start.setCellValueFactory(new PropertyValueFactory<>("start"));
        tbl_col_end.setCellValueFactory(new PropertyValueFactory<>("end"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Add user data to dashboard
        displayTable();

        // Build the combo box
        ObservableList<Customer> allCustomers = CustomerDaoImpl.getAllCustomer();
        if (allCustomers != null) {
            // build the customer combo box
            allCustomers.forEach(c -> {
                if (c.getActive() == 1) combobox_customer.getItems().add(c);
            });
        }

        // Build Start and End time combo boxes
        String convertedTime = "";
        for (int i = 0; i <= 23; i++) {
            if (i == 0) {
                convertedTime = (i + 12) + ":00 am";
            } else if (i <= 11) {
                convertedTime = i + ":00 am";
            } else if (i == 12) {
                convertedTime = (i) + ":00 pm";
            } else {
                convertedTime = (i - 12) + ":00 pm";
            }
            covertTimeList.add(new ClockUtility(i, convertedTime));
        }
        combo_box_start_time.setItems(covertTimeList);
        combo_box_end_time.setItems(covertTimeList);

    }

}
