package me.cahrypt.kitpvpcore.kit.anotation;

import me.cahrypt.kitpvpcore.item.items.KitCoreItems;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecificInteractAtPlayerAbility {
    KitCoreItems.AbilityItems listenItem();
}
