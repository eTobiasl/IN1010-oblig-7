import java.util.Iterator;

public class Lenkeliste<T> implements Liste<T> {
  Node inngang;
  //Node klassen defineres
  class Node{
    T innhold;
    Node neste = null;
    Node(T innholdet){
      innhold = innholdet;
    }
  }
    public Iterator<T> iterator(){
      return new LenkelisteIterator(this);
    }
    class LenkelisteIterator implements Iterator<T>{
      Liste<T> lenkelisten;
      int pos = 0;
      Node hjelpePeker = inngang;

      public LenkelisteIterator(Lenkeliste<T> nyListe){
        lenkelisten = nyListe;

      }
      public T next(){
        Node retur = hjelpePeker;
        hjelpePeker = hjelpePeker.neste;
        pos++;
        return retur.innhold;
      }
      public boolean hasNext(){
        return (pos < stoerrelse());
      }
    }
  //Metode for å finne størelsen til en liste
  @Override
  public int stoerrelse(){
    int counter = 1;
    Node hjelpePeker = inngang;
    if (hjelpePeker == null){
      return 0;
    }
    while (hjelpePeker.neste != null){
      hjelpePeker = hjelpePeker.neste;
      counter++;
    }
    return counter;
  }
  //Metode for å legge til på en vilkårlig plass i listen
  @Override
  public void leggTil(int pos, T x){
    Node nyNode = new Node(x);
    Node hjelpePeker = inngang;
    if (stoerrelse() == 0 && pos != 0 || pos > stoerrelse() || pos < 0){
      throw new UgyldigListeIndeks(-1);
    }else{
      if(pos == 0){
        nyNode.neste = inngang;
        inngang = nyNode;
      }else{
        int teller = 1;
        while(teller++ != pos){
          hjelpePeker = hjelpePeker.neste;
        }
        nyNode.neste = hjelpePeker.neste;
        hjelpePeker.neste = nyNode;
        }
      }
    }
    //Metode for å legge til i listen uten plassering
  @Override
  public void leggTil(T x){
    Node nyNode = new Node(x);
    Node hjelpePeker = inngang;

    if (stoerrelse() == 0){
      inngang = nyNode;
    }else{
      while(hjelpePeker.neste != null){
        hjelpePeker = hjelpePeker.neste;
      }
      hjelpePeker.neste = nyNode;
    }
  }
  //Metode for å erstatte en node med en ny node
  @Override
  public void sett(int pos, T x){ //pos = 1
    Node nyNode = new Node(x);
    Node hjelpePeker = inngang;
    int teller = 0;
    if (pos < stoerrelse() && pos >= 0){
      if (pos == 0){
        nyNode.neste = inngang.neste;
        inngang = nyNode;
      }else{
        while(teller++ != pos-1){
          hjelpePeker = hjelpePeker.neste;
        }
        nyNode.neste = hjelpePeker.neste.neste;
        hjelpePeker.neste = nyNode;
      }
    }else{
      throw new UgyldigListeIndeks(-1);
    }
  }
  //Metode for å hente en Nodes innhold
  @Override
  public T hent(int pos){
    Node hjelpePeker = inngang;
    int teller = 0;
    if (pos < stoerrelse() && pos >= 0){
      while(teller++ != pos){
        hjelpePeker = hjelpePeker.neste;
      }
      return hjelpePeker.innhold;
    }else{
      throw new UgyldigListeIndeks(pos);
    }
  }
  //Metode for å fjerne in node på en vilkårlig plass
  @Override
  public T fjern(int pos){
    Node hjelpePeker = inngang;
    int teller = 0;
    if(pos < stoerrelse() && pos >= 0){
      if (pos == 0){
        T retur = inngang.innhold;
        inngang = null;
        return retur;
      }
      while(teller++ != pos-1){
        hjelpePeker = hjelpePeker.neste;
      }
      Node holder = hjelpePeker.neste.neste;
      T retur = hjelpePeker.neste.innhold;
      hjelpePeker.neste = holder;
      return retur;
    }else{
      throw new UgyldigListeIndeks(pos);
    }
}
//Metode for å fjerne en node uten plassering
  @Override
  public T fjern(){
    if(stoerrelse() != 0){
      T retur;
      Node hjelpePeker = inngang;
      if(inngang.neste == null){
        retur = inngang.innhold;
          inngang = null;
      }else{
        hjelpePeker = inngang.neste;
        retur =  inngang.innhold;
        inngang = hjelpePeker;
      }
      return retur;
    }else{
      throw new UgyldigListeIndeks(0);
    }
  }
}
