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


/**
* @class SRGCWSCS srgcwscs.SRGCWSCS
*
* @brief This class encapsulates the SR-GCWS methodology
* 
* @authors Angel A. Juan, Miquel Gilibert, Marcos Fernandez
* 
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class SRGCWSCS 
{

	/**
     * @brief Set of characteristics for the test to be run. InstanceName, Constraints, Maximiun Computing time,...
     */
    private Test aTest;
    
    /**
     * @brief Inputs a representation of the different components of the problem, nodes, edges and vehicles
     */
    private Inputs inputs;
   
    /**
     * @brief Savings edges list
     */
    private Edge[] savingsList;
    
    /**
     * @brief Randomized CWS 
     */
    private RandCWS cwsAlg; 
    
    /**
     * @brief CWS heuristic basic solution 
     */
    private Solution cwsSol;
   
    /**
     * @brief Metaheuristic improved solution
     */
    private Solution SplitAndRandSol;
    
    /**
     * @brief Execution time control
     */
    long startTime;

    /**
   	* @brief SRGCWSCS Constructor
   	*
   	* @param test - Test characteristics for the test to be run
   	* 
   	* @param inputData - Inputs representation of the different components of the problem, nodes, edges and vehicles
   	*/
    public SRGCWSCS(Test test, Inputs inputData) 
    {
        aTest = test;
        inputs = inputData;
        savingsList = createSavingsList(inputs);
        cwsAlg = new RandCWS(aTest, inputs);
        startTime = ElapsedTime.systemTime();
        cwsSol = cwsAlg.solve(savingsList, false);
        
        cwsSol.setTime(ElapsedTime.calcElapsed(startTime,
                ElapsedTime.systemTime()));
        // printSolOnScreen(cwsSol, true);
    }

    /**
   	* @brief Solve the VRP problem returning an Output manager instance. Number of iterations will depend on time and a maximum limit of iterations.
   	*
   	* @remarks Builds a simple CWS solutions and a randomized improved solution.The multi start process is being limited by time and interations
   	* 
   	* @return Outputs - instance to manage the results
   	* 
   	* @see solveInTime() splitAndSolve()
   	*/
    public Outputs solveInTimeAndIterations() 
    {
        // Set initial variables
        startTime = ElapsedTime.systemTime();
        double elapsed = 0.0;
        Solution ourBestSol = cwsSol; // our best solution so far
        Solution newSol;
        int nRuns = 1;
        double maxTime = aTest.getMaxTime();                                                                
        
        // Multi-start iterative process

        while (nRuns <= aTest.getnIterRandCWS() && elapsed < maxTime) 
        {
        	// Find a new solution to the problem    
            newSol = cwsAlg.solve(savingsList, true);

            // Update elapsed time
            elapsed = ElapsedTime.calcElapsed(startTime, ElapsedTime.systemTime());

            // Update our best solution so far
            if (newSol.getCosts() < ourBestSol.getCosts()) 
            {
                newSol.setTime(elapsed);
                ourBestSol = newSol;
            }

            // Update number of runs performed
            nRuns++;

        }

        System.out.println(aTest.getInstanceName() + " " + cwsSol.getCosts() + " "
                + ourBestSol.getCosts() + " " + ourBestSol.getTime());

        printSolOnScreen(ourBestSol, false);
        Outputs out = new Outputs(aTest, cwsSol, ourBestSol);

        return out;
    }


    /**
   	* @brief Solve the VRP problem returning an Output manager instance. Number of iterations will depend on time.
   	*
   	* @remarks Builds a simple CWS solutions and a randomized improved solution.The 
   	* number of iterations alone depends on the variable maxtime in Test aTest
   	* 
   	* @return Outputs - instance to manage the results
   	* 
   	* @see solveInTimeAndIterations() splitAndSolve()
   	*/
    public Outputs solveInTime() 
    {	 
        // Set initial variables
        startTime = ElapsedTime.systemTime();
        double elapsed = 0.0;
        Solution ourBestSol = cwsSol;
        Solution newSol; 
        double maxTime = aTest.getMaxTime();

         
       int nSolutions = 0;
        
        while (elapsed < maxTime) 
        {

            // Find a new solution to the problem
            newSol = cwsAlg.solve(savingsList, true);

            // Update elapsed time
            elapsed = ElapsedTime.calcElapsed(startTime, ElapsedTime.systemTime());

            // Update our best solution so far
            if (newSol.getCosts() < ourBestSol.getCosts()) {
                newSol.setTime(elapsed);
                ourBestSol = newSol;
            }
            
            nSolutions ++;
        }

        System.out.println("instancia: "+aTest.getInstanceName()+" costCws: "+
                cwsSol.getCosts() +" tempCWS: "
               +cwsSol.getTime()  + " costBS: "+ ourBestSol.getCosts() 
               + " RutesBS: "+ ourBestSol.getRoutes().size()
               + " tempBS: "+ ourBestSol.getTime() + " solutions: "+ nSolutions);

        Outputs out = new Outputs(aTest, cwsSol, ourBestSol);        
        
        return out;
    }


    /**
   	* @brief Solve the VRP problem returning an Output manager instance. Number of iterations will depend on time.
   	*
   	* @remarks Builds a simples CWS solutions and a randomized improved solution and applies the splitting technique.The 
   	* number of iterations alone depends on the variable maxtime in Test aTest
   	* 
   	* @return Outputs - instance to manage the results
   	* 
   	* @see solveInTime() solveInTimeAndIterations()
   	*/
     public Outputs splitAndSolve()
     {
    	 // Set initial variables
        startTime = ElapsedTime.systemTime();
        double elapsed = 0.0;
        Solution ourBestSol = cwsSol; // our best solution so far
        Solution newSol;
        double maxTime = aTest.getMaxTime();

        // Multi-start iterative process
        while (elapsed < maxTime) 
        {
        	// Find a new solution to the problem
            Split split = new Split(inputs);
            newSol = split.splitSolve(aTest, 4);

            // Update elapsed time
            elapsed = ElapsedTime.calcElapsed(startTime, ElapsedTime.systemTime());

            // Update our best solution so far
            if (newSol.getCosts() < ourBestSol.getCosts()) 
            {
                newSol.setTime(elapsed);
                ourBestSol = newSol;
            }
        }       
        Outputs out = new Outputs(aTest, cwsSol, ourBestSol);

        return out;
     }
   

	/**
	 * @brief Create a savings list according to CWS heuristic
	 *    	
	 * @param input - Inputs inputs manager instance
	 * 
	 * @return ArrayList<Edge> - array list representing savings list edges
	 */
    private Edge[] createSavingsList(Inputs input) 
    {
        input.fillEdgeList();
        Edge[] array = input.getEdgeList();//create the array that contain the edges
        // Sort using the compareTo() method of the Edge class (TIE ISSUE #1)
        Arrays.sort(array);
        return array;
    }

    /**
	 * @brief Prints solution characteristics by standard output
	 *    	
	 * @param aSolution - Solution solution manager instance
	 * 
	 * @param isCWSSol - boolean true for printing just CWS heuristic solution
	 */
    private void printSolOnScreen(Solution aSolution, boolean isCWSSol) 
    {
        if (isCWSSol == true) 
        {
            System.out.print("\n** CWS Solution: ");
        } else 
        {
            System.out.print("\n** CWS = " + cwsSol.getCosts() + "; Seed = "
                    + aTest.getSeed() + "; OBS = " + aSolution.getCosts()
                    + "; Time = " + aSolution.getTime());
        }
    }
    
    /**
	 * @brief Gets the CWS heuristic solution
	 * 
	 * @param Solution - solution instance representing CWS heuristic proposal
	 */
    public Solution getCwsSol() 
    {
        return cwsSol;
    }

}
