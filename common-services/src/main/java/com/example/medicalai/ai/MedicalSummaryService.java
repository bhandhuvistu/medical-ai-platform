package com.example.medicalai.ai;

import com.example.medicalai.contracts.MedicalSummaryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MedicalSummaryService {

    private final ChatClient chatClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public MedicalSummaryService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public MedicalSummaryResponse generateSummary(String reportText) {

        String template = """
                Return ONLY valid JSON matching exactly this schema.
                Do not return markdown.
                Do not return backticks.
                Do not return explanations outside JSON.
                Never leave any field empty.
                If a field is not clearly available from the report, use:
                "Not clearly mentioned in report"

                Schema:
                {
                  "disease": "",
                  "rootCause": "",
                  "treatment": "",
                  "labExplanation": "",
                  "dietRecommendations": "",
                  "recoveryAdvice": "",
                  "preventionTips": "",
                  "disclaimer": "AI generated summary. Consult a doctor for medical advice."
                }

                Rules:
                - disease: probable diagnosis or main medical condition from report
                - rootCause: likely cause/risk factor if inferable, otherwise "Not clearly mentioned in report"
                - treatment: medicines or treatment plan mentioned in report
                - labExplanation: explain important abnormal lab values in simple language
                - dietRecommendations: practical diet advice based on report
                - recoveryAdvice: practical recovery advice in simple language
                - preventionTips: future precautions or prevention steps; if not directly available, provide safe general prevention tips relevant to the disease
                - disclaimer must always be exactly:
                  "AI generated summary. Consult a doctor for medical advice."

                Summarize the following hospital reports for a patient in simple language:

                <report_text>
                """;

        try {
            PromptTemplate promptTemplate = PromptTemplate.builder()
                    .template(template)
                    .renderer(
                            StTemplateRenderer.builder()
                                    .startDelimiterToken('<')
                                    .endDelimiterToken('>')
                                    .build()
                    )
                    .build();

            Prompt prompt = promptTemplate.create(
                    Map.of("report_text", reportText)
            );

            String content = chatClient
                    .prompt(prompt)
                    .call()
                    .content()
                    .trim();

            content = extractJson(content);

            MedicalSummaryResponse response =
                    mapper.readValue(content, MedicalSummaryResponse.class);

            fillMissingFields(response);

            return response;

        } catch (Exception e) {
            MedicalSummaryResponse r = new MedicalSummaryResponse();
            r.setDisease("Summary (fallback)");
            r.setRootCause("Not clearly mentioned in report");
            r.setTreatment("Please consult a doctor for accurate diagnosis.");
            r.setLabExplanation(
                    reportText.substring(0, Math.min(500, reportText.length()))
            );
            r.setDietRecommendations("Follow a balanced diet and consult a nutritionist.");
            r.setRecoveryAdvice("Rest well and follow medical guidance.");
            r.setPreventionTips("Regular health checkups and healthy lifestyle.");
            r.setDisclaimer("AI generated summary. Consult a doctor for medical advice.");
            return r;
        }
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }

        return text;
    }

    private void fillMissingFields(MedicalSummaryResponse r) {
        if (isBlank(r.getDisease())) {
            r.setDisease("Not clearly mentioned in report");
        }
        if (isBlank(r.getRootCause())) {
            r.setRootCause("Not clearly mentioned in report");
        }
        if (isBlank(r.getTreatment())) {
            r.setTreatment("Not clearly mentioned in report");
        }
        if (isBlank(r.getLabExplanation())) {
            r.setLabExplanation("Not clearly mentioned in report");
        }
        if (isBlank(r.getDietRecommendations())) {
            r.setDietRecommendations("Not clearly mentioned in report");
        }
        if (isBlank(r.getRecoveryAdvice())) {
            r.setRecoveryAdvice("Not clearly mentioned in report");
        }
        if (isBlank(r.getPreventionTips())) {
            r.setPreventionTips("Regular checkups, healthy diet, exercise, and taking medicines as advised.");
        }
        r.setDisclaimer("AI generated summary. Consult a doctor for medical advice.");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}