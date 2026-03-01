package ru.yandex.practicum.gym;

import java.util.Objects;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {

    private int count;
    private Coach coach;

    public  CounterOfTrainings(int count, Coach coach) {
        this.count = count;
        this.coach = coach;
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        return Integer.compare(o.count, count);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CounterOfTrainings that = (CounterOfTrainings) o;
        return count == that.count && Objects.equals(coach, that.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, coach);
    }

    public int getCount() {
        return count;
    }

    public Coach getCoach() {
        return coach;
    }
}
//