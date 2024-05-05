package view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Main;
import model.Menu;
import database.Database;

public class HomePage {
	private Stage stage;
	private BorderPane root = new BorderPane();
	private GridPane gp = new GridPane();
	private Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
	private Label headerlb1 = new Label("Add New Menu");
	private Label codelbl = new Label("Code");
	private Label namelb1 = new Label("Name");
	private Label pricelb1 = new Label("Price");
	private Label stocklb1 = new Label("Stock");
	private TextField codetf = new TextField();
	private TextField nametf = new TextField();
	private TextField pricetf = new TextField();
	private TextField stocktf = new TextField();
	private TableView<Menu> table = new TableView<Menu>();
	private TableColumn<Menu, String> codecol = new TableColumn<Menu, String>("Code");
	private TableColumn<Menu, String> namecol = new TableColumn<Menu, String>("Name");
	private TableColumn<Menu, Integer> pricecol = new TableColumn<Menu, Integer>("Price");
	private TableColumn<Menu, Integer> stockcol = new TableColumn<Menu, Integer>("Stock");
	private Button addBtn = new Button("Add");
	private Button updateBtn = new Button("Update");
	private Button deleteBtn = new Button("Delete");
	private HBox buttonBox = new HBox(addBtn, updateBtn, deleteBtn);
	private Menu selected;
	private ObservableList<Menu> menuList = FXCollections.observableArrayList();
	
	public HomePage(Stage stage) {
		this.stage = stage;
		this.setComponent();
		this.setStyle(); 
		this.setTableColumns();
		this.setListener();
		Database.getInstance();
		this.populateTable();
		this.handleButton();
	}

	@SuppressWarnings("unchecked")
	private void setComponent() {
		gp.add(headerlb1, 0, 0, 2, 1);
		gp.add(codelbl, 0, 1);
		gp.add(codetf, 1, 1);
		gp.add(namelb1, 0, 2);
		gp.add(nametf, 1, 2);
		gp.add(pricelb1, 0, 3);
		gp.add(pricetf, 1, 3);
		gp.add(stocktf, 1, 4);
		gp.add(stocklb1, 0, 4);
		gp.add(buttonBox, 0, 5, 2 ,1);
		
		table.getColumns().addAll(codecol, namecol,pricecol, stockcol);
		
		root.setTop(table);
		root.setCenter(gp);
		stage.setScene(scene);
	}
	
	private void setStyle() {
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		gp.setVgap(15);
		
		GridPane.setHalignment(headerlb1, HPos.CENTER);
		GridPane.setHalignment(buttonBox, HPos.CENTER);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		addBtn.setMinWidth(90);
		updateBtn.setMinWidth(90);
		deleteBtn.setMinWidth(90);
		buttonBox.setSpacing(10);
		stage.setResizable(false);
	}
	
	private void setTableColumns() {
		codecol.setCellValueFactory(new PropertyValueFactory<Menu, String>("code"));
		namecol.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
		pricecol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("price"));
		stockcol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("stock"));
		
	}
	
	private void setListener() {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			
			if (newValue != null) {
				this.selected = newValue;
				codetf.setText(newValue.getCode());
				nametf.setText(newValue.getName());
				pricetf.setText(String.valueOf(newValue.getPrice()));
				stocktf.setText(String.valueOf(newValue.getStock()));
			}
		});
	}
	
	private void populateTable() {
		menuList = Database.getall();
		table.setItems(menuList);
		codetf.clear();
		nametf.clear();
		pricetf.clear();
		stocktf.clear();
	}
	
	private void handleButton() {
		addBtn.setOnAction(event -> {
			String code = codetf.getText();
			String name = nametf.getText();
			String price = pricetf.getText();
			String stock = stocktf.getText();
			
			
			for (Menu pudding : menuList) {
				if (pudding.getCode().equals(code)) {
					alert(AlertType.ERROR, "Error", "Validation Error", "Code must be unique");
					return;
				}
			}
			
			if(code.isEmpty() || name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Validation Error", "All box must be filled");
				return;
			}
			
			if(!code.startsWith("PD-")) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Code must start with PD-");
				return;
			}
			
			try {
				Integer.valueOf(stock);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Stock must be numeric");
				return;
			}
			
			try {
				Integer.valueOf(price);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Price must be numeric");
				return;
			}
			
			Database.add(new Menu(code, name, Integer.valueOf(price), Integer.valueOf(stock)));
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "The menu succesfully added");
		});
		
		updateBtn.setOnAction(event->{
			String code = codetf.getText();
			String name = nametf.getText();
			String price = pricetf.getText();
			String stok = stocktf.getText();
			
			if(code.isEmpty() || name.isEmpty() || price.isEmpty() || stok.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Validation Error", "All box must be filled");
				return;
			}
			
			Database.update(new Menu(code, name, Integer.parseInt(price), Integer.parseInt(stok)));
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "The menu succesfully updated");
		});
		
		deleteBtn.setOnAction(event->{
			Database.delete(selected);
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "The menu succesfully delete");
		});
	}
	
	private void alert(AlertType alerttype, String title, String header, String content) {
		Alert alert = new Alert(alerttype);
		alert.initOwner(stage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
