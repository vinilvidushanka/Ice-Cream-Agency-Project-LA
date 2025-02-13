
package lk.ijse.agencyla.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.agencyla.bo.BOFactory;
import lk.ijse.agencyla.bo.custom.CreditBillBO;
import lk.ijse.agencyla.bo.custom.CustomerBO;
import lk.ijse.agencyla.bo.custom.EmployeeBO;
import lk.ijse.agencyla.bo.custom.RouteBO;
import lk.ijse.agencyla.dao.custom.impl.RouteDAOImpl;
import lk.ijse.agencyla.db.DBConnection;

import lk.ijse.agencyla.dto.CustomerDTO;
import lk.ijse.agencyla.dto.RouteDTO;
import lk.ijse.agencyla.entity.Route;
import lk.ijse.agencyla.view.tdm.CustomerTM;
import lk.ijse.agencyla.view.tdm.RouteTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashBoardFormController {

    @FXML
    private TableColumn<?, ?> colDay;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colRoute;

    @FXML
    private TableColumn<?, ?> colVanId;
    @FXML
    public TableView<RouteTM> tblVan;

    @FXML
    private Label lblCusCount;

    @FXML
    private Label lblName;

    @FXML
    private AnchorPane root;
    public AnchorPane rootNode;
    @FXML
    private AnchorPane Load;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblEmpCount;

    private int customerCount;

    private double creditAmount;

    private int employeeCount;

    private String name;

    public DashBoardFormController() {
    }

    RouteBO routeBO  = (RouteBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ROUTE);
    CustomerBO customerBO  = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    EmployeeBO employeeBO  = (EmployeeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.EMPLOYEE);
    CreditBillBO creditBillBO  = (CreditBillBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CREDIT_BILL);

    private void setCustomerCount(int customerCount) {
        lblCusCount.setText(String.valueOf(customerCount));
    }

    private void setEmployeeCount(int employeeCount) {
        lblEmpCount.setText(String.valueOf(employeeCount));
    }

    private void setCreditAmount(double creditAmount) {
        lblAmount.setText(String.valueOf(creditAmount));
    }

    public void initialize() throws SQLException {

        setCellValueFactory();
        loadAllRoute();
        initClock();

        //get customer count
        try {
            customerCount = customerBO.getCustomerCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setCustomerCount(customerCount);

        //get employee count
        try {
            employeeCount = employeeBO.getEmployeeCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setEmployeeCount(employeeCount);

        //get credit bill ammount
        try {
            creditAmount = creditBillBO.getCreditAmount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setCreditAmount(creditAmount);

    }

    private void loadAllRoute() {
        tblVan.getItems().clear();
        try {
            ArrayList<RouteDTO> allRoutes = routeBO.getAllRoutes();

            for (RouteDTO r : allRoutes) {
                tblVan.getItems().add(new RouteTM(r.getRouteId(), r.getName(),r.getVanId(), r.getDay()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd        HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }



    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("routeId"));
        colRoute.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colVanId.setCellValueFactory(new PropertyValueFactory<>("vanId"));

    }


    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/customer_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnOrdersOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/orders_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/employee_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }


    public void btnDaillyTransactionOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/daily_transaction.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnCreditBillsOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/credit_bill.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnStocksOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/stock_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }


    public void btnDailyLoadingOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/daily_loading.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }


    public void btnExpensesOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/expenses_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }

    public void btnExitOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
        stage.setTitle("Login Form");
    }

    public void btnSalaryOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/salary_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        Load.getChildren().clear();
        Load.getChildren().add(load);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), Load);
        transition.setFromX(load.getScene().getWidth());
        transition.setToX(0);
        transition.play();
    }



    public void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }
}
