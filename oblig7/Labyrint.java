import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Labyrint{
  private Rute[][] ruteArray;
  private static int rader;
  private static int kolonner;
  private Monitor monitor = new Monitor();

  static class Monitor{
    Lock laas = new ReentrantLock();
    protected static Liste<String> utveiListe = new Lenkeliste<String>();

    public Liste<String> hentListe(){
      return utveiListe;
    }

    public void fjern(int indeks){
      laas.lock();
      try{
        utveiListe.fjern(indeks);
      } finally{
        laas.unlock();
      }
    }

    public void leggTil(String string){
      laas.lock();
      try{
      utveiListe.leggTil(string);
      } finally{
        laas.unlock();
      }
    }
  }

  private Labyrint(Rute[][] array, int antallKolonner, int antallRader){
    rader = antallRader;
    kolonner = antallKolonner;
    ruteArray = array;
  }

  protected static Labyrint lesFraFil(File fil)throws FileNotFoundException, NullPointerException{
    Scanner scanner = new Scanner(fil);
    String[] linje = scanner.nextLine().split(" ");
    int rad = Integer.parseInt(linje[0]);
    int kol = Integer.parseInt(linje[1]);
    Rute[][] array = new Rute[kol][rad];
    Labyrint labyrint = new Labyrint(array, kol, rad);

    int radTeller = -1;
    while(scanner.hasNextLine()){
      char[] splittetLinje = scanner.nextLine().toCharArray();
      radTeller++;
      int kolTeller = -1;
      for (char tegn : splittetLinje){
        kolTeller++;

        if (tegn == '.'){
          if (kolTeller == 0 || kolTeller == kol-1 || radTeller == 0 || radTeller == rad-1){
            array[kolTeller][radTeller] = new Aapning(kolTeller, radTeller, labyrint);
          }else{
            array[kolTeller][radTeller] = new HvitRute(kolTeller, radTeller, labyrint);
          }
        }else{
          array[kolTeller][radTeller] = new SortRute(kolTeller, radTeller, labyrint);
        }
      }
    }
  for (int rTeller = 1; rTeller < rader-1; rTeller++){
    for (int kTeller = 1; kTeller < kolonner-1; kTeller++){
      if (rTeller > 0){
        array[kTeller][rTeller].settNabo(array[kTeller][rTeller-1], "nord");
      }
      if (rTeller < rad-1){
        array[kTeller][rTeller].settNabo(array[kTeller][rTeller+1], "syd");
      }
      if (kTeller < kol-1){
        array[kTeller][rTeller].settNabo(array[kTeller+1][rTeller], "ost");
      }
      if (kTeller > 0){
        array[kTeller][rTeller].settNabo(array[kTeller-1][rTeller], "vest");
      }
    }
  }
  scanner.close();
  return labyrint;
}
  //hjelpemetode for å hente monitor for å legge til veier i Aapning
  protected Monitor hentMonitor(){
    return monitor;
  }
  protected Liste<String> finnUtveiFra(int kolonne, int rad){
    if (monitor.hentListe().stoerrelse() > 0){
      monitor.fjern(0);
    }
    ruteArray[kolonne][rad].finnUtvei(monitor.hentListe());
    return monitor.hentListe();
  }

  protected Rute[][] hentRuteArray(){
    return ruteArray;
  }

  protected int hentAntallRader(){
    return rader;
  }

  protected int hentAntallKolonner(){
    return kolonner;
  }

}
