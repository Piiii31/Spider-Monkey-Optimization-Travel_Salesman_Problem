package algo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataReader {

    public static Vertex[] read(File file) {
        Vertex[] arrayVertex = null;
        try {
            FileReader reader = new FileReader(file);
            try (BufferedReader br = new BufferedReader(reader)) {
                String line;
                ArrayList<Vertex> listVertex = new ArrayList<>();

                // Read header data set
                boolean readVertex = false;
                while ((line = br.readLine()) != null) {

                    // Validate line------------------------
                    if (line.length() >= 18
                            && line.substring(0, 18).equals("NODE_COORD_SECTION")) {
                        readVertex = true;
                    } else if (line.equals("EOF")) {
                        readVertex = false;
                        break;
                    }
                    // End of validation------------------------

                    if (readVertex) {
                        line = line.replaceAll("\\s+", " ");
                        if (line.substring(0, 1).equals(" ")) {
                            line = line.substring(1);
                        }
                        String[] lineData = line.split("\\s");
                        if (lineData.length == 3) {
                            String label = lineData[0];
                            double x = Double.parseDouble(lineData[1]);
                            double y = Double.parseDouble(lineData[2]);
                            Vertex v = new Vertex(label, x, y);
                            listVertex.add(v);
                            // System.out.println(v.toString());
                        }
                    }
                }// End of while

                if (listVertex.size() > 0) {
                    // Convert to arrayVertex
                    int n = listVertex.size();
                    arrayVertex = new Vertex[n];
                    for (int i = 0; i < arrayVertex.length; i++) {
                        arrayVertex[i] = listVertex.get(i);
                    }
                }
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            // ex.printStackTrace();
        } catch (IOException ex) {
            // ex.printStackTrace();
        }
        return arrayVertex;
    }
}
