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

import java.io.File;
import java.util.ArrayList;

/**
* @class srgcwscsTester srgcwscs.srgcwscsTester
*
* @brief This class contains the main() function to test the SRGCWSCS class. Override main method in order to change its default behavior.
* By default this methods calls for standard (Golden, et al. 1998) instances which are located in inputs project folder. In order to solve
* the VRP problem is used the SRGCWSCS.solveInTime() that has as limit the configured maximum time.
* 
* @bibliography Golden, B.L., Wasil, E.A., Kelly, J.P., and Chao, I-M. (1998). Metaheuristics in vehicle 
* routing. In: Crainic, T.G., and Laporte, G. (eds), Fleet Management and Logistics, pages 
* 33–56. Kluwer, Boston. 
* 
* @authors Angel A. Juan, Miquel Gilibert, Marcos Fernandez
* 
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class srgcwscsTester 
{
    /**
     * @brief Application main method, makes the basic input, solving and output calls
     * 
     * @remarks Override this method to change the application functionality. This method can be a configuration pattern
     * for using the library inside a different project.
     *  
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	//System.out.println("****  WELCOME TO THIS PROGRAM  ****");
        long programStart = ElapsedTime.systemTime();

        // 1. Get the list of tests to run (test = instanceName + testParameters)
        //String testsFilePath = "inputs" + File.separator + "testToRun.txt";// inputs/tets2run.txt
        String testsFilePath = "inputs" + File.separator + "Test2Run.txt";
        TestsPlanner planner = new TestsPlanner(testsFilePath);
        ArrayList<Test> testsList = planner.getTestsList();

        // 2. For each test (instanceName + testParameters) in the list...
        int nTests = testsList.size();
        for (int k = 0; k < nTests; k++) 
        {
            Test aTest = testsList.get(k);
          // System.out.println("\n# STARTING TEST " + (k + 1) + " OF " + nTests);
            long testStart = ElapsedTime.systemTime();

            // 2.1.1 Get the instance inputs (nodes data)and the vcap inputs .
            String inputsFilePath = "inputs" + File.separator + aTest.getInstanceName() + "_input_nodes.txt";
            // MARCOS normal instances String inputsVehicleFilePath = "inputs" + File.separator + "vehicles.txt";
            // MARCOS KELLY INSTANCES
            String inputsVehicleFilePath = "inputs" + File.separator + "kellyVehicles" + File.separator + aTest.getInstanceName();
            InputsManager inMngr = new InputsManager(inputsFilePath,inputsVehicleFilePath);
            Inputs inputs = inMngr.getInputs();
            // 2.1.2 Get the vcap inputs


            // 2.2. Use the SR-GCWS-CS algorithm to solve the instance
            SRGCWSCS algorithm = new SRGCWSCS(aTest, inputs);
            Outputs output = algorithm.solveInTime();
            //2.3. Print out the results to a file with local output
            String outputsFilePath = "outputs" + File.separator
                    + aTest.getInstanceName() + "_" + aTest.getSeed() + "_outputs.txt";
            output.sendToFile(outputsFilePath);

         
            //2.4. End of current test
             long testEnd = ElapsedTime.systemTime();
        }
    }
}
