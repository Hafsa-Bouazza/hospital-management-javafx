package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class RendezVous {
    // Propriétés JavaFX
    private final ObjectProperty<Patient> patient = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> heure = new SimpleObjectProperty<>();
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty medecinId = new SimpleIntegerProperty();

    // Constructeurs
    public RendezVous() {
        // Constructeur par défaut
    }

    public RendezVous(Patient patient, LocalDate date, LocalTime heure) {
        this.patient.set(patient);
        this.date.set(date);
        this.heure.set(heure);
    }

    public RendezVous(Patient patient, LocalDate date, LocalTime heure, int medecinId) {
        this(patient, date, heure);
        this.medecinId.set(medecinId);
    }

    // Getters et setters pour les propriétés

    public Patient getPatient() {
        return patient.get();
    }

    public void setPatient(Patient patient) {
        this.patient.set(patient);
    }

    public ObjectProperty<Patient> patientProperty() {
        return patient;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalTime getHeure() {
        return heure.get();
    }

    public void setHeure(LocalTime heure) {
        this.heure.set(heure);
    }

    public ObjectProperty<LocalTime> heureProperty() {
        return heure;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getMedecinId() {
        return medecinId.get();
    }

    public void setMedecinId(int medecinId) {
        this.medecinId.set(medecinId);
    }

    public IntegerProperty medecinIdProperty() {
        return medecinId;
    }

    // toString personnalisé
    @Override
    public String toString() {
        return String.format("%s %s - %s %s",
                getPatient() != null ? getPatient().getNom() : "Inconnu",
                getPatient() != null ? getPatient().getPrenom() : "",
                getDate() != null ? getDate().toString() : "Date inconnue",
                getHeure() != null ? getHeure().toString() : "Heure inconnue");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RendezVous)) return false;
        RendezVous other = (RendezVous) obj;
        return getId() == other.getId() &&
                getMedecinId() == other.getMedecinId() &&
                Objects.equals(getDate(), other.getDate()) &&
                Objects.equals(getHeure(), other.getHeure()) &&
                Objects.equals(getPatient(), other.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMedecinId(), getDate(), getHeure(), getPatient());
    }
}
