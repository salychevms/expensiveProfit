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

    //validating data which contained on @PathVariable when we want to create a new expense
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

    //validating data which contained on @PathVariable when we want to create a new expense
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

    //validating data which contained on @PathVariable when we want to create a new expense
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

    //The cost, price and quantity expense data validation
    private static Expense expenseCostValidator(Expense expense) {
        Double quantity = expense.getQuantity();
        BigDecimal price = expense.getPrice();
        BigDecimal cost = expense.getCost();
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
            expense.setCost(expense.getPrice());
            expense.setQuantity(DEFAULT_QUANTITY);
        } else if (nullQuantity && nullPrice && !nullCost) {
            expense.setPrice(expense.getCost());
            expense.setQuantity(DEFAULT_QUANTITY);
        }
        //to get a value of quantity
        if (nullQuantity && !nullCost && !nullPrice) {
            try {
                result = (expense.getCost().divide(expense.getPrice())).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= quantity");
                expense.setQuantity(result.doubleValue());
            } catch (Exception e) {
                System.out.println("Dividing error! Check the data! Can't get a quantity value.");
                e.printStackTrace();
            }
        }
        //to get a value of price
        if (nullPrice && !nullCost && !nullQuantity) {
            try {
                result = expense.getCost().divide(BigDecimal.valueOf(expense.getQuantity())).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= price");
                expense.setPrice(result);
            } catch (Exception e) {
                System.out.println("Dividing error! Check the data! Can't get a price value.");
                e.printStackTrace();
            }
        }
        //to get a value of cost
        if (nullCost && !nullPrice && !nullQuantity) {
            try {
                result = BigDecimal.valueOf(expense.getQuantity()).multiply(expense.getPrice()).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= cost");
                expense.setCost(result);
            } catch (Exception e) {
                System.out.println("Multiplication error! Check the data! Can't get a cost value.");
                e.printStackTrace();
            }
        }
        return expense;
    }
    //The cost, price and quantity profit data validation
    private static Profit profitCostValidator(Profit profit) {
        Double quantity = profit.getQuantity();
        BigDecimal price = profit.getPrice();
        BigDecimal cost = profit.getCost();
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
            profit.setCost(profit.getPrice());
            profit.setQuantity(DEFAULT_QUANTITY);
        } else if (nullQuantity && nullPrice && !nullCost) {
            profit.setPrice(profit.getCost());
            profit.setQuantity(DEFAULT_QUANTITY);
        }
        //to get a value of quantity
        if (nullQuantity && !nullCost && !nullPrice) {
            try {
                result = (profit.getCost().divide(profit.getPrice())).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= quantity");
                profit.setQuantity(result.doubleValue());
            } catch (Exception e) {
                System.out.println("Dividing error! Check the data! Can't get a quantity value.");
                e.printStackTrace();
            }
        }
        //to get a value of price
        if (nullPrice && !nullCost && !nullQuantity) {
            try {
                result = profit.getCost().divide(BigDecimal.valueOf(profit.getQuantity())).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= price");
                profit.setPrice(result);
            } catch (Exception e) {
                System.out.println("Dividing error! Check the data! Can't get a price value.");
                e.printStackTrace();
            }
        }
        //to get a value of cost
        if (nullCost && !nullPrice && !nullQuantity) {
            try {
                result = BigDecimal.valueOf(profit.getQuantity()).multiply(profit.getPrice()).setScale(3, RoundingMode.HALF_UP);
                System.out.println(result + " <========= cost");
                profit.setCost(result);
            } catch (Exception e) {
                System.out.println("Multiplication error! Check the data! Can't get a cost value.");
                e.printStackTrace();
            }
        }
        return profit;
    }

    //validation of the expense data which we're got from post request
    public static Expense expensePostValidating(Expense expense, Long currencyId,
                                                Long typeId, Long unitId,
                                                CurrencyRepository currencyRepository,
                                                TypeRepository typeRepository,
                                                UnitRepository unitRepository) {
        try {
            expenseCostValidator(expense);
            expense.setCurrency(currencyValidator(currencyId, currencyRepository));
            expense.setType(typeValidator(typeId, typeRepository));
            expense.setUnit(unitValidator(unitId, unitRepository));
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
    //validation of the profit data which we're got from post request
    public static Profit profitPostValidating(Profit profit, Long currencyId,
                                                Long typeId, Long unitId,
                                                CurrencyRepository currencyRepository,
                                                TypeRepository typeRepository,
                                                UnitRepository unitRepository) {
        try {
            profitCostValidator(profit);
            profit.setCurrency(currencyValidator(currencyId, currencyRepository));
            profit.setType(typeValidator(typeId, typeRepository));
            profit.setUnit(unitValidator(unitId, unitRepository));
            if (profit.getDate() == null)
                profit.setDate(new java.util.Date());
            if (profit.getType() != null &&
                    profit.getCurrency() != null &&
                    profit.getUnit() != null) {
                System.out.println("Success! New expense is valid\n" + profit);
                return profit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Validation error!\n" + profit);
        return null;
    }

    //validation of the expense data which we're got from put request
    public static Expense expensePutValidating(Expense request, Expense updatableExpense,
                                               Long currencyId, Long typeId, Long unitId,
                                               CurrencyRepository currencyRepository,
                                               TypeRepository typeRepository,
                                               UnitRepository unitRepository) {
        try {
            //currency
            try {
                if (currencyId != null) {
                    if (currencyValidator(currencyId, currencyRepository) != null) {
                        updatableExpense.setCurrency(currencyValidator(currencyId, currencyRepository));
                        System.out.println("A currency changed to: \t" + updatableExpense.getUnit().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The currency not changed!");
            }
            //type
            try {
                if (typeId != null) {
                    if (typeValidator(typeId, typeRepository) != null) {
                        updatableExpense.setType(typeValidator(typeId, typeRepository));
                        System.out.println("A type changed: to \t" + updatableExpense.getType().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The type not changed!");

            }
            //unit
            try {
                if (unitId != null) {
                    if (unitValidator(unitId, unitRepository) != null) {
                        updatableExpense.setUnit(unitValidator(unitId, unitRepository));
                        System.out.println("A unit changed to: \t" + updatableExpense.getUnit().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The unit not changed!");
            }
            //date
            if (request.getDate() != null && !Objects.equals(updatableExpense.getDate(), request.getDate())) {
                updatableExpense.setDate(request.getDate());
                System.out.println("A date changed to: \t" + updatableExpense.getDate());
            } else System.out.println("The date not changed!");
            //comment
            if (request.getComment() != null && !Objects.equals(request.getComment(), updatableExpense.getComment())) {
                updatableExpense.setComment(request.getComment());
                System.out.println("A comment changed to: \t" + updatableExpense.getComment());
            } else System.out.println("The comment not changed!");

            //Attention!!!
            /// if only one parameter changed (cost, price or quantity) then necessary to ask user what he wants to change also
            //cost
            if (request.getCost() != null && !Objects.equals(request.getCost(), updatableExpense.getCost())) {
                updatableExpense.setCost(request.getCost());
                System.out.println("A cost changed to: \t" + updatableExpense.getCost());
            } else System.out.println("The cost not changed!");
            //price
            if (request.getPrice() != null && !Objects.equals(request.getPrice(), updatableExpense.getPrice())) {
                updatableExpense.setPrice(request.getPrice());
                System.out.println("A price changed to: \t" + updatableExpense.getPrice());
            } else System.out.println("The price not changed!");
            //quantity
            if (request.getQuantity() != null && !Objects.equals(request.getQuantity(), updatableExpense.getQuantity())) {
                updatableExpense.setQuantity(request.getQuantity());
                System.out.println("A quantity changed to: \t" + updatableExpense.getQuantity());
            } else System.out.println("The quantity not changed!");
            ///===========================================================================================================

            return updatableExpense;
        } catch (Exception e) {
            System.out.println("An error in the update process was found! Check the request data: \n" + request);
            e.printStackTrace();
        }
        return null;
    }

    //validation of the expense data which we're got from put request
    public static Profit profitPutValidating(Profit request, Profit updatableProfit,
                                               Long currencyId, Long typeId, Long unitId,
                                               CurrencyRepository currencyRepository,
                                               TypeRepository typeRepository,
                                               UnitRepository unitRepository) {
        try {
            //currency
            try {
                if (currencyId != null) {
                    if (currencyValidator(currencyId, currencyRepository) != null) {
                        updatableProfit.setCurrency(currencyValidator(currencyId, currencyRepository));
                        System.out.println("A currency changed to: \t" + updatableProfit.getUnit().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The currency not changed!");
            }
            //type
            try {
                if (typeId != null) {
                    if (typeValidator(typeId, typeRepository) != null) {
                        updatableProfit.setType(typeValidator(typeId, typeRepository));
                        System.out.println("A type changed: to \t" + updatableProfit.getType().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The type not changed!");

            }
            //unit
            try {
                if (unitId != null) {
                    if (unitValidator(unitId, unitRepository) != null) {
                        updatableProfit.setUnit(unitValidator(unitId, unitRepository));
                        System.out.println("A unit changed to: \t" + updatableProfit.getUnit().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("The unit not changed!");
            }
            //date
            if (request.getDate() != null && !Objects.equals(updatableProfit.getDate(), request.getDate())) {
                updatableProfit.setDate(request.getDate());
                System.out.println("A date changed to: \t" + updatableProfit.getDate());
            } else System.out.println("The date not changed!");
            //comment
            if (request.getComment() != null && !Objects.equals(request.getComment(), updatableProfit.getComment())) {
                updatableProfit.setComment(request.getComment());
                System.out.println("A comment changed to: \t" + updatableProfit.getComment());
            } else System.out.println("The comment not changed!");

            //Attention!!!
            /// if only one parameter changed (cost, price or quantity) then necessary to ask user what he wants to change also
            //cost
            if (request.getCost() != null && !Objects.equals(request.getCost(), updatableProfit.getCost())) {
                updatableProfit.setCost(request.getCost());
                System.out.println("A cost changed to: \t" + updatableProfit.getCost());
            } else System.out.println("The cost not changed!");
            //price
            if (request.getPrice() != null && !Objects.equals(request.getPrice(), updatableProfit.getPrice())) {
                updatableProfit.setPrice(request.getPrice());
                System.out.println("A price changed to: \t" + updatableProfit.getPrice());
            } else System.out.println("The price not changed!");
            //quantity
            if (request.getQuantity() != null && !Objects.equals(request.getQuantity(), updatableProfit.getQuantity())) {
                updatableProfit.setQuantity(request.getQuantity());
                System.out.println("A quantity changed to: \t" + updatableProfit.getQuantity());
            } else System.out.println("The quantity not changed!");
            ///===========================================================================================================

            return updatableProfit;
        } catch (Exception e) {
            System.out.println("An error in the update process was found! Check the request data: \n" + request);
            e.printStackTrace();
        }
        return null;
    }
}
