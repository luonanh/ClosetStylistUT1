package com.adl.closetstylist;


public class WeatherInfo {
	private int tempMin = 0;
	private int tempMax = 120;
	private int tempCurrent = 50;
	private float rainAmount = 0;
	private float windSpeed = 0;
	private PlaceRecord placeRecord = null;

	public WeatherInfo(int min, int max, int current, PlaceRecord place) {
		tempMin = min;
		tempMax = max;
		tempCurrent = current;
		placeRecord = place;
	}

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public int getTempMax() {
		return tempMax;
	}

	public void setTempMax(int tempMax) {
		this.tempMax = tempMax;
	}

	public int getTempCurrent() {
		return tempCurrent;
	}

	public void setTempCurrent(int tempCurrent) {
		this.tempCurrent = tempCurrent;
	}

	public float getRainAmount() {
		return rainAmount;
	}

	public void setRainAmount(float rainAmount) {
		this.rainAmount = rainAmount;
	}
	
	public boolean isRain() {
		return (this.rainAmount > 0);
	}

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float wind) {
		this.windSpeed = wind;
	}

	public PlaceRecord getPlaceRecord() {
		return placeRecord;
	}

	public void setPlaceRecord(PlaceRecord placeRecord) {
		this.placeRecord = placeRecord;
	}	
}
