package com.example.medicalai.contracts;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalSummaryResponse {
  private String disease;
  private String rootCause;
  private String treatment;
  private String labExplanation;
  private String dietRecommendations;
  private String recoveryAdvice;
  private String preventionTips;
  private String disclaimer = "AI generated summary. Consult a doctor for medical advice.";

  public String getDisease() {
    return disease;
  }

  public void setDisease(String v) {
    this.disease = v;
  }

  public String getRootCause() {
    return rootCause;
  }

  public void setRootCause(String v) {
    this.rootCause = v;
  }

  public String getTreatment() {
    return treatment;
  }

  public void setTreatment(String v) {
    this.treatment = v;
  }

  public String getLabExplanation() {
    return labExplanation;
  }

  public void setLabExplanation(String v) {
    this.labExplanation = v;
  }

  public String getDietRecommendations() {
    return dietRecommendations;
  }

  public void setDietRecommendations(String v) {
    this.dietRecommendations = v;
  }

  public String getRecoveryAdvice() {
    return recoveryAdvice;
  }

  public void setRecoveryAdvice(String v) {
    this.recoveryAdvice = v;
  }

  public String getPreventionTips() {
    return preventionTips;
  }

  public void setPreventionTips(String v) {
    this.preventionTips = v;
  }

  public String getDisclaimer() {
    return disclaimer;
  }

  public void setDisclaimer(String v) {
    this.disclaimer = v;
  }

  @Override
  public String toString() {
    return "MedicalSummaryResponse{" +
            "disease='" + disease + '\'' +
            ", rootCause='" + rootCause + '\'' +
            ", treatment='" + treatment + '\'' +
            ", labExplanation='" + labExplanation + '\'' +
            ", dietRecommendations='" + dietRecommendations + '\'' +
            ", recoveryAdvice='" + recoveryAdvice + '\'' +
            ", preventionTips='" + preventionTips + '\'' +
            ", disclaimer='" + disclaimer + '\'' +
            '}';
  }
}
