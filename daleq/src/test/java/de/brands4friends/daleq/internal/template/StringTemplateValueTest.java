package de.brands4friends.daleq.internal.template;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.brands4friends.daleq.TemplateValue;
import de.brands4friends.daleq.test.EqualsAssert;

public class StringTemplateValueTest {

    @Test
    public void testHashCodeAndEquals() {
        EqualsAssert.assertProperEqualsAndHashcode(StringTemplateValue.class);
    }

    @Test
    public void renderATemplateWithAString_should_returnSubstitutedValue() {
        assertRendering("${_}", 42, "42");
    }

    @Test
    public void renderingATemplateWithoutVar_should_returnTemplateStr() {
        assertRendering("FOOBAR", 34, "FOOBAR");
    }

    @Test
    public void anEscapedVar_should_returnTheVar() {
        assertRendering("$${_}", 123, "${_}");
    }

    @Test
    public void replacingAVarInAstring_should_returnTheReplacedStr() {
        assertRendering("ABC${_}EFGH", 42, "ABC42EFGH");
    }

    @Test
    public void null_shouldBe_null() {
        assertRendering(null, 34, null);
    }

    private void assertRendering(final String template, final long binding, final String expectedStr) {
        final TemplateValue templateValue = new StringTemplateValue(template);
        assertThat(templateValue.render(binding), is(expectedStr));
    }
}
