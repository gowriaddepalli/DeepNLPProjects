import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class Main {

	static int VECTOR_SIZE = 50;
	static String GLOVE_FILE = "/Users/sreelakshmiaddepalli/Desktop/NLP/glove.6B/glove.6B.50d.txt";

	public static void main(String[] args) throws IOException {

		String inputFileName = "/Users/sreelakshmiaddepalli/Desktop/NLP/wordsim-353.txt";
		String outputFileName = "/Users/sreelakshmiaddepalli/Desktop/NLP/output.txt";
		
		FileWriter fw = new FileWriter(outputFileName);
		File fileval = new File(inputFileName);

		List<Double> humanScores = new ArrayList<>();
		List<Double> machineScores = new ArrayList<>();

		
		List<String> allLines = Files.readAllLines(fileval.toPath(), StandardCharsets.UTF_8);
            
        allLines = allLines.subList(11, allLines.size());

		for (String s : allLines) {
			String arr[] = s.split("\t");
			String word1 = arr[1], word2 = arr[2];
			double[] vector1 = getWordVector(word1.toLowerCase()), vector2 = getWordVector(word2.toLowerCase());
			double humanScore = Double.parseDouble(arr[3]);
			double machineScore = cosineSimilarity(vector1, vector2);
			humanScores.add(humanScore);
			machineScores.add(machineScore);
			fw.write(word1 + "\t" + word2 + "\t" + humanScore + "\t" + machineScore+"\n");
		}
		
		
	    int size=humanScores.size();

	    double[] x = new double[size];
	    double[] y = new double[size];

		int i = 0;

		for(Double d : humanScores){
  			x[i] = (double) d;
 			i++;
		}

		int j = 0;

		for(Double d : machineScores) {
  			y[j] = (double) d;
 			j++;
		}


		double corr = new PearsonsCorrelation().correlation( x , y);

		System.out.println(corr);

		fw.write("\nCorrelation Score: " + corr +"\n");
		fw.close();
		
	}
	

	public static double[] getWordVector(String word) throws FileNotFoundException {
		double[] vec = new double[VECTOR_SIZE];
		Scanner sc = new Scanner(
				new File(GLOVE_FILE));
		while (sc.hasNextLine()) {
			String str[] = sc.nextLine().split(" ");
			if (str[0].equals(word)) {
				for (int j = 1; j <= VECTOR_SIZE; j++)
					vec[j - 1] = Double.parseDouble(str[j]);
				break;
			}
		}
		sc.close();
		return vec;
	}
	

	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
}
