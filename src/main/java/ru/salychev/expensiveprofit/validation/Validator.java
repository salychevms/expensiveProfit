package ru.salychev.expensiveprofit.validation;

import ru.salychev.expensiveprofit.models.*;
import ru.salychev.expensiveprofit.repo.CurrencyRepository;
import ru.salychev.expensiveprofit.repo.TypeRepository;
import ru.salychev.expensiveprofit.repo.UnitRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

public class Validator {
    private static final String DEFAULT_TYPE = "UNDEFINED";
    private static final String DEFAULT_CURRENCY = "EUR";
    private static final String DEFAULT_UNIT = "UNITS";
    private static final Double DEFAULT_QUANTITY = 1.0;

    //validating data which contained on @PathVariable when we want to create a new operation
    private static Currency currencyValidator(Long currencyId, CurrencyRepository currencyRepository) {
        //currency
        Currency validCurrency = null;
        try {
            if (currencyId == null) { //set default currency
                validCurrency = currencyRepository.findByName(DEFAULT_CURRENCY);
            } else {//set existing currency
                Optional<Currency> newCurrency = currencyRepository.findById(currencyId);
                if (newCurrency.isPresent())
                    validCurrency = newCurrency.get();
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

    //validating data which contained on @PathVariable when we want to create a new operation
    private static Unit unitValidator(Long unitId, UnitRepository unitRepository) {
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

    //validating data which contained on @PathVariable when we want to create a new operation
    private static Type typeValidator(Long typeId, TypeRepository typeRepository) {
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

    //The cost, price and quantity validation
    private static Operation operationCostValidator(Operation operation) {
        Double quantity = operation.getQuantity();
        BigDecimal price = operation.getPrice();
        BigDecimal cost = operation.getCost();
        BigDecimal result;
        boolean nullQuantity = false;
        boolean nullPrice = false;
        boolean nullCost = false;
        if (quantity == null || quantity == 0)
            nullQuantity = true;
        if (price == null || price.equals(BigDecimal.valueOf(0)))
            nullPrice = true;
        if (cost == null || cost.equals(BigDecimal.valueOf(0)))
            nullCost = true;
        if (nullQuantity && nullCost && !nullPrice) {
            operation.setCost(operation.getPrice());
            operation.setQuantity(DEFAULT_QUANTITY);
        } else if (nullQuantity && nullPrice && !nullCost) {
            operation.setPrice(operation.getCost());
            operation.setQuantity(DEFAULT_QUANTITY);
        }
        //to get a value of quantity
        if (nullQuantity && !nullCost && !nullPrice) {
            try {
                result = (operation.getCost().divide(operation.getPrice())).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= quantity");
                operation.setQuantity(result.doubleValue());
            } catch (Exception e) {
                System.out.println("Dividing error! Check the data! Can't get a quantity value.");
                e.printStackTrace();
            }
        }
        //to get a value of price
        if (nullPrice && !nullCost && !nullQuantity) {
            try {
                result = operation.getCost().divide(BigDecimal.valueOf(operation.getQuantity())).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= price");
                operation.setPrice(result);
            } catch (Exception e) {
                System.out.println("Dividing error! Check the data! Can't get a price value.");
                e.printStackTrace();
            }
        }
        //to get a value of cost
        if (nullCost && !nullPrice && !nullQuantity) {
            try {
                result = BigDecimal.valueOf(operation.getQuantity()).multiply(operation.getPrice()).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= cost");
                operation.setCost(result);
            } catch (Exception e) {
                System.out.println("Multiplication error! Check the data! Can't get a cost value.");
                e.printStackTrace();
            }
        }
        return operation;
    }

    //validation of the operation data which we're got from post request
    public static Operation validationOnCreation(Operation operation, Long currencyId,
                                                 Long typeId, Long unitId,
                                                 CurrencyRepository currencyRepository,
                                                 TypeRepository typeRepository,
                                                 UnitRepository unitRepository) {
        try {
            operationCostValidator(operation);
            operation.setCurrency(currencyValidator(currencyId, currencyRepository));
            operation.setType(typeValidator(typeId, typeRepository));
            operation.setUnit(unitValidator(unitId, unitRepository));
            if (operation.getDate() == null)
                operation.setDate(new java.util.Date());
            if (operation.getType() != null &&
                    operation.getCurrency() != null &&
                    operation.getUnit() != null) {
                System.out.println("Success! New operation is valid\n" + operation);
                return operation;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Validation error!\n" + operation);
        return null;
    }

    //validation of the operation data which we're got from put request
    public static Operation validationOnUpdate(Operation request, Operation updatableOperation,
                                               Long currencyId, Long typeId, Long unitId,
                                               CurrencyRepository currencyRepository,
                                               TypeRepository typeRepository,
                                               UnitRepository unitRepository) {
        try {
            //currency
            try {
                if (currencyId != null) {
                    if (currencyValidator(currencyId, currencyRepository) != null) {
                        updatableOperation.setCurrency(currencyValidator(currencyId, currencyRepository));
                        System.out.println("A currency changed to: \t" + updatableOperation.getUnit().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The currency not changed!");
            }
            //type
            try {
                if (typeId != null) {
                    if (typeValidator(typeId, typeRepository) != null) {
                        updatableOperation.setType(typeValidator(typeId, typeRepository));
                        System.out.println("A type changed: to \t" + updatableOperation.getType().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The type not changed!");

            }
            //unit
            try {
                if (unitId != null) {
                    if (unitValidator(unitId, unitRepository) != null) {
                        updatableOperation.setUnit(unitValidator(unitId, unitRepository));
                        System.out.println("A unit changed to: \t" + updatableOperation.getUnit().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The unit not changed!");
            }
            //date
            if (request.getDate() != null && !Objects.equals(updatableOperation.getDate(), request.getDate())) {
                updatableOperation.setDate(request.getDate());
                System.out.println("A date changed to: \t" + updatableOperation.getDate());
            } else System.out.println("The date not changed!");
            //comment
            if (request.getComment() != null && !Objects.equals(request.getComment(), updatableOperation.getComment())) {
                updatableOperation.setComment(request.getComment());
                System.out.println("A comment changed to: \t" + updatableOperation.getComment());
            } else System.out.println("The comment not changed!");

            //Attention!!!
            /// if only one parameter changed (cost, price or quantity) then necessary to ask user what he wants to change also
            //cost
            if (request.getCost() != null && !Objects.equals(request.getCost(), updatableOperation.getCost())) {
                updatableOperation.setCost(request.getCost());
                System.out.println("A cost changed to: \t" + updatableOperation.getCost());
            } else System.out.println("The cost not changed!");
            //price
            if (request.getPrice() != null && !Objects.equals(request.getPrice(), updatableOperation.getPrice())) {
                updatableOperation.setPrice(request.getPrice());
                System.out.println("A price changed to: \t" + updatableOperation.getPrice());
            } else System.out.println("The price not changed!");
            //quantity
            if (request.getQuantity() != null && !Objects.equals(request.getQuantity(), updatableOperation.getQuantity())) {
                updatableOperation.setQuantity(request.getQuantity());
                System.out.println("A quantity changed to: \t" + updatableOperation.getQuantity());
            } else System.out.println("The quantity not changed!");
            ///===========================================================================================================

            return updatableOperation;
        } catch (Exception e) {
            System.out.println("An error in the update process was found! Check the request data: \n" + request);
            e.printStackTrace();
        }
        return null;
    }
}
