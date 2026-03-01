package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {


    private LinkedHashMap<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new LinkedHashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        if (trainingSession == null) {
            return;
        }

        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        ArrayList<TrainingSession> trainings;
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        if (daySchedule == null) {
            daySchedule = new TreeMap<>();
            trainings = new ArrayList<>();
            trainings.add(trainingSession);
            daySchedule.put(timeOfDay, trainings);
            timetable.put(dayOfWeek, daySchedule);
        } else {
            trainings = daySchedule.getOrDefault(timeOfDay, new ArrayList<>());
            trainings.add(trainingSession);
            daySchedule.put(timeOfDay, trainings);
        }

    }

    public Map<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> schedule = timetable.get(dayOfWeek);
        if (schedule == null) {
            return null;
        } else {
            return Collections.unmodifiableSortedMap(schedule);
        }
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        if (daySchedule == null) {
            return null;
        }
        else  {
            return Collections.unmodifiableList(daySchedule.get(timeOfDay));
        }
    }

    private Map<Coach, Integer> getAllTrainingSession() {
        Map<Coach, Integer> coaches = new HashMap<>();
        for (var entry : timetable.entrySet()) {
            for (var entries : entry.getValue().entrySet()) {
                for (TrainingSession trainingSession : entries.getValue()) {
                    coaches.put(trainingSession.getCoach(),coaches.getOrDefault(trainingSession.getCoach(),
                            0) + 1);
                }
            }
        }
        return coaches;
    }

    public List<CounterOfTrainings> getCounterOfTrainings() {
        List<CounterOfTrainings> counterOfTrainingsList = new ArrayList<>();
        Map<Coach, Integer> coaches = getAllTrainingSession();
        for (var entry : coaches.entrySet()) {
            counterOfTrainingsList.add(new CounterOfTrainings(entry.getValue(), entry.getKey()));
        }

        Collections.sort(counterOfTrainingsList);
        return Collections.unmodifiableList(counterOfTrainingsList);
    }
}
//
>>>>>>> 6638f9d (разобраться как работает git)
