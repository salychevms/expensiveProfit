package ru.salychev.expensiveprofit.validation;

import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;
import ru.salychev.expensiveprofit.repo.TypeRepository;
import ru.salychev.expensiveprofit.repo.UnitRepository;

import java.math.BigDecimal;

public class Validator {
    static final String DEFAULT_TYPE = "UNDEFINED";
    static final String DEFAULT_CURRENCY = "EUR";
    static final String DEFAULT_UNIT = "UNITS";
    final BigDecimal DEFAULT_BIG_DECIMAL = BigDecimal.valueOf(0);
    final Double DEFAULT_QUANTITY = 1.0;

    //validating data which contained on @RequestBody when we want to create a new expense
    public static byte postValidator(Expense expense, CurrencyRepository currencyRepository) {
        byte validate = 0;//identification code for currency errors
        try {
            if (expense.getCurrency() == null) {
                System.out.println("Warning! A currency doesn't exist on request body");
                //and if default currency doesn't exist on currency table too
                if (currencyRepository.findAllByName(DEFAULT_CURRENCY).isEmpty()) {
                    System.out.println("Warning! The default currency doesn't exist!");
                    //create default currency
                    currencyRepository.save(new Currency(DEFAULT_CURRENCY));
                    System.out.println("The default currency was created!");
                } else {
                    validate += 1;
                    System.out.println("Error << " + String.valueOf(validate) + " >>! Can't creating default currency!");
                }
                //if default currency is exists we save it on new expense
                expense.setCurrency(currencyRepository.findByName(DEFAULT_CURRENCY));
                System.out.println("The default currency saved on the request body");
            } else {
                validate += 2;
                System.out.println("Error << " + String.valueOf(validate) + " >>! Can't saving a default currency!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (currencyRepository.findAllByName(expense.getCurrency().getName()).isEmpty()) {
                System.out.println("Warning! The currency from request doesnt' exist on the currency table!");
                //create new currency
                Currency currency = currencyRepository.save(expense.getCurrency());
                System.out.println("The new currency is created");
                //if currency doesn't exist on request
            } else {
                validate += 4;
                System.out.println("Error << " + String.valueOf(validate) + " >>! Can't creating a new currency");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validate;
    }

    //validating data which contained on @RequestBody when we want to create a new expense
    public static byte postValidator(Expense expense, UnitRepository unitRepository) {
        byte validate = 0;//identification code for unit errors
        if (unitRepository.findAllByName(expense.getUnit().getName()).isEmpty()) {
            System.out.println("Warning! The unit from request doesnt' exist on the unit table!");
            //create new unit
            Unit unit = unitRepository.save(expense.getUnit());
            System.out.println("The new unit is created");
            //if unit doesn't exist on request
        } else {
            validate += 1;
            System.out.println("Error " + String.valueOf(validate) + "! Can't creating a new unit");
        }
        if (expense.getUnit() == null) {
            System.out.println("Warning! A unit doesn't exist on request body");
            //and if default unit doesn't exist on unit table too
            if (unitRepository.findAllByName(DEFAULT_UNIT).isEmpty()) {
                System.out.println("Warning! The default unit doesn't exist!");
                //create default unit
                unitRepository.save(new Unit(DEFAULT_UNIT));
                System.out.println("The default unit was created!");
            } else {
                validate += 2;
                System.out.println("Error " + validate + "! Can't creating default unit!");
            }
            //if default unit is exists we save it on new expense
            expense.setUnit(unitRepository.findByName(DEFAULT_UNIT));
            System.out.println("a default unit saved on the request body");
        } else {
            validate += 4;
            System.out.println("Error " + validate + "! Can't saving a default unit!");
        }
        return validate;
    }

    //validating data which contained on @RequestBody when we want to create a new expense
    public static byte postValidator(Expense expense, TypeRepository typeRepository) {
        byte validate = 0;//identification code for type errors
        if (typeRepository.findAllByName(expense.getType().getName()).isEmpty()) {
            System.out.println("Warning! The type from request doesnt' exist on the type table!");
            //create new type
            Type type = typeRepository.save(expense.getType());
            System.out.println("The new type is created");
            //if type doesn't exist on request
        } else {
            validate += 1;
            System.out.println("Error " + validate + "! Can't creating a new type");
        }
        if (expense.getType() == null) {
            System.out.println("Warning! A type doesn't exist on request body");
            //and if default type doesn't exist on type table too
            if (typeRepository.findAllByName(DEFAULT_TYPE).isEmpty()) {
                System.out.println("Warning! The default type doesn't exist!");
                //create default type
                typeRepository.save(new Type(DEFAULT_TYPE));
                System.out.println("The default type was created!");
            } else {
                validate += 2;
                System.out.println("Error " + validate + "! Can't creating default type!");
            }
            //if default type is exists we save it on new expense
            expense.setType(typeRepository.findByName(DEFAULT_TYPE));
            System.out.println("a default type saved on the request body");
        } else {
            validate += 4;
            System.out.println("Error " + validate + "! Can't saving a default type!");
        }
        return validate;
    }

}
