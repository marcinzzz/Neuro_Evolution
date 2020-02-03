import java.util.LinkedList;

public class Population {
    private final int POPULATION;

    private LinkedList<Brain> brains;

    public Population(int size, float mutationRate, int inputNodes, int[] hiddenNodes, int outputNodes) {
        this.POPULATION = size;
        this.brains = new LinkedList<>();

        for (int i = 0; i < POPULATION; i++)
            brains.add(new Brain(new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, mutationRate)));
    }

    public Population(int size, float mutationRate, String directory) {
        this.POPULATION = size;
        this.brains = new LinkedList<>();

        for (int i = 0; i < POPULATION; i++)
            brains.add(new Brain(new NeuralNetwork(directory, mutationRate)));
    }

    public void test(float[] input, float[] output) {
        for (Brain brain : brains) {
            brain.test(input, output);
        }
    }

    public void nextGeneration() {
        for (int i = 0; i < POPULATION; i++) {
            if (POPULATION % 3 == 0)
                brains.get(i).reset();
            else
                brains.set(i, new Brain(brains.get(i / 3 * 3)));
            if (i != 0)
                brains.get(i).mutate();
        }
    }

    public void sort() {
        brains.sort((b1, b2) -> {
            if (b1.getFitness() < b2.getFitness())
                return -1;
            return 1;
        });
    }

    public void exportLeader(String directory) {
        brains.getFirst().getNn().export(directory);
    }

    public void print() {
        for (Brain brain : brains)
            System.out.println(brain.getFitness());
    }

    public Brain getLeader() {
        return brains.getFirst();
    }
}
