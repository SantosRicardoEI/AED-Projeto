package pt.ulusofona.aed.deisimdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TipoEntidade {
    ATOR,
    REALIZADOR,
    GENERO_CINEMATOGRAFICO,
    FILME,
    INPUT_INVALIDO
}

class Filme {
    int id;
    String name;
    float duration;
    float budget;
    String realese;
    ArrayList<String> genres;
    float rating;
    int ratingCount;

    public Filme(int id, String name, String realese) {
        this.id = id;
        this.name = name;
        this.realese = realese;
        this.genres = new ArrayList<>();
        this.rating = 0.0f;
        this.ratingCount = 0;
    }

    public Filme(int id, String name, String realese, float duration, float budget) {
        this.id = id;
        this.name = name;
        this.realese = realese;
        this.genres = new ArrayList<>();
        this.duration = duration;
        this.budget = budget;
        this.rating = 0.0f;
        this.ratingCount = 0;
    }

    public void adicionarGenero(String genre) {
        genres.add(genre);
    }

    public void setRating(float rating, int ratingCount) {
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.name + " | " + dateToYYYY_MM_DD(realese);
    }

    public String dateToYYYY_MM_DD(String dataDD_MM_YYYY) {
        String day = dataDD_MM_YYYY.substring(0, 2);
        String month = dataDD_MM_YYYY.substring(3, 5);
        String year = dataDD_MM_YYYY.substring(6, 10);
        return year + "-" + month + "-" + day;
    }
}

class Ator {
    int id;
    String name;
    char gender;
    int movieid;

    public Ator(int id, String name, char gender, int movieid) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.movieid = movieid;
    }

    String genderToText() {
        if (Character.toLowerCase(this.gender) == 'm') {
            return "Masculino";
        }
        if (Character.toLowerCase(this.gender) == 'f') {
            return "Feminino";
        }
        return "Desconhecido";
    }

    @Override
    public String toString() {
        return this.id + " | " + this.name + " | " + genderToText() + " | " + this.movieid;
    }
}

class Realizador {
    int id;
    String name;
    int movieid;

    public Realizador(int id, String name, int movieid) {
        this.id = id;
        this.name = name;
        this.movieid = movieid;
    }

    @Override
    public String toString() {
        return this.id + " | " + name + " | " + movieid;
    }
}

class GeneroCinematografico {
    int id;
    String name;

    public GeneroCinematografico(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.name;
    }
}

class InvalidInput {
    String fileName;
    int validLines;
    int invalidLines;
    int firstErrorLine;

    public InvalidInput(String fileName, int validLines, int invalidLines, int firstErrorLine) {
        this.fileName = fileName;
        this.validLines = validLines;
        this.invalidLines = invalidLines;
        this.firstErrorLine = firstErrorLine;
    }

    @Override
    public String toString() {
        return this.fileName + " | " + this.validLines + " | " + this.invalidLines + " | " + this.firstErrorLine;
    }
}

public class Main {

    static ArrayList<Filme> filmes = new ArrayList<Filme>();
    static ArrayList<Ator> atores = new ArrayList<Ator>();
    static ArrayList<GeneroCinematografico> generos = new ArrayList<GeneroCinematografico>();
    static ArrayList<Realizador> realizadores = new ArrayList<Realizador>();
    static ArrayList<InvalidInput> invalidInputs = new ArrayList<InvalidInput>();


    static boolean parseFilmes(File file) {

        filmes.clear();

        List<Integer> idsEncontrados = new ArrayList<>();

        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                linhaAtual++;

                if (linha.isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(",");

                boolean linhaInvalida = false;

                if (partes.length != 5) {
                    linhaInvalida = true;
                } else {
                    try {
                        int movieid = Integer.parseInt(partes[0].trim());
                        String movieName = partes[1].trim();
                        float movieDuration = Float.parseFloat(partes[2].trim());
                        float movieBudget = Float.parseFloat(partes[3].trim());
                        String releaseDate = partes[4].trim();

                        if (partes[0].isEmpty() ||
                                partes[1].isEmpty() ||
                                partes[2].isEmpty() ||
                                partes[3].isEmpty() ||
                                partes[4].isEmpty()) {
                            linhaInvalida = true;
                        }


                        if (idsEncontrados.contains(movieid)) {
                            linhaInvalida = true;
                        } else {
                            idsEncontrados.add(movieid);
                            Filme filme = new Filme(movieid, movieName, releaseDate, movieDuration, movieBudget);
                            filmes.add(filme);
                        }

                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                if (linhaInvalida) {
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseAtores(File file) {

        atores.clear();

        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                linhaAtual++;

                if (linha.isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(",");
                boolean linhaInvalida = false;

                if (partes.length != 4) {
                    linhaInvalida = true;
                } else {
                    try {
                        int id = Integer.parseInt(partes[0].trim());
                        String name = partes[1].trim();
                        char gender = partes[2].trim().charAt(0);
                        int movieid = Integer.parseInt(partes[3].trim());

                        if (partes[0].isEmpty() ||
                                partes[1].isEmpty() ||
                                partes[2].isEmpty() ||
                                partes[3].isEmpty()) {
                            linhaInvalida = true;
                        }

                        boolean duplicados = false;

                            Ator ator = new Ator(id, name, gender, movieid);
                            atores.add(ator);

                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        linhaInvalida = true;
                    }
                }

                if (linhaInvalida) {
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseRealizadores(File file) {

        realizadores.clear();

        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                linhaAtual++;

                if (linha.isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(",");

                boolean linhaInvalida = false;

                if (partes.length != 3) {
                    linhaInvalida = true;

                } else {
                    try {
                        int directorid = Integer.parseInt(partes[0].trim());
                        String directorName = partes[1].trim();
                        int movieid = Integer.parseInt(partes[2].trim());

                        if (partes[0].isEmpty() ||
                                partes[1].isEmpty() ||
                                partes[2].isEmpty()){
                            linhaInvalida = true;
                        }

                        boolean duplicado = false;
                        for (Realizador realizador : realizadores) {
                            if (realizador.id == directorid && realizador.movieid == movieid) {
                                duplicado = true;
                                break;
                            }
                        }

                        if (duplicado) {
                            linhaInvalida = true;

                        } else {
                            Realizador realizador = new Realizador(directorid, directorName, movieid);
                            realizadores.add(realizador);
                        }

                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                if (linhaInvalida) {
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseGeneros(File file) {

        generos.clear();

        List<Integer> idsEncontrados = new ArrayList<>();

        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                linhaAtual++;

                if (linha.isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(",");

                boolean linhaInvalida = false;

                if (partes.length != 2) {
                    linhaInvalida = true;
                } else {
                    try {
                        int genreid = Integer.parseInt(partes[0].trim());
                        String genreName = partes[1].trim();

                        if (partes[0].isEmpty() ||
                                partes[1].isEmpty()) {
                            linhaInvalida = true;
                        }

                        if (idsEncontrados.contains(genreid)) {
                            linhaInvalida = true;
                        } else {
                            idsEncontrados.add(genreid);
                            GeneroCinematografico genero = new GeneroCinematografico(genreid, genreName);
                            generos.add(genero);
                        }

                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                if (linhaInvalida) {
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseGeneroDoFilme(File file) {

        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                linhaAtual++;

                if (linha.isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(",");

                boolean linhaInvalida = false;

                if (partes.length != 2) {
                    linhaInvalida = true;
                } else {
                    try {
                        int genreid = Integer.parseInt(partes[0].trim());
                        int movieid = Integer.parseInt(partes[1].trim());

                        if (partes[0].isEmpty() ||
                                partes[1].isEmpty()) {
                            linhaInvalida = true;
                        }

                        String genreName = null;
                        for (GeneroCinematografico genero : generos) {
                            if (genero.id == genreid) {
                                genreName = genero.name;
                                break;
                            }
                        }

                        if (genreName != null) {
                            for (Filme filme : filmes) {
                                if (filme.id == movieid) {
                                    filme.adicionarGenero(genreName);
                                    break;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                if (linhaInvalida) {
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseVotosFilme(File file) {

        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                linhaAtual++;
                String linha = scanner.nextLine();
                String[] partes = linha.split(",");
                boolean linhaInvalida = false;


                if (partes.length == 3) {
                    try {
                        int movieid = Integer.parseInt(partes[0].trim());
                        float movieRating = Float.parseFloat(partes[1].trim());
                        int movieRatingCount = Integer.parseInt(partes[2].trim());

                        if (partes[0].isEmpty() ||
                                partes[1].isEmpty() ||
                                partes[2].isEmpty()) {
                            linhaInvalida = true;
                        }

                        for (Filme filme : filmes) {
                            if (filme.id == movieid) {
                                filme.setRating(movieRating, movieRatingCount);
                                break;
                            }
                        }

                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                } else {
                    linhaInvalida = true;
                }

                if (linhaInvalida) {
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseFiles(File pasta) {

        invalidInputs.clear();

        return parseFilmes(new File(pasta, "movies.csv")) &&
                parseAtores(new File(pasta, "actors.csv")) &&
                parseRealizadores(new File(pasta, "directors.csv")) &&
                parseGeneros(new File(pasta, "genres.csv")) &&
                parseGeneroDoFilme(new File(pasta, "genres_movies.csv")) &&
                parseVotosFilme(new File(pasta, "movie_votes.csv"));
    }


    static ArrayList<?> getObjects(TipoEntidade entidade) {
        return switch (entidade) {
            case ATOR -> atores;
            case REALIZADOR -> realizadores;
            case GENERO_CINEMATOGRAFICO -> generos;
            case FILME -> filmes;
            case INPUT_INVALIDO -> invalidInputs;
        };
    }

    static void testarSubstrings() {
        Filme filme = new Filme(149, "À deriva na Lusofona", "07-02-1998");
        Ator ator = new Ator(167, "Júlio Andor", 'm', 479);
        GeneroCinematografico genero = new GeneroCinematografico(66, "Terror");
        Realizador realizador = new Realizador(4, "Thomas", 80);
        InvalidInput invalid = new InvalidInput("movies.csv", 80, 2, 34);

        System.out.println("\nTestes toString():");
        System.out.println("\nFilme: \n" + filme);
        System.out.println("\nAtor: \n" + ator);
        System.out.println("\nGenero: \n" + genero);
        System.out.println("\nRealizador: \n" + realizador);
        System.out.println("\nInvalid: \n" + invalid);
    }

    static void imprimirListaEntidade(TipoEntidade entidade) {
        for (Object obj : getObjects(entidade)) {
            System.out.println(obj);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("Bem-vindo ao deisIMBD");
        long start = System.currentTimeMillis();
        boolean parseOk = parseFiles(new File("."));
        if (!parseOk) {
            System.out.println("Erro na leitura dos ficheiros");
            return;
        }
        long end = System.currentTimeMillis();

        System.out.println();
        imprimirListaEntidade(TipoEntidade.INPUT_INVALIDO);
        System.out.println();
        System.out.println("Ficheiros lidos com sucesso em " + (end - start) + " ms");


    }
}