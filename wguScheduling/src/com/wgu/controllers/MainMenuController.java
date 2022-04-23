package com.wgu.controllers;

import com.wgu.models.Appointment;
import com.wgu.service.impl.AppointmentDaoImpl;
import com.wgu.utility.LoginUser;
import com.wgu.utility.MenuState;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    private ZoneId systemDefaultZoneId = ZoneId.systemDefault();
    private ZoneId utcDefaultZoneId = ZoneId.of("UTC");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private MenuState menu = new MenuState();

    public void OnActionMainCustomer(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("MainCustomerView", actionEvent);
    }

    public void OnActionCustomer(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("CustomerView", actionEvent);
    }

    public void OnActionCalendar(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("CalendarView", actionEvent);
    }

    public void OnActionReport(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("ReportView", actionEvent);
    }

    public void OnActionUsers(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("UserView", actionEvent);
    }

    public void OnActionAddress(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("AddressView", actionEvent);
    }

    public void OnActionCity(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("CityView", actionEvent);
    }

    public void OnActionCountry(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("CountryView", actionEvent);
    }

    public void OnActionAppointment(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("AppointmentView", actionEvent);
    }

    public void OnActionExit(ActionEvent actionEvent) {
        menu.exitApp(actionEvent);
    }


    public void OnActionAdmin(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("AdminView", actionEvent);
    }

    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("MainMenu", actionEvent);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Appointment> allAppointments = AppointmentDaoImpl.getAppointmentByUser(LoginUser.getUserId());
        if(allAppointments != null) {
            for (Appointment a : allAppointments) {
                String[] parseStart = dateTimeFormatter.format(parseDateTime(a.getStart())).split(" ");
                LocalDate meetingDate = LocalDate.parse(parseStart[0]);
                String[] meetingTime = parseStart[1].split(":");
                LocalTime meetingLocalTime = LocalTime.of(Integer.parseInt(meetingTime[0]), Integer.parseInt(meetingTime[1]));

                if(LocalDate.now().equals(meetingDate)){
                    if(LocalTime.now().isBefore(meetingLocalTime)) {
                        int withinTheHour = meetingLocalTime.getHour() - LocalTime.now().getHour();
                        if(withinTheHour == 1) {
                            int getMinute =  LocalTime.now().getMinute() - meetingLocalTime.getMinute();
                            if(getMinute >= 45) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Meeting coming up");
                                alert.setHeaderText(a.getTitle() + " at " + a.getLocation());
                                alert.setContentText(" @ " + dateTimeFormatter.format(parseDateTime(a.getStart())) + " to " + dateTimeFormatter.format(parseDateTime(a.getEnd())));
                                alert.show();
                            }
                        }
                    }


                }


            }
        }
    }
}
