package business.intervento;

import java.util.Date;
import adisys.server.strumenti.DateFormatConverter;

/**
 * @author Nicola Gazzilli
 * @author Gianmarco Divittorio
 *
 */
public class Rilevazione {
	//Costanti

	private Date timestamp;

	private double gpsLatitude;
	private double gpsLongitude;
	private double gpsAltitude;
	private double gpsAccuracy;
	
	private double accX;
	private double accY;
	private double accZ;

	public Rilevazione()
	{
		timestamp = new Date();
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public double getGpsLatitude() {
		return gpsLatitude;
	}

	public double getGpsLongitude() {
		return gpsLongitude;
	}

	public double getGpsAltitude() {
		return gpsAltitude;
	}

	public double getGpsAccuracy() {
		return gpsAccuracy;
	}

	public double getAccX() {
		return accX;
	}

	public double getAccY() {
		return accY;
	}

	public double getAccZ() {
		return accZ;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setGpsLatitude(double gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public void setGpsLongitude(double gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public void setGpsAltitude(double gpsAltitude) {
		this.gpsAltitude = gpsAltitude;
	}

	public void setGpsAccuracy(double gpsAccuracy) {
		this.gpsAccuracy = gpsAccuracy;
	}

	public void setAccX(double accX) {
		this.accX = accX;
	}

	public void setAccY(double accY) {
		this.accY = accY;
	}

	public void setAccZ(double accZ) {
		this.accZ = accZ;
	}

	public boolean setTimestampFromString(String newTimestamp, String formato)
	{
		if (DateFormatConverter.parseable(newTimestamp, formato))
		{
			timestamp.setTime(DateFormatConverter.dateString2long(newTimestamp, formato));
			return true;
		}
		else
		{
			System.out.println("Rilevazione -> Impossibile settare timestamp");
			return false;
		}
	}
	
}
