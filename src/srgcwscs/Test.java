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

import java.util.ArrayList;

/**
* @class Test srgcwscs.Test
*
* @brief This class represents a test to be run. A test has a set of properties related to a VRP Instances. These properties are: The instance name,
* the maximum computational time to search for new solutions, the geometric distribution to generate random numbers, the maximum cost allowed for a route,
* the maximum service costs, the maximum number of iterations to search for new solutions,...
* 
* @author Angel A. Juan, Marcos Fernandez
* 
* @package srgcwscs;
* 
* @date 020112
*
* @copyright GNU Public License, version 2.
**/
public class Test 
{

	/**
     * @brief Name Instance
     */
    private String instanceName;
    
    /**
     * @brief Maximum computational time (in minutes)
     */
    private int maxTime;  
    
    /**
     * @brief Geometric distribution
     */
    private String distribution;
    
    /**
     * @brief Maximum route cost
     */
    private int maxRouteCost;
    
    /**
     * @brief Service costs
     */
    private int serviceCosts;
    
    /**
     * @brief Maximum number of iterations to estimate a randomized CWS Solution
     */
    private int nIterRandCWS;
    
    /**
     * @brief Number of solutions
     */
    private int nSols;
    
    /**
     * @brief Minimum beta value
     */
    private float betaMin;
    
    /**
     * @brief Maximum beta value
     */
    private float betaMax;
    
    /**
     * @brief Use lecuyer library
     */
    private boolean useLecuyer;
    
    /**
     * @brief seed for generating random numbers
     */
    private int seed;
 

    /**
     * @brief Minimum beta value
     * 
     * @param  name - String Instance Name 
     * 
     * @param  maxRouteCost2 - int maximum route cost
     * 
     * @param  serviceCosts2 - int Service costs
     * 
     * @param time - int maximum time allowed to estimate a randomized CWS solution
     * 
     * @param  nIterRandCWS2 - int maximum multi-start iterations allowed to estimate a randomized CWS solution
     * 
     * @param  nSols2 - int number of solutions
     * 
     * @param  d - String Geometric distribution (T Triangular, G Geometric, U uniform)
     * 
     * @param  min - float Minimum beta value
     * 
     * @param  max - float Maximum beta value
     * 
     * @param  useLecuyer2 - boolean true for using lecuyer lybrari
     * 
     * @param  seed2 - int seed initialization to generate random numbers
     */
    public Test(String name, int maxRouteCost2, int serviceCosts2, int time, int nIterRandCWS2, int nSols2, String d, float min, float max, boolean useLecuyer2, int seed2) 
    {
        instanceName = name;
        maxRouteCost = maxRouteCost2;
        serviceCosts = serviceCosts2;
        maxTime = time;
        nIterRandCWS = nIterRandCWS2;
        nSols = nSols2;
        distribution = d;
        betaMin = min;
        betaMax = max;
        useLecuyer = useLecuyer2;
        seed = seed2;
    }

    /**
     * @brief Get instance name
     * 
     * @return String - name of instance
     */
    public String getInstanceName() 
    {
        return instanceName;
    }
    
    /**
     * @brief Gets maximum time to build a randomized CWS solution
     * 
     * @return int - maximum time
     */
    public int getMaxTime() 
    {
        return maxTime;
    }

    /**
     * @brief Gets gometric distribution to be used when generating random numbers
     * 
     * @remarks T for triangular, G for geometric, U or otherwise for Uniform
     * 
     * @return String distribution
     */
    public String getDistribution() 
    {
        return distribution;
    }

    /**
     * @brief Gets seed initialization
     * 
     * @return int - seed initialization
     */
    public int getSeed() 
    {
        return seed;
    }

    /**
     * @brief Gets maximum route costs
     * 
     * @return int - maximum route costs
     */
    public int getMaxRouteCost() 
    {
        return maxRouteCost;
    }

    /**
     * @brief Gets Number of iterations to build Randomized CWS solution
     * 
     * @return int - number of iterations
     */
    public int getnIterRandCWS() 
    {
        return nIterRandCWS;
    }

    /**
     * @brief Gets number of solutions
     * 
     * @return int - number of solutions
     */
    public int getnSols() 
    {
        return nSols;
    }

    /**
     * @brief Gets Service Costs
     * 
     * @return int - service costs
     */
    public int getServiceCosts() 
    {
        return serviceCosts;
    }

    /**
     * @brief Gets beta maximum value
     * 
     * @return float - beta maximum
     */
    public float getBetaMax() 
    {
        return betaMax;
    }

    /**
     * @brief Gets beta minimum value
     * 
     * @return float - beta minimum
     */
    public float getBetaMin()
    {
        return betaMin;
    }

    /**
     * @brief is Lecuyer library configurated
     * 
     * @return true - if configurated
     */
    public boolean isUseLecuyer() 
    {
        return useLecuyer;
    }

    
}
