/* 
 	File is part of CwsMcsLib.

    CwsMcsLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, GPLv2.

    CwsMcsLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CwsMcsLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package srgcwscs;

/**
 * @class ElapsedTime srgcwscs.ElapsedTime 
 *
 * @brief Given an elapsed time in seconds, the method doPause() of this class forces
 * the code to do a pause for a time interval of that size.
 * 
 * @authors Angel A. Juan, Miquel Gilibert, Marcos Fernandez
 *
 * @package srgcwscs;
 * 
 * @date 240109
 *
 * @copyright GNU Public License, version 2.
 **/
public class ElapsedTime 
{
	/**
	 * @brief ElapsedTime Constructor
	 *
	 */
    public ElapsedTime() 
    {
    }

    /**
	 * @brief Gets the current time from system
	 */
    public static long systemTime() 
    {
        long time = System.nanoTime();
        return time;
    }

    /**
	 * @brief Estimates the elapsed time between a range of values
	 * 
	 * @param start - long starting value
	 * 
	 * @param end - long finishing value
	 * 
	 * @return double difference between start and end time
	 */
    public static double calcElapsed(long start, long end) 
    {
        double elapsed = (end - start) / 1.0e+9;
        return elapsed;
    }

    /**
	 * @brief Gets a string of the elapsed time between a range of values
	 * 
	 * @remarks Returns the value formatted as XhYmZs
	 * 
	 * @param start - long starting value
	 * 
	 * @param end - long finishing value
	 * 
	 * @return double difference between start and end time
	 */
    public static String calcElapsedHMS(long start, long end) 
    {
        String s = "";
        double elapsed = (end - start) / 1.0e+9;
        s = s + calcHMS((int) Math.round(elapsed));
        return s;
    }

    /**
	 * @brief Formats a time value in seconds
	 * 
	 * @remarks Returns the value formatted as XhYmZs
	 * 
	 * @param timeInSeconds - int time in seconds
	 * 
	 * @return String formatted value
	 */
    public static String calcHMS(int timeInSeconds) 
    {
        String s = "";

        int hours, minutes, seconds;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;       
        s = s + hours + "h " + minutes + "m " + seconds + "s";

        return s;
    }

    
    /**
	 * @brief Forces the code to do a pause for a time interval
	 * 
	 * @param timeInSeconds - int time interval in seconds for stopping the code
	 * 
	 */
    public static void doPause(int timeInSeconds) 
    {
        long t0, t1;

        t0 = System.currentTimeMillis();
        t1 = System.currentTimeMillis() + (timeInSeconds * 1000);

        do 
        {
            t0 = System.currentTimeMillis();
        } while (t0 < t1);
    }
}
