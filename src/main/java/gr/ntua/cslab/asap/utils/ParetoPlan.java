/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.asap.utils;

import gr.ntua.cslab.asap.staticLibraries.MaterializedWorkflowLibrary;
import gr.ntua.cslab.asap.workflow.Workflow1DPTable;
import gr.ntua.cslab.asap.workflow.WorkflowNode;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author letrung
 */
public class ParetoPlan {
    static String IRES_HOME = ReadFile.readhome("IRES_HOME");
    static String ASAP_HOME = IRES_HOME;
    static String IRES_library = ASAP_HOME+"/asap-platform/asap-server";
    static String Library = IRES_library+"/target";
    static String Workflow = Library+"/workflows";
    static String MOEA_HOME = ReadFile.readhome("MOEA_HOME");
    public static List<List<WorkflowNode>> ParetoSet (String MaterializedWorkflowLink, List<List<WorkflowNode>> goodPlan) throws IOException{
        TestMO.main(new String[] {"arg"});
        String fileOldResult = MaterializedWorkflowLink+".csv";
        String fileNewResult = MaterializedWorkflowLink+"_result.csv";
        System.out.println(fileOldResult);              
        double[][] matrixOld = ReadMatrixCSV.readMatrix(fileOldResult, goodPlan.size());
        ReadMatrixCSV.printMatrix(matrixOld);
        int Max = CsvFileReader.count(fileNewResult);
        System.out.println(fileNewResult);
        double[][] matrixNew = ReadMatrixCSV.readMatrix(fileNewResult, Max);
        ReadMatrixCSV.printMatrix(matrixNew);
        System.out.println("read Matrix is OK");
        
        int check = 0;
        for (int i = 0; i < matrixOld.length; i++){
            check = 0;
            for (int j = 0; j < matrixNew.length; j++){
                for (int k = 0; k < matrixNew[0].length; k++){
                    if (matrixOld[i][k+1] == matrixNew[j][k]) check++;
                }
            }
            if (check == matrixNew[0].length){
                System.out.println("good Plan["+i+"]"+goodPlan.get(i));               
            } 
            else {
                goodPlan.remove(new Integer(i));
            }
        }
        return goodPlan; 
    }
    public static List<List<WorkflowNode>> buildGoodPlan (Workflow1DPTable dpTable, WorkflowNode r, String fullName, List<List<WorkflowNode>> goodPlan, int index) throws IOException{
        goodPlan.add(dpTable.getPlan(r.dataset));            
        //System.out.println(Workflow+"/plans.csv");
        //System.out.println(Library+"/"+MaterializedWorkflowLibrary.getWorkflowDirectory()+"/"+fullName);
        //System.out.println(MOEA_HOME+"/plan.txt");
        String streamFile = Library+"/"+MaterializedWorkflowLibrary.getWorkflowDirectory()+"/"+fullName;
        WriteFile.writeMOEA(MOEA_HOME+"/plan.txt", streamFile);
        Writematrix2CSV.storeStringToCSV(dpTable.getMetrics(r.dataset).toString(), Library+"/"+MaterializedWorkflowLibrary.getWorkflowDirectory()+"/"+fullName+".csv", index);
        //System.out.println("tempPlan=dpTable.getPlan(r.dataset);"+tempPlan+" and Plan["+index+"]"+Plan[index]);						
        //System.out.println("Metrics:="+Metrics);
        return goodPlan;
    }
    
}
