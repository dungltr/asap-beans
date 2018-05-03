/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.asap.utils;

import java.io.IOException;
import gr.ntua.cslab.asap.utils.ReadMatrixCSV;
//import static gr.ntua.cslab.asap.utils.ReadMatrixCSV.readMatrix;
import gr.ntua.ece.cslab.panic.core.containers.beans.InputSpacePoint;
import gr.ntua.ece.cslab.panic.core.containers.beans.OutputSpacePoint;
import gr.ntua.ece.cslab.panic.core.utils.CSVFileManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author letrungdung
 */
public class dream {
    public static String fileParameter (String file){
        return file.replace(".csv","_") + "parameter" + ".csv";
    }
    public static String fileRealValue (String file){
        return file.replace(".csv","_") + "realValue" + ".csv";
    }
    public static String fileEstimateValue (String file){
        return file.replace(".csv","_") + "estimateValue" + ".csv";
    }
    public static String fileErrorValue (String file){
        return file.replace(".csv","_") + "error" + ".csv";
    }
  
    public static int estimateSizeOfMatrix(int Max_Line, int numberOfVariable, String file, double R_2_limit) throws IOException {
        String fileParameter = fileParameter(file);
        String fileRealValue = fileRealValue(file);
        String fileEstimate = fileEstimateValue(file);
        int Max_Estimate = CsvFileReader.count(file)-1;
        int MaxOfLine;
        if (Max_Estimate < Max_Line)
            MaxOfLine = Max_Estimate;
        else MaxOfLine = Max_Line;
        int M = numberOfVariable + 2;
        double R_2 = 0;
        double R_2_2 = 0;
        double[][] realValue;// = readMatrix(fileValue, M);
        double[][] estimateValue;
        double[][] Parameter;
        double[][] x;
        double[][] a;
        double[] B;
        double[] b;
        double[] c;// = new double[realValue.length];
        double[] d;// = new double[realValue.length];
        int sizeOfMatrix = Max_Line;
        while(((Math.abs(R_2_2)< R_2_limit)||(Math.abs(R_2_2)> 1))&&(M < MaxOfLine))
        {   //System.out.println("\nMatrix Real Value");
            realValue = ReadMatrixCSV.readMatrix(fileRealValue, M);
            //System.out.println("\nMatrix Estimate Value");
            estimateValue = ReadMatrixCSV.readMatrix(fileEstimate, M);
            Parameter = ReadMatrixCSV.readMatrix(fileParameter, M);
            x = new double[realValue.length][realValue[0].length - 1];
            a = new double[realValue.length][realValue[0].length];
            B = new double[realValue[0].length];
            if (realValue.length < estimateValue.length) {
                c = new double[realValue.length];
                d = new double[realValue.length];
            }
            else {
                c = new double[estimateValue.length];
                d = new double[estimateValue.length];
            }
            //System.out.println("length of D: "+d.length);
            //System.out.println("\nMatrix Real Value");
            x = setupMatrixX(realValue);
            //System.out.println("\nMatrix A");
            a = setupMatrixA(x);
            //System.out.println("\n");
            //System.out.println("\nMatrix c");
            c = setupMatrixC(realValue);
            d = setupMatrixC(estimateValue);
            //System.out.println("length of C: "+c.length);
            B = multiply(multiply(invert(multiply(transpose(a),a)),transpose(a)),c);
            for (int k = 0; k < B.length; k++){
                if (Double.isNaN(B[k])) {
                //System.out.println("\nNew Parameter is infinity, use old Parameter");
                    B = setupParameterB(Parameter);
                    k = B.length;
                }
            }
            for (int i = 0; i < d.length; i++) {
                d[i] = 0;
                for (int j = 0; j < a[0].length; j++)
                    d[i] = d[i]+a[i][j]*B[j];
                //System.out.println("d["+i+"] in "+d.length+":"+d[i]);
            }
            double average = 0;
            for (int i = 0; i < c.length; i++)
                average = average + c[i];
            average = average/c.length;
            //System.out.println("\nAverage Value: " + average);
            double SSR = 0;
            double SST = 0;
            double SSE = 0;
            double SSY = 0;

            for (int k = 0; k < c.length; k++) {
                SSE = SSE + (c[k]-d[k])*(c[k]-d[k]);
            }
            //System.out.println("\na SSE Value: " + SSE);
            for (int k = 0; k < c.length; k++){
                SSY = SSY + (c[k]-average)*(c[k]-average);
            }
            //System.out.println("\na SSY Value: " + SSY);
            R_2 = 1 - SSE/SSY;

            for (int j = 0; j < d.length; j++)
                SSR = SSR + (d[j]-average)*(d[j]-average);
            //System.out.println("\nSSR Value: " + SSR);

            for (int i = 0; i < c.length; i++)
                SST = SST + (c[i]-average)*(c[i]-average);

            //System.out.println("\nSST Value: " + SST);
            R_2_2 = SSR/SST;

            //System.out.println("\nR^2 Value: " + R_2);
            //System.out.println("\nR^2_2 Value: " + R_2_2);
            int index = 0;
            double R_2_tmp;
            while(index < M)
//            for (int index = 0; index < M; ++index)
            {
                R_2_tmp = lookingOtherParameter(x,c,setupParameterBindex(Parameter[index]));
//                System.out.println(" and the ErrorSquare is: " + estimateErrorSquare(x,c,setupParameterBindex(Parameter[index])) + " and the R^2 is: " + R_2_tmp);
                if (( R_2_limit < R_2_tmp)&&(R_2_tmp < 1)&&(R_2_2<R_2_tmp))
                {
                    //System.out.println("\nR^2_2 Value: " + R_2);
                    //System.out.println("\nR^2_2 Value: " + R_2_2);
//                    R_2_2 = R_2_tmp;
                    //System.out.println("\nR^2_2 Repair: " + R_2_tmp);
                    //System.out.println("\nR^2 Value with Parameter["+index+"]:" + R_2_tmp);//Double.toString(lookingOtherParameter(x,c,setupParameterBindex(Parameter[index]))));
                    index = M;
                }
                index++;
            }
            if (M < MaxOfLine) sizeOfMatrix = M;
            M = M+1;
        }
        //System.out.println("\nR^2 Value: " + R_2);
        //System.out.println("\nR^2_2 Value: " + R_2_2);
        //System.out.println("\nR^2 Value Limit: " + R_2_limit);
        //System.out.println("\nSize of real Value: " + sizeOfMatrix);
        //System.out.println("\nEstimate the maximum of Matrix:------------------------------------------------------------------------ ");
        return sizeOfMatrix;
    }
    public static double[][] setupMatrixX (double[][] tmp) {
        double[][] x = new double[tmp.length][tmp[0].length - 1];
        for (int i = 0; x.length > i; i++)
            for (int j = 0; x[0].length > j; j++)
                x[i][j] = tmp[i][j];
//        testScilab.printMatrix(x);
        return x;        
    } 
    public static double[] setupMatrixC (double[][] tmp) {
        double[] c = new double[tmp.length];
        for (int i = 0; c.length > i; i++)
                c[i] = tmp[i][tmp[0].length-1];
//        testScilab.printArray(c);
        return c;        
    }
    public static double[][] setupMatrixA (double[][] x) {
        int i=0;
        int j = 0;
        int k = 0;
        double[][] a = new double[x.length][x[0].length + 1];
        for (j=0; a.length > j; j++)
            for (i=0; a[0].length > i; i++)
                if (i==0) {
                    a[j][i]=1;
                }              
                else
                {
                    k = i - 1;
                    a[j][i]=x[j][k];
                }
//        testScilab.printMatrix(a);
        return a;
    }
    public static double[][] transpose(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                b[j][i] = a[i][j];
        return b;
    }
     public static double[][] multiply(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        return c;
    }

    // matrix-vector multiplication (y = A * x)
    public static double[] multiply(double[][] a, double[] x) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += a[i][j] * x[j];
        return y;
    }


    // vector-matrix multiplication (y = x^T A)
    public static double[] multiply(double[] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += a[i][j] * x[i];
        return y;
    }
    public static double determinant(double A[][],int N)
    {
        double det=0;
        if(N == 1)
        {
            det = A[0][0];
        }
        else if (N == 2)
        {
            det = A[0][0]*A[1][1] - A[1][0]*A[0][1];
        }
        else
        {
            det=0;
            for(int j1=0;j1<N;j1++)
            {
                double[][] m = new double[N-1][];
                for(int k=0;k<(N-1);k++)
                {
                    m[k] = new double[N-1];
                }
                for(int i=1;i<N;i++)
                {
                    int j2=0;
                    for(int j=0;j<N;j++)
                    {
                        if(j == j1)
                            continue;
                        m[i-1][j2] = A[i][j];
                        j2++;
                    }
                }
                det += Math.pow(-1.0,1.0+j1+1.0)* A[0][j1] * determinant(m,N-1);
            }
        }
        return det;
    }
    public static double[][] invert(double a[][]) 
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) 
            b[i][i] = 1;
 
 // Transform the matrix into an upper triangle
        gaussian(a, index);
 
 // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                    	    -= a[index[j]][i]*b[index[i]][k];
 
 // Perform backward substitutions
        for (int i=0; i<n; ++i) 
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k) 
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }
    // Method to carry out the partial-pivoting Gaussian
    // elimination.  Here index[] stores pivoting order.
 
    public static void gaussian(double a[][], int index[]) 
    {
        int n = index.length;
        double c[] = new double[n];
 
 // Initialize the index
        for (int i=0; i<n; ++i) 
            index[i] = i;
 
 // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i) 
        {
            double c1 = 0;
            for (int j=0; j<n; ++j) 
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }
 
 // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j) 
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i) 
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) 
                {
                    pi1 = pi0;
                    k = i;
                }
            }
 
   // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i) 	
            {
                double pj = a[index[i]][j]/a[index[j]][j];
 
 // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;
 
 // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }    
    public static double[] setupParameterB(double[][] Parameter) {
        int i = 0;
        double[] B = new double[Parameter[0].length]; 
        for (i=0; B.length > i; i++)
        {
            B[i] = Parameter[Parameter.length-1][i];
        }
//        testScilab.printArray(B);
        return B;
    }
    public static double[] setupParameterBindex(double[] Parameter) {
        double[] BB = new double[Parameter.length]; 
        for (int i=0; Parameter.length > i; i++)
        {
            BB[i] = Parameter[i];
        }
//        System.out.println("\nParameter old is:"+ Arrays.toString(BB));
//        testScilab.printArray(BB);
        return BB;
    }
     public static double lookingOtherParameter(double[][] x, double[] c, double[] b){
        double average = 0;
            for (int i = 0; i < c.length; i++)
                average = average + c[i];
            average = average/c.length;
        double SSR = 0;
        double SST = 0;
        double[] d = new double[c.length];
        double[][] a = setupMatrixA(x);
        for (int i = 0; i < d.length; i++)
            {
                d[i] = 0;
                for (int j = 1; j < a[0].length; j++)
                    d[i] = d[i]+a[i][j]*b[j];
//                System.out.println("d["+i+"] in "+d.length+":"+d[i]);
            }
        for (int j = 0; j < c.length; j++)
                SSR = SSR + (d[j]-average)*(d[j]-average);                                   
            for (int i = 0; i < c.length; i++)
                SST = SST + (c[i]-average)*(c[i]-average);                                  
        double R_2_2 = SSR/SST;
        return R_2_2;
    }
     public static void printMatrix(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++)
            {
                System.out.printf("%.2f",a[i][j]);
                System.out.print(" ");
            }    
            System.out.println(); 
        }        
    }
    public static void printArray(double[] a) {
        int m = a.length;
        for (int i = 0; i < m; i++){
                System.out.printf("%.2f",a[i]);
                System.out.print(" ");
        }        
    }
    public static void updateParameter(String filename, double[] b) throws IOException{
        Path filePath = Paths.get(filename);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        Writematrix2CSV.addArray2Csv(filename, b); 
    }

    public static List<OutputSpacePoint> runDream(String Library, String directory, String key) throws IOException {
        //List<OutputSpacePoint> outPoints = new ArrayList<>();
        System.out.println("directory:= " +Library + "/"+ directory);
        String fileName = Library + "/"+ directory + "/data/" + key + ".csv";
        double R_2_limit = 0.8;
        System.out.println("fileName:= " + fileName);                      
        double [][] testVariable = ReadMatrixCSV.readMatrix(fileName, 2);
        printMatrix(testVariable);
        int variables = testVariable[testVariable.length-1].length - 1;
        System.out.println("the number of variables is: "+ variables);
        int maxLine = dream.estimateSizeOfMatrix(CsvFileReader.count(fileName),variables,fileName,R_2_limit);
        System.out.println("the number of maxLine is: "+ maxLine);
        double [][] realValue = ReadMatrixCSV.readMatrix(fileName, maxLine);
        dream.printMatrix(realValue);
        double [] M = {maxLine};
        updateParameter(fileName.replace(".csv","_maxDream.csv"),M);
        Writematrix2CSV.writeMatrix2Csv(fileName.replace(".csv","_tmpDream.csv"), realValue);
        System.out.println("the maximum line for UserFunction is: " + maxLine);                      
        CSVFileManager fileDung = new CSVFileManager();
        fileDung.setFilename(fileName.replace(".csv", "_tmpDream.csv"));
        /*for (InputSpacePoint in : fileDung.getInputSpacePoints()) {
            OutputSpacePoint out = fileDung.getActualValue(in);
            outPoints.add(out);
            System.out.println(out);// Dung edit
        } 
        */
        return oldRun(fileDung);//outPoints;      
    }

    public static List<OutputSpacePoint> oldRun(CSVFileManager file) {
        List<OutputSpacePoint> outPoints = new ArrayList<>();
        for (InputSpacePoint in : file.getInputSpacePoints()) {
            OutputSpacePoint out = file.getActualValue(in);
            outPoints.add(out);
            System.out.println(out);// Dung edit
        }
        return outPoints; 
    }
}
