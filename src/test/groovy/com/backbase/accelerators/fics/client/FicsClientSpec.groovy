package com.backbase.accelerators.fics.client

import com.backbase.accelerators.fics.TestData
import com.backbase.accelerators.fics.model.MortgageTransactionItem
import org.tempuri.WsFICSSoap
import spock.lang.Specification

class FicsClientSpec extends Specification {

    private WsFICSSoap wsFICSSoap = Mock()
    private FicsClient ficsClient = new FicsClient(wsFICSSoap)

    void 'getMortgageTransactions returns a list of transactions with the provided loan number' () {
        given:
        String loanNumber = '314236101'

        when:
        Set<MortgageTransactionItem> result = ficsClient.getMortgageTransactions(loanNumber)

        then:
        1 * wsFICSSoap.ficsGetHistory(loanNumber) >> TestData.mortgageTransactionResponse()

        and:
        result.size() == 36
    }
}
