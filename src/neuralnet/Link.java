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
public class Link {
    private double weight;
    private double deltaWeight;
    
    //Constructor for link that initialises weight with random value
    public Link(){
        this.weight = genRandomWeight();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }
    
    //Generates random weight between 0 and 1
    private double genRandomWeight(){
        int bound = 100000;
        return (double)NeuralNet.rand.nextInt(bound + 1) / (double)bound;
    }
}
