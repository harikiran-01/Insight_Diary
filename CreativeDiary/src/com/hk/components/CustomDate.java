package com.hk.components;

import java.io.Serializable;
import java.util.Objects;

public class CustomDate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1501970447052842312L;
	private int day,month,year;
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof CustomDate)) {
            return false;
        }
        CustomDate date = (CustomDate) o;
        return day == date.day && month == date.month && year == date.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }
	
	public CustomDate() {
		this(0,0,0);
	}
	public CustomDate(int d,int m,int y){
		this.day = d;
		this.month = m;
		this.year = y;
	}
	
	public CustomDate getDate() {
		return this;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDay() {
		return this.day;
	}

	public int getMonth() {
		return this.month;
	}

	public int getYear() {
		return this.year;
	}
	
	public String toString() {
		return day+"/"+month+"/"+year;	
	}
}

