package com.example.test.design.observe.define;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeatherData implements Subject {
    private List<Observer> observerList;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherData() {
        observerList = new ArrayList<Observer>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update(temperature, humidity, pressure);
        }
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyObservers();
    }
}
