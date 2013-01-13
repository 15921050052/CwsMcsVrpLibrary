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

import java.util.Arrays;
import java.util.LinkedList;


/**
* @class Split srgcwscs.Split
*
* @brief This class represents the splitting process to solve CWS-MCS over small problem regions
* 
* @authors Juan Carlos, Marcos Fernandez
*
* @date 090315
*
* @copyright GNU Public License, version 2.
**/
public class Split 
{
	/**
	 * @brief number of nodes
	 */
    private Node[] vrpnodes;
    
    /**
	 * @brief (x-bar, y-bar) is a geometric center for the VRP
	 */
    private double[] vrpCenter; 
    
    /**
	 * @brief Array of inputs
	 */
    private Inputs[] inputsList;
    
    /**
	 * @brief Array of list savings
	 */
    private LinkedList<Edge[]> listOfSavingList;
    
    /**
	 * @brief solution split
	 */
    private Solution splitSolution;
    
    /**
	 * @brief number of vehicles
	 */
    private int nVehicles;
  

    /**
	 * @brief Split constructor
	 * 
	 * @param inputs - Inputs
	 */
    public Split(Inputs inputs) 
    {
        vrpnodes = inputs.getNodeList();
        vrpCenter = calcGeometricCenter(vrpnodes);
        inputsList = splitVrpnodes(vrpnodes);
        nVehicles=inputs.getVehiclesList().size();
    }

    /**
	 * @brief Returns the geometric center coordinates
	 * 
	 * @return double [], vector of two positions, first representing x coordinate and y representing y coordinate
	 */
    public double[] calcGeometricCenter(Node[] nodes) 
    {
        // 1. Declare and initialize variables
        double sumX = (double) 0.0; // sum of x[i]
        double sumY = (double) 0.0; // sum of y[i]
        double[] center = new double[2]; // center as (x, y) coordinates

        // 2. Calculate sums of x[i] and y[i] for all iNodes in nodes
        Node iNode; // iNode = ( x[i], y[i] )
        for (int i = 0; i < nodes.length; i++) 
        {
            iNode = nodes[i];
            sumX = sumX + iNode.getX();
            sumY = sumY + iNode.getY();
        }

        // 3. Calculate means for x[i] and y[i]
        center[0] = sumX / nodes.length; // mean for x[i]
        center[1] = sumY / nodes.length; // mean for y[i]

        // 4. Return center as (x-bar, y-bar)
        return center;
    }

    /**
	 * @brief Divides the list of nodes in a set of input regions
	 * 
	 * @return Inputs[], a set of inputs each of one representing a sub VRP problem
	 */
    public Inputs[] splitVrpnodes(Node[] vrpnodes) 
    {
        Inputs[] list = new Inputs[4];
        int[] num = new int[4];
        num[0] = 1;
        num[1] = 1;
        num[2] = 1;
        num[3] = 1;
        
        for (int i = 1; i < vrpnodes.length; i++) 
        {
        	//depot is consideratred in every field
            if ((vrpnodes[i].getX() <= vrpCenter[0]) && (vrpnodes[i].getY() >= vrpCenter[1])) 
            {
                num[0]++;
            } 
            else if ((vrpnodes[i].getX() > vrpCenter[0]) && (vrpnodes[i].getY() > vrpCenter[1])) 
            {
                num[1]++;
            } 
            else if ((vrpnodes[i].getX() < vrpCenter[0]) && (vrpnodes[i].getY() < vrpCenter[1])) 
            {
                num[2]++;
            } 
            else 
            {
                num[3]++;
            }
        }
        
        for (int i = 0; i < list.length; i++)
        {
            list[i] =new Inputs(num[i]);
            System.out.println("El tamañ],nVehiclo de la lista "+i+" es "+num[i]);
        }
        
        num[0] = 1;
        num[1] = 1;
        num[2] = 1;
        num[3] = 1;
        
        //put a depot in all every field
        list[0].setNode(0, vrpnodes[0]);
        list[1].setNode(0, vrpnodes[0]);
        list[2].setNode(0, vrpnodes[0]);
        list[3].setNode(0, vrpnodes[0]);
        
        for (int i = 1; i < vrpnodes.length; i++) 
        {
            if ((vrpnodes[i].getX() <= vrpCenter[0]) && (vrpnodes[i].getY() >= vrpCenter[1])) 
            {
                list[0].setNode(num[0], vrpnodes[i]);
                num[0]++;
            } 
            else if ((vrpnodes[i].getX() > vrpCenter[0]) && (vrpnodes[i].getY() > vrpCenter[1])) 
            {
                list[1].setNode(num[1], vrpnodes[i]);
                num[1]++;
            } 
            else if ((vrpnodes[i].getX() < vrpCenter[0]) && (vrpnodes[i].getY() < vrpCenter[1])) 
            {
                System.out.println(num[2]);
                list[2].setNode(num[2], vrpnodes[i]);
                num[2]++;
            } else {
                list[3].setNode(num[3], vrpnodes[i]);
                num[3]++;
            }
        }
        
        for (int i = 0; i < list.length; i++) 
        {
            listOfSavingList.add(createSavingsList(list[i]));
        }
        
        return list;
    }

    /**
	 * @brief Create a savings list according to CWS heuristic
	 *    	
	 * @return Edge[] - array  representing savings list edges
	 */
    public Edge[] createSavingsList(Inputs input) 
    {
        input.fillEdgeList();
        Edge[] array = input.getEdgeList();//create the array that contain the edges
        // Sort using the compareTo() method of the Job class (TIE ISSUE #1)
        Arrays.sort(array);
        return array;
    }

    /**
	 * @brief Applies CWS-MCS solving process to each problem region
	 *    	
	 * @return Solution - Built solution
	 */
    public Solution splitSolve(Test atest, int splitIterator) 
    {
        splitSolution = new Solution();
        Solution[] solutions = new Solution[4];
        
        for (int i = 0; i < inputsList.length; i++) 
        {
            RandCWS cwsAlg = new RandCWS(atest, inputsList[i]);
            int a = 1;
            solutions[i] = cwsAlg.solve(listOfSavingList.get(i), true);
            a++;
            Solution newSol = null;
            
            while (a <= splitIterator) 
            {
                newSol = cwsAlg.solve(listOfSavingList.get(i), true);
                if (newSol.getCosts() < solutions[i].getCosts()) 
                {
                    solutions[i] = newSol;
                }
            }
            
            splitSolution.setCosts(splitSolution.getCosts() + solutions[i].getCosts());
           
            for (int b = 0; b < solutions[i].getRoutes().size(); b++) 
            {
                splitSolution.addRoute(solutions[i].getRoutes().get(b));
            }
            
            splitSolution.setTime(solutions[i].getTime());
        }
        
        return splitSolution;
    }
}
