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

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;

/**
* @class InputsManager srgcwscs.InputsManager
*
* @brief This class parses input VRP instance files.
* 
* @authors Angel A. Juan, Marcos Fernandez
*
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class InputsManager 
{

	/**
     * @brief inputs file path 
     */
	private String filePath; 
	
	/**
     * @brief vehicles file path
     */
	private String vehicleFilePath;
	
	/**
     * @brief Inputs instance, stores inputs features
     */
    private Inputs inputs;

    
    /**
   	* @brief InputsManager Constructor
   	*
   	* @param inputsFilePath - path for inputs file
   	* 
   	* @param inputsVehicleFilePath .path for vehicle features file
   	*/
    public InputsManager(String inputsFilePath, String inputsVehicleFilePath) 
    {
        filePath = inputsFilePath;
        vehicleFilePath=inputsVehicleFilePath;
    }

    /**
   	* @brief Gets input for VRP problem to be solved from definition files (Nodes and Vehicles)
   	*/
    public Inputs getInputs() 
    {   
        try 
        {
            FileReader reader = new FileReader(filePath);
            Scanner in = new Scanner(reader);
            int k = 0;

            // Count the size of filePath in lines
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String f = null;
            int nnodes = 0;
            while ((f = br.readLine()) != null) {
                if (f.charAt(0) == '#') {
                    //this is a comment line
                } else {
                    nnodes++;
                }
            }

            inputs = new Inputs(nnodes);

            while (in.hasNextLine()) 
            {
                String s = in.next();

                if (s.charAt(0) == '#') // this is a comment line
                {
                    in.nextLine(); // skip comment lines
                } else {
                    double x = Double.parseDouble(s);
                    double y = in.nextDouble();
                    int demand = in.nextInt();
                    Node node = new Node(k, x, y, demand);
                    inputs.setNode(k, node);
                    k++;
                }
            }          
            in.close();
            
            //INPUT VEHICLE
            FileReader reader2 = new FileReader(vehicleFilePath);
            Scanner in2 = new Scanner(reader2); 
            
            Vehicle v=null;
           
            while (in2.hasNextLine()) 
            {
            	String s = in2.next();
            	if (s.charAt(0) != '#') // this is a comment line
                {
            		v=new Vehicle(Integer.parseInt(s));
            		inputs.setVehicle(v);
                	    
                }
            	in2.nextLine();
            }
            in2.close();

        } catch (IOException exception) {
            System.out.println("Error processing inputs file: " + exception);
        }
        return inputs;
    }
}
