package ru.salychev.expensiveprofit.validation;

import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;
import ru.salychev.expensiveprofit.repo.TypeRepository;
import ru.salychev.expensiveprofit.repo.UnitRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class Validator {
    private static final String DEFAULT_TYPE = "UNDEFINED";
    private static final String DEFAULT_CURRENCY = "EUR";
    private static final String DEFAULT_UNIT = "UNITS";
    private static final BigDecimal DEFAULT_BIG_DECIMAL = BigDecimal.valueOf(0);
    private static final Double DEFAULT_QUANTITY = 1.0;

    //validating data which contained on @RequestBody when we want to create a new expense
    private static Currency postCurrencyValidator(Long currencyId, CurrencyRepository currencyRepository) {
        //currency
        Currency validCurrency = null;
        try {
            if (currencyId == null) { //set default currency
                validCurrency = currencyRepository.findByName(DEFAULT_CURRENCY);
            } else {//set existing currency
                Optional<Currency> newCurrency = currencyRepository.findById(currencyId);
                if(newCurrency.isPresent())
                    validCurrency=newCurrency.get();
                return validCurrency;
            }
            assert validCurrency != null;
            System.out.println("Currency id: " + validCurrency.getId() + " is valid!");
        } catch (Exception e) {
            System.out.println("\nWrong currency id: " + currencyId);
            e.printStackTrace();
        }
        return null;
    }

    //validating data which contained on @RequestBody when we want to create a new expense
    private static Unit postUnitValidator(Long unitId, UnitRepository unitRepository) {
        //unit
        Unit validUnit = null;
        try {
            if (unitId == null) { //set default unit
                validUnit = unitRepository.findByName(DEFAULT_UNIT);
            } else {//set existing unit
                Optional<Unit> newUnit = unitRepository.findById(unitId);
                if (newUnit.isPresent())
                    validUnit = newUnit.get();
                return validUnit;

            }
            assert validUnit != null;
            System.out.println("Unit id: " + validUnit.getId() + " is valid!");
        } catch (Exception e) {
            System.out.println("\nWrong unit id: " + unitId);
            e.printStackTrace();
        }
        return null;
    }

    //validating data which contained on @RequestBody when we want to create a new expense
    private static Type postTypeValidator(Long typeId, TypeRepository typeRepository) {
        //type
        Type validType = null;
        try {
            if (typeId == null) { //set default type
                validType = typeRepository.findByName(DEFAULT_TYPE);
            } else {//set existing type
                Optional<Type> newType = typeRepository.findById(typeId);
                if (newType.isPresent())
                    validType = newType.get();
                return validType;
            }
            assert validType != null;
            System.out.println("Type id: " + validType.getId() + " is valid!");
        } catch (Exception e) {
            System.out.println("\nWrong type id: " + typeId);
            e.printStackTrace();
        }
        return null;
    }

    public static Expense postValidating(Expense expense, Long currencyId, Long typeId, Long unitId,
                                         CurrencyRepository currencyRepository,
                                         TypeRepository typeRepository,
                                         UnitRepository unitRepository) {
        try {
            expense.setCurrency(postCurrencyValidator(currencyId, currencyRepository));
            expense.setType(postTypeValidator(typeId, typeRepository));
            expense.setUnit(postUnitValidator(unitId, unitRepository));
            if (expense.getDate() == null)
                expense.setDate(new java.util.Date());
            if (expense.getType() != null &&
                    expense.getCurrency() != null &&
                    expense.getUnit() != null) {
                System.out.println("Success! New expense is valid\n" + expense);
                return expense;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Validation error!\n" + expense);
        return null;
    }
}
