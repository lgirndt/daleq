package de.brands4friends.daleq.internal.builder;

import de.brands4friends.daleq.internal.conversion.TypeConversion;
import de.brands4friends.daleq.internal.template.TemplateValueFactory;

public class SimpleContext implements Context {
    private final TypeConversion typeConversion = new TypeConversion();
    private final TemplateValueFactory templateValueFactory = TemplateValueFactory.getInstance();

    @Override
    public TypeConversion getTypeConversion() {
        return this.typeConversion;
    }

    @Override
    public TemplateValueFactory getTemplateValueFactory() {
        return templateValueFactory;
    }
}
