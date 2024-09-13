package JavaClass;

public class PhotoPrediction {

    private Double confidence;
    private String predictionClass;

    public PhotoPrediction(Double confidence, String predictionClass) {
        this.confidence = confidence;
        this.predictionClass = predictionClass;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getPredictionClass() {
        return predictionClass;
    }

    public void setPredictionClass(String predictionClass) {
        this.predictionClass = predictionClass;
    }
}
