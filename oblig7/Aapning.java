class Aapning extends HvitRute{
  protected Aapning(int kolonnen, int raden, Labyrint labyrint){
    super(kolonnen, raden, labyrint);
  }
  @Override
  protected void gaa(Rute ruteViKomFra, String forelopigVei){
  forelopigVei += "(" + Integer.toString(kolonne) + ", " + Integer.toString(rad) + ")";
  minLabyrint.hentMonitor().leggTil(forelopigVei);
  }

  @Override
  protected char tilTegn(){
    return 'O';
  }
}
