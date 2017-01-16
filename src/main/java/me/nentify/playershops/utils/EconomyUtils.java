package me.nentify.playershops.utils;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import java.math.BigDecimal;

public class EconomyUtils {

    public static ResultType transferWithTax(UniqueAccount from, UniqueAccount to, Currency currency, BigDecimal amount, BigDecimal tax, Cause cause) {
        TransactionResult transfer = from.transfer(to, currency, amount, cause);

        if (transfer.getResult() == ResultType.SUCCESS) {
            TransactionResult withdrawTax = to.withdraw(currency, amount.multiply(tax), cause);
            if (withdrawTax.getResult() != ResultType.SUCCESS) {
                //Ignore this as transaction mostly worked
                System.err.println("Failed to withdraw tax from recipient " + withdrawTax.toString());
            }
        }
        return transfer.getResult();
    }
}
