package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import LPRS.LearningPath;
import usuario.*;
import actividad.*;
import pregunta.PreguntaCerrada;
import pregunta.Opcion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class testQuiz {
    
    private Quiz quiz;
    private Profesor profesor;
    private Estudiante estudiante;
    private Map<Estudiante, Status> estadosPorEstudiante;
    private List<PreguntaCerrada> listaPreguntas;
    private LearningPath learningPath;

    @BeforeEach
    public void setUp() {
        profesor = new Profesor("Juan", "password", "juan@example.com", new ArrayList<>(), new ArrayList<>());
        estudiante = new Estudiante("Maria", "password", "maria@example.com");

        listaPreguntas = new ArrayList<>();

        // Configurar PreguntaCerrada
        PreguntaCerrada pregunta1 = new PreguntaCerrada("¿Cuál es la capital de Francia?");
        Dictionary<Opcion, String> opcionA1 = new Hashtable<>();
        opcionA1.put(Opcion.A, "Madrid");
        Dictionary<Opcion, String> opcionB1 = new Hashtable<>();
        opcionB1.put(Opcion.B, "París"); // Respuesta correcta
        pregunta1.setOpcionA(opcionA1);
        pregunta1.setOpcionB(opcionB1);
        pregunta1.setRespuesta(opcionB1);

        PreguntaCerrada pregunta2 = new PreguntaCerrada("¿Qué planeta es conocido como el planeta rojo?");
        Dictionary<Opcion, String> opcionA2 = new Hashtable<>();
        opcionA2.put(Opcion.A, "Venus");
        Dictionary<Opcion, String> opcionB2 = new Hashtable<>();
        opcionB2.put(Opcion.B, "Marte"); // Respuesta correcta
        pregunta2.setOpcionA(opcionA2);
        pregunta2.setOpcionB(opcionB2);
        pregunta2.setRespuesta(opcionB2);

        listaPreguntas.add(pregunta1);
        listaPreguntas.add(pregunta2);

        // Crear el Quiz
        estadosPorEstudiante = new HashMap<>();
        quiz = new Quiz(
                "Quiz Inicial",
                Nivel.Intermedio,
                "Probar conocimientos básicos",
                30,
                1.0,
                LocalDateTime.now().plusDays(5),
                estadosPorEstudiante,
                Obligatoria.SI,
                listaPreguntas,
                70.0, // Calificación mínima para aprobar
                profesor,
                new ArrayList<>(),
                new ArrayList<>()
        );

        // Crear LearningPath
        List<Actividad> actividades = new ArrayList<>();
        actividades.add(quiz);

        learningPath = new LearningPath(
                "Learning Path de Prueba",
                Nivel.Intermedio,
                "Camino de aprendizaje de prueba",
                "Objetivo general",
                120,
                profesor,
                4.5f,
                actividades
        );

        learningPath.inscripcionEstudiante(estudiante); // Inscribir al estudiante en el LearningPath
        // Inscribir al estudiante en el Quiz
        quiz.inscripcionEstudiante(estudiante);
    }

    @Test
    public void testGetListaPreguntas(){

        List<PreguntaCerrada> listaPreguntas = quiz.getListaPreguntas();
        assertEquals(2, listaPreguntas.size());


    }

    @Test
    public void testGetCalificacionMinima(){

        double calificacionMinima = quiz.getCalificacionMinima();
        assertEquals(70.0, calificacionMinima);

    }

    @Test
    public void testGetCalificacionObtenida(){

        double calificacionObtenida = quiz.getCalificacionObtenida(estudiante);
        assertEquals(0.0, calificacionObtenida);

    }

    @Test
    public void testGetCalificacionesObtenidas(){

        Map<Estudiante, Double> calificacionesObtenidas = quiz.getCalificacionesObtenidas();
        assertEquals(1, calificacionesObtenidas.size());

        // 1 por el estudiante inscrito

    }

    @Test
    public void testSetListaPreguntas(){

        // Configurar PreguntaCerrada
        PreguntaCerrada pregunta3 = new PreguntaCerrada("¿Cuál es la capital de España?");
        Dictionary<Opcion, String> opcionA3 = new Hashtable<>();
        opcionA3.put(Opcion.A, "Madrid"); // Respuesta correcta
        Dictionary<Opcion, String> opcionB3 = new Hashtable<>();
        opcionB3.put(Opcion.B, "París");
        pregunta3.setOpcionA(opcionA3);
        pregunta3.setOpcionB(opcionB3);
        pregunta3.setRespuesta(opcionA3);

        List<PreguntaCerrada> listaPreguntas = new ArrayList<>();
        listaPreguntas.add(pregunta3);

        quiz.setListaPreguntas(listaPreguntas);

        assertEquals(1, quiz.getListaPreguntas().size());

    }

    @Test
    public void testSetCalificacionMinima(){

        quiz.setCalificacionMinima(80.0);
        assertEquals(80.0, quiz.getCalificacionMinima());

    }

    @Test
    public void testSetCalificacionObtenida(){

        quiz.setCalificacionObtenida(estudiante, 75.0);
        assertEquals(75.0, quiz.getCalificacionObtenida(estudiante));

    }

    @Test
    public void testSetCalificacionesObtenidas(){

        Map<Estudiante, Double> calificacionesObtenidas = new HashMap<>();
        calificacionesObtenidas.put(estudiante, 75.0);
        quiz.setCalificacionesObtenidas(calificacionesObtenidas);

        assertEquals(75.0, quiz.getCalificacionObtenida(estudiante));

    }   

    @Test
    public void testResponderValido(){

        // Responder las preguntas correctamente en formato correcto

        quiz.responder(estudiante, "1:B,2:B");  

        assertEquals(100.0, quiz.getCalificacionObtenida(estudiante));



    }

    @Test
    public void testResponderInvalidoPorEstudianteNulo(){

        // Se deberia madnar un mensaje de error

        assertThrows(IllegalArgumentException.class, () -> {
            quiz.responder(null, "1:B,2:B");
        });
    }

    @Test 
    public void testResponderInvalidoPorFormatoIncorrecto(){

        // Formato incorrecto

        assertThrows(IllegalArgumentException.class, () -> {
            quiz.responder(estudiante, "");
        });

    }

    @Test

    public void testResponderInvalidoPorYaCompletado(){

        // Responder las preguntas correctamente en formato correcto

        quiz.responder(estudiante, "1:B,2:B");

        // Se deberia madnar un mensaje de error

        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.responder(estudiante, "1:B,2:B");
        });

    }

    @Test
    public void testEsExitosaValido(){

        // Responder las preguntas correctamente en formato correcto

        quiz.responder(estudiante, "1:B,2:B");

        assertTrue(quiz.esExitosa(estudiante));

    }

    @Test
    public void testEsExitosaInvalidoPorEstudianteNulo(){

        // Estudiante nulo

        quiz.responder(estudiante, "1:B,2:B");

        // Se deberia madnar un mensaje de error

        assertThrows(IllegalArgumentException.class, () -> {
            quiz.esExitosa(null);
        });

    }

    @Test
    public void testEsExitosaInvalidoPorNoCompletado(){

        // No se ha completado el quiz

        assertFalse(quiz.esExitosa(estudiante));

    }

    @Test
    public void testEsExitosaInvalidoPorCalificacionMinimaNoAlcanzada(){

        // Responder las preguntas correctamente en formato correcto

        quiz.responder(estudiante, "1:B,2:A");

        assertFalse(quiz.esExitosa(estudiante));

    }

    @Test
    public void testReintentarValido(){

        // Responder las preguntas incorrectamente en formato correcto

        quiz.responder(estudiante, "1:B,2:A");

        // Reintentar el quiz

        quiz.reintentar(estudiante);

        assertEquals(0.0, quiz.getCalificacionObtenida(estudiante));

        // Responder las preguntas correctamente en formato correcto

        quiz.responder(estudiante, "1:B,2:B");

        assertEquals(100.0, quiz.getCalificacionObtenida(estudiante));
    }

    @Test 
    public void testReintentarInvalidoPorNoEstarInscritoEnQuizNiLearningPath(){

        // Estudiante no inscrito en el quiz

        Estudiante estudiante2 = new Estudiante("Pedro", "123", "pedro@example.com");

        // Inscribirlo al LearningPath
        // Se deberia madnar un mensaje de error

        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.reintentar(estudiante2);
        });


    }

    @Test
    public void testReintentarInvalidoPorEstudianteNoInscritoEnElLearningPathCorrecto(){

        // Estudiante no inscrito en el LearningPath correcto

        Estudiante estudiante2 = new Estudiante("Pedro", "123", "pedro@example.com");

        // Inscribirlo a un LearningPath incorrecto

        LearningPath learningPath2 = new LearningPath(
            "Learning Path de Prueba 2",
            Nivel.Intermedio,
            "Camino de aprendizaje de prueba 2",
            "Objetivo general 2",
            120,
            profesor,
            4.5f,
            new ArrayList<>()
        );

        learningPath2.inscripcionEstudiante(estudiante2);

        // Inscribirlo al quiz va a mandar un mensaje de error, entonces intentemos reintenar el quiz

        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.reintentar(estudiante2);
        });



    }

    @Test
    public void testReintentarInvalidoPorEstudianteNulo(){

        // Estudiante nulo

        quiz.responder(estudiante, "1:B,2:A");

        // Se deberia madnar un mensaje de error

        assertThrows(IllegalArgumentException.class, () -> {
            quiz.reintentar(null);
        });


    }


    @Test
    public void testReintentarInvalidoPorCalificacionMinimaAlcanzada(){

        // Responder las preguntas correctamente en formato correcto

        quiz.responder(estudiante, "1:B,2:B");

        // Se deberia madnar un mensaje de error

        assertThrows(UnsupportedOperationException.class, () -> {quiz.reintentar(estudiante);});

    }

    @Test
    public void testAgregarPreguntaValido(){

        // Configurar PreguntaCerrada
        PreguntaCerrada pregunta3 = new PreguntaCerrada("¿Cuál es la capital de España?");
        Dictionary<Opcion, String> opcionA3 = new Hashtable<>();
        opcionA3.put(Opcion.A, "Madrid"); // Respuesta correcta
        Dictionary<Opcion, String> opcionB3 = new Hashtable<>();
        opcionB3.put(Opcion.B, "París");
        pregunta3.setOpcionA(opcionA3);
        pregunta3.setOpcionB(opcionB3);
        pregunta3.setRespuesta(opcionA3);

        quiz.agregarPregunta(pregunta3);

        assertEquals(3, quiz.getListaPreguntas().size());



    }

    @Test
    public void testAgregarPreguntaInvalidoPorPreguntaNula(){

        // Pregunta nula

        assertThrows(IllegalArgumentException.class, () -> {
            quiz.agregarPregunta(null);
        });

    }

    @Test
    public void testEliminarPreguntaValido(){

        // Configurar PreguntaCerrada
        PreguntaCerrada pregunta3 = new PreguntaCerrada("¿Cuál es la capital de España?");
        Dictionary<Opcion, String> opcionA3 = new Hashtable<>();
        opcionA3.put(Opcion.A, "Madrid"); // Respuesta correcta
        Dictionary<Opcion, String> opcionB3 = new Hashtable<>();
        opcionB3.put(Opcion.B, "París");
        pregunta3.setOpcionA(opcionA3);
        pregunta3.setOpcionB(opcionB3);
        pregunta3.setRespuesta(opcionA3);

        quiz.agregarPregunta(pregunta3);

        assertEquals(3, quiz.getListaPreguntas().size());

        quiz.eliminarPregunta(pregunta3);

        assertEquals(2, quiz.getListaPreguntas().size());


    }

    @Test
    public void testEliminarPreguntaInvalidoPorPreguntaNula(){

        // Pregunta nula

        assertThrows(IllegalArgumentException.class, () -> {
            quiz.eliminarPregunta(null);
        });

    }

    @Test
    public void testEliminarPreguntaInvalidoPorPreguntaNoExistente(){

        // Configurar PreguntaCerrada
        PreguntaCerrada pregunta3 = new PreguntaCerrada("¿Cuál es la capital de España?");
        Dictionary<Opcion, String> opcionA3 = new Hashtable<>();
        opcionA3.put(Opcion.A, "Madrid"); // Respuesta correcta
        Dictionary<Opcion, String> opcionB3 = new Hashtable<>();
        opcionB3.put(Opcion.B, "París");
        pregunta3.setOpcionA(opcionA3);
        pregunta3.setOpcionB(opcionB3);
        pregunta3.setRespuesta(opcionA3);

        quiz.agregarPregunta(pregunta3);

        assertEquals(3, quiz.getListaPreguntas().size());

        // Pregunta no existente

        PreguntaCerrada pregunta4 = new PreguntaCerrada("¿Cuál es la capital de Alemania?");
        Dictionary<Opcion, String> opcionA4 = new Hashtable<>();
        opcionA4.put(Opcion.A, "Madrid"); // Respuesta correcta
        Dictionary<Opcion, String> opcionB4 = new Hashtable<>();
        opcionB4.put(Opcion.B, "Berlín");
        pregunta4.setOpcionA(opcionA4);
        pregunta4.setOpcionB(opcionB4);
        pregunta4.setRespuesta(opcionB4);

        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.eliminarPregunta(pregunta4);
        });

    }

    @Test
    public void testEliminarPreguntaInvalidoPorTamanio(){

    // Vacias todas las preguntas

    quiz.eliminarPregunta(listaPreguntas.get(1));

   // Al eliminar la unica pregunta que hay en el quiz, se deberia mandar un mensaje de error

    assertThrows(UnsupportedOperationException.class, () -> {
        quiz.eliminarPregunta(listaPreguntas.get(0));
    });

 }

    @Test
    public void testInscripcionEstudianteValido(){

        Estudiante estudiante2 = new Estudiante("Pedro", "123", "pedro@example.com");

        learningPath.inscripcionEstudiante(estudiante2);

        quiz.inscripcionEstudiante(estudiante2);

        assertEquals(2, quiz.getEstadosPorEstudiante().size());

        


        }

    @Test

    public void testInscripcionEstudianteInvalidoPorEstudianteNulo(){

        // Estudiante nulo

        assertThrows(IllegalArgumentException.class, () -> {
            quiz.inscripcionEstudiante(null);
        });

    }

    @Test

    public void testInscripcionEstudianteInvalidoPorEstudianteYaInscrito(){

        // Estudiante ya inscrito

        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.inscripcionEstudiante(estudiante);
        });

    }

    @Test
    public void testInscripcionEstudianteInvalidoPorLearningPathNulo(){

        // LearningPath nulo

        Estudiante estudiante2 = new Estudiante("Pedro", "123", "pedro@example.com");
    
        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.inscripcionEstudiante(estudiante2);
        });
    
    
    }

    @Test
    public void testInscripcionEstudianteInvalidoPorLearningPathIncorrecto(){

        // LearningPath incorrecto

        Estudiante estudiante2 = new Estudiante("Pedro", "123", "pedro@example.com");

        LearningPath learningPath2 = new LearningPath(
            "Learning Path de Prueba 2",
            Nivel.Intermedio,
            "Camino de aprendizaje de prueba 2",
            "Objetivo general 2",
            120,
            profesor,
            4.5f,
            new ArrayList<>()
        );

        learningPath2.inscripcionEstudiante(estudiante2);

        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.inscripcionEstudiante(estudiante2);
        });

    }


}



    


