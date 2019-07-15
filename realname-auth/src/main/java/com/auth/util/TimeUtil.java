package com.auth.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class TimeUtil {

	private static ZoneOffset ZONEOFFSET = ZoneOffset.of("+8");
	
	public static DateTimeFormatter FULLY_DIGITAL = new DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmmss").toFormatter();
	
	public static long currentMilli() {
		return LocalDateTime.now().toInstant(ZONEOFFSET).toEpochMilli();
	}
	
	public static long unixTimeStamp() {
		return currentMilli()/1000;
	}
	
	public static String currentYMDHMS() {
		return LocalDateTime.now().format(FULLY_DIGITAL);
	}
	
	public static Date dateLastTime(Date date) {
		LocalDate localDate = date.toInstant().atZone(ZONEOFFSET).toLocalDate();
		return Date.from(LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 23, 59, 59).toInstant(ZONEOFFSET));
	}
	
	public static long secondInterval(Date d1, Date d2) {
		return Duration.between(d1.toInstant(), d2.toInstant()).getSeconds();
	}
	
	public static long secondInterval(long d1, long d2) {
		return Duration.between(Instant.ofEpochMilli(d1), Instant.ofEpochMilli(d2)).getSeconds();
	}
	
	public static Date delay24Hour(Date date) {
		LocalDateTime time = LocalDateTime.ofInstant(date.toInstant(), ZONEOFFSET);
		return Date.from(time.plusDays(1).toInstant(ZONEOFFSET));
	}
	
	public static long delay24Hour(long date) {
		LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZONEOFFSET);
		return time.plusDays(1).toInstant(ZONEOFFSET).toEpochMilli();
	}
	
	public static String longFormat(long time, DateTimeFormatter formatter) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZONEOFFSET).format(formatter);
	}
}
