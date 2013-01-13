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

import java.io.IOException;
import java.io.PrintWriter;

/**
* @class Outputs srgcwscs.Outputs
*
* @brief This class manages the outputs of the program
* 
* @author Angel A. Juan, Marcos Fernandez
*
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class Outputs 
{

	/**
     * @brief Clarke and Wright classical heuristic solution
     */
    private Solution cwsSolution;
    
    /**
     * @brief Best found solution
     */
    private Solution bestSols;
    
    /**
     * @brief Maximum costs
     */
    private int maxCosts;
    
    /**
     * @brief Services costs
     */
    private int serviceCosts;
    
    /**
     * @brief Number of interations to build a random solution
     */
    private int nIterRandCWS;
    
    /**
     * @brief number of solutions
     */
    private int nSols;

    /**
  	* @brief Outputs Constructor
  	*
  	* @param aTest  - Test Set of characteristics for the test to be run. InstanceName, Constraints, Maximiun Computing time,...
  	* 
  	* @param cwsSol - Solution Clarke and Wright solution constructed
  	* 
  	* @param bestSolutions - Solution Best Solution found
  	*
  	*/
    Outputs(Test atest, Solution cwsSol, Solution bestSolutions) 
    {
        maxCosts = atest.getMaxRouteCost();
        nIterRandCWS = atest.getnIterRandCWS();
        nSols = atest.getnSols();
        cwsSolution = cwsSol;
        bestSols = bestSolutions;
    }

    /**
  	* @brief Print to file Solutions built
  	* 
  	* @param outFile - String name file to print the output
  	* 
  	*/
    public void sendToFile(String outFile) 
    {
        try 
        {
            PrintWriter out = new PrintWriter(outFile);
            out.println("***************************************************");
            out.println("*                                                 *");
            out.println("*          RESULTS FROM SIMUROUTE PROJECT         *");
            out.println("*                                                 *");
            out.println("***************************************************");


            out.println("\r\n");
            out.println("***************************************************");
            out.println("*                INPUTS & NOTES                   *");
            out.println("***************************************************");

            out.println("\r\nINPUT PARAMETERS:\r\n");
            out.println("maxRouteLength = " + maxCosts);
            out.println("serviceCosts = " + serviceCosts);
            out.println("nIterRandCWS = " + nIterRandCWS);
            out.println("nSols = " + nSols);

            out.println("\r\n");
            out.println("***************************************************");
            out.println("*                      OUTPUTS                    *");
            out.println("***************************************************");
            out.println("\r\n");
            out.println("--------------------------------------------");
            out.println("Clarke & Wright Solution (parallel version)");
            out.println("--------------------------------------------");
            out.println(cwsSolution.toString() + "\r\n");
            out.println("\r\n BEST SOLUTIONS:\r\n");
            out.println("--------------------------------------------");
            out.println(bestSols.toString() + "\r\n");

            out.close();
        } 
        catch (IOException exception) 
        {
            System.out.println("Error processing output file: " + exception);
        }

    }

    

}
