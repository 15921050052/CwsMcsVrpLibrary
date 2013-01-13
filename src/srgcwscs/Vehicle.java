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
* @class Vehicle srgcwscs.Vehicle
* 
* @brief This class manage vehicle issues
* 
* @author Angel A. Juan, Marcos Fernandez
* 
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/

public class Vehicle implements Comparable<Vehicle> 
{
	/**
     * @brief Vehicle capacity
     */
    private int vcap;

    /**
     * @brief Vehicle Constructor
     * 
     * @param vcap - int vehicle capacity
     */
    public Vehicle(int vcap) 
    {
        this.vcap = vcap;
    }
 
    /**
     * @brief Gets vehicle capacity
     * 
     * @return vcap - int vehicle capacity
     */
    public int getVcap() 
    {
        return vcap;
    }

    /**
     * @brief Updates vehicle capacity
     * 
     * @param vcap - int vehicle capacity
     */
    public void setVcap(int vcap) 
    {
        this.vcap = vcap;
    }

    /**
     * @brief Compares two given vehicles in terms of capacity
     * 
     * @param Vehicle - reference to a Vehicle to be compared with
     * 
     * @return -1 if otherVehicle has more capacity than current one otherwise 1 except 0 if both are equal 
     */
     public int compareTo(Vehicle otherVehicle) 
     {
        Vehicle other = otherVehicle;
        double s1 = this.getVcap();
        double s2 = other.getVcap();
        if (s1 < s2) 
        {
            return -1;
        } 
        else if (s1 > s2) 
        {
            return 1;
        }
        return 0;
    }


}
