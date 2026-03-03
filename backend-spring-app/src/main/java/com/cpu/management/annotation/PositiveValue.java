package com.cpu.management.annotation;

import java.lang.annotation.*;

/**
 * Adnotacja walidująca, że pole numeryczne musi mieć wartość dodatnią.
 * Używana dla pól takich jak cores, threads, frequencyGhz w encji CPU.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface PositiveValue {
    String message() default "Value must be positive";
}
