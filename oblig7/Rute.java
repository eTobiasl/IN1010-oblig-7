abstract class Rute{
  protected int kolonne;
  protected int rad;
  protected Labyrint minLabyrint;
  protected Rute nordNabo;
  protected Rute sydNabo;
  protected Rute ostNabo;
  protected Rute vestNabo;

  private boolean besokStatus = false;

  protected Rute(int kolonnen, int raden, Labyrint labyrint){
    rad = raden;
    kolonne = kolonnen;
    minLabyrint = labyrint;
  }
  //Metode for å sette naboer til gitt rettning
  protected void settNabo(Rute nabo, String rettning){
    if (rettning.equals("nord")){
      nordNabo = nabo;
    }
    else if (rettning.equals("syd")){
      sydNabo = nabo;
    }
    else if (rettning.equals("ost")){
      ostNabo = nabo;
    }
    else if (rettning.equals("vest")){
      vestNabo = nabo;
    }
  }
  //Metode som henter besokStatus
  protected boolean hentBesokStatus(){
    return besokStatus;
  }
  //Metode som setter besokstatus til onsket verdi
  protected void settBesokStatus(boolean status){
    besokStatus = status;
  }

  protected abstract void gaa(Rute ruteViKomFra, String forelopigVei);

  protected void finnUtvei(Liste<String> utveier){
    gaa(this, "");
  }

  protected int hentRad(){
    return rad;
  }

  protected int hentKolonne(){
    return kolonne;
  }

  protected abstract char tilTegn();
}
