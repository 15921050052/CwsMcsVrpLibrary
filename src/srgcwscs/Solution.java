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
import java.util.HashMap;

/**
* @class Solution srgcwscs.java
*
* @brief This class represents a solution (array of routes) of a VRP instance.
* 
* @authors Angel A. Juan, Miquel Gilibert, Marcos Fernandez 
*
* @package srgcwscs;
* 
* @date 090315
*
* @copyright GNU Public License, version 2.
**/
public class Solution implements Comparable<Solution> 
{

    /**
	 * @brief number of instances
	 */
	private static int nInstances = 0; 
	
	/**
	 * @brief solution ID
	 */
	private int id;
	
	/**
	 * @brief solution costs
	 */
	private float costs;
	
	/**
	 * @brief list of routes in this solution
	 */
	private ArrayList<Route> routes;
	
	/**
	 * @brief elapsed computational time (in seconds)
	 */
	private double time; 

	
	/**
	* @brief Solution Constructor 
	*/
    public Solution() 
    {
        nInstances++;
        id = nInstances;
        costs = 0;
        routes = new ArrayList<Route>();
        time = 0;
    }

    /**
	* @brief Adds a new route to the solution
	* 
	* @param aRoute - Route instance to the new route to be added
	*/
    public void addRoute(Route aRoute) 
    {
        routes.add(aRoute);
    }

    /**
	* @brief Adds a given route costs to the global solution cost
	* 
	* @param aRoute - Route instance to the route to add costs
	*/
    public void addCosts(Route aRoute) 
    {
        costs += aRoute.getCosts();
    }

    /**
	* @brief Remove a given route costs to the global solution cost
	* 
	* @param aRoute - Route instance to the route to remove costs
	*/
    public void substractCosts(Route aRoute) 
    {
        costs -= aRoute.getCosts();
    }
  
    /**
	* @brief Update the costs for the global solution
	* 
	* @param c - float new costs value
	*/
    public void setCosts(float c) 
    {
        costs = c;
    }

    /**
	* @brief Gets global solution id
	* 
	* @return int - global solution id
	*/
    public int getId() 
    {
        return id;
    }

    /**
	* @brief Gets global solution costs
	* 
	* @return float - global solution costs value
	*/
    public float getCosts() 
    {
        return costs;
    }

    /**
	* @brief Gets the array list of routes for the global solution
	* 
	* @return ArrayList <Route> list of routes
	*/
    public ArrayList<Route> getRoutes() 
    {
        return routes;
    }

    /**
	* @brief Gets the total demands for the global solution
	* 
	* @return float - total solution demand
	*/
    public float getDemand() 
    {
        float demand = 0;
        for (Route aRoute : this.routes) 
        {
            demand = demand + aRoute.getDemand();
        }

        return demand;
    }

    /**
	* @brief Gets the nodes associated to the global solution
	* 
	* @return ArrayList <Node> - list of nodes
	*/
    public ArrayList<Node> getNodes() 
    {
        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Node> candidates = new ArrayList<Node>();

        // In any list of nodes, the first one is the depot
        for (int i = 0; i < routes.size(); i++) 
        {
            Route iRoute = routes.get(i);
            candidates = iRoute.getNodes();
            for (int j = 0; j < candidates.size(); j++) 
            {
                Node jCandidate = candidates.get(j);
                if (nodes.contains(jCandidate) == false) 
                {
                    if (jCandidate.getId() == 0) // the depot must be the first
                    {
                        nodes.add(0, jCandidate);
                    } else {
                        nodes.add(jCandidate);
                    }
                }
            }
        }

        return nodes;
    }

    /**
	* @brief translates the Solution class information to a printable string
	*/
    public String toString() 
    {
        Route aRoute; // auxiliary Route variable
        String s = "";
        s = s.concat("\r\n");
        s = s.concat("Sol ID : " + this.getId() + "\r\n");
        s = s.concat("Sol costs: " + getCosts()
                + "\r\n");
        s = s.concat("# of routes in sol: " + routes.size());
        s = s.concat("\r\n\r\n\r\n");
        s = s.concat("List of routes (cost and nodes): \r\n\r\n");
        for (int i = 1; i <= routes.size(); i++) {
            aRoute = routes.get(i - 1);
            s = s.concat("Route " + i + " || ");
            s = s.concat("Costs = " + (double) aRoute.getCosts());
            s = s.concat("\r\n");
        }
        s = s.concat("\r\n");
        for (int i = 1; i <= routes.size(); i++) {
            aRoute = routes.get(i - 1);
            for (Edge e : aRoute.getEdges()) {
            	s = s.concat("\r\n" + e.getOrigin().getId() + "->" + e.getEnd().getId() );
            	/*if (e.getOrigin().getId() == 0 && i == 1) {
                    s = s.concat("0" + "\r\n" + e.getEnd().getId());
                } else {
                    s = s.concat("\r\n" + e.getEnd().getId());
                }*/
            }
        }
        s = s.concat("\r\n\r\n");
        return s;
    }

    /**
  	* @brief Compare the solution with a given solution according to the costs
  	*
  	* @remarks returns -1 if the given solution is better than the current, 1 if the current is best and 0 if equal costs
  	* 
  	* @param otherSolution - Solution instance for the given solution to compare
  	* 
  	* @return int - comparison evaluation (1 current best, -1 given best, 0 equal)
  	*/
    public int compareTo(Solution otherSol) 
    {
        Solution other = otherSol;
        float e1 = this.getCosts();
        float e2 = other.getCosts();
        if (e1 < e2) {
            return -1;
        } else if (e1 > e2) {
            return 1;
        }
        return 0;
    }

    /**
  	* @brief Updates the limitation time for building a solution
  	* 
  	* @param time - double new time
  	*/
    public void setTime(double time) 
    {
        this.time = time;
    }

    /**
  	* @brief Gets a the limitation time for building a solution
  	* 
  	* @returns time - double new time
  	*/
    public double getTime() 
    {
        return time;
    }
}
