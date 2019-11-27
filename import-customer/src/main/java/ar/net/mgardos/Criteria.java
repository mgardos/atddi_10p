package ar.net.mgardos;

public class Criteria {
    private final String identificationType;
    private final String identificationNumber;

    public Criteria(String identificationType, String identificationNumber) {
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }
}
