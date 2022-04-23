package com.wgu.controllers;

import com.wgu.models.Appointment;
import com.wgu.service.impl.AppointmentDaoImpl;
import com.wgu.service.impl.CustomerDaoImpl;
import com.wgu.utility.LoginUser;
import com.wgu.utility.MenuState;
import com.wgu.utility.Months;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;

public class CalenderController implements Initializable {

    @FXML
    private ToggleButton buttonAllSelector;
    @FXML
    private ToggleButton buttonWeekSelector;
    @FXML
    private ToggleButton buttonMonthSelector;
    @FXML
    private VBox vBoxCalendarView;

    private ObservableList<Appointment> allAppointmentList = FXCollections.observableArrayList();
    private ObservableList<Appointment> currentMonthAppointmentList = FXCollections.observableArrayList();
    private int lineSize = 400;
    private String mainFont = "Arial";
    private String dots = new String(new char[lineSize]).replace("\0", ".");

    private Calendar calendar = Calendar.getInstance();
    int currentYear = calendar.get(Calendar.YEAR);
    int currentMonth = calendar.get(Calendar.MONTH);
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    private Months monthsDetails = new Months();
    private ZoneId systemDefaultZoneId = ZoneId.systemDefault();
    private ZoneId utcDefaultZoneId = ZoneId.of("UTC");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private ZonedDateTime parseDateTime(String dateTimeString) {
        String[] parseString = dateTimeString.split(" ");
        return convertToSystemTimezone(LocalDate.parse(parseString[0]), LocalTime.parse(parseString[1]));
    }

    private ZonedDateTime convertToSystemTimezone(LocalDate date, LocalTime time) {
        ZonedDateTime systemDateTime = ZonedDateTime.of(date, time, utcDefaultZoneId);
        Instant zdtToInstant = systemDateTime.toInstant();
        return zdtToInstant.atZone(systemDefaultZoneId);
    }

    private int getDates(int i) {
        int getCurrentMonth = monthsDetails.getMonthsDay(calendar.get(Calendar.MONTH));
        int getPreviousMonth;
        int getCurrentYear = calendar.get(Calendar.YEAR);
        if( getCurrentYear % 4 == 0) {
            monthsDetails.updateMonthsDay(1, 29);
        } else {
            monthsDetails.updateMonthsDay(1, 28);
        }
        if( calendar.get(Calendar.MONTH) -1 < 0) {
            getPreviousMonth = 11;
        } else {
            getPreviousMonth = calendar.get(Calendar.MONTH) - 1;
        }
        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_MONTH) + (i - calendar.get(Calendar.DAY_OF_WEEK) + 1);
        if( dayOfTheWeek < 1) {
            dayOfTheWeek = monthsDetails.getMonthsDay(getPreviousMonth) + (i + 2 - calendar.get(Calendar.DAY_OF_WEEK));
        } else if(dayOfTheWeek > getCurrentMonth) {
            dayOfTheWeek = calendar.get(Calendar.DAY_OF_MONTH) + (i - calendar.get(Calendar.DAY_OF_WEEK) + 1) - getCurrentMonth;
        }
        return dayOfTheWeek;
    }

    private Label createLabel(String text, String font, double fontSize, Pos alignment){
        Label label = new Label(text);
        label.setFont(new Font(font, fontSize));
        label.getStyleClass().add("all-text");
        label.setAlignment(alignment);
        return label;
    }

    private VBox createWeekDay(int i, int dayOfTheWeek){
        VBox vBoxDay = new VBox();
        vBoxDay.setMinWidth(125);
        vBoxDay.getChildren().add(weekDayNameAndNumber(i, dayOfTheWeek));
        return  vBoxDay;
    }

    private VBox createMonthDay(int i) {
        VBox vBox = new VBox();
        vBox.getChildren().add(createLabel(String.valueOf(i), mainFont, 25, Pos.TOP_CENTER));
        vBox.getChildren().add(new Label(dots));
        return vBox;
    }

    private ScrollPane createMonthView(){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("month-scroll-pane");
        VBox vBox = new VBox();
        String getCurrentYear = String.valueOf(currentYear);
        String getCurrentMonth = monthsDetails.getMonthName(currentMonth);
        vBox.getChildren().add(createLabel(
                String.format("%s %s", getCurrentYear, getCurrentMonth),
                "Arial",
                30,
                Pos.TOP_CENTER)
        );
        int getTotalMonthDays = monthsDetails.getMonthsDay(calendar.get(Calendar.MONTH));
        vBox.getChildren().add(new Label(dots));
        for(int i=0; i <= getTotalMonthDays; i++) {
            for (Appointment appointment : currentMonthAppointmentList) {
                String[] parseStartDate = appointment.getStart().split(" ");
                LocalDate startDate = LocalDate.parse(parseStartDate[0]);
                if(startDate.getDayOfMonth() == i){
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10.0);
                    gridPane.add(createLabel(CustomerDaoImpl.getCustomerById(appointment.getCustomerId()).getCustomerName(), mainFont, 15, Pos.TOP_LEFT), 0, 0, 1, 1);
                    gridPane.add(createLabel(appointment.getTitle(), mainFont, 12, Pos.TOP_LEFT), 1, 0, 1, 1);
                    gridPane.add(createLabel(appointment.getDescription(), mainFont, 12, Pos.TOP_LEFT), 2, 0, 1, 1);
                    gridPane.add(createLabel(appointment.getDescription(), mainFont, 12, Pos.TOP_LEFT), 3, 0, 1, 1);
                    gridPane.add(createLabel(appointment.getContact(), mainFont, 12, Pos.TOP_LEFT), 4, 0, 1, 1);
                    gridPane.add(createLabel(appointment.getLocation(), mainFont, 12, Pos.TOP_LEFT), 5, 0, 1, 1);
                    gridPane.add(createLabel(appointment.getUrl(), mainFont, 12, Pos.TOP_LEFT), 6, 0, 1, 1);
                    gridPane.add(createLabel(dateTimeFormatter.format(parseDateTime(appointment.getStart())), mainFont, 12, Pos.TOP_LEFT), 7, 0, 1, 1);
                    gridPane.add(createLabel(dateTimeFormatter.format(parseDateTime(appointment.getEnd())), mainFont, 12, Pos.TOP_LEFT), 8, 0, 1, 1);
                    vBox.getChildren().add(gridPane);
                }
            }
            vBox.getChildren().add(createMonthDay(i));
        }
        scrollPane.setContent(vBox);
        return scrollPane;
    }

    private VBox weekDayNameAndNumber(int i, int day){
        VBox vbox = new VBox();
        String dayName = "Unknown";
        if(i == 0) {
            dayName = "Sunday";
        } else if (i == 1) {
            dayName = "Monday";
        }else if (i == 2) {
            dayName = "Tuesday";
        } else if (i == 3) {
            dayName = "Wednesday";
        } else if (i == 4) {
            dayName = "Thursday";
        } else if (i == 5) {
            dayName = "Friday";
        } else if (i == 6) {
            dayName = "Saturday";
        } else {
            dayName = "Unknown";
        }
        vbox.getChildren().add(createLabel(dayName, mainFont, 18, Pos.TOP_LEFT));
        vbox.getChildren().add(createLabel(String.valueOf(day), mainFont, 30, Pos.TOP_LEFT));
        return vbox;
    }

    private VBox createWeek() {
        VBox vBoxWeek = new VBox();
        String startingDate = "";
        String endingDate = "";
        String getCurrentYear = String.valueOf(currentYear);
        String getCurrentMonth = monthsDetails.getMonthName(currentMonth);
        vBoxWeek.getChildren().add(createLabel(
                String.format("%s %s", getCurrentYear, getCurrentMonth),
                "Arial",
                18,
                Pos.TOP_CENTER)
        );
        Label date = createLabel("DATE", "Arial", 18, Pos.TOP_CENTER);
        vBoxWeek.getChildren().add(date);
        vBoxWeek.getChildren().add(createLabel(dots, mainFont, 12, Pos.TOP_CENTER));
        for(int i=0; i < 7; i++) {
            HBox hBoxDay = new HBox();
            int dayOfTheWeek;
                if(i == 0) {
                    dayOfTheWeek = getDates(i);
                    startingDate = String.valueOf(dayOfTheWeek);
                    String dateLabelText = String.format("The Week of %s %s",
                            getCurrentMonth,
                            startingDate);
                    date.setText(dateLabelText);
                } else if (i == 1) {
                    dayOfTheWeek = getDates(i);
                }else if (i == 2) {
                    dayOfTheWeek = getDates(i);
                } else if (i == 3) {
                    dayOfTheWeek = getDates(i);
                } else if (i == 4) {
                    dayOfTheWeek = getDates(i);
                } else if (i == 5) {
                    dayOfTheWeek = getDates(i);
                } else {
                    dayOfTheWeek = getDates(i);
                }
            hBoxDay.getChildren().add(createWeekDay(i, dayOfTheWeek));
            vBoxWeek.getChildren().add(hBoxDay);
            for (Appointment appointment : currentMonthAppointmentList) {
                String[] parseStartDate = appointment.getStart().split(" ");
                LocalDate startDate = LocalDate.parse(parseStartDate[0]);
                if(startDate.getDayOfMonth() == dayOfTheWeek){
                    HBox hBox = new HBox();

                    dateTimeFormatter.format(parseDateTime(appointment.getEnd()));
                    parseDateTime(appointment.getStart());

                    hBox.setSpacing(10.0);
                    hBox.getChildren().add(createLabel(CustomerDaoImpl.getCustomerById(appointment.getCustomerId()).getCustomerName(), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(appointment.getTitle(), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(appointment.getDescription(), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(appointment.getDescription(), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(appointment.getContact(), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(appointment.getLocation(), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(appointment.getUrl(), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(dateTimeFormatter.format(parseDateTime(appointment.getStart())), mainFont, 12, Pos.TOP_LEFT));
                    hBox.getChildren().add(createLabel(dateTimeFormatter.format(parseDateTime(appointment.getEnd())), mainFont, 12, Pos.TOP_LEFT));
                    vBoxWeek.getChildren().add(hBox);
                }
            }


            vBoxWeek.getChildren().add(createLabel(dots, mainFont, 12, Pos.TOP_CENTER));

        }
        return vBoxWeek;
    }

    private VBox createMonth() {
        VBox vBoxMonth = new VBox();
        vBoxMonth.getChildren().add(createMonthView());
        return vBoxMonth;
    }

    private VBox createAll() {
        VBox vBoxAllAppointments = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10,10,10));
        int count = 0;
        for(Appointment a : allAppointmentList) {
            if(a.getUserId() == LoginUser.getUserId())
            {
                gridPane.add(createLabel(CustomerDaoImpl.getCustomerById(a.getCustomerId()).getCustomerName(), mainFont, 12, Pos.TOP_LEFT), 0, count, 1, 1);
                gridPane.add(createLabel(a.getTitle(), mainFont, 12, Pos.TOP_LEFT), 1, count, 1, 1);
                gridPane.add(createLabel(a.getDescription(), mainFont, 12, Pos.TOP_LEFT), 2, count, 1, 1);
                gridPane.add(createLabel(a.getDescription(), mainFont, 12, Pos.TOP_LEFT), 3, count, 1, 1);
                gridPane.add(createLabel(a.getContact(), mainFont, 12, Pos.TOP_LEFT), 4, count, 1, 1);
                gridPane.add(createLabel(a.getLocation(), mainFont, 12, Pos.TOP_LEFT), 5, count, 1, 1);
                gridPane.add(createLabel(a.getUrl(), mainFont, 12, Pos.TOP_LEFT), 6, count, 1, 1);
                gridPane.add(createLabel(dateTimeFormatter.format(parseDateTime(a.getStart())), mainFont, 12, Pos.TOP_LEFT), 7, count, 1, 1);
                gridPane.add(createLabel(dateTimeFormatter.format(parseDateTime(a.getEnd())), mainFont, 12, Pos.TOP_LEFT), 8, count, 1, 1);
                count++;
            }

        }
        if (gridPane.getChildren().size() <= 0){
            vBoxAllAppointments.getChildren().add(createLabel("No appointments", mainFont, 12, Pos.TOP_LEFT));
        } else {
            vBoxAllAppointments.getChildren().add(gridPane);
        }

        return vBoxAllAppointments;
    }

    private void disableWeekMonthSelector() {
        if(buttonAllSelector.isSelected()){
            buttonMonthSelector.setDisable(false);
            buttonWeekSelector.setDisable(false);
            buttonAllSelector.setDisable(true);
        } else if(buttonWeekSelector.isSelected()){
            buttonMonthSelector.setDisable(false);
            buttonWeekSelector.setDisable(true);
            buttonAllSelector.setDisable(false);
        } else if(buttonMonthSelector.isSelected()){
            buttonMonthSelector.setDisable(true);
            buttonWeekSelector.setDisable(false);
            buttonAllSelector.setDisable(false);
        }
    }

    @FXML
    private void OnActionShowWeek() {
        disableWeekMonthSelector();
        if(vBoxCalendarView.getChildren().size() > 1) {
            vBoxCalendarView.getChildren().remove(1);
        }
        vBoxCalendarView.getChildren().add(createWeek());
    }

    @FXML
    private void OnActionShowMonth() {
        disableWeekMonthSelector();
        if(vBoxCalendarView.getChildren().size() > 1) {
            vBoxCalendarView.getChildren().remove(1);
        }
        vBoxCalendarView.getChildren().add(createMonth());

    }

    @FXML
    private void OnActionShowAll() {
        disableWeekMonthSelector();
        if(vBoxCalendarView.getChildren().size() > 1) {
            vBoxCalendarView.getChildren().remove(1);
        }
        vBoxCalendarView.getChildren().add(createAll());
    }

    @FXML
    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("MainMenu", actionEvent);
    }

    public void OnActionAddAppointment(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("AppointmentView", actionEvent);
    }

    public void OnActionCustomer(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("CustomerView", actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonWeekSelector.setDisable(true);
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        vBoxCalendarView.getChildren().add(createWeek());
        vBoxCalendarView.setAlignment(Pos.TOP_CENTER);
        allAppointmentList = AppointmentDaoImpl.getAllAppointments();
        for(Appointment a : allAppointmentList) {
            String[] parseStart = a.getStart().split(" ");
            LocalDate startDate = LocalDate.parse(parseStart[0]);
            if(currentMonth+1 == startDate.getMonth().getValue()) {
                currentMonthAppointmentList.add(a);
            }
        }
        OnActionShowWeek();
    }

}
