package t4DB5;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ListeDerBestellungenDialog extends Dialog<ButtonType> {

	public ListeDerBestellungenDialog() {
		ObservableList<BuchFX> olBuecher = FXCollections.observableArrayList();
		ObservableList<KundeFX> olKunde = FXCollections.observableArrayList();

		try {
			for (Kunde ein : Datenbank.readKunden()) {
				long id = ein.getId();
				String name = ein.getName();
				LocalDate ld = ein.getDatum();
				KundeFX kFX = new KundeFX(new Kunde(id, name, ld));
				olKunde.add(kFX);
			}
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
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

		TableColumn<KundeFX, String> kundeIdCol = new TableColumn<>("ID");
		kundeIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		kundeIdCol.setPrefWidth(100);
		TableColumn<KundeFX, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setPrefWidth(100);
		TableColumn<KundeFX, String> datumCol = new TableColumn<>("Datum");
		datumCol.setCellValueFactory(new PropertyValueFactory<>("datum"));
		datumCol.setPrefWidth(150);
		TableView<KundeFX> tvKunde = new TableView<>(olKunde);
		tvKunde.getColumns().addAll(kundeIdCol, nameCol, datumCol);

		Button entfKunde = new Button("Kunde entferen");

		VBox vb = new VBox(10, tvKunde, tvBuecher, entfKunde);
		vb.setPadding(new Insets(5));
		this.getDialogPane().setContent(vb);

		this.getDialogPane().getButtonTypes().add(new ButtonType("Beenden", ButtonData.CANCEL_CLOSE));

		tvKunde.setOnMouseClicked(e -> {
			// System.out.println(tvKunde.getSelectionModel().getSelectedItem().getId());
			olBuecher.clear();
			try {
				ArrayList<Buch> buecherDesKundes = Datenbank
						.readBestellungen(tvKunde.getSelectionModel().getSelectedItem().getId());
				if (buecherDesKundes != null) {
					for (Buch ein : buecherDesKundes) {
						// System.out.println(ein);
						BuchFX bFX = new BuchFX(ein);
						olBuecher.add(bFX);
					}
				}
			} catch (SQLException e1) {
				new Alert(AlertType.ERROR, "Problem with adding a book").showAndWait();
			}
		});

		entfKunde.setOnAction(e -> {
			Long idZuEntfernen = tvKunde.getSelectionModel().getSelectedItem().getId();
			try {
				Datenbank.kundeEntfernen(idZuEntfernen);
				olKunde.remove(tvKunde.getSelectionModel().getSelectedItem());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
	}
}
