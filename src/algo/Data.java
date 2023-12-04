package algo;

public class Data {

    private Vertex[] arrayVertex = null;

    public Data(Vertex[] arrayVertex) {
        this.arrayVertex = arrayVertex;
    }

    public Vertex[] getArrayVertex() {
        return arrayVertex;
    }

    public void setArrayVertex(Vertex[] arrayVertex) {
        this.arrayVertex = arrayVertex;
    }

    public double calculateDistance(int indexVertex1, int indexVertex2) {
        double distance = -1;
        // Validation
        if (arrayVertex != null
                && indexVertex1 >= 0
                && indexVertex1 < arrayVertex.length
                && indexVertex2 >= 0
                && indexVertex2 < arrayVertex.length) {
            // If valid
            Vertex v1 = arrayVertex[indexVertex1];
            Vertex v2 = arrayVertex[indexVertex2];

            // Calculate Euclidean distance
            double x1 = v1.x;
            double y1 = v1.y;

            double x2 = v2.x;
            double y2 = v2.y;

            double xDiff = x1 - x2;
            double yDiff = y1 - y2;

            double xDiffSquared = xDiff * xDiff;
            double yDiffSquared = yDiff * yDiff;

            double sumSquared = xDiffSquared + yDiffSquared;
            distance = Math.sqrt(sumSquared);
        }
        return distance;
    }

    @Override
    public String toString() {
        String result = null;
        if (arrayVertex != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Data----------------------------------------------\n");
            sb.append("label ( x , y )\n");
            for (int i = 0; i < arrayVertex.length; i++) {
                sb.append(arrayVertex[i].toString()).append("\n");
            }
            sb.append("--------------------------------------------------\n");
            result = sb.toString();
        }
        return result;
    }
}
