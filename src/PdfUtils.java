/**
 * 
 * @author Cody King 
 * @version 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class PdfUtils extends Application
{
	private GridPane pane;
	private Button chooseDirBtn, chooseF1Btn, chooseF2Btn, mergeBtn;

	private File fn1, fn2, newFn;

	public PdfUtils()
	{
		pane = new GridPane();
		chooseDirBtn = new Button("Select");
		chooseF1Btn = new Button("Select");
		chooseF2Btn = new Button("Select");
		mergeBtn = new Button("Merge");
	}

	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("PDF Merger");
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		pane.setHgap(5.5);
		pane.setVgap(5.5);

		Scene scene = new Scene(pane);

		// Top Menu Bar
		MenuBar menuBar = new MenuBar();
		Menu menuHelp = new Menu("Help");
		MenuItem menuItemAbout = new MenuItem("About");
		MenuItem menuItemHelp = new MenuItem("Help");

		/*menuItemAbout.setOnAction(new EventHandler<ActionEvent>()
        {
        @Override public void handle(ActionEvent e)
        {
        aboutMenuAction();
        }
        });*/

		menuItemAbout.setOnAction(e -> aboutMenuAction());
		menuItemHelp.setOnAction(e -> helpMenuAction());

		menuHelp.getItems().add(menuItemAbout);
		menuHelp.getItems().add(menuItemHelp);
		menuBar.getMenus().add(menuHelp);
		//menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		final String os = System.getProperty("os.name");
		menuBar.useSystemMenuBarProperty().set(os != null && os.startsWith("Mac"));

		pane.getChildren().add(menuBar);
		primaryStage.setScene(scene);
		primaryStage.setWidth(300);
		primaryStage.setHeight(300);

		///////////////////////////////////////////////////

		pane.add(new Label("Select file:"), 0, 0); 
		pane.add(chooseF1Btn, 1, 1);
		Label fn1Label = new Label("No file selected");
        fn1Label.setTextFill(Color.RED);
        pane.add(fn1Label, 0, 1);

		pane.add(new Label("Select file:"), 0, 2); 
		pane.add(chooseF2Btn, 1, 3);
		Label fn2Label = new Label("No file selected");
        fn2Label.setTextFill(Color.RED);
        pane.add(fn2Label, 0, 3);

		pane.add(new Label("Select new file:"), 0, 4); 
		pane.add(chooseDirBtn, 1, 5);
		Label newFnLabel = new Label("No destination selected");
        newFnLabel.setTextFill(Color.RED);
        pane.add(newFnLabel, 0, 5);

		pane.add(mergeBtn, 1, 7);  // doesn't add blank rows
		Label successLabel = new Label("");
        successLabel.setTextFill(Color.RED);
        pane.add(successLabel, 0, 7);

		PDFMergerUtility ut = new PDFMergerUtility();

		chooseDirBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				FileChooser fileChooser = new FileChooser();
				File fn = fileChooser.showSaveDialog(primaryStage);

				if(fn != null){
					System.out.println(fn.getAbsolutePath());
					newFn = fn;
					newFnLabel.setText(newFn.toString());
					newFnLabel.setTextFill(Color.BLACK);
				}
			}
		});

		chooseF1Btn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose File");
				File fn = fileChooser.showOpenDialog(primaryStage);

				if(fn != null){
					System.out.println(fn.getAbsolutePath());
					fn1 = fn;
					fn1Label.setText(fn1.toString());
					fn1Label.setTextFill(Color.BLACK);
				}
			}
		});

		chooseF2Btn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose File");
				File fn = fileChooser.showOpenDialog(primaryStage);

				if(fn != null){
					System.out.println(fn.getAbsolutePath());
					fn2 = fn;
					fn2Label.setText(fn2.toString());
					fn2Label.setTextFill(Color.BLACK);
				}
			}
		});

		mergeBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				try 
				{
					ut.addSource(new File(FilenameUtils.normalize(fn1.toString())));
					ut.addSource(new File(FilenameUtils.normalize(fn2.toString())));
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}

				ut.setDestinationFileName(newFn.toString());
				try 
				{
					ut.mergeDocuments(null);
				} 
				catch (IOException e1) 
				{
					successLabel.setText("Error with merge.");
					e1.printStackTrace();
					return;
				}

				System.out.println("Success!\n");
				successLabel.setText("Success!");
				successLabel.setTextFill(Color.BLACK);
			}
		});

		primaryStage.show();
	}

	private void aboutMenuAction()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Stage aboutStage = new Stage();
		aboutStage.setTitle("About Loan Calculator");
		aboutStage.setScene(new Scene(grid, 250, 225));

		Text aboutText = new Text("King's Loan Calculator");
		aboutText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Text coderText = new Text("Created by Cody King\nFeel free to redistribute application and\n"
				+ "adapt code to fit your needs.");
		coderText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
		Text verText = new Text("Ver 1.0");
		verText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));

		grid.add(aboutText, 0, 0);
		grid.add(coderText, 0, 1);
		grid.add(verText, 0, 3);

		aboutStage.show();
	}

	private void helpMenuAction()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Stage aboutStage = new Stage();
		aboutStage.setTitle("Help - Loan Calculator");
		aboutStage.setScene(new Scene(grid, 350, 275));

		Text aboutText = new Text("King's Loan Calculator");
		aboutText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Text coderText = new Text("Only one field may be left blank (the value being calculated)\n" +
				"Negative values mean money has been deposited.");
		coderText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));

		grid.add(aboutText, 0, 0);
		grid.add(coderText, 0, 1);

		aboutStage.show();
	}

	public static void main(String[] args)
	{
		Application.launch(args);
	}
}

