package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("deprecation")
public class TemperatureWatcher implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();

	private DnDFlexTable temperatureDnDFlextable1;
	private DnDFlexTable temperatureDnDFlextable2;
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newCountryTextBox = new TextBox();
	private TextBox newCityTextBox = new TextBox();
	private TextBox newAreaTextBox = new TextBox();
	private Button addCityButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
//	private ArrayList<Temperature> listOfTemperatures = new ArrayList<Temperature>();
//	private ArrayList<Temperature> listOfTemperatures2 = new ArrayList<Temperature>();
	private TemperatureServiceAsync temperaturesSvc = GWT.create(TemperatureService.class);
	private Label errorMsgLabel = new Label();
	private final static int REFRESH_INTERVAL =5000*1000; //to long. change to *10
	private Temperature currentPlace = null;
	private AbsolutePanel absolutePanel;
	private FlexTableDragController tableDragController;

	public void onModuleLoad() {
		createDnDFlexTables();
	}

	private void createDnDFlexTables() {

		absolutePanel = new AbsolutePanel();
		absolutePanel.setPixelSize(850, 800);
		RootPanel.get("stockList").add(absolutePanel);

		final DragHandler demoDragHandler = new DragHandlerAdapter();

		tableDragController = new FlexTableDragController(absolutePanel);
		tableDragController.addDragHandler(demoDragHandler);

		temperatureDnDFlextable1 = new DnDFlexTable();
		temperatureDnDFlextable2 = new DnDFlexTable();

		createLayout();

		FlexTableDropController flexTableDropController1 = new FlexTableDropController(temperatureDnDFlextable1);
		FlexTableDropController flexTableDropController2 = new FlexTableDropController(temperatureDnDFlextable2);
		tableDragController.registerDropController(flexTableDropController1);
		tableDragController.registerDropController(flexTableDropController2);

		initCities();

		// Setup timer to refresh list automatically. Refresh for the other table?
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshWatchList(temperatureDnDFlextable1);
				refreshWatchList(temperatureDnDFlextable2);
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

	}

	/**
	 * Creates the basic layout along with some listeners on functionality
	 */
	
	private void createLayout() {

		// Add the Widgets
		newCountryTextBox.setTitle("Country");
		newAreaTextBox.setTitle("Region");
		newCityTextBox.setTitle("City");

		addPanel.add(newCountryTextBox);
		addPanel.add(newAreaTextBox);
		addPanel.add(newCityTextBox);
		addPanel.add(addCityButton);
		addPanel.addStyleName("addPanel");

		errorMsgLabel.setStyleName("errorMessage");
		errorMsgLabel.setVisible(false);



		//Creates the first FlexTable
		HTML handle = new HTML("Country");
		handle.addStyleName("drag-handle");
		temperatureDnDFlextable1.setWidget(0, 0, handle);		
		temperatureDnDFlextable1.setText(0, 1, "Area");
		temperatureDnDFlextable1.setText(0, 2, "City");
		temperatureDnDFlextable1.setText(0, 3, "Temp");
		temperatureDnDFlextable1.setText(0, 4, "Change");
		temperatureDnDFlextable1.setText(0, 5, "Remove");
		temperatureDnDFlextable1.setCellPadding(6);


		// Add styles to elements in the stock list table.
		temperatureDnDFlextable1.getRowFormatter().addStyleName(0, "watchListHeader");
		temperatureDnDFlextable1.addStyleName("watchList");	
		temperatureDnDFlextable1.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
		temperatureDnDFlextable1.getCellFormatter().addStyleName(0, 4, "watchListRemoveColumn");

		//Creates the second FlexTable
		HTML handle2 = new HTML("Country");
		handle.addStyleName("drag-handle");
		temperatureDnDFlextable2.setWidget(0, 0, handle2);
		temperatureDnDFlextable2.setText(0, 1, "Area");
		temperatureDnDFlextable2.setText(0, 2, "City");
		temperatureDnDFlextable2.setText(0, 3, "Temp");
		temperatureDnDFlextable2.setText(0, 4, "Change");
		temperatureDnDFlextable2.setText(0, 5, "Remove");
		temperatureDnDFlextable2.setCellPadding(6);

		// Add styles to elements in the stock list table.
		temperatureDnDFlextable2.getRowFormatter().addStyleName(0, "watchListHeader");
		temperatureDnDFlextable2.addStyleName("watchList");
		temperatureDnDFlextable2.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
		temperatureDnDFlextable2.getCellFormatter().addStyleName(0, 4, "watchListRemoveColumn");

		// Add the Widgets to the absolutePanel
		absolutePanel.add(errorMsgLabel);
		absolutePanel.add(temperatureDnDFlextable1);			
		absolutePanel.add(addPanel);
		absolutePanel.add(lastUpdatedLabel);
		absolutePanel.add(temperatureDnDFlextable2);
		newCountryTextBox.setFocus(true);


		// Associate the Main panel with the HTML host page.
		RootPanel.get().add(mainPanel);
		
		//Adding some listeners

		// Listen for mouse events on the Add button.
		addCityButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				temperatureDnDFlextable1.setListOfTemperatures(addCity(temperatureDnDFlextable1)); 
			}
		});
		// Listen for keyboard events in the input box.
		newCountryTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					temperatureDnDFlextable1.setListOfTemperatures(addCity(temperatureDnDFlextable1)); 
				}
			}
		});
		newCityTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					temperatureDnDFlextable1.setListOfTemperatures(addCity(temperatureDnDFlextable1));  
				}
			}
		});
		newAreaTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					temperatureDnDFlextable1.setListOfTemperatures(addCity(temperatureDnDFlextable1)); 
				}
			}
		});

	}

	/**
	 * Add stock to FlexTable. Executed when the user clicks the addStockButton or
	 * presses enter in the newSymbolTextBox.
	 */
	private ArrayList<Temperature> addCity(final DnDFlexTable temperatureDnDFlextableParam) {
		final String country = newCountryTextBox.getText().toUpperCase().trim();
		final String city = newCityTextBox.getText().toUpperCase().trim();
		final String area = newAreaTextBox.getText().toUpperCase().trim();
		newCountryTextBox.setFocus(true);

		// Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
		if (!country.matches("^[0-9A-ZÂ‰÷≈ƒ÷\\.]{1,10}$") || !city.matches("^[0-9A-ZÂ‰ˆ≈ƒ÷\\.]{1,10}$") || !area.matches("^[0-9A-ZÂ‰ˆ≈ƒ÷\\.]{1,10}$")) {
			Window.alert("Some input is not correct");
			newCountryTextBox.selectAll();
			return temperatureDnDFlextableParam.getListOfTemperatures();			
		}

		newCountryTextBox.setText("");
		newAreaTextBox.setText("");
		newCityTextBox.setText("");

		//Checks if the city is already in the flextable
		for(int i = 0; i<temperatureDnDFlextableParam.getListOfTemperatures().size(); i++){
			if (temperatureDnDFlextableParam.getListOfTemperatures().get(i).getCity().equals(city.toUpperCase())) return temperatureDnDFlextableParam.getListOfTemperatures();
		}
		
		//Adds the new city to the flextable
		
		final int row = temperatureDnDFlextableParam.getRowCount(); 
		Temperature tempTemp = new Temperature();
		tempTemp.setArea(area);
		tempTemp.setCity(city);
		tempTemp.setCountry(country);
		temperatureDnDFlextableParam.addTemperature(tempTemp);
		
		HorizontalPanel countryPanel = new HorizontalPanel();
		final Label countryLabel = new Label(country);
		countryPanel.add(countryLabel);
		temperatureDnDFlextableParam.setWidget(row, 0, countryPanel);

		//Creates a draghandel for the country
		HTML handle = new HTML(country);
		handle.addStyleName("drag-handle");
		temperatureDnDFlextableParam.setWidget(row, 0, handle);
		tableDragController.makeDraggable(handle);

		HorizontalPanel areaPanel = new HorizontalPanel();
		final Label areaLabel = new Label(area);
		areaPanel.add(areaLabel);
		temperatureDnDFlextableParam.setWidget(row, 1, areaPanel);

		HorizontalPanel cityPanel = new HorizontalPanel();
		final Label cityLabel = new Label(city);
		cityPanel.add(cityLabel);
		temperatureDnDFlextableParam.setWidget(row, 2, cityPanel);

		
		temperatureDnDFlextableParam.setWidget(row, 4, new Label());
		temperatureDnDFlextableParam.getCellFormatter().addStyleName(row, 3, "watchListNumericColumn");
		temperatureDnDFlextableParam.getCellFormatter().addStyleName(row, 4, "watchListNumericColumn");
		temperatureDnDFlextableParam.getCellFormatter().addStyleName(row, 5, "watchListRemoveColumn");

		//Adds a remove button with a listener
		Button removeStockButton = new Button("x");
		removeStockButton.addStyleDependentName("remove");
		removeStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { 
				Boolean boo = false;
				int removedIndex = 0;
				
				for(removedIndex = 0; removedIndex<temperatureDnDFlextable1.getListOfTemperatures().size() && !boo ; removedIndex++){  
					if (temperatureDnDFlextable1.getListOfTemperatures().get(removedIndex).getCity().toUpperCase().equals(city.toUpperCase())){
						temperatureDnDFlextable1.removeTemperature(removedIndex);						
						temperatureDnDFlextable1.removeRow(removedIndex+1);				
					}
				}
				for(removedIndex = 0; removedIndex<temperatureDnDFlextable2.getListOfTemperatures().size() && !boo ; removedIndex++){  
					if (temperatureDnDFlextable2.getListOfTemperatures().get(removedIndex).getCity().toUpperCase().equals(city.toUpperCase())){
						temperatureDnDFlextable2.removeTemperature(removedIndex);						
						temperatureDnDFlextable2.removeRow(removedIndex+1);				
					}
				}
			
				
			}
		});
		temperatureDnDFlextableParam.setWidget(row, 5, removeStockButton);

		refreshWatchList(temperatureDnDFlextableParam);
		return temperatureDnDFlextableParam.getListOfTemperatures();
	}

	
	
	private void refreshWatchList(final DnDFlexTable temperatureDnDFlextableParam) {

		// Initialize the service proxy.
		if (temperaturesSvc == null) {
			temperaturesSvc = GWT.create(TemperatureService.class);

		}

		// Set up the callback object.
		AsyncCallback<ArrayList<Temperature>> callback = new AsyncCallback<ArrayList<Temperature>>() {
			public void onFailure(Throwable caught) {
				// If the stock code is in the list of delisted codes, display an error message.
				String details = caught.getMessage();
				if (caught instanceof DelistedException) {
					details = "The City '" + ((DelistedException)caught).getSymbol() + "' was delisted";
				}
				errorMsgLabel.setText("Error: " + details);
				errorMsgLabel.setVisible(true);
			}

			@Override
			public void onSuccess(ArrayList<Temperature> result) {
				if (temperatureDnDFlextableParam.getListOfTemperatures().equals(temperatureDnDFlextable1.getListOfTemperatures())) temperatureDnDFlextable1.setListOfTemperatures(result);
				else temperatureDnDFlextable2.setListOfTemperatures(result);
				updateTable(result, temperatureDnDFlextableParam);

			}
		};

		// Make the call to the stock price service.

		temperaturesSvc.getTemperatures(temperatureDnDFlextableParam.getListOfTemperatures(), callback); 

	}


	private void updateTable(ArrayList<Temperature> result, DnDFlexTable temperatureDnDFlextableParam) {

		for (int i = 0; i < result.size(); i++) {
			updateTable(result.get(i), temperatureDnDFlextableParam, temperatureDnDFlextableParam.getListOfTemperatures());
		}
		// Display timestamp showing last refresh.
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
		// Clear any errors.
		errorMsgLabel.setVisible(false);
	}
	/**
	 * Update a single row in the stock table.
	 *
	 * @param price Stock data for a single row.
	 */
	private void updateTable(Temperature temperature, FlexTable temperatureDnDFlextableParam, ArrayList<Temperature> listOfTemperaturesParam) {
		// Make sure the stock is still in the stock table.
		Boolean boo = false;
		int row = 0;

		for(row = 0; row<listOfTemperaturesParam.size() && !boo ; row++){
			if (listOfTemperaturesParam.get(row).getCity().toUpperCase().equals(temperature.getCity().toUpperCase())) boo=true;  //can be improved, only compares the city, i.e two cities with the same name in defferent countries/region can't be added
		}
		if (!boo) return;	
		//			row--; //TODO, correct?

		// Format the data in the Price and Change fields.
		String tempText = NumberFormat.getFormat("#,##0.00").format(temperature.getTemperature());
		NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
		String changeText = changeFormat.format(temperature.getChange());
		if (changeText==null) changeText="";
		String changePercentText = changeFormat.format(temperature.getChangePercent());
		if (changePercentText==null) changePercentText ="";

		// Populate the Price and Change fields with new data.

		temperatureDnDFlextableParam.setText(row, 3, tempText);
		Label changeWidget = (Label)temperatureDnDFlextableParam.getWidget(row, 4);
		changeWidget.setText(changeText + " (" + changePercentText + "%)"); 

		// Change the color of text in the Change field based on its value.
		String changeStyleName = "noChange";
		if (temperature.getChangePercent() < -0.1f) {
			changeStyleName = "negativeChange";
		}
		else if (temperature.getChangePercent() > 0.1f) {
			changeStyleName = "positiveChange";
		}

		changeWidget.setStyleName(changeStyleName);
	}
	public Temperature getCurrentPlace(){
		return currentPlace;
	}

	public void setCurrentPlaceNull() {
		this.currentPlace=null;

	}

	private void initCities(){
		//
		//
		//		newCountryTextBox.setText("SWEDEN");
		//		newAreaTextBox.setText("BLEKINGE");
		//		newCityTextBox.setText("KARLSKRONA");
		//		addCity(temperatureDnDFlextable, listOfTemperatures);
		//
		//		newCountryTextBox.setText("SWEDEN");
		//		newAreaTextBox.setText("DALARNA");
		//		newCityTextBox.setText("ORSA");
		//		addCity(temperatureDnDFlextable, listOfTemperatures);
		//
		newCountryTextBox.setText("SWEDEN");
		newAreaTextBox.setText("NORRBOTTEN");
		newCityTextBox.setText("KIRUNA");
		
		temperatureDnDFlextable2.setListOfTemperatures(addCity(temperatureDnDFlextable2)); 


	}


}

