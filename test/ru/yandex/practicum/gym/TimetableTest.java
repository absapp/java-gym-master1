package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);


        var mondayTrainingSession = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, mondayTrainingSession.size());
        //Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));

    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие

        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        int[] expectedTime = {780, 1200};
        var index = 0;
        var thursdayTrainingSession = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        for (var entry : thursdayTrainingSession.entrySet()) {
            assertEquals(expectedTime[index++], entry.getKey().toMinutes());
        }

        // Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));

    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> trainingSessionsOnMonday = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 00));
        List<TrainingSession> trainingSessionsOnTuesday = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.TUESDAY,
                new TimeOfDay(14, 00));

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(1,  trainingSessionsOnMonday.size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertNull(trainingSessionsOnTuesday);
    }
    @Test
    void testBoundaryTimeValues() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Петров", "Петр", "Петрович");
        Group group = new Group("Утренняя зарядка", Age.ADULT, 30);

        TrainingSession startOfDay = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(0, 0));
        TrainingSession endOfDay = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(23, 59));
        TrainingSession boundaryMinute = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));
        TrainingSession boundaryHour = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(9, 59)); //

        timetable.addNewTrainingSession(startOfDay);
        timetable.addNewTrainingSession(endOfDay);
        timetable.addNewTrainingSession(boundaryMinute);
        timetable.addNewTrainingSession(boundaryHour);

        Map<TimeOfDay, ArrayList<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(4, mondaySessions.size());

        int[] expectedMinutes = {0, 599, 720, 1439}; // 00:00, 09:59, 12:00, 23:59
        int index = 0;
        for (var entry : mondaySessions.entrySet()) {
            assertEquals(expectedMinutes[index++], entry.getKey().toMinutes());
        }

        for (ArrayList<TrainingSession> sessions : mondaySessions.values()) {
            assertEquals(1, sessions.size());
        }
    }

    @Test
    void testCounterOfTrainingsNotEmpty() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession singleTrainingSession1 = new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        timetable.addNewTrainingSession(singleTrainingSession);
        TrainingSession singleTrainingSession2 = new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession1);
        timetable.addNewTrainingSession(singleTrainingSession2);

        int size = timetable.getCounterOfTrainings().size();

        assertNotNull(timetable.getCounterOfTrainings());
        assertEquals(2, size);
    }

    @Test
    void testCounterOfTrainings() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        Coach coach3 = new Coach("Сидоров", "Сидор", "Сидорович");

        Group group = new Group("Фитнес", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession
                (group, coach1, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession
                (group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession
                (group, coach1, DayOfWeek.MONDAY, new TimeOfDay(11, 0)));

        timetable.addNewTrainingSession(new TrainingSession
                (group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession
                (group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession
                (group, coach3, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));

        List<CounterOfTrainings> counters = timetable.getCounterOfTrainings();

        assertEquals(3, counters.size());

        // Проверяем сортировку (по убыванию)
        assertEquals(3, counters.get(0).getCount());
        assertEquals(2, counters.get(1).getCount());
        assertEquals(1, counters.get(2).getCount());

        // Проверяем, что тренеры соответствуют...
        assertEquals(coach1, counters.get(0).getCoach());
        assertEquals(coach2, counters.get(1).getCoach());
        assertEquals(coach3, counters.get(2).getCoach());
    }

    @Test
    void testCounterOfTrainingsSameCoachMultipleDays() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Йога", Age.ADULT, 60);

        // Один тренер ведет тренировки в разные дни
        timetable.addNewTrainingSession(new TrainingSession
                (group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession
                (group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession
                (group, coach, DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));

        List<CounterOfTrainings> counters = timetable.getCounterOfTrainings();

        assertEquals(1, counters.size());
        assertEquals(3, counters.get(0).getCount());
        assertEquals(coach, counters.get(0).getCoach());
    }

}
