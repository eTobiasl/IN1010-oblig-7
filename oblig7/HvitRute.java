
class HvitRute extends Rute{

  class MinR implements Runnable{
    private HvitRute rute;
    private String vei;
    private HvitRute forrige;

    private MinR(HvitRute denneRuten, HvitRute forrigeRute, String denForelopigeVeien){
      rute = denneRuten;
      vei = denForelopigeVeien;
      forrige = forrigeRute;
    }
    public void run(){
      rute.gaa(forrige, vei);
    }
  } 

  protected HvitRute( int kolonnen, int raden, Labyrint labyrint){
    super(kolonnen, raden, labyrint);
    
  }
  @Override
  protected char tilTegn(){
    return '.';
  }

  @Override
  protected void gaa(Rute ruteViKomFra, String forelopigVei){
    if (hentBesokStatus() != false){
      return;
    }
    settBesokStatus(true);
    Rute[] naboArray = new Rute[4];
    naboArray[0] = nordNabo;
    naboArray[1] = sydNabo;
    naboArray[2] = ostNabo;
    naboArray[3] = vestNabo;

    forelopigVei += "(" + Integer.toString(kolonne) + ", " + Integer.toString(rad) + ") --> ";

    for (Rute i : naboArray){
      if (i != null){
        if (!i.equals(ruteViKomFra)){
          i.gaa(this, forelopigVei);
        }
      }
    }
    settBesokStatus(false);
  }
}
