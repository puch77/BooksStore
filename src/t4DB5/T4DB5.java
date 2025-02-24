package t4DB5;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

//import org.eclipse.jgit.api.errors.GitAPIException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class T4DB5 extends Application {

	private ObservableList<BuchFX> olBuecher = FXCollections.observableArrayList();
	private KundeFX kundeFx;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage arg0) {
		ArrayList<String> alThemen;
		try {
			//Datenbank.updateGitHubRepository();
			Datenbank.createKunden();
			Datenbank.createBestellungen();
			alThemen = Datenbank.readThemen();
			alThemen.add("Alle");
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
			alThemen = new ArrayList<>();
		}

		ComboBox<String> cbThemen = new ComboBox<>(FXCollections.observableArrayList(alThemen));
		Button suchen = new Button("Suchen");
		HBox hb = new HBox(20, new Label("Thema der Bücher"), cbThemen, suchen);
		hb.setPadding(new Insets(5));

		TableColumn<BuchFX, String> themaCol = new TableColumn<>("Thema");
		themaCol.setCellValueFactory(new PropertyValueFactory<>("thema"));
		themaCol.setPrefWidth(100);
		TableColumn<BuchFX, String> isbnCol = new TableColumn<>("ISBN");
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		isbnCol.setPrefWidth(100);
		TableColumn<BuchFX, String> titelCol = new TableColumn<>("Titel");
		titelCol.setCellValueFactory(new PropertyValueFactory<>("titel"));
		titelCol.setPrefWidth(150);
		TableColumn<BuchFX, String> authorCol = new TableColumn<>("Author");
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		authorCol.setPrefWidth(150);
		TableColumn<BuchFX, Integer> jahrCol = new TableColumn<>("Jahr");
		jahrCol.setCellValueFactory(new PropertyValueFactory<>("jahr"));
		jahrCol.setPrefWidth(80);
		TableColumn<BuchFX, Integer> preisCol = new TableColumn<>("Preis");
		preisCol.setCellValueFactory(new PropertyValueFactory<>("preis"));
		preisCol.setPrefWidth(80);
		TableView<BuchFX> tvBuecher = new TableView<>(olBuecher);
		tvBuecher.getColumns().addAll(themaCol, isbnCol, titelCol, authorCol, jahrCol, preisCol);
		tvBuecher.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		TextField txtName = new TextField();
		txtName.setPrefWidth(100);
		Button login = new Button("Login");
		login.setDisable(true);
		Button korb = new Button("in den Warenkorb");
		korb.setDisable(true);
		Button kassa = new Button("Kassa");
		kassa.setDisable(true);
		Button alle = new Button("Alle Bestellungen anzeigen");
		Button close = new Button("Beenden");
		
		HBox hbBottom = new HBox(10, new Label("Name"), txtName, login, korb, kassa, alle, close);
		hbBottom.setPadding(new Insets(5));

		suchen.setOnAction(e -> {
			readBooks(cbThemen.getSelectionModel().getSelectedItem());
		});

		txtName.setOnKeyReleased(e -> login.setDisable(txtName.getText().length() == 0));

		login.setOnAction(e -> {
			kundeFx = new KundeFX(new Kunde(System.currentTimeMillis(), txtName.getText(), LocalDate.now()));
			try {
				Datenbank.insertKunde(kundeFx.getModellKunde());
				txtName.setDisable(true);
				login.setDisable(true);
			} catch (SQLException e1) {
				new Alert(AlertType.ERROR, e1.toString()).showAndWait();
			}

		});

		tvBuecher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BuchFX>() {
			@Override
			public void changed(ObservableValue<? extends BuchFX> arg0, BuchFX arg1, BuchFX arg2) {
				if (kundeFx != null && arg2 != null)
					korb.setDisable(false);
			}
		});
		korb.setOnAction(e -> {
			for(BuchFX ein : tvBuecher.getSelectionModel().getSelectedItems())
				kundeFx.getBestellungen().add(ein);
			kassa.setDisable(false);
		});
		kassa.setOnAction(e -> {
			new BestellungDialog(kundeFx).showAndWait();
			kundeFx = null;
			txtName.setText("");
			txtName.setDisable(false);
			login.setDisable(true);
			korb.setDisable(true);
			kassa.setDisable(true);
		});
		
		alle.setOnAction(e -> {
			new ListeDerBestellungenDialog().showAndWait();
		});

		close.setOnAction(e -> arg0.close());
		
		VBox vb = new VBox(10, hb, tvBuecher, hbBottom);
		vb.setPadding(new Insets(5));
		arg0.setScene(new Scene(vb));
		arg0.setTitle("T4DB5");
		arg0.show();
	}

	private void readBooks(String thema) {
		try {
			ArrayList<Buch> alBuecher = Datenbank.readBuecher(thema.equals("Alle") ? null : thema);
			olBuecher.clear();
			for (Buch ein : alBuecher)
				olBuecher.add(new BuchFX(ein));
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}

}
