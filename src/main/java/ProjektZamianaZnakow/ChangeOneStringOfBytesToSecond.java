package ProjektZamianaZnakow;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
Program bedzie sprzedawany wielu uzytkownikom. Zrobic program, ktory przyjmuje 4 parametry:
1. nazwe przeszukiwanego katalogu
2. rozszerzenie nazwy pliku
3. wyszukiwany ciag bajtow
4. to, na jakki ciag bajtow ma byc zamieniony plik
Program ma wyszukiwac dane pliki we wszystkich katalogach i podkatalogach.
Program powinien miec jakis przycisk zeby rozpoczac wyszukiwanie
 */


public class ChangeOneStringOfBytesToSecond extends Application {


    private void listFilesAndFilesSubDirectories(String directoryName, TextField secondValue, TextField thirdValue, TextField fourthValue) {

        File directory = new File(directoryName);
        String newString = thirdValue.getText();
        String oldString = fourthValue.getText();

        // zwraca sciezki
        File[] fList = directory.listFiles();

        String s = secondValue.getText();

        assert fList != null; // zeby nie bylo NullPointerException

        for (File file : fList) {
            try {
                if (file.isFile()) {
                    if (file.getName().endsWith(s)) {
                        try {
                            String contents = new String(Files.readAllBytes(Paths.get(file.getPath()))); // zamienienie pliku na Stringa
                            String contents2 = contents.replace(newString, oldString); // String po zamianie znakow

                            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());

                            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos)); // pozwala na wpisanie do pliku

                            outStream.writeUTF(contents2);

                            outStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (file.isDirectory()) {
                    listFilesAndFilesSubDirectories(file.getAbsolutePath(), secondValue, thirdValue, fourthValue);
                }
            } catch (NullPointerException e) {

            }
        }
    }


    @Override
    public void start(Stage myStage) {

        myStage.setTitle("Zamiana ciagu bajtow");

        GridPane rootNode = new GridPane();
        rootNode.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // tworzenie odstepu pomiedzy elementami
        rootNode.setHgap(5);
        rootNode.setVgap(5);

        rootNode.setAlignment(Pos.CENTER);

        Scene myScene = new Scene(rootNode, 900, 350);

        // tworzenie przyciskow
        rootNode.add(new Label("Wprowadź nazwę katalogu, w którym chcesz dokonać zmiany"), 0, 0);
        TextField firstValue = new TextField();

        rootNode.add(firstValue, 1, 0);

        rootNode.add(new Label("Wpisz rozszerzenie pliku (np .txt, .png, .jpg)"), 0, 1);
        TextField secondValue = new TextField();
        rootNode.add(secondValue, 1, 1);

        rootNode.add(new Label("Wpisz ciąg bajtów jaki chcesz zamienić"), 0, 2);
        TextField thirdValue = new TextField();
        rootNode.add(thirdValue, 1, 2);

        rootNode.add(new Label("Wpisz ciąg bajtów na jaki chcesz zamienić poprzedni ciąg bajtów"), 0, 3);
        TextField fourthValue = new TextField();
        rootNode.add(fourthValue, 1, 3);


        // zmiana rozmiaru czcionki i jej pogrubienie
        rootNode.setStyle("-fx-font-size: 18px; -fx-font-weight: 900;");

        Button executiveButton = new Button("Wykonaj");
        executiveButton.setStyle("-fx-font-size: 20pt;");
        rootNode.add(executiveButton, 1, 5);


        // akcja po nacisnieciu
        executiveButton.setOnAction(e -> {

            String directory = firstValue.getText();
            try {

                listFilesAndFilesSubDirectories(directory, secondValue, thirdValue, fourthValue);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Sukces!");
                alert.setContentText("Pomyslnie dokonano zmiany.");

                alert.showAndWait();

            } catch (Exception e3) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);

                errorAlert.setHeaderText("Błąd!");
                errorAlert.setContentText("Upewnij się że wpisałeś dobrą ścieżkę.");
                errorAlert.showAndWait();
            }

        });

        myStage.setScene(myScene);
        myStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}