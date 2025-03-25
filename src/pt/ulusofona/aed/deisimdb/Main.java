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
    String release;
    ArrayList<String> genres;
    float rating;
    int ratingCount;
    int numeroAtores;

    public Filme(int id, String name, String release, float duration, float budget) {
        this.id = id;
        this.name = name;
        this.release = release;
        this.genres = new ArrayList<>();
        this.duration = duration;
        this.budget = budget;
        this.rating = 0.0f;
        this.ratingCount = 0;
        this.numeroAtores = 0;
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
        if (this.id < 1000) {
            return this.id + " | " + this.name + " | " + dateToYYYY_MM_DD(release) + " | " + this.numeroAtores;
        }
        return this.id + " | " + this.name + " | " + dateToYYYY_MM_DD(release);
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

class IdPosicaoFilme {
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<Integer> posicoes = new ArrayList<>();

    void adicionar(int id, int posicao) {
        ids.add(id);
        posicoes.add(posicao);
    }

    int procurarPosicao(int id) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id) {
                return posicoes.get(i);
            }
        }
        return -1;
    }

    void limpar() {
        ids.clear();
        posicoes.clear();
    }
}

/* Tentativas de otimização:

   - Em vez de chamar .size na condição do ciclo (é verificado a cada volta) guardar o valor numa variável antes para usar sem chamar .size() tantas vezes
   - Em ParseFilmes usamos um array de boolean em que cada o o indice representa o id do filme, o valor guardado é se aquele id já foi visto, se nao foi marca-o como visto (true). (Duplicados)
   - Classe idPosicaoFilme:
        - Adiciona id à lista de ids e adiciona a posicao de esse id na lista de posicoes (Arrays paralelos).
        - A correspondencia de elementos entre as listas está no indice.
   -


*/
public class Main {

    static String ficheiroFilmes = "movies.csv";
    static String ficheiroAtores = "actors.csv";
    static String ficheiroGeneros = "genres.csv";
    static String ficheiroRealizadores = "directors.csv";
    static String ficheiroGenerosFilmes = "genres_movies.csv";
    static String ficheiroVotosFilmes = "movie_votes.csv";

    static ArrayList<Filme> filmes = new ArrayList<Filme>();
    static ArrayList<Ator> atores = new ArrayList<Ator>();
    static ArrayList<GeneroCinematografico> generos = new ArrayList<GeneroCinematografico>();
    static ArrayList<Realizador> realizadores = new ArrayList<Realizador>();
    static ArrayList<InvalidInput> invalidInputs = new ArrayList<InvalidInput>() {
    };
    static IdPosicaoFilme idPosicaoFilme = new IdPosicaoFilme();

    static void atualizarIdsEPosicoesFilmes() {
        idPosicaoFilme.limpar();
        int filmesSize = filmes.size();
        for (int i = 0; i < filmesSize; i++) {
            idPosicaoFilme.adicionar(filmes.get(i).id, i);
        }
    }

    static void gravarInvalidInput(File file, int ok, int erro, int primeiraErro) {
        invalidInputs.add(new InvalidInput(file.getName(), ok, erro, primeiraErro));
    }

    static boolean temEspacosVazios(String... espacos) {
        for (String espaco : espacos) {
            if (espaco == null || espaco.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    static void validarLinhas(boolean linhaInvalida, int linhaAtual, int[] invalidInput) {
        if (linhaInvalida) {
            invalidInput[1]++;
            if (invalidInput[2] == -1) {
                invalidInput[2] = linhaAtual;
            }
        } else {
            invalidInput[0]++;
        }
    }


    static boolean parseFilmes(File file) {

        filmes.clear();

        boolean[] idVisto = new boolean[1_000_000];

        int linhaAtual = 0;
        int[] invalidInput = new int[]{0, 0, -1};

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();
                linhaAtual++;
                boolean linhaInvalida = false;

                if (linha.isEmpty()) {
                    linhaInvalida = true;
                } else {

                    String[] partes = linha.split(",");
                    if (partes.length != 5) {
                        linhaInvalida = true;
                    } else {
                        try {
                            int movieid = 0;
                            String movieName = "";
                            float movieDuration = 0;
                            float movieBudget = 0;
                            String releaseDate = "";

                            String idStr = partes[0].trim();
                            String nameStr = partes[1].trim();
                            String durStr = partes[2].trim();
                            String budgetStr = partes[3].trim();
                            String dateStr = partes[4].trim();

                            if (temEspacosVazios(idStr, nameStr, durStr, budgetStr, dateStr)) {
                                linhaInvalida = true;
                            } else {

                                movieid = Integer.parseInt(idStr);
                                movieName = nameStr;
                                movieDuration = Float.parseFloat(durStr);
                                movieBudget = Float.parseFloat(budgetStr);
                                releaseDate = dateStr;

                                if (!idVisto[movieid]) {
                                    idVisto[movieid] = true;
                                    Filme filme = new Filme(movieid, movieName, releaseDate, movieDuration, movieBudget);
                                    filmes.add(filme);
                                }
                            }

                        } catch (NumberFormatException e) {
                            linhaInvalida = true;
                        }
                    }
                }
                validarLinhas(linhaInvalida, linhaAtual, invalidInput);
            }
            gravarInvalidInput(file, invalidInput[0], invalidInput[1], invalidInput[2]);
            atualizarIdsEPosicoesFilmes();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseAtores(File file) {

        atores.clear();
        int linhaAtual = 0;
        int[] invalidInput = new int[]{0, 0, -1};

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();
                linhaAtual++;
                boolean linhaInvalida = false;

                if (linha.isEmpty()) {
                    linhaInvalida = true;
                } else {
                    String[] partes = linha.split(",");
                    if (partes.length != 4) {
                        linhaInvalida = true;
                    } else {
                        try {
                            String idStr = partes[0].trim();
                            String nameStr = partes[1].trim();
                            String genderStr = partes[2].trim();
                            String movieIdStr = partes[3].trim();

                            if (temEspacosVazios(idStr, nameStr, genderStr, movieIdStr)) {
                                linhaInvalida = true;
                            } else {
                                int id = Integer.parseInt(idStr);
                                String name = nameStr;
                                char gender = genderStr.charAt(0);
                                int movieid = Integer.parseInt(movieIdStr);

                                int pos = idPosicaoFilme.procurarPosicao(movieid);
                                if (pos != -1) {
                                    filmes.get(pos).numeroAtores++;
                                }

                                atores.add(new Ator(id, name, gender, movieid));
                            }

                        } catch (Exception e) {
                            linhaInvalida = true;
                        }
                    }
                }
                validarLinhas(linhaInvalida, linhaAtual, invalidInput);
            }
            gravarInvalidInput(file, invalidInput[0], invalidInput[1], invalidInput[2]);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseRealizadores(File file) {

        realizadores.clear();
        int linhaAtual = 0;
        int[] invalidInput = new int[]{0, 0, -1};

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();
                linhaAtual++;
                boolean linhaInvalida = false;

                if (linha.isEmpty()) {
                    linhaInvalida = true;
                } else {
                    String[] partes = linha.split(",");
                    if (partes.length != 3) {
                        linhaInvalida = true;
                    } else {
                        try {
                            String idStr = partes[0].trim();
                            String nameStr = partes[1].trim();
                            String movieIdStr = partes[2].trim();

                            if (temEspacosVazios(idStr, nameStr, movieIdStr)) {
                                linhaInvalida = true;
                            } else {
                                int id = Integer.parseInt(idStr);
                                String name = nameStr;
                                int movieid = Integer.parseInt(movieIdStr);

                                realizadores.add(new Realizador(id, name, movieid));
                            }

                        } catch (Exception e) {
                            linhaInvalida = true;
                        }
                    }
                }
                validarLinhas(linhaInvalida, linhaAtual, invalidInput);
            }
            gravarInvalidInput(file, invalidInput[0], invalidInput[1], invalidInput[2]);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseGeneros(File file) {

        generos.clear();
        int linhaAtual = 0;
        int[] invalidInput = new int[]{0, 0, -1};

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();
                linhaAtual++;
                boolean linhaInvalida = false;

                if (linha.isEmpty()) {
                    linhaInvalida = true;
                } else {
                    String[] partes = linha.split(",");
                    if (partes.length != 2) {
                        linhaInvalida = true;
                    } else {
                        try {
                            String idStr = partes[0].trim();
                            String nameStr = partes[1].trim();

                            if (temEspacosVazios(idStr, nameStr)) {
                                linhaInvalida = true;
                            } else {
                                int id = Integer.parseInt(idStr);
                                String name = nameStr;

                                generos.add(new GeneroCinematografico(id, name));
                            }

                        } catch (Exception e) {
                            linhaInvalida = true;
                        }
                    }
                }
                validarLinhas(linhaInvalida, linhaAtual, invalidInput);
            }
            gravarInvalidInput(file, invalidInput[0], invalidInput[1], invalidInput[2]);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseGeneroDoFilme(File file) {

        int linhaAtual = 0;
        int[] invalidInput = new int[]{0, 0, -1};

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                linhaAtual++;
                boolean linhaInvalida = false;

                if (linha.isEmpty()) {
                    linhaInvalida = true;
                } else {
                    String[] partes = linha.split(",");
                    if (partes.length != 2) {
                        linhaInvalida = true;
                    } else {
                        try {
                            String genreIdStr = partes[0].trim();
                            String movieIdStr = partes[1].trim();

                            if (temEspacosVazios(genreIdStr, movieIdStr)) {
                                linhaInvalida = true;
                            } else {
                                int genreid = Integer.parseInt(genreIdStr);
                                int movieid = Integer.parseInt(movieIdStr);
                            }

                        } catch (NumberFormatException e) {
                            linhaInvalida = true;
                        }
                    }
                }
                validarLinhas(linhaInvalida, linhaAtual, invalidInput);
            }
            gravarInvalidInput(file, invalidInput[0], invalidInput[1], invalidInput[2]);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseVotosFilme(File file) {

        int linhaAtual = 0;
        int[] invalidInput = new int[]{0, 0, -1};

        try (Scanner scanner = new Scanner(file)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                linhaAtual++;
                boolean linhaInvalida = false;

                String[] partes = linha.split(",");
                if (partes.length != 3) {
                    linhaInvalida = true;
                } else {
                    try {
                        String idStr = partes[0].trim();
                        String ratingStr = partes[1].trim();
                        String ratingCountStr = partes[2].trim();

                        if (temEspacosVazios(idStr, ratingStr, ratingCountStr)) {
                            linhaInvalida = true;
                        } else {
                            int movieid = Integer.parseInt(idStr);
                            float movieRating = Float.parseFloat(ratingStr);
                            int movieRatingCount = Integer.parseInt(ratingCountStr);

                            // Falta adicionar os votos

                        }

                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }
                validarLinhas(linhaInvalida, linhaAtual, invalidInput);
            }
            gravarInvalidInput(file, invalidInput[0], invalidInput[1], invalidInput[2]);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseFiles(File pasta) {

        filmes.clear();
        atores.clear();
        realizadores.clear();
        generos.clear();
        invalidInputs.clear();

        return parseFilmes(new File(pasta, ficheiroFilmes)) &&
                parseAtores(new File(pasta, ficheiroAtores)) &&
                parseRealizadores(new File(pasta, ficheiroRealizadores)) &&
                parseGeneros(new File(pasta, ficheiroGeneros)) &&
                parseGeneroDoFilme(new File(pasta, ficheiroGenerosFilmes)) &&
                parseVotosFilme(new File(pasta, ficheiroVotosFilmes));
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