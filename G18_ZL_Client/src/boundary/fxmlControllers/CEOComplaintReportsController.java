package boundary.fxmlControllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.Quarters;
import entity.Store;
import entity.SurveyQuestion;
import entity.SurveySumAnswers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*

											1.
"SELECT Count(id_complaint) as count"+
"FROM complaint C"+
"WHERE Year(date_complaint) = "+year+" AND Month(date_complaint) = "+month

GET -> complaint/count/byYearMonth/year/m1 and m2 and m3
	
											2.
"SELECT id_store"+
"FROM survey s"+
"WHERE s.id_survey = "+surveyID

GET -> survey/storeid/surveyID		

											3.
"SELECT *"+
"FROM survey_question SQ"+
"WHERE SQ.id_survey = "+surveyID

GET -> survey_question/by/id_survey/surveyID											
			
											4.
"SELECT question"+
"FROM question Q"+
"WHERE Q.id_question = "+questionID

GET -> question/by/questionID
											
* */

public class CEOComplaintReportsController implements Initializable {
	
	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */

	@FXML
    private BarChart<String, Integer> barChartComplaints;

    @FXML
    private Button buttonDownload;

    @FXML
    private Button buttonShow;

    @FXML
    private ChoiceBox<String> choiceBoxQuarterComplaint;

    @FXML
    private ComboBox<String> choiceBoxSurveyPDF;

    @FXML
    private ChoiceBox<String> choiceBoxYearComplaint;

    @FXML
    private ComboBox<String> choiceBoxYearPDF;
	
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    private HashMap<String, HashMap<Integer, SurveyQuestion>> yearIdQuestionsHashMap;
    
    // the selected year
    private String year;
    
    // the first month in the selected quarter
    private int firstMonthInQuarter = 1;
    
    /* XYChart series to insert values in the line chart */
    XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	initChoiceBoxes();
    	choiceBoxYearComplaint.setOnAction(this::onYearComplaintSelection);
    	choiceBoxQuarterComplaint.setOnAction(this::onQuarterComplaintSelection);
    	barChartComplaints.setLegendVisible(false);
	}
    
    /* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
    
    /**
     * Action to do when years complaint choice box has a selection
     * 
     * @param event
     */
    public void onYearComplaintSelection(ActionEvent event) {
    	System.out.println(choiceBoxYearComplaint.getSelectionModel().getSelectedItem());
    	year = choiceBoxYearComplaint.getSelectionModel().getSelectedItem();
    	if(!choiceBoxQuarterComplaint.getSelectionModel().isEmpty()) {
    		buttonShow.setDisable(false);
    	}
    }
    
    /**
     * Action to do when quarter is selected
     * 
     * @param event
     */
    public void onQuarterComplaintSelection(ActionEvent event) {
    	Quarters q = Quarters.valueOf(choiceBoxQuarterComplaint.getSelectionModel().getSelectedItem());
    	firstMonthInQuarter += 3*q.ordinal();
    	
    	if(!choiceBoxYearComplaint.getSelectionModel().isEmpty()) {
    		buttonShow.setDisable(false);
    	}
    }
    
    /**
     * Action to do when show is pressed
     * 
     * @param event
     */
    @SuppressWarnings("unchecked")
	public void onShowPressed(ActionEvent event) {
    	barChartComplaints.getData().clear();
    	series = new XYChart.Series<String, Integer>();
    	ArrayList<Integer> countOfComplaintsInQuarter = (ArrayList<Integer>)MainController.getMyClient().send(MessageType.GET, "complaint/count/inQuarter/"+year+"/"+firstMonthInQuarter, null);
    	for(int i=0; i<3 ; i++) {
    		series.getData().add(new XYChart.Data<String, Integer>(firstMonthInQuarter+i+"", countOfComplaintsInQuarter.get(i)));
    	}
    	firstMonthInQuarter = 1;
    	barChartComplaints.getData().add(series);
    }
    
    /* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
    
    /**
     * initialize all choice boxes
     */
    private void initChoiceBoxes() {
    	initSurveyChoiceBoxes();
    	initComplaintsChoiceBoxes();
    }
    
    /**
     * initialize choice boxes for downloading PDF report
     */
    @SuppressWarnings("unchecked")
	private void initSurveyChoiceBoxes() {
    	ArrayList<String> yearsOfSurveys = (ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "survey/years", null);
    	if(yearsOfSurveys.size() == 0) return;
    	choiceBoxYearPDF.getItems().addAll(yearsOfSurveys);
    }
    
    /**
     * initialize all choice boxes for complaints chart
     */
    @SuppressWarnings("unchecked")
	private void initComplaintsChoiceBoxes() {
		ArrayList<String> yearsToChoose = (ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "complaint/years", null);
		choiceBoxYearComplaint.getItems().addAll(yearsToChoose);
		
		for(Quarters q : Quarters.values()) {
			choiceBoxQuarterComplaint.getItems().add(q.toString());
		}
    }
    
}
