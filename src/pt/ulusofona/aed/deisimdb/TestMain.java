package pt.ulusofona.aed.deisimdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestMain {

    // Testes id <1000

    @Test
    public void TestIdInferiorMilAtores1() {
        Filme filme = new Filme(0, "A fuga das galinhas", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = 0;
        String resultado = filme.toString();
        String esperado = "0 | A fuga das galinhas | 1998-01-01 | 0";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdInferiorMilAtores2() {
        Filme filme = new Filme(2, "", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = 1;
        String resultado = filme.toString();
        String esperado = "2 |  | 1998-01-01 | 1";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdInferiorMilAtores3() {
        Filme filme = new Filme(10, "A fuga das galinhas", "01-01-1998", 0f, 0);
        filme.numeroAtores = 2;
        String resultado = filme.toString();
        String esperado = "10 | A fuga das galinhas | 1998-01-01 | 2";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdInferiorMilAtores4() {
        Filme filme = new Filme(-1, "\n", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = 10000;
        String resultado = filme.toString();
        String esperado = "-1 | \n | 1998-01-01 | 10000";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdInferiorMilAtores5() {
        Filme filme = new Filme(144, "A fuga das galinhas", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = -1;
        String resultado = filme.toString();
        String esperado = "144 | A fuga das galinhas | 1998-01-01 | -1";
        Assertions.assertEquals(esperado, resultado, "");
    }

    // Testes id >=1000

    @Test
    public void TestIdMaiorIgualMil1() {
        Filme filme = new Filme(1000, "A fuga das galinhas", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = 2;
        String resultado = filme.toString();
        String esperado = "1000 | A fuga das galinhas | 1998-01-01";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdMaiorIgualMil2() {
        Filme filme = new Filme(1001, "A fuga das galinhas", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = 2;
        String resultado = filme.toString();
        String esperado = "1001 | A fuga das galinhas | 1998-01-01";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdMaiorIgualMil3() {
        Filme filme = new Filme(1000000, "", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = 2;
        String resultado = filme.toString();
        String esperado = "1000000 |  | 1998-01-01";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdMaiorIgualMil4() {
        Filme filme = new Filme(5555, "\n", "01-01-1998", 85.0f, 120);
        filme.numeroAtores = 2;
        String resultado = filme.toString();
        String esperado = "5555 | \n | 1998-01-01";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestIdMaiorIgualMil5() {
        Filme filme = new Filme(9999, ".,..?", "01-01-1998", 0f, 0);
        filme.numeroAtores = 2;
        String resultado = filme.toString();
        String esperado = "9999 | .,..? | 1998-01-01";
        Assertions.assertEquals(esperado, resultado, "");
    }


    // Teste atores

    @Test
    public void TestAtoresTostring1() {
        Ator ator = new Ator(10, "Jack Antonio", 'M', 10);
        String resultado = ator.toString();
        String esperado = "10 | Jack Antonio | Masculino | 10";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestAtoresTostring2() {
        Ator ator = new Ator(20, "Jack Antonio", 'M', 10);
        String resultado = ator.toString();
        String esperado = "20 | Jack Antonio | Masculino | 10";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestAtoresTostring3() {
        Ator ator = new Ator(30, "Jack Antonio", 'M', 10);
        String resultado = ator.toString();
        String esperado = "30 | Jack Antonio | Masculino | 10";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestAtoresTostring4() {
        Ator ator = new Ator(40, "Jack Antonio", 'M', 10);
        String resultado = ator.toString();
        String esperado = "40 | Jack Antonio | Masculino | 10";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestAtoresTostring5() {
        Ator ator = new Ator(50, "Jack Antonio", 'M', 10);
        String resultado = ator.toString();
        String esperado = "50 | Jack Antonio | Masculino | 10";
        Assertions.assertEquals(esperado, resultado, "");
    }

    // Teste realizadores

    @Test
    public void TestRealizadoresTostring1() {
        Realizador realizador = new Realizador(11,"Antonio Banderas",15);
        String resultado = realizador.toString();
        String esperado = "11 | Antonio Banderas | 15";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestRealizadoresTostring2() {
        Realizador realizador = new Realizador(1000,"123 123",2000);
        String resultado = realizador.toString();
        String esperado = "1000 | 123 123 | 2000";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestRealizadoresTostring3() {
        Realizador realizador = new Realizador(0,"",0);
        String resultado = realizador.toString();
        String esperado = "0 |  | 0";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestRealizadoresTostring4() {
        Realizador realizador = new Realizador(10,"\n",14);
        String resultado = realizador.toString();
        String esperado = "10 | \n | 14";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestRealizadoresTostring5() {
        Realizador realizador = new Realizador(10,"-.,?",14);
        String resultado = realizador.toString();
        String esperado = "10 | -.,? | 14";
        Assertions.assertEquals(esperado, resultado, "");
    }

    // Teste generos

    @Test
    public void TestGenerosCinematograficosTostring1() {
        GeneroCinematografico genero =new GeneroCinematografico(0,"Terror");
        String resultado = genero.toString();
        String esperado = "0 | Terror";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestGenerosCinematograficosTostring2() {
        GeneroCinematografico genero =new GeneroCinematografico(10000,"\n");
        String resultado = genero.toString();
        String esperado = "10000 | \n";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestGenerosCinematograficosTostring3() {
        GeneroCinematografico genero =new GeneroCinematografico(10,"");
        String resultado = genero.toString();
        String esperado = "10 | ";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestGenerosCinematograficosTostring4() {
        GeneroCinematografico genero =new GeneroCinematografico(-1,"History");
        String resultado = genero.toString();
        String esperado = "-1 | History";
        Assertions.assertEquals(esperado, resultado, "");
    }

    @Test
    public void TestGenerosCinematograficosTostring5() {
        GeneroCinematografico genero =new GeneroCinematografico(100,"-.,?");
        String resultado = genero.toString();
        String esperado = "100 | -.,?";
        Assertions.assertEquals(esperado, resultado, "");
    }

    // Testes elementos

    @Test
    public void elementosEmGetObjects6() {
        Main.parseFiles(new File("test-files"));

        Assertions.assertTrue(Main.getObjects(TipoEntidade.FILME).size() >= 2, "Devem existir pelo menos 2 filmes");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.ATOR).size() >= 2, "Devem existir pelo menos 2 atores");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.REALIZADOR).size() >= 2, "Devem existir pelo menos 2 realizadores");
        Assertions.assertFalse(Main.getObjects(TipoEntidade.GENERO_CINEMATOGRAFICO).isEmpty(), "Deve existir pelo menos 1 género");
    }

    @Test
    public void elementosEmGetObjects7() {
        Main.parseFiles(new File("test-files"));

        Assertions.assertTrue(Main.getObjects(TipoEntidade.FILME).size() >= 2, "Devem existir pelo menos 2 filmes");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.ATOR).size() >= 2, "Devem existir pelo menos 2 atores");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.REALIZADOR).size() >= 2, "Devem existir pelo menos 2 realizadores");
        Assertions.assertFalse(Main.getObjects(TipoEntidade.GENERO_CINEMATOGRAFICO).isEmpty(), "Deve existir pelo menos 1 género");
    }

    @Test
    public void elementosEmGetObjects8() {
        Main.parseFiles(new File("test-files"));

        Assertions.assertTrue(Main.getObjects(TipoEntidade.FILME).size() >= 2, "Devem existir pelo menos 2 filmes");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.ATOR).size() >= 2, "Devem existir pelo menos 2 atores");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.REALIZADOR).size() >= 2, "Devem existir pelo menos 2 realizadores");
        Assertions.assertFalse(Main.getObjects(TipoEntidade.GENERO_CINEMATOGRAFICO).isEmpty(), "Deve existir pelo menos 1 género");
    }

    @Test
    public void elementosEmGetObjects9() {
        Main.parseFiles(new File("test-files"));

        Assertions.assertTrue(Main.getObjects(TipoEntidade.FILME).size() >= 2, "Devem existir pelo menos 2 filmes");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.ATOR).size() >= 2, "Devem existir pelo menos 2 atores");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.REALIZADOR).size() >= 2, "Devem existir pelo menos 2 realizadores");
        Assertions.assertFalse(Main.getObjects(TipoEntidade.GENERO_CINEMATOGRAFICO).isEmpty(), "Deve existir pelo menos 1 género");
    }

    @Test
    public void elementosEmGetObjects10() {
        Main.parseFiles(new File("test-files"));

        Assertions.assertTrue(Main.getObjects(TipoEntidade.FILME).size() >= 2, "Devem existir pelo menos 2 filmes");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.ATOR).size() >= 2, "Devem existir pelo menos 2 atores");
        Assertions.assertTrue(Main.getObjects(TipoEntidade.REALIZADOR).size() >= 2, "Devem existir pelo menos 2 realizadores");
        Assertions.assertFalse(Main.getObjects(TipoEntidade.GENERO_CINEMATOGRAFICO).isEmpty(), "Deve existir pelo menos 1 género");
    }

    // Testes invalidInput

    @Test
    public void comLinhasInvalidas1() {
        Main.parseFiles(new File("test-files"));

        var invalidos = Main.getObjects(TipoEntidade.INPUT_INVALIDO);

        boolean encontrouInvalido = false;
        for (Object obj : invalidos) {
            InvalidInput input = (InvalidInput) obj;
            if (input.invalidLines > 0) {
                encontrouInvalido = true;
            }
        }
        Assertions.assertTrue(encontrouInvalido, "Nenhum dos ficheiros tem linha invalidass");
    }

    @Test
    public void comLinhasInvalidas2() {
        Main.parseFiles(new File("test-files"));

        var invalidos = Main.getObjects(TipoEntidade.INPUT_INVALIDO);

        boolean encontrouInvalido = false;
        for (Object obj : invalidos) {
            InvalidInput input = (InvalidInput) obj;
            if (input.invalidLines > 0) {
                encontrouInvalido = true;
            }
        }
        Assertions.assertTrue(encontrouInvalido, "Nenhum dos ficheiros tem linha invalidass");
    }

    @Test
    public void comLinhasInvalidas3() {
        Main.parseFiles(new File("test-files"));

        var invalidos = Main.getObjects(TipoEntidade.INPUT_INVALIDO);

        boolean encontrouInvalido = false;
        for (Object obj : invalidos) {
            InvalidInput input = (InvalidInput) obj;
            if (input.invalidLines > 0) {
                encontrouInvalido = true;
            }
        }
        Assertions.assertTrue(encontrouInvalido, "Nenhum dos ficheiros tem linha invalidass");
    }

    @Test
    public void comLinhasInvalidas4() {
        Main.parseFiles(new File("test-files"));

        var invalidos = Main.getObjects(TipoEntidade.INPUT_INVALIDO);

        boolean encontrouInvalido = false;
        for (Object obj : invalidos) {
            InvalidInput input = (InvalidInput) obj;
            if (input.invalidLines > 0) {
                encontrouInvalido = true;
            }
        }
        Assertions.assertTrue(encontrouInvalido, "Nenhum dos ficheiros tem linha invalidass");
    }

    @Test
    public void comLinhasInvalidas5() {
        Main.parseFiles(new File("test-files"));

        var invalidos = Main.getObjects(TipoEntidade.INPUT_INVALIDO);

        boolean encontrouInvalido = false;
        for (Object obj : invalidos) {
            InvalidInput input = (InvalidInput) obj;
            if (input.invalidLines > 0) {
                encontrouInvalido = true;
            }
        }
        Assertions.assertTrue(encontrouInvalido, "Nenhum dos ficheiros tem linha invalidass");
    }
}