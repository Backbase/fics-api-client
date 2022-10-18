package com.backbase.accelerators.fics.client;

import com.backbase.accelerators.fics.exception.FicsClientException;
import com.backbase.accelerators.fics.model.MortgageAccountItem;
import com.backbase.accelerators.fics.model.MortgageTransactionItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.XML;
import org.tempuri.WsFICSSoap;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class FicsClient {

    private static final ObjectMapper objectMapper;
    private static final XmlMapper xmlMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
    }

    private final WsFICSSoap wsFicsSoap;

    public FicsClient(WsFICSSoap wsFicsSoap) {
        this.wsFicsSoap = wsFicsSoap;
    }

    public MortgageAccountItem getMortgageAccount(String loanNumber) {
        log.debug("Entering getMortgageAccount() with loanNumber: {}", loanNumber);

        try {
            return wsFicsSoap.ficsGetInfoXML(loanNumber)
                    .getContent()
                    .parallelStream()
                    .map(this::serializeToString)
                    .map(this::sanitize)
                    .filter(StringUtils::isNotBlank)
                    .map(this::mapToMortgageAccountItem)
                    .filter(Objects::nonNull)
                    .map(mortgageAccountItem -> setLoanNumber(mortgageAccountItem, loanNumber))
                    .findFirst()
                    .orElseThrow();
        } catch (Exception e) {
            throw new FicsClientException("Could not retrieve mortgage account with loanNumber: " + loanNumber, e);
        }
    }

    public Set<MortgageTransactionItem> getMortgageTransactions(String loanNumber) {
        log.debug("Entering getMortgageTransactions() with loanNumber: {}", loanNumber);

        var responseXml = wsFicsSoap.ficsGetHistory(loanNumber);
        log.debug("Received raw XML response from FICS: {}", responseXml);
        return parseResponse(responseXml);
    }

    private String serializeToString(Object object) {
        log.debug("Serializing raw response object to string: {}", object);

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Exception occurred in serializing response object: {}", e.getMessage());
            return StringUtils.EMPTY;
        }
    }

    private String sanitize(String str) {
        log.debug("Removing invalid characters from response: {}", str);
        var responseString = StringEscapeUtils.unescapeJava(str);
        return responseString.substring(1, responseString.length() - 1);
    }

    private MortgageAccountItem mapToMortgageAccountItem(String response) {
        log.debug("Mapping response to MortgageAccountItem: {}", response);

        try {
            var mortgageAccountItem = xmlMapper.readValue(response, MortgageAccountItem.class);
            log.debug("Mapped mortgage account: {}", mortgageAccountItem);

            return mortgageAccountItem;
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException caught while mapping response to MortgageAccountItem: {}",
                    e.getMessage());

            return null;
        }
    }

    public Set<MortgageTransactionItem> parseResponse(String responseXml) {
        List<JsonNode> nodes;

        try {
            nodes = objectMapper.readTree(XML.toJSONObject(responseXml).toString()).findValues("HISTORY");
        } catch (JsonProcessingException | JSONException e) {
            log.error("Failed to parse transaction XML response: {}", e.getMessage());
            throw new FicsClientException("Failed to parse transaction XML response: " + e.getMessage(), e);
        }

        var response = nodes.parallelStream()
                .map(this::mapToMortgageTransactionItem)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        log.debug("Mortgage transaction mapping complete: {}", response);
        return response;
    }

    private List<MortgageTransactionItem> mapToMortgageTransactionItem(JsonNode jsonNode) {
        log.debug("Mapping {} transaction records...", jsonNode.size());

        try {
            return objectMapper.readValue(jsonNode.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON string: {}", e.getMessage());
            throw new FicsClientException("Failed to deserialize JSON string: " + e.getMessage(), e);
        }
    }

    private MortgageAccountItem setLoanNumber(MortgageAccountItem mortgageAccountItem, String loanNumber) {
        mortgageAccountItem.setAccountNumber(loanNumber);
        return mortgageAccountItem;
    }
}