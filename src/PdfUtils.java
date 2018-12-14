/**
 * 
 * @author Cody King 
 * @version 0.0.1 - 12-12-2018
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

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
	private Button chooseDirBtn, chooseF1Btn, chooseF2Btn, mergeBtn, partialMergeBtn;
	private TextField fn1TextField;

	private File fn1, fn2, newFn;

	public PdfUtils()
	{
		pane = new GridPane();
		chooseDirBtn = new Button("Select");
		chooseF1Btn = new Button("Select");
		chooseF2Btn = new Button("Select");
		mergeBtn = new Button("Merge");
		partialMergeBtn = new Button("Partial Merge");
		fn1TextField = new TextField();
	}

	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("PDF Toolbox");
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
		primaryStage.setWidth(450);
		primaryStage.setHeight(300);

		///////////////////////////////////////////////////

		pane.add(new Label("Select file:"), 0, 0); 
		pane.add(fn1TextField, 1, 0);
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

		pane.add(partialMergeBtn, 1, 8);

		PDFMergerUtility ut = new PDFMergerUtility();

		chooseDirBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				FileChooser fileChooser = new FileChooser();
				File fn = fileChooser.showSaveDialog(primaryStage);

				if(fn != null)
				{
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

				if(fn != null)
				{
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

				if(fn != null)
				{
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
				if(fn1 != null && fn2 != null && newFn != null)
				{
					try 
					{
						ut.addSource(new File(FilenameUtils.normalize(fn1.toString())));
						ut.addSource(new File(FilenameUtils.normalize(fn2.toString())));
						ut.setDestinationFileName(newFn.toString());
					} 
					catch (FileNotFoundException e1) 
					{
						e1.printStackTrace();
					}

					try 
					{
						ut.mergeDocuments(null);
					} 
					catch (IOException e1) 
					{
						successLabel.setText("Error with merge.");
						successLabel.setTextFill(Color.RED);
						e1.printStackTrace();
						return;
					}

					successLabel.setText("Success!");
					successLabel.setTextFill(Color.BLACK);
				}
				else
				{
					successLabel.setText("Select your files.");
					successLabel.setTextFill(Color.RED);
				}
			}
		});

		partialMergeBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				//Reads in pdf document  
				PDDocument pdDoc = null;

				//Creates a new pdf document  
				PDDocument document = null;

				//Adds specific page "i" where "i" is the page number and then saves the new pdf document  
				if(fn1 != null && newFn != null)
				{
					try 
					{  
						pdDoc = PDDocument.load(new File(FilenameUtils.normalize(fn1.toString())));
						document = new PDDocument();   
						
						List<Integer> range = getPageRange(1);
						for(int i = 0; i < range.size(); i++)
						{
							document.addPage((PDPage) pdDoc.getDocumentCatalog().getPages().get(range.get(i)));
						}
						document.save(new File(FilenameUtils.normalize(newFn.toString())));  
						document.close();
					}
					catch (IOException e1) 
					{
						successLabel.setText("Error with merge.");
						successLabel.setTextFill(Color.RED);
						e1.printStackTrace();
						return;
					}

					successLabel.setText("Success!");
					successLabel.setTextFill(Color.BLACK);
				}
				else
				{
					successLabel.setText("Select your files.");
					successLabel.setTextFill(Color.RED);
				}
			}
		});

		primaryStage.show();
	}
	
	private List<Integer> getPageRange(int file)
	{
		String text = "";
		
		if(file == 1)
			text = fn1TextField.getText();
		//if(file == 1)
			//return fn2TextField.getText();
		
		List<Integer> range = new ArrayList<Integer>();
		String[] values = text.split(",");
		
		for(int i = 0; i < values.length; i++)
		{
			values[i] = values[i].trim();
			int dash = values[i].indexOf("-");
			if(dash != -1)
			{
				int start = Integer.parseInt(values[i].substring(0, dash)) - 1;
				int end = Integer.parseInt(values[i].substring(dash+1)) - 1;
				for(int j = start; j <= end; j++)
				{
					range.add(j);
				}
			}
			else if(!values[i].equals(""))
			{
				range.add(Integer.parseInt(values[i]) - 1); 
			}
		}
		
		return range;
	}

	private void aboutMenuAction()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Stage aboutStage = new Stage();
		aboutStage.setTitle("About PDF Toolbox");
		aboutStage.setScene(new Scene(grid, 250, 225));

		Text aboutText = new Text("King's PDF Toolbox");
		aboutText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Text coderText = new Text("Created by Cody King\nFeel free to redistribute application and\n"
				+ "adapt code to fit your needs.");
		coderText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
		Text verText = new Text("Ver 0.0.1 - 12-12-2018");
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
		aboutStage.setTitle("Help - PDF Toolbox");
		aboutStage.setScene(new Scene(grid, 350, 275));

		Text aboutText = new Text("King's PDF Toolbox");
		aboutText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Text coderText = new Text("Send feedback to kingcodyj at gmail dot come");
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
