package de.brands4friends.daleq.internal.structure;

import org.junit.Test;

import de.brands4friends.daleq.test.EqualsAssert;

public class FieldStructureTest{

    @Test
    public void testHashCodeAndEquals(){
        EqualsAssert.assertProperEqualsAndHashcode(FieldStructure.class);
    }
}