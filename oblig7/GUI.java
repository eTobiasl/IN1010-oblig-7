import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import java.io.FileNotFoundException;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import java.io.File;


 
public class GUI extends Application{
    private Labyrint labyrint;
    private Stage teater;
    private GridPane labyrintensGrid;
    private boolean[][] losningTabell;
    private boolean klikket = false;
    private boolean visUtenLabyrint = true;
    private int vistLosningNummer = 1;
    private ToolBar top;
    private Liste<Button> endredeKnapper = new Lenkeliste<Button>();
    private Lenkeliste<String> utveiListe;
    private BorderPane hoved;
    
    //Lar brukeren velge et nytt startpunkt i labyrinten
    private class ResetKnappBehandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent reset){
            reset();
        }
    }

    //viser forrige losning
    private class forrigeLosning implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent forrige){
            if (utveiListe != null){
                if (vistLosningNummer > 1 && klikket == true){
                    vistLosningNummer--;
                }else{
                    vistLosningNummer = utveiListe.stoerrelse();
                }
                VBox box = new VBox(new Text(Integer.toString(vistLosningNummer)+"/"+Integer.toString(utveiListe.stoerrelse())));
                box.setMinWidth(50);
                box.setAlignment(Pos.CENTER);
                top.getItems().set(2, box);
                visLosning();
            }
        }
    }

    //viser neste losning
    private class nesteLosning implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent neste){
            if (utveiListe != null){
                if(vistLosningNummer != utveiListe.stoerrelse() && klikket == true){
                    vistLosningNummer++;
                }else{
                    vistLosningNummer = 1;
                }

                VBox box = new VBox(new Text(Integer.toString(vistLosningNummer)+"/"+Integer.toString(utveiListe.stoerrelse())));
                box.setAlignment(Pos.CENTER);
                box.setMinWidth(50);
                top.getItems().set(2, box);
                visLosning();
            }
        }
    }

    //Hva som skjer naar en startRute blir valgt
    private class KnappTrykk implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent orginalRute){
            if(klikket == false){
                vistLosningNummer = 1;
                Button knappTrykket = (Button)orginalRute.getSource();
                Liste<String> utveier = labyrint.finnUtveiFra(GridPane.getColumnIndex(knappTrykket), GridPane.getRowIndex(knappTrykket));
                if (utveier.stoerrelse() != 0) {
                    utveiListe = (Lenkeliste<String>)utveier;
                    klikket = true;
            
                    visLosning();
                    VBox box = new VBox(new Text(Integer.toString(vistLosningNummer)+"/"+Integer.toString(utveier.stoerrelse())));
                    settRiktigPosisjon(box);
                    top.getItems().set(2, box);
                }
            }
        }   
    }
    //for aa bytte labyrint
    private class VelgFilBehandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent velgFil){
            try{
                start(teater);
                reset();
                
            } catch(Exception feil){}
        }
    }
    
    @Override
    public void start(Stage teater) throws FileNotFoundException{
        this.teater = teater;
        
        if (visUtenLabyrint != true){
            labyrint = lesFil();
            settOppKnapper(lagGridPane(labyrint));
            teater.setTitle("Trykk paa en hvit rute for aa finne en utvei");
        }else{
            teater.setTitle("Velg en fil for aa vise labyrinten");
            visUtenLabyrint = false;
        }

        teater.setMinWidth(370);
        teater.setScene(new Scene(lagScrollPane()));
        teater.show();
    }

    //Legger til alle elementene til en scrollPane og returnerer den ferdig utfylte scrollPanen
    private ScrollPane lagScrollPane(){
        hoved = new BorderPane();
        VBox box = new VBox(new Text("0/0"));

        Button resetKnapp = new Button("Reset");
        Button forrigeKnapp = new Button("<");
        Button nesteKnapp = new Button(">");
        Button velgFilKnapp = new Button("Velg fil");
        velgFilKnapp.setPadding(new Insets(10,10,10,10));
        resetKnapp.setPadding(new Insets(10,10,10,10));

        settRiktigPosisjon(box);
        top = new ToolBar(resetKnapp);
        
        forrigeKnapp.setMinSize(30, 30);
        nesteKnapp.setMinSize(30, 30);
        velgFilKnapp.setMinSize(60, 10);
        resetKnapp.setMinSize(60, 10);
        resetKnapp.setOnAction(new ResetKnappBehandler());
        forrigeKnapp.setOnAction(new forrigeLosning());
        nesteKnapp.setOnAction(new nesteLosning());
        velgFilKnapp.setOnAction(new VelgFilBehandler());
        
        hoved.setCenter(labyrintensGrid);
        hoved.setTop(top);
        if (labyrintensGrid != null){
            BorderPane.setMargin(labyrintensGrid, new Insets(0,35,0,0));
        }
        top.setMinSize(370, 10);

        top.getItems().add(forrigeKnapp);
        top.getItems().add(box);
        top.getItems().add(nesteKnapp);
        top.getItems().add(velgFilKnapp);
            
        return new ScrollPane(hoved);
    }
    //resetter labyrinten
    private void reset(){
        if (utveiListe != null){
            utveiListe = null;
            for (Button knapp : endredeKnapper){
                knapp.setStyle("-fx-background-color: white; -fx-background-radius: 0;"); 
            }
            if (endredeKnapper.stoerrelse() != 0){
                endredeKnapper.fjern(0);
            }
            klikket = false;

            VBox box = new VBox(new Text("0/0"));
            settRiktigPosisjon(box);
            top.getItems().set(2, box);
        }
    }

    //Leser fra fil og returnerer labyrinten. Passer ogsaa paa at bare .in filer blir tatt inn
    private Labyrint lesFil() throws FileNotFoundException{
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter riktigFilType = new FileChooser.ExtensionFilter("Labyrint filer", "*.in");
        fileChooser.getExtensionFilters().add(riktigFilType);

        File fil = fileChooser.showOpenDialog(teater);
        if (fil != null){
            labyrint = Labyrint.lesFraFil(fil);
        }
        return labyrint;
    }

    //lager gridPane
    private GridPane lagGridPane(Labyrint labyrint){
        GridPane labyrintGrid = new GridPane();
        for ( Rute[] rad : labyrint.hentRuteArray()){
            for (Rute rute : rad){
                Button knapp = new Button(""); 
                if (!(rute instanceof SortRute)){
                    knapp.setStyle("-fx-background-color: white; -fx-background-radius: 0;");
                } else{
                    knapp.setStyle("-fx-background-color: black; -fx-background-radius: 0;");
                }

                GridPane.setMargin(knapp, new Insets(1, 1, 1, 1));
                labyrintGrid.add(knapp, rute.hentKolonne(), rute.hentRad());
            }

        }
        
        return labyrintGrid;
    }
    //Viser losningen for brukeren ved at utveien blir graa
    private void visLosning(){
        for (Button knapp : endredeKnapper){
            knapp.setStyle("-fx-background-color: white; -fx-background-radius: 0;"); 
        }
        if (endredeKnapper.stoerrelse() != 0){
            endredeKnapper.fjern(0);
        }
        
        losningTabell = losningStringTilTabell(utveiListe.hent(vistLosningNummer-1), labyrint.hentAntallKolonner(), labyrint.hentAntallRader());
        for (int i = 0; i<losningTabell.length; i++){
            for(int j = 0; j<losningTabell[i].length; j++){
                if (losningTabell[i][j] == true){
                    for(Node element : labyrintensGrid.getChildren()){
                        if (GridPane.getColumnIndex(element) != null && GridPane.getRowIndex(element) != null){
                             if(GridPane.getColumnIndex(element)==j && GridPane.getRowIndex(element)==i){
                                if(element instanceof Button){
                                    Button knapp = (Button)element;
                                    knapp.setStyle("-fx-background-color: grey; -fx-background-radius: 0;");   
                                    endredeKnapper.leggTil(knapp);      
                                }   
                            }
                        }
                    }
                }
            }
        }

    }

    //Gjor at VBoxen ikke endrer stoerelse, og at teksten blir plassert i midten av den
    private VBox settRiktigPosisjon(VBox b){
        b.setMinWidth(50);
        b.setAlignment(Pos.CENTER);
        return b;
    }

    private void settOppKnapper(GridPane lgp ){
        
        labyrintensGrid = lgp;
        KnappTrykk knappTrykk = new KnappTrykk();
        for (Node n : lgp.getChildren()){
            if (n instanceof Button){
                Button b = (Button) n;
                b.setOnAction(knappTrykk);
            }
        }
    }


    // Vedlagt kode
    static boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
        boolean[][] losning = new boolean[hoyde][bredde];
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
        java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
        while (m.find()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            losning[y][x] = true;
        }
        return losning;
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}