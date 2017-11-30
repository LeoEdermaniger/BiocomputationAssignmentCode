/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnet;

/**
 *
 * @author leoed
 */
public class Type3 extends Type{
    double[] inputs;
    double[] outputs;
    
    public Type3(String[] values, String result){
        this.inputs = new double[6];
        this.outputs = new double[1];
        
        for(int x = 0; x < values.length; x++)
            this.inputs[x] = Double.parseDouble(values[x]);
        if(result.equals("1"))
                this.outputs[0] = 1;
            else
                this.outputs[0] = 0;
    }

    @Override
    public double[] getInputs() {
        return inputs;
    }

    @Override
    public double[] getOutputs() {
        return outputs;
    }
}
