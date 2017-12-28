package com.tl.discountsaroundme.entities;

import org.junit.Test;

public class ItemValidatorTest {

    private ItemValidator itemValidator = new ItemValidator();

    @Test(expected = Exception.class)
    public void areStringsEmpty() throws Exception {
        boolean areStringsEmpty = itemValidator.areStringsEmpty("", "", "", "", "");
        if (areStringsEmpty)
            throw new Exception();
    }

    @Test(expected = Exception.class)
    public void isDiscountInRange1() throws Exception {
        boolean isInRange = itemValidator.isDiscountInRange(150);

        if (!isInRange)
            throw new Exception();
    }

    @Test(expected = Exception.class)
    public void isDiscountInRange2() throws Exception {
        boolean isInRange = itemValidator.isDiscountInRange(0);

        if (isInRange)
            throw new Exception();
    }

    @Test(expected = Exception.class)
    public void isDiscountInRange3() throws Exception {
        boolean isInRange = itemValidator.isDiscountInRange(100);

        if (isInRange)
            throw new Exception();
    }

    @Test(expected = Exception.class)
    public void isDiscountInRange4() throws Exception {
        boolean isInRange = itemValidator.isDiscountInRange(-150);

        if (!isInRange)
            throw new Exception();
    }

}