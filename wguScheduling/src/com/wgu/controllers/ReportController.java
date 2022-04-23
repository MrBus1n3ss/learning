package com.wgu.controllers;

import com.wgu.models.Appointment;
import com.wgu.models.Customer;
import com.wgu.models.User;
import com.wgu.service.impl.AppointmentDaoImpl;
import com.wgu.service.impl.CustomerDaoImpl;
import com.wgu.service.impl.UserDaoImpl;
import com.wgu.utility.MenuState;
import com.wgu.utility.Months;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ReportController implements Initializable {

    @FXML
    private TextArea text_area_report;
    private Months monthsDetails = new Months();

    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private ObservableList<User> allUserList = FXCollections.observableArrayList();
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("MainMenu", actionEvent);
    }

    public void OnActionTypeReport() {
        Map<Integer, HashMap<String, Integer>> totalTypeReportMap = new HashMap<>();
        for(int i = 1; i<=12; i++){
            totalTypeReportMap.put(i, new HashMap<>());
        }
        for(Appointment appointment : allAppointments) {
            String[] parseStart = appointment.getStart().split(" ");
            LocalDate startDate = LocalDate.parse(parseStart[0]);
            for(int i = 1; i<=12; i++) {
                if(i == startDate.getMonth().getValue()) {
                    if(totalTypeReportMap.get(i).containsKey(appointment.getType())) {
                        int count = totalTypeReportMap.get(i).get(appointment.getType());
                        totalTypeReportMap.get(i).put(appointment.getType(), count + 1);
                    } else {
                        totalTypeReportMap.get(i).put(appointment.getType(), 1);
                    }

                }
            }
        }
        StringBuilder data = new StringBuilder();
        for(Integer month : totalTypeReportMap.keySet()){
            data.append("-----------------------------------\n");
            data.append(monthsDetails.getMonthName(month - 1)).append("\n");
            for(String type : totalTypeReportMap.get(month).keySet()){
                data.append("    Meeting Types: ").append(type).append(" | number of meeting types: ").append(totalTypeReportMap.get(month).get(type)).append("\n");
            }
        }
        text_area_report.setText(data.toString());
    }

    public void OnActionConsultantReport() {
        HashMap<String, ArrayList<Appointment>> allScheduleConsultant = new HashMap<>();
        for(User user: allUserList) {
            allScheduleConsultant.put(user.getUserName(), new ArrayList<>());
        }
        for(User user : allUserList){
            for (Appointment appointment : allAppointments) {
                if(user.getUserId() == appointment.getUserId()) {
                    allScheduleConsultant.get(user.getUserName()).add(appointment);
                }
            }
        }
        StringBuilder data = new StringBuilder();
        data.append("Time is in UTC\n");
        for(String user : allScheduleConsultant.keySet()) {
            data.append("------------------------------------\n");
            data.append(user).append("\n");
            for(Appointment appointment : allScheduleConsultant.get(user)){
                data.append(appointment.getStart()).append(" ");
                data.append(appointment.getEnd()).append(" ");
                data.append(appointment.getLocation()).append(" ");
                data.append(CustomerDaoImpl.getCustomerById(appointment.getCustomerId()).getCustomerName()).append("\n");
            }
        }
        text_area_report.setText(data.toString());
    }

    public void OnActionCustomerReport() {
        HashMap<String, ArrayList<Appointment>> allCustomerMeetings = new HashMap<>();
        for(Customer customer : allCustomers) {
            allCustomerMeetings.put(customer.getCustomerName(), new ArrayList<>());
        }
        for(Customer customer : allCustomers)
            for(Appointment appointment : allAppointments) {
                if(customer.getCustomerId() == appointment.getCustomerId()) {
                    allCustomerMeetings.get(customer.getCustomerName()).add(appointment);
                }
            }
        StringBuilder data = new StringBuilder();
        for(String customer : allCustomerMeetings.keySet()) {
            data.append("-------------------------------------\n");
            data.append(customer).append(": ").append(allCustomerMeetings.get(customer).size()).append("\n");
        }
        text_area_report.setText(data.toString());
    }

    public void OnActionClear() {
        text_area_report.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allAppointments = AppointmentDaoImpl.getAllAppointments();
        allUserList = UserDaoImpl.getAllUsers();
        allCustomers = CustomerDaoImpl.getAllCustomer();
    }
}
