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
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
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
	private Button editButton = new Button("Edit");
	private Label lastUpdatedLabel = new Label();

	private TemperatureServiceAsync temperaturesSvc = GWT.create(TemperatureService.class);
	private Label errorMsgLabel = new Label();
	private final static int REFRESH_INTERVAL =5000; //to long. change to *10
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

		temperatureDnDFlextable1 = new DnDFlexTable(1);
		temperatureDnDFlextable2 = new DnDFlexTable(2);

		createLayout();

		FlexTableDropController flexTableDropController1 = new FlexTableDropController(temperatureDnDFlextable1,this);
		FlexTableDropController flexTableDropController2 = new FlexTableDropController(temperatureDnDFlextable2,this);
		tableDragController.registerDropController(flexTableDropController1);
		tableDragController.registerDropController(flexTableDropController2);

		initCities();

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
		temperatureDnDFlextable1.addClickHandler(new ClickHandler() { //adding clickhandler to deal with the ability to modify data in the flextable
			@Override
			public void onClick(ClickEvent event) {
				initiateClickHandler(temperatureDnDFlextable1, event);


			}
		});
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

		temperatureDnDFlextable2.addClickHandler(new ClickHandler() { //adding clickhandler to deal with the ability to modify data in the flextable
			@Override
			public void onClick(ClickEvent event) {
				initiateClickHandler(temperatureDnDFlextable2, event);


			}
		});

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
				addCity(temperatureDnDFlextable1); 
			}
		});
		// Listen for keyboard events in the input box.
		newCountryTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addCity(temperatureDnDFlextable1); 
				}
			}
		});
		newCityTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addCity(temperatureDnDFlextable1);  
				}
			}
		});
		newAreaTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addCity(temperatureDnDFlextable1); 
				}
			}
		});

	}

	protected void initiateClickHandler(final DnDFlexTable temperatureDnDFlextableParam, ClickEvent event) {
		final Cell src = temperatureDnDFlextableParam.getCellForEvent(event);
		if(src==null) return;
//		if (src.getCellIndex() != 0 || src.getCellIndex() != 1 || src.getCellIndex() != 2) return;
		final int rowIndex = src.getRowIndex(); 
		final String oldValue = temperatureDnDFlextableParam.getText(rowIndex,src.getCellIndex());
		final String oldCity = temperatureDnDFlextableParam.getText(rowIndex, 2);
		final PopupPanel popup = new PopupPanel(false);
		final AbsolutePanel popupPanel = new AbsolutePanel();
		popup.setTitle("input country here");
		final TextArea popupInput = new TextArea();
		popupPanel.add(popupInput);
		popupPanel.add(editButton);
		popup.add(popupPanel);
		popup.center();

		popup.show();
		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String tempText = popupInput.getText();
				AsyncCallback<String> callback = new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
					}
					@Override
					public void onSuccess(String result) {
						populateWithDbData();
					}

				};
				popup.hide();
				System.out.println("popup is hided");
				if (src.getCellIndex() == 0){
					temperaturesSvc.setCountry(tempText,oldValue,oldCity, callback);
					temperatureDnDFlextableParam.removeTemperature(rowIndex);						
					temperatureDnDFlextableParam.removeRow(rowIndex+1);	
				}
				if (src.getCellIndex() == 1){
					temperaturesSvc.setArea(tempText,oldValue, oldCity,callback);
				}
				if (src.getCellIndex() == 2){
					temperaturesSvc.setCity(tempText,oldCity, callback);
				} 
			}
		});

		popupInput.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				System.out.println("im here!");
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {

					String tempText = popupInput.getText();
					AsyncCallback<String> callback = new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							System.out.println("failure");
						}
						@Override
						public void onSuccess(String result) {
							System.out.println("i'm a sucess");
							populateWithDbData();
							//							refreshWatchList(temperatureDnDFlextableParam);

						}

					};
					popup.hide();
					System.out.println("popup is hided");
					if (src.getCellIndex() == 0){
						temperaturesSvc.setCountry(tempText,oldValue,oldCity, callback);
					}
					if (src.getCellIndex() == 1){
						temperaturesSvc.setArea(tempText,oldValue, oldCity,callback);
					}
					if (src.getCellIndex() == 2){
						temperaturesSvc.setCity(tempText,oldCity, callback);
					}
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
		//		for(int i = 0; i<temperatureDnDFlextable1.getListOfTemperatures().size(); i++){
		//			if (temperatureDnDFlextable1.getListOfTemperatures().get(i).getCity().equals(city.toUpperCase())) return temperatureDnDFlextableParam.getListOfTemperatures();
		//		}
		//		for(int i = 0; i<temperatureDnDFlextable2.getListOfTemperatures().size(); i++){
		//			if (temperatureDnDFlextable2.getListOfTemperatures().get(i).getCity().equals(city.toUpperCase())) return temperatureDnDFlextableParam.getListOfTemperatures();
		//		}


		//Adds the new city to the flextable

		//		final int row = temperatureDnDFlextableParam.getRowCount(); 
		final Temperature tempTemp = new Temperature(country, area, city, temperatureDnDFlextableParam.getId(), temperatureDnDFlextableParam.getListOfTemperatures().size()+1);
		//
		//		temperatureDnDFlextableParam.addTemperature(tempTemp);

		//Adds the city to the db
		if (temperaturesSvc == null) {
			temperaturesSvc = GWT.create(TemperatureService.class);

		}
		AsyncCallback<Temperature> callback = new AsyncCallback<Temperature>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Temperature result) {
				populateWithDbData();
				//				initiateHTMLElements(temperatureDnDFlextableParam, tempTemp);
				//				refreshWatchList(temperatureDnDFlextableParam);
			}
		};
		temperaturesSvc.initiateCity(tempTemp, callback);
		return temperatureDnDFlextableParam.getListOfTemperatures();

	}
	private void initiateHTMLElements(final DnDFlexTable temperatureDnDFlextableParam,final Temperature temperature){
//		final int row = temperatureDnDFlextableParam.getRowCount();
		final int row = temperature.getRow();

		TextArea countryText = new TextArea();
		countryText.setText(temperature.getCountry());

		temperatureDnDFlextableParam.setWidget(row, 0, countryText);

		//Creates a draghandel for the country
		HTML handle = new HTML(temperature.getCountry());
		handle.addStyleName("drag-handle");
		temperatureDnDFlextableParam.setWidget(row, 0, handle);
		tableDragController.makeDraggable(handle);

		HorizontalPanel areaPanel = new HorizontalPanel();
		final Label areaLabel = new Label(temperature.getArea());
		areaPanel.add(areaLabel);
		temperatureDnDFlextableParam.setWidget(row, 1, areaPanel);

		HorizontalPanel cityPanel = new HorizontalPanel();
		final Label cityLabel = new Label(temperature.getCity());
		cityPanel.add(cityLabel);
		temperatureDnDFlextableParam.setWidget(row, 2, cityPanel);

		HorizontalPanel temperaturePanel = new HorizontalPanel();
		final Label tempPanel = new Label(Double.toString(temperature.getTemperature()));
		temperaturePanel.add(tempPanel);
		temperatureDnDFlextableParam.setWidget(row, 3, temperaturePanel);

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
					if (temperatureDnDFlextable1.getListOfTemperatures().get(removedIndex).getCity().toUpperCase().equals(temperature.getCity().toUpperCase())){
						deleteEntryFromDb(temperatureDnDFlextable1.getListOfTemperatures().get(removedIndex)); 
						
						temperatureDnDFlextable1.removeTemperature(removedIndex);						
						temperatureDnDFlextable1.removeRow(removedIndex+1);	

					}
				}
				for(removedIndex = 0; removedIndex<temperatureDnDFlextable2.getListOfTemperatures().size() && !boo ; removedIndex++){  
					if (temperatureDnDFlextable2.getListOfTemperatures().get(removedIndex).getCity().toUpperCase().equals(temperature.getCity().toUpperCase())){

						deleteEntryFromDb(temperatureDnDFlextable2.getListOfTemperatures().get(removedIndex)); 
						temperatureDnDFlextable2.removeTemperature(removedIndex);						
						temperatureDnDFlextable2.removeRow(removedIndex+1);
					}

				}


			}
		});
		temperatureDnDFlextableParam.setWidget(row, 5, removeStockButton);


	}

	protected void populateWithDbData(){
		if (temperaturesSvc == null) {
			temperaturesSvc = GWT.create(TemperatureService.class);

		}
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
				temperatureDnDFlextable1.removeAllTemperature();
				temperatureDnDFlextable2.removeAllTemperature();
				for(int j = 0; j<result.size(); j++){
					if(result.get(j).getTable()==1){
						temperatureDnDFlextable1.addTemperature(result.get(j));
					}
					else{
						temperatureDnDFlextable2.addTemperature(result.get(j));
					}
				}

				for(int i = 0; i<temperatureDnDFlextable1.getListOfTemperatures().size();i++){
					initiateHTMLElements(temperatureDnDFlextable1,temperatureDnDFlextable1.getListOfTemperatures().get(i));
				}
				for(int i = 0; i<temperatureDnDFlextable2.getListOfTemperatures().size();i++){

					initiateHTMLElements(temperatureDnDFlextable2,temperatureDnDFlextable2.getListOfTemperatures().get(i));
				}

			}
		};

		temperaturesSvc.getAllData(callback);

	}

	protected void deleteEntryFromDb(Temperature temperature){
		AsyncCallback<Temperature> callback = new AsyncCallback<Temperature>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Temperature result) {
				populateWithDbData(); //TODO, could be removed?
			}
		};

		// Make the call to the stock price service.

		temperaturesSvc.deleteEntryFromDb(temperature,callback);

	
	}
	public Temperature getCurrentPlace(){
		return currentPlace;
	}

	public void setCurrentPlaceNull() {
		this.currentPlace=null;

	}

	private void initCities(){
		populateWithDbData();
	}
	
	protected void updateRowInDb(String city1, int row1, String city2, int row2){
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				populateWithDbData();
			}
		};
		temperaturesSvc.setRowsInDb(city1, row1, city2, row2 ,callback);

	}


}

