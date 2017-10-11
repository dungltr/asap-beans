/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;
import static Algorithms.ReadMatrixCSV.readMatrix;
import static Algorithms.testScilab.invert;
import static Algorithms.testScilab.multiply;
import static Algorithms.testScilab.transpose;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
/**
 *
 * @author letrungdung
 */
public class Algorithms {
    
    private static final String COMMA_DELIMITER = ",";
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
    public static double[] estimateValue(double[][] x, double[] B) {
        int i = 0;
        int j = 0;
//        testScilab.printArray(a);
        double[] d = new double[x.length]; 
        for (i=0; x.length > i; i++)
        {
            d[i] = B[0];
            for (j = 0; x[0].length > j; j++)
                d[i] = d[i] + B[j+1]*x[i][j];
        }
        testScilab.printArray(d);
        return d;
    }
    public static double estimateCurrentCostValue(double[] x, double[] B) {
        int i,j = 0;
        double d = B[0];
        String output = "\nc=b[0]+";
        String output2 = "="+B[0];
        for (i = 0; B.length - 1 > i; i++)           
        {
            d = d + B[i+1]*x[i];
            j = i + 1;
            output = output + "b["+j+"]*x["+i+"]+";
            output2 = output2 +"+"+ B[i+1]+ "*" + x[i];
        }        
        System.out.println(output + "epsilon" + output2 + "=" + d);
        testScilab.printArray(x);
        testScilab.printArray(B);
        return d;
    }
    public static double[] estimateError(double[] c, double[] d) {
        int i = 0;
        int j = 0;
        double[] err = new double[c.length]; 
        for (i=0; c.length > i; i++)
        {
            err[i] = d[i] - c[i];
        }
        return err;
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
    public static void updateParameter(String filename, double[] b) throws IOException{
        Writematrix2CSV.addArray2Csv(filename, b);
        
    }
    public static double[] fillone(double[] X){
        double[] one = new double [X.length]; 
        for (int i = 0; i < X.length; i++)
            one[i] = 1;
        return one;    
    }
    public static String fileName (String directory, String delay, String name, String KindOfRunning){
	return directory +"/"+ delay +"_"+ KindOfRunning +"_"+ name + ".csv";
    }
    public static String NameOfFileName(String delay, String name, String KindOfRunning){
	return delay+"_"+KindOfRunning+"_"+name;
    }
    public static int estimateSizeOfMatrix(int Max_Line, int numberOfVariable, String directory, double R_2_limit, String delay, String KindOfRunning) throws IOException {
        String fileRealValue = fileName(directory,delay,"realValue",KindOfRunning);
        String fileParameter = fileName(directory,delay,"parameter",KindOfRunning);
        String fileEstimate = fileName(directory,delay,"estimate",KindOfRunning);
        int Max_Estimate = CsvFileReader.count(fileEstimate)-1;
        int MaxOfLine;
        if (Max_Estimate < Max_Line)
            MaxOfLine = Max_Estimate;
        else MaxOfLine = Max_Line;
        
        System.out.println("\nMax Of Line is:"+MaxOfLine);
        Path filePathParameter = Paths.get(fileParameter);
        
        double[] X = new double [numberOfVariable+1];
        X = fillone(X);
        double[][] initParameter = new double [2][X.length];
        initParameter = initParameterValue(X);
        if (!Files.exists(filePathParameter)) {
            Files.createFile(filePathParameter);
            int n = initParameter[0].length;
            int i = 0;
            String tmp = "";
            for (i = 0; n -1 >i; i++)
            tmp = tmp + "b[" + i + "]" + COMMA_DELIMITER;
            if (n - 1 == i) tmp = tmp + "b[" + i + "]";
            String FILE_HEADER = tmp;
            
            System.out.println("\nNeed to update Parameter Value");
            Writematrix2CSV.Writematrix2CSV(initParameter, fileParameter, FILE_HEADER);
            }
        int M = numberOfVariable + 2;
        // = readMatrix(fileParameter, M);
        double R_2 = 0;
        double R_2_2 = 0;       
        int M_init = M;
        int N = 1;
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
            realValue = readMatrix(fileRealValue, M);
            //System.out.println("\nMatrix Estimate Value"); 
            estimateValue = readMatrix(fileEstimate, M);
            Parameter = readMatrix(fileParameter, M);
            
            x = new double[realValue.length][realValue[0].length - 1];
            a = new double[realValue.length][realValue[0].length];
            B = new double[realValue[0].length];
            if (realValue.length < estimateValue.length)
                {
                c = new double[realValue.length];
                d = new double[realValue.length];
                }
            else 
                {
                c = new double[estimateValue.length];
                d = new double[estimateValue.length];
                }
            
            //System.out.println("length of D: "+d.length);
//            System.out.println("\nMatrix Real Value");
            x = setupMatrixX(realValue);
//            System.out.println("\nMatrix A");
            a = setupMatrixA(x);
//            System.out.println("\n");            
            
//            System.out.println("\nMatrix c");
            c = setupMatrixC(realValue);
            d = setupMatrixC(estimateValue);
            //System.out.println("length of C: "+c.length);
            B = multiply(multiply(invert(multiply(transpose(a),a)),transpose(a)),c);
            for (int k = 0; k < B.length; k++){
                if (Double.isNaN(B[k]))
                {
//                System.out.println("\nNew Parameter is infinity, use old Parameter");
                B = setupParameterB(Parameter);
                k = B.length;
                }
            }
            
          for (int i = 0; i < d.length; i++)
            {
                d[i] = 0;
                for (int j = 0; j < a[0].length; j++)
                    d[i] = d[i]+a[i][j]*B[j]; 
//                System.out.println("d["+i+"] in "+d.length+":"+d[i]);
            }

     
            double average = 0;
            for (int i = 0; i < c.length; i++)
                average = average + c[i];
            average = average/c.length;
//            System.out.println("\nAverage Value: " + average);

            double SSR = 0;
            double SST = 0;
            double SSE = 0;
            double SSY = 0;
            
            for (int k = 0; k < c.length; k++)
            {
                SSE = SSE + (c[k]-d[k])*(c[k]-d[k]);                
            }
            
//            System.out.println("\na SSE Value: " + SSE);
            for (int k = 0; k < c.length; k++){
                SSY = SSY + (c[k]-average)*(c[k]-average);                
            }
//            System.out.println("\na SSY Value: " + SSY);
            R_2 = 1 - SSE/SSY;
            
            for (int j = 0; j < d.length; j++)
                SSR = SSR + (d[j]-average)*(d[j]-average);            
//            System.out.println("\nSSR Value: " + SSR);            
            
            for (int i = 0; i < c.length; i++)
                SST = SST + (c[i]-average)*(c[i]-average);                
            
//            System.out.println("\nSST Value: " + SST);                    
            R_2_2 = SSR/SST; 
            
//            System.out.println("\nR^2 Value: " + R_2);
//            System.out.println("\nR^2_2 Value: " + R_2_2);
            int index = 0;
            double R_2_tmp;
            while(index < M)
//            for (int index = 0; index < M; ++index)
            { 
                R_2_tmp = lookingOtherParameter(x,c,setupParameterBindex(Parameter[index]));
//                System.out.println(" and the ErrorSquare is: " + estimateErrorSquare(x,c,setupParameterBindex(Parameter[index])) + " and the R^2 is: " + R_2_tmp);
                if (( R_2_limit < R_2_tmp)&&(R_2_tmp < 1)&&(R_2_2<R_2_tmp))
                {   
                    System.out.println("\nR^2_2 Value: " + R_2);
                    System.out.println("\nR^2_2 Value: " + R_2_2);
//                    R_2_2 = R_2_tmp;
                    System.out.println("\nR^2_2 Repair: " + R_2_tmp);
                    System.out.println("\nR^2 Value with Parameter["+index+"]:" + R_2_tmp);//Double.toString(lookingOtherParameter(x,c,setupParameterBindex(Parameter[index]))));
                    index = M;
                }
                index++;
            }
            if (M < MaxOfLine) sizeOfMatrix = M;
            
            M = M+1;
        }
        System.out.println("\nR^2 Value: " + R_2);
        System.out.println("\nR^2_2 Value: " + R_2_2);
        System.out.println("\nR^2 Value Limit: " + R_2_limit);
        System.out.println("\nSize of real Value: " + sizeOfMatrix);
        System.out.println("\nEstimate the maximum of Matrix:------------------------------------------------------------------------ ");
        return sizeOfMatrix;
    }
    
    public static double estimateErrorSquare (double[][] x, double[] c, double[] B){
        double ErrorSquare = 0;
        double[][] a = setupMatrixA(x);
        double[] d = new double[c.length];
        for (int i = 0; i < d.length; i++)
            {
                d[i] = 0;
                for (int j = 0; j < a[0].length; j++)
                    d[i] = d[i]+a[i][j]*B[j]; 
                ErrorSquare = ErrorSquare + (c[i]-d[i])*(c[i]-d[i]);
            }        
        return ErrorSquare;       
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
                for (int j = 0; j < a[0].length; j++)
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
    public static double[][] initParameterValue(double[] X) {
        double[] X1 = new double [X.length];
        double[] X2 = new double [X.length];
        for (int i = 0; i < X.length; i++)
        { 
            X1[i] = 1;
            X2[i] = 1;
        }
        double [][] B = {X1,X2};
        return B;
}
        
    public static double estimateCostValue(int sizeOfValue, String fileValue, String fileParameter, double[] X, double R_2_limit) throws IOException{
        int M = sizeOfValue;
        int N = 1;
        Path filePathParameter = Paths.get(fileParameter);
//        double[][] initParameter = {{1,1,1,1,1},{1,1,1,1,1}};
        double[][] initParameter = initParameterValue(X);
        if (!Files.exists(filePathParameter)) {
            Files.createFile(filePathParameter);
            System.out.println("\nNeed to update Parameter Value");
            int n = initParameter[0].length;
            int i = 0;
            String tmp = "";
            for (i = 0; n -1 >i; i++)
            tmp = tmp + "b[" + i + "]" + COMMA_DELIMITER;
            if (n - 1 == i) tmp = tmp + "b[" + i + "]";
            String FILE_HEADER = tmp;
            Writematrix2CSV.Writematrix2CSV(initParameter, fileParameter, FILE_HEADER);
            }
        double[][] realValue = readMatrix(fileValue, M);        
        double[][] Parameter = readMatrix(fileParameter, M);
        double[][] x = new double[realValue.length][realValue[0].length - 1];
        double[][] a = new double[realValue.length][realValue[0].length]; 
        double[] b = new double[Parameter[0].length];
        double[] c = new double[realValue.length];
        double[] d = new double[realValue.length];
        double[] err = new double[realValue.length];
       
        System.out.println("\nMatrix Real Value");
        x = setupMatrixX(realValue); 
        testScilab.printMatrix(x);
        System.out.println("\nMatrix Real Cost Fucntion");
        c = setupMatrixC(realValue);
        testScilab.printArray(c);
        System.out.println("\nMatrix A");
        a = setupMatrixA(x);
        testScilab.printMatrix(a);
        double[] B = new double[Parameter[0].length];
        System.out.println("\nThe Old Paramater");
        B = setupParameterB(Parameter);
        testScilab.printArray(B);
        
        System.out.println("\nMatrix new B");
        B = multiply(multiply(invert(multiply(transpose(a),a)),transpose(a)),c);
            if (Double.isNaN(B[0])){
                System.out.println("New Parameter is infinity, use old Parameter");
                B = setupParameterB(Parameter);
                }        
        testScilab.printArray(B); 
        
///////////////////////////////////////////////////////////////////////////////////        
        for (int i = 0; i < d.length; i++)
            {
                d[i] = 0;
                for (int j = 0; j < a[0].length; j++)
                    d[i] = d[i]+a[i][j]*B[j]; 
//                System.out.println("d["+i+"] in "+d.length+":"+d[i]);
            }        
        double average = 0;
            for (int i = 0; i < c.length; i++)
                average = average + c[i];
            average = average/c.length;
//            System.out.println("\nAverage Value: " + average);

            double SSR = 0;
            double SST = 0;
            
            for (int j = 0; j < d.length; j++)
                SSR = SSR + (d[j]-average)*(d[j]-average);            
//            System.out.println("\nSSR Value: " + SSR);            
            
            for (int i = 0; i < c.length; i++)
                SST = SST + (c[i]-average)*(c[i]-average);                
            
//            System.out.println("\nSST Value: " + SST);                    
            double R_2_2 = SSR/SST; 
            
//            System.out.println("\nR^2 Value: " + R_2);
//            System.out.println("\nR^2_2 Value: " + R_2_2);
            int index = 0;
            double R_2_tmp;
            double[] b_tmp;
            while(index < M)
//            for (int index = 0; index < M; ++index)
            {   b_tmp = setupParameterBindex(Parameter[index]);  
                R_2_tmp = lookingOtherParameter(x,c,b_tmp);
//                System.out.println(" and the ErrorSquare is: " + estimateErrorSquare(x,c,b_tmp) + " and the R^2 is: " + R_2_tmp);
                if (( R_2_limit < R_2_tmp)&&(R_2_tmp < 1)&&(R_2_2<R_2_limit))
                {   System.out.println("\nMax of Line is: " + M);
                    System.out.println("\nR^2_2 Value: " + R_2_2);
                    System.out.println("\nError with R^2_2 Value: " + R_2_2+ " is " + estimateErrorSquare(x,c,B));
//                    R_2_2 = R_2_tmp;
                    System.out.println("\nR^2_2_tmp Repair: " + R_2_tmp);
                    System.out.println("\nR^2 Value " + R_2_tmp + " with Parameter ["+index+"]: " + Arrays.toString(b_tmp));//Double.toString(lookingOtherParameter(x,c,setupParameterBindex(Parameter[index]))));                    
//                    for (int j=0; j < b_tmp.length; j++)
//                        B[j] = b_tmp[j];
                    System.out.println("\nError with R^2_2 Repair: " + R_2_2+ " is " + estimateErrorSquare(x,c,B));
                    index = M;
                }
                index++;
            }
        System.out.println("\nThe newest Parameter is:"+ Arrays.toString(B)+" and Error with R^2_2: " + R_2_2+ " is " + estimateErrorSquare(x,c,B));    
        updateParameter(fileParameter,B);    
        return estimateCurrentCostValue(X, B);
    }
    
    public static double[] setupValue(double[] size, double value){
        double[] Value = new double [size.length+1];
        for (int i = 0; i < size.length; i++)
            Value[i] = size[i];
        Value[size.length] = value;
        return Value;
    }

    public static double [] setupStochasticValue(double[] size){
        double[] StochasticValue = new double [size.length];
        for (int i = 0; i < size.length; i++)
            StochasticValue[i] = size[i];
        return StochasticValue;
    }
    public static double [] initParamter(int numberParameter){
        double [] Parameter = new double [numberParameter];
        for (int i = 0; i < numberParameter; i++)
            Parameter[i] = 1;
        return Parameter;
    }
}
