import java.util.ArrayList;
import java.util.List;

public class JavaSeminar6 {
  public static void main(String[] args) {
    Infrastructure infrastructure = new Infrastructure();


    System.out.println(infrastructure.getAllInfo(1));
    System.out.println(infrastructure.getAllInfo(2));
    System.out.println(infrastructure.getAllInfo(3));
    System.out.println(infrastructure.getAllInfo(4));
    System.out.println();
    System.out.println(infrastructure.getTitleFilminfo("Dar", false));
  }
}

class Infrastructure {
  public Infrastructure() {
    init();
  }

  Db db;

  public Db getDb() {
    return db;
  }

  /**
   * @param title заданые символы которые ищем
   * @param fullMatch
   * @return
   * Метод выдает строку с совпадением заданых символов.
   */
  public List<String> getTitleFilminfo(String title, boolean fullMatch) {
    if (db == null) {
      return List.of();
    }
    var films = db.getTitleFilms(title, fullMatch);
    return films.stream().map(f -> getAllInfo(f.id)).toList();
  }

  public String getAllInfo(int idCinema) {
    Cinema c = db.films.get(idCinema - 1);

    return String.format("%d %s %s %s",
        c.id,
        c.name,
        db.genres.get(c.genre - 1).name,
        db.prod.get(c.filmProd - 1).titleName);
  }

  Db init() {
    db = new Db();
    Cinema c1 = new Cinema(1, "halo", 1, 1);
    Cinema c2 = new Cinema(2, "Dark knight", 1, 2);
    Cinema c3 = new Cinema(3, "Elden Ring", 3, 4);
    Cinema c4 = new Cinema(4, "Dark souls", 3, 3);

    db.films.add(c1);
    db.films.add(c2);
    db.films.add(c3);
    db.films.add(c4);

    db.genres.add(new Genre(1, "Drama"));
    db.genres.add(new Genre(2, "Comedy"));
    db.genres.add(new Genre(3, "Dark fantasy"));
    FilmProducerFactory pf = new FilmProducerFactory();
    db.addFilmProducer(pf.getFilmProducer("Mosfilm"));
    db.addFilmProducer(pf.getFilmProducer("Fond Kino"));
    db.addFilmProducer(pf.getFilmProducer("Marvel"));
    db.addFilmProducer(pf.getFilmProducer("DC"));

    return db;
  }
}

class Db {
  ArrayList<Cinema> films = new ArrayList<>();
  ArrayList<FilmProducer> prod = new ArrayList<>();
  ArrayList<Genre> genres = new ArrayList<>();

  public void addFilmProducer(FilmProducer producer) {
    prod.add(producer);
  }

  /**
   * @param title  набор символов которые надо найти.
   * @param fullMatch ручник
   * @return
   */
  List<Cinema> getTitleFilms(String title, boolean fullMatch) {
    if (title == null || title.isEmpty()) {
      return List.of();
    }

    if (fullMatch) {
      return films.stream().filter(t -> t != null && title.equalsIgnoreCase(t.name)).toList();
    } else {
      String ttl = title.toLowerCase();
      return films.stream().filter(t -> t != null && t.name.toLowerCase().contains(ttl)).toList();
    }

  }

}

class Cinema {
  int id;
  int filmProd;
  String name;
  int genre;

  public Cinema(int id, String name, int genre, int filmProd) {
    this.id = id;
    this.filmProd = filmProd;
    this.name = name;
    this.genre = genre;
  }
}

class FilmProducer {
  int id;
  String titleName;
}

class Genre {
  int id;
  String name;

  public Genre(int id, String name) {
    this.id = id;
    this.name = name;
  }
}

class FilmProducerFactory {
  int count = 1;

  public FilmProducer getFilmProducer(String name) {
    FilmProducer fp = new FilmProducer();
    fp.id = count++;
    fp.titleName = name;
    return fp;
  }
}