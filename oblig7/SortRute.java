class SortRute extends Rute{

  protected SortRute(int raden, int kolonnen, Labyrint labyrint){
    super(raden, kolonnen, labyrint);
  }
  @Override
  protected void gaa(Rute ruteViKomFra, String forelopigVei){
  }

  @Override
  protected char tilTegn(){
    return '#';
  }
}
