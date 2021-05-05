package org.realtors.rets.metadata.attrib;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataParseException;

public class AttrBooleanTest
    extends AttrTypeTest
{
    public void testBoolean() throws Exception
    {
        String[] trues = {"true", "1", "TrUe", "Y"};
        String[] falses = {"false", "FALSE","0", "", "N"};
        String[] exceptions = {"weird", "#(*&", "2", "falze"};


        AttrType<Boolean> parser = new AttrBoolean();
        assertEquals("Wrong Class returned", Boolean.class, parser.getType());
        for (String input : trues) {
            boolean value = (Boolean) parser.parse(input, true);
            assertTrue("Expected true return for " + input, value);
        }
        for (String input : falses) {
            boolean value = (Boolean) parser.parse(input, true);
            assertFalse("Expected false return for " + input, value);

        }
        for (String input : exceptions) {
            assertParseException(parser, input);
        }
    }

    public void testBooleanOutput() throws MetadataParseException
    {
        AttrBoolean parser = new AttrBoolean();
        Boolean output = parser.parse("true",true);
        assertEquals("1", parser.render(output));
        output = parser.parse("false",true);
        assertEquals("0", parser.render(output));
    }
}
