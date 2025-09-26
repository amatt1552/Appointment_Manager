package scheduleapplication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helps Handle all the timezones for this project.
 * @author Austin Matthews
 */
public class ZoneManager 
{
    
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    private final static ZoneId dbZoneID = ZoneId.of("UTC");
    private final static ZoneId officeZoneID = ZoneId.of("America/New_York");
    private final static LocalTime officeOpenTime = LocalTime.of(8,0);
    private final static LocalTime officeCloseTime = LocalTime.of(22,0);
    
    /**
     * Converts zoned Date time to office's zone.
     * @param zonedDateTime
     * @return 
     */
    protected static ZonedDateTime toOfficeHours(ZonedDateTime zonedDateTime)
    {
        if(zonedDateTime != null)
        {
            return zonedDateTime.withZoneSameInstant(officeZoneID);
        }
        return null;
    }
    
    /**
     * Converts database Date time to local zoned date time.
     * @param dateTime
     * @return 
     */
    protected static ZonedDateTime dbToLocalHours(LocalDateTime dateTime)
    {
        if(dateTime != null)
        {
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, dbZoneID);
            return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        }
        return null;
    }
    
    /**
     * Converts zoned Date time to database's zone.
     * @param zonedDateTime
     * @return 
     */
    protected static LocalDateTime toDBHours(ZonedDateTime zonedDateTime)
    {
        if(zonedDateTime != null)
        {
            return zonedDateTime.withZoneSameInstant(dbZoneID).toLocalDateTime();
        }
        return null;
    }
    
    /**
     * Date and Time formatted in local time.
     * @param zonedDateTime
     * @return 
     */
    protected static String formatedDateTime(ZonedDateTime zonedDateTime)
    {
        if(zonedDateTime != null)
        {
            return zonedDateTime.format(formatter);
        }
        return "";
    }
    
    /**
     * Gets work hours in local time.
     * @return 
     */
    protected static String getWorkHoursLocal() 
    {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        ZonedDateTime openTime = ZonedDateTime.of(zonedDateTime.toLocalDate(), officeOpenTime, officeZoneID);
        ZonedDateTime closeTime = ZonedDateTime.of(zonedDateTime.toLocalDate(), officeCloseTime, officeZoneID);
        String localHours = openTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime() 
                + "-" 
                + closeTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
        return localHours;
    }
    
    /**
     * Checks if time is within office hours. 
     * @param zonedDateTime
     * @return if within office hours true, else false.
     */
    protected static boolean officeHourCheck(ZonedDateTime zonedDateTime)
    {
       if(zonedDateTime != null)
       {
            ZonedDateTime newZonedDateTime = zonedDateTime.withZoneSameInstant(officeZoneID);
            ZonedDateTime openTime = ZonedDateTime.of(newZonedDateTime.toLocalDate(), officeOpenTime, officeZoneID);
            ZonedDateTime closeTime = ZonedDateTime.of(newZonedDateTime.toLocalDate(), officeCloseTime, officeZoneID);
            return newZonedDateTime.isAfter(openTime.minusSeconds(1)) && newZonedDateTime.isBefore(closeTime.plusSeconds(1));
       }
       return false;
    }
    
    /**
     * Gets the local offset.
     * @return 
     */
    protected static String getLocalOffset()
    {
        Instant instant = Instant.now(); //can be LocalDateTime
        ZoneId localZone = ZoneId.systemDefault(); // my timezone
        return localZone.getRules().getOffset(instant).toString();
    }
}
