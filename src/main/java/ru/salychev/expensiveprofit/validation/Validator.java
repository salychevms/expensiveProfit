package ru.salychev.expensiveprofit.validation;

import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;
import ru.salychev.expensiveprofit.repo.TypeRepository;
import ru.salychev.expensiveprofit.repo.UnitRepository;

import java.math.BigDecimal;

public class Validator {
    private static CurrencyRepository currencyRepository;
    private static UnitRepository unitRepository;
    private static TypeRepository typeRepository;
    private static final String DEFAULT_TYPE = "UNDEFINED";
    private static final String DEFAULT_CURRENCY = "EUR";
    private static final String DEFAULT_UNIT = "UNITS";
    private static final BigDecimal DEFAULT_BIG_DECIMAL = BigDecimal.valueOf(0);
    private static final Double DEFAULT_QUANTITY = 1.0;

    //validating data which contained on @RequestBody when we want to create a new expense
    private static String postCurrencyValidator(String currency) {
        try {
            if (currency == null || currency.isEmpty()) {
                System.out.println("Warning! A currency doesn't exist on request body");
                //and if default currency doesn't exist on currency table too
                if (currencyRepository.findAllByName(DEFAULT_CURRENCY) == null) {
                    System.out.println("Warning! The default currency doesn't exist!");
                    //create default currency
                    currency = currencyRepository.save(new Currency(DEFAULT_CURRENCY)).getName();
                    System.out.println("The default currency << " + currency + " >> was created!");
                } else {
                    //if default currency is exists we save it on new expense
                    currency = currencyRepository.findByName(DEFAULT_CURRENCY).getName();
                    System.out.println("The default currency << " + currency + " >> saved on the request body");
                }
            } else if (currencyRepository.findByName(currency) == null) {
                System.out.println("Warning! The currency from request doesnt' exist on the currency table!");
                //create new currency
                currency = currencyRepository.save(new Currency(currency)).getName();
                System.out.println("The new currency << " + currency + " >> is created");
                //if currency doesn't exist on request
            } else {
                System.out.println("The currency << " + currency + " >> is exists on the currency table!");
            }
        } catch (Exception e) {
            System.out.println("Currency validation error!\n" + currency);
            e.printStackTrace();
        }
        return currency;
    }

    //validating data which contained on @RequestBody when we want to create a new expense
    private static Unit postUnitValidator(String unit) {
        try {
            if (unit == null) {
                System.out.println("Warning! A unit doesn't exist on request body");
                //and if default unit doesn't exist on currency table too
                if (unitRepository.findAllByName(DEFAULT_UNIT).isEmpty()) {
                    System.out.println("Warning! The default unit doesn't exist!");
                    //create default unit
                    unitRepository.save(new Unit(DEFAULT_UNIT));
                    System.out.println("The default unit << " + DEFAULT_UNIT + " >> was created!");
                } else {
                    //if default unit is exists we save it on new expense
                    unit = unitRepository.findByName(DEFAULT_UNIT).getName();
                    System.out.println("The default unit << " + unit + " >> saved on the request body");
                }
            } else
                System.out.println("Error! Can't saving a default unit!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (unitRepository.findAllByName(unit).isEmpty()) {
                System.out.println("Warning! The unit from request doesnt' exist on the unit table!");
                //create new unit
                unitRepository.save(new Unit(unit));
                System.out.println("The new unit << " + unit + " >> is created");
                //if unit doesn't exist on request
            } else
                System.out.println("Error! Can't creating a new unit");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Unit(unit);
    }

    //validating data which contained on @RequestBody when we want to create a new expense
    private static Type postTypeValidator(String type) {
        try {
            if (type == null) {
                System.out.println("Warning! A type doesn't exist on request body");
                //and if default type doesn't exist on type table too
                if (typeRepository.findAllByName(DEFAULT_TYPE).isEmpty()) {
                    System.out.println("Warning! The default type doesn't exist!");
                    //create default type
                    typeRepository.save(new Type(DEFAULT_TYPE));
                    System.out.println("The default type << " + DEFAULT_TYPE + " >> was created!");
                } else {
                    //if default type is exists we save it on new expense
                    type = typeRepository.findByName(DEFAULT_TYPE).getName();
                    System.out.println("The default type << " + type + " >> saved on the request body");
                }
            } else
                System.out.println("Error! Can't saving a default type!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (typeRepository.findAllByName(type).isEmpty()) {
                System.out.println("Warning! The type from request doesnt' exist on the type table!");
                //create new type
                typeRepository.save(new Type(type));
                System.out.println("The new type << " + type + " >> is created");
                //if type doesn't exist on request
            } else
                System.out.println("Error! Can't creating a new type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Type(type);
    }

    public static Expense postValidating(Expense expense, String currencyName, String typeName, String unitName) {
        try {
            expense.setCurrency(currencyRepository.findByName(Validator.postCurrencyValidator(currencyName)));
            expense.setType(postTypeValidator(expense.getType().getName()));
            expense.setUnit(postUnitValidator(expense.getUnit().getName()));
            System.out.println("Success!\n" + expense);
            return expense;
        } catch (Exception e) {
            System.out.println("Validation error!\n" + expense);
            e.printStackTrace();
            return null;
        }
    }
}
