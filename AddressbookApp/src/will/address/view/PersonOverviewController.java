package will.address.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import will.address.MainApp;
import will.address.model.Person;
import will.address.util.AlertUtil;
import will.address.util.DateUtil;

public class PersonOverviewController {
	@FXML
	private TextField searchField;
	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	@FXML
	private TableColumn<Person, String> lastNameColumn;

	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label birthdayLabel;

	// Reference to the main application.
	private MainApp mainApp;

	private ObservableList<Person> personData = FXCollections.observableArrayList();;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public PersonOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Initialize the person table with the two columns.
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

		// Clear person details.
		showPersonDetails(null);

		// Listen for selection changes and show the person details when
		// changed.
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		personData = mainApp.getPersonData();

		// Add observable list data to the table
		personTable.setItems(personData);

		FilteredList<Person> filteredData = new FilteredList<>(personData, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter
				// text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (person.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else if (person.getLastName().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Person> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(personTable.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		personTable.setItems(sortedData);

	}

	/**
	 * Fills all text fields to show details about the person. If the specified
	 * person is null, all text fields are cleared.
	 * 
	 * @param person
	 *            the person or null
	 */
	private void showPersonDetails(Person person) {
		if (person != null) {
			// Fill the labels with info from the person object.
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			streetLabel.setText(person.getStreet());
			postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
			cityLabel.setText(person.getCity());

			// TODO: We need a way to convert the birthday into a String!
			// birthdayLabel.setText(...);
			birthdayLabel.setText(DateUtil.format(person.getBirthday()));
		} else {
			// Person is null, remove all the text.
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			streetLabel.setText("");
			postalCodeLabel.setText("");
			cityLabel.setText("");
			birthdayLabel.setText("");
		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeletePerson() {
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			personTable.getItems().remove(selectedIndex);
		} else {
			// Nothing selected.
			AlertUtil.reportError(AlertType.WARNING, mainApp.getPrimaryStage(), "No Selection", "No Person Selected",
					"Please select a person in the table.");
		}
	}

	/**
	 * Called when the user clicks the new button. Opens a dialog to edit
	 * details for a new person.
	 */
	@FXML
	private void handleNewPerson() {
		Person tempPerson = new Person();
		boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
		if (okClicked) {
			mainApp.getPersonData().add(tempPerson);
		}
	}

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected person.
	 */
	@FXML
	private void handleEditPerson() {
		Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
			if (okClicked) {
				showPersonDetails(selectedPerson);
			}

		} else {
			// Nothing selected.
			AlertUtil.reportError(AlertType.WARNING, mainApp.getPrimaryStage(), "No Selection", "No Person Selected",
					"Please select a person in the table.");
		}
	}
}