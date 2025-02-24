package t4DB5;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.util.Callback;
import java.sql.SQLException;

//import org.eclipse.jgit.api.errors.GitAPIException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BestellungDialog extends Dialog<ButtonType> {

	public BestellungDialog(KundeFX kundeFX) {
		// Aufbau des GUI
		int gesamtPreis = 0;
		ObservableList<BuchFX> olBuecher = FXCollections.observableArrayList();
		for (BuchFX ein : kundeFX.getBestellungen()) {
			olBuecher.add(ein);
			gesamtPreis += ein.getPreis();
		}

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

		Label gesamt = new Label("");
		gesamt.setPrefWidth(250);
		gesamt.setText("Gesamt  " + String.valueOf(gesamtPreis) + " Euro");
		Label entfLb = new Label("Augewähltes Buch entfernen");
		Button entfBtn = new Button("Entfernen");
		HBox hb = new HBox(10, gesamt, entfLb, entfBtn);
		hb.setPadding(new Insets(5));

		VBox vb = new VBox(10, tvBuecher, hb);
		vb.setPadding(new Insets(5));
		this.getDialogPane().setContent(vb);

		ButtonType bezahlen = new ButtonType("Bezahlen", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(bezahlen, new ButtonType("Beenden", ButtonData.CANCEL_CLOSE));

		entfBtn.setOnAction(e -> {
			BuchFX zuEntfernen = tvBuecher.getSelectionModel().getSelectedItem();
			kundeFX.getBestellungen().remove(zuEntfernen);
			olBuecher.remove(zuEntfernen);
			tvBuecher.refresh();
		});


		this.setResultConverter(new Callback<ButtonType, ButtonType>() { // when bezahlen pressed
			@Override
			public ButtonType call(ButtonType arg0) {
				if (arg0 == bezahlen) {
					try {
						for (BuchFX ein : kundeFX.getBestellungen()) {
							Bestellung b = new Bestellung(0, kundeFX.getId(), ein.getModellBuch());
							Datenbank.insertBestellung(b);
							//Datenbank.updateGitHubRepository();
							System.out.println(kundeFX.getId());
						}
						new Alert(AlertType.INFORMATION, "Vielen Dank");
					} catch (SQLException e) {
						new Alert(AlertType.ERROR, e.toString()).showAndWait();
					}
				}
				return arg0;
			}

		});
	}
}
