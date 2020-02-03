public class Brain {
    private NeuralNetwork nn;
    private float fitness;

    Brain(NeuralNetwork nn) {
        this.nn = nn;
        fitness = 0;
    }

    Brain(Brain brain) {
        this.nn = new NeuralNetwork(brain.nn);
        fitness = 0;
    }

    public void test(float[] input, float[] output) {
        float[] results = nn.feedForward(input);
        for (int j = 0; j < results.length; j++) {
            fitness += Math.pow(Math.abs(results[j] - output[j]), 2);
        }
    }

    public void mutate() {
        nn.mutate();
    }

    public void reset() {
        this.fitness = 0;
    }

    public float getFitness() {
        return fitness;
    }

    public NeuralNetwork getNn() {
        return nn;
    }
}
