package pt.ulusofona.aed.deisimdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

        // Limpa a lista anterior
        filmes.clear();

        // Variaveis para contar erros e detetar linhas
        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            // Para ignorar a primeira linha (o cabeçalho)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Enquanto houver linhas para ler
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                // Conta a linha atual
                linhaAtual++;

                // Se a linha estiver vazia salta para a próxima
                if (linha.isEmpty()) {
                    continue;
                }

                // Divide a linha nas partes
                String[] partes = linha.split(",");

                // Linha ainda nao é invalida....
                boolean linhaInvalida = false;

                // Se a linha tiver partes a mais, a linha é inválida e será contabilizada mais abaixo
                if (partes.length != 5) {
                    linhaInvalida = true;
                    // Caso tenha as partes certas tenta atribuir os valores a cada atributo
                } else {
                    try {
                        int movieid = Integer.parseInt(partes[0].trim());
                        String movieName = partes[1].trim();
                        float movieDuration = Float.parseFloat(partes[2].trim());
                        float movieBudget = Float.parseFloat(partes[3].trim());
                        String releaseDate = partes[4].trim();

                        // Deteta se ja foi lido um filme com o id nesta linha
                        boolean idExiste = false;
                        for (Filme filme : filmes) {
                            if (filme.id == movieid) {
                                idExiste = true;
                                break;
                            }
                        }

                        // Se sim a linha é invalida e será contabilizada mais abaixo
                        if (idExiste) {
                            linhaInvalida = true;
                            // Se nao adiciono esta linha (filme) à lista de filmes
                        } else {
                            Filme filme = new Filme(movieid, movieName, releaseDate, movieDuration, movieBudget);
                            filmes.add(filme);
                            System.out.println("Adicionado filme");
                        }

                        // Caso nao consiga ler a linha para os atributos corretamente marca a linha como invalida
                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                // Se a linha foi marcada com invalida durante o ciclo contabiliza o erro.
                if (linhaInvalida) {
                    // Se for a primeira linha guarda o numero da linha
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            // Guarda o registo da leitura (objeto InvalidInput) na lista invalidInputs
            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            // Se chegou aqui conseguiu abrir o ficheiro
            return true;
        } catch (FileNotFoundException e) {
            // Se chegou aqui não conseguiu abrir o ficheiro
            return false;
        }
    }

    static boolean parseAtores(File file) {

        // Limpa a lista anterior
        atores.clear();

        // Variáveis para contar erros e detectar linhas
        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            // Ignorar a primeira linha (cabeçalho)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Ler cada linha do arquivo
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                // Conta a linha atual
                linhaAtual++;

                // Ignora linhas vazias
                if (linha.isEmpty()) {
                    continue;
                }

                // Separa os dados
                String[] partes = linha.split(",");
                boolean linhaInvalida = false;

                // Verifica se a linha tem a quantidade de dados correta
                if (partes.length != 4) {
                    linhaInvalida = true;
                } else {
                    try {
                        int id = Integer.parseInt(partes[0].trim());
                        String name = partes[1].trim();
                        char gender = partes[2].trim().charAt(0);
                        int movieid = Integer.parseInt(partes[3].trim());

                        // Verifica se existem entradas duplicadas
                        boolean duplicado = false;
                        for (Ator ator : atores) {
                            if (ator.id == id && ator.movieid == movieid) {
                                duplicado = true;
                                break;
                            }
                        }

                        // Se é duplicado marca linha como invalida
                        if (duplicado) {
                            linhaInvalida = true;
                        } else {
                            // Adiciona o ator à lista
                            Ator ator = new Ator(id, name, gender, movieid);
                            atores.add(ator);
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        linhaInvalida = true;
                    }
                }

                // Contabiliza o erro se a linha for inválida
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

            // Guardaas informações sobre a leitura
            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    static boolean parseRealizadores(File file) {


        // Limpa a lista anterior
        realizadores.clear();

        // Variaveis para contar erros e detetar linhas
        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            // Para ignorar a primeira linha (o cabeçalho)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Enquanto houver linhas para ler
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                // Conta a linha atual
                linhaAtual++;

                // Se a linha estiver vazia salta para a próxima
                if (linha.isEmpty()) {
                    continue;
                }

                // Divide a linha nas partes
                String[] partes = linha.split(",");

                // Linha ainda nao é invalida....
                boolean linhaInvalida = false;

                // Se a linha tiver partes a mais, a linha é inválida e será contabilizada mais abaixo
                if (partes.length != 3) {
                    linhaInvalida = true;

                    // Caso tenha as partes certas tenta atribuir os valores a cada atributo
                } else {
                    try {
                        int directorid = Integer.parseInt(partes[0].trim());
                        String directorName = partes[1].trim();
                        int movieid = Integer.parseInt(partes[2].trim());

                        // Deteta se duplicado
                        boolean duplicado = false;
                        for (Realizador realizador : realizadores) {
                            if (realizador.id == directorid && realizador.movieid == movieid) {
                                duplicado = true;
                                break;
                            }
                        }

                        // Se sim a linha é invalida e será contabilizada mais abaixo
                        if (duplicado) {
                            linhaInvalida = true;

                            // Se nao adiciono esta linha (realizador) à lista de filmes
                        } else {
                            Realizador realizador = new Realizador(directorid, directorName, movieid);
                            realizadores.add(realizador);
                        }

                        // Caso nao consiga ler a linha para os atributos corretamente marca a linha como invalida
                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                // Se a linha foi marcada com invalida durante o ciclo contabiliza o erro.
                if (linhaInvalida) {
                    // Se for a primeira linha guarda o numero da linha
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            // Guarda o registo da leitura (objeto InvalidInput) na lista invalidInputs
            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            // Se chegou aqui conseguiu abrir o ficheiro
            return true;
        } catch (FileNotFoundException e) {
            // Se chegou aqui não conseguiu abrir o ficheiro
            return false;
        }
    }

    static boolean parseGeneros(File file) {

        // Limpa a lista anterior
        generos.clear();

        // Variaveis para contar erros e detetar linhas
        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            // Para ignorar a primeira linha (o cabeçalho)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Enquanto houver linhas para ler
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                // Conta a linha atual
                linhaAtual++;

                // Se a linha estiver vazia salta para a próxima
                if (linha.isEmpty()) {
                    continue;
                }

                // Divide a linha nas partes
                String[] partes = linha.split(",");

                // Linha ainda nao é invalida....
                boolean linhaInvalida = false;

                // Se a linha tiver partes a mais, a linha é inválida e será contabilizada mais abaixo
                if (partes.length != 2) {
                    linhaInvalida = true;
                    // Caso tenha as partes certas tenta atribuir os valores a cada atributo
                } else {
                    try {
                        int genreid = Integer.parseInt(partes[0].trim());
                        String genreName = partes[1].trim();

                        // Deteta se ja foi lido um filme com o id nesta linha
                        boolean idExiste = false;
                        for (GeneroCinematografico genero : generos) {
                            if (genero.id == genreid) {
                                idExiste = true;
                                break;
                            }
                        }

                        // Se sim a linha é invalida e será contabilizada mais abaixo
                        if (idExiste) {
                            linhaInvalida = true;

                            // Se nao adiciono esta linha (genero) à lista de filmes
                        } else {
                            GeneroCinematografico genero = new GeneroCinematografico(genreid, genreName);
                            generos.add(genero);
                        }

                        // Caso nao consiga ler a linha para os atributos corretamente marca a linha como invalida
                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                // Se a linha foi marcada com invalida durante o ciclo contabiliza o erro.
                if (linhaInvalida) {
                    // Se for a primeira linha guarda o numero da linha
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            // Guarda o registo da leitura (objeto InvalidInput) na lista invalidInputs
            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            // Se chegou aqui conseguiu abrir o ficheiro
            return true;
        } catch (FileNotFoundException e) {
            // Se chegou aqui não conseguiu abrir o ficheiro
            return false;
        }
    }

    static boolean parseGenerosFilme(File file) {

        // Variaveis para contar erros e detetar linhas
        int linhaAtual = 0;
        int linhasOk = 0;
        int linhasComErro = 0;
        int primeiraLinhaComErro = -1;
        boolean ePrimeiraComErro = true;

        try (Scanner scanner = new Scanner(file)) {

            // Para ignorar a primeira linha (o cabeçalho)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Enquanto houver linhas para ler
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                // Conta a linha atual
                linhaAtual++;

                // Se a linha estiver vazia salta para a próxima
                if (linha.isEmpty()) {
                    continue;
                }

                // Divide a linha nas partes
                String[] partes = linha.split(",");

                // Linha ainda nao é invalida....
                boolean linhaInvalida = false;

                // Se a linha tiver partes a mais, a linha é inválida e será contabilizada mais abaixo
                if (partes.length != 2) {
                    linhaInvalida = true;
                    // Caso tenha as partes certas tenta atribuir os valores a cada atributo
                } else {
                    try {
                        int genreid = Integer.parseInt(partes[0].trim());
                        int movieid = Integer.parseInt(partes[1].trim());

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
                        // Caso nao consiga ler a linha para os atributos corretamente marca a linha como invalida
                    } catch (NumberFormatException e) {
                        linhaInvalida = true;
                    }
                }

                // Se a linha foi marcada com invalida durante o ciclo contabiliza o erro.
                if (linhaInvalida) {
                    // Se for a primeira linha guarda o numero da linha
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }

            // Guarda o registo da leitura (objeto InvalidInput) na lista invalidInputs
            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            // Se chegou aqui conseguiu abrir o ficheiro
            return true;
        } catch (FileNotFoundException e) {
            // Se chegou aqui não conseguiu abrir o ficheiro
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
                    // Se for a primeira linha guarda o numero da linha
                    if (ePrimeiraComErro) {
                        primeiraLinhaComErro = linhaAtual;
                        ePrimeiraComErro = false;
                    }
                    linhasComErro++;
                } else {
                    linhasOk++;
                }
            }


            // Guarda o registo da leitura (objeto InvalidInput) na lista invalidInputs
            InvalidInput invalidInput = new InvalidInput(file.getName(), linhasOk, linhasComErro, primeiraLinhaComErro);
            invalidInputs.add(invalidInput);

            // Se chegou aqui conseguiu abrir o ficheiro
            return true;
        } catch (FileNotFoundException e) {
            // Se chegou aqui não conseguiu abrir o ficheiro
            return false;
        }
    }

    static boolean parseFiles(File pasta) {
        return parseFilmes(new File(pasta, "movies.csv")) &&
                parseAtores(new File(pasta, "actors.csv")) &&
                parseRealizadores(new File(pasta, "directors.csv")) &&
                parseGeneros(new File(pasta, "genres.csv")) &&
                parseVotosFilme(new File(pasta, "movie_votes.csv")) &&
                parseGenerosFilme(new File(pasta, "genres_movies.csv"));
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
