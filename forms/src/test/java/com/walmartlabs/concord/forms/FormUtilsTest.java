package com.walmartlabs.concord.forms;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2025 Walmart Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FormUtilsTest {

    private static final FormValidatorLocale LOCALE = new DefaultFormValidatorLocale();

    private static FormField field(String name, String type, FormField.Cardinality c) {
        return FormField.builder()
                .name(name)
                .type(type)
                .cardinality(c)
                .build();
    }

    private static Form form(String name, FormField... fields) {
        var b = Form.builder().name(name).eventName(name);
        for (var f : fields) {
            b.addFields(f);
        }
        return b.build();
    }

    @Test
    public void testGetRunAsUsersNullReturnsEmpty() {
        assertTrue(FormUtils.getRunAsUsers("f", null).isEmpty());
    }

    @Test
    public void testGetRunAsUsersMissingReturnsEmpty() {
        assertTrue(FormUtils.getRunAsUsers("f", Map.of()).isEmpty());
    }

    @Test
    public void testGetRunAsUsersString() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_USERNAME_KEY, "alice");
        assertEquals(java.util.Set.of("alice"), FormUtils.getRunAsUsers("f", params));
    }

    @Test
    public void testGetRunAsUsersCollection() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_USERNAME_KEY, new java.util.ArrayList<>(List.of("alice", "bob")));
        assertEquals(java.util.Set.of("alice", "bob"), FormUtils.getRunAsUsers("f", params));
    }

    @Test
    public void testGetRunAsUsersInvalidElementThrows() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_USERNAME_KEY, new java.util.ArrayList<>(List.of("alice", 7)));
        var ex = assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("f", params));
        assertTrue(ex.getMessage().contains("Expected a string or a list of strings value"));
    }

    @Test
    public void testGetRunAsUsersInvalidTypeThrows() {
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.RUN_AS_USERNAME_KEY, 123);
        var ex = assertThrows(RuntimeException.class, () -> FormUtils.getRunAsUsers("f", params));
        assertTrue(ex.getMessage().contains("Invalid form definition"));
    }

    @Test
    public void testGetRunAsLdapGroupsNullReturnsEmpty() {
        assertTrue(FormUtils.getRunAsLdapGroups("f", null).isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsMissingReturnsEmpty() {
        assertTrue(FormUtils.getRunAsLdapGroups("f", Map.of()).isEmpty());
    }

    @Test
    public void testGetRunAsLdapGroupsMapWithSingleString() {
        Map<String, Serializable> params = Map.of(Constants.RUN_AS_LDAP_KEY,
                (Serializable) new HashMap<>(Map.of(Constants.RUN_AS_GROUP_KEY, "g1")));
        assertEquals(java.util.Set.of("g1"), FormUtils.getRunAsLdapGroups("f", params));
    }

    @Test
    public void testGetRunAsLdapGroupsMapWithCollection() {
        Map<String, Serializable> params = Map.of(Constants.RUN_AS_LDAP_KEY,
                (Serializable) new HashMap<>(Map.of(Constants.RUN_AS_GROUP_KEY, new java.util.ArrayList<>(List.of("g1", "g2")))));
        assertEquals(java.util.Set.of("g1", "g2"), FormUtils.getRunAsLdapGroups("f", params));
    }

    @Test
    public void testGetRunAsLdapGroupsMapWithInvalidElementThrows() {
        Map<String, Serializable> params = Map.of(Constants.RUN_AS_LDAP_KEY,
                (Serializable) new HashMap<>(Map.of(Constants.RUN_AS_GROUP_KEY, new java.util.ArrayList<>(List.of("g1", 5)))));
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("f", params));
    }

    @Test
    public void testGetRunAsLdapGroupsLegacyListSyntax() {
        var item1 = new HashMap<String, Serializable>();
        item1.put(Constants.RUN_AS_GROUP_KEY, "g1");
        var item2 = new HashMap<String, Serializable>();
        item2.put(Constants.RUN_AS_GROUP_KEY, "g2");

        Map<String, Serializable> params = Map.of(Constants.RUN_AS_LDAP_KEY,
                (Serializable) new java.util.ArrayList<>(List.of(item1, item2)));

        assertEquals(java.util.Set.of("g1", "g2"), FormUtils.getRunAsLdapGroups("f", params));
    }

    @Test
    public void testGetRunAsLdapGroupsLegacyListSyntaxBadElementThrows() {
        Map<String, Serializable> params = Map.of(Constants.RUN_AS_LDAP_KEY,
                (Serializable) new java.util.ArrayList<>(List.of(123)));
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("f", params));
    }

    @Test
    public void testGetRunAsLdapGroupsInvalidTypeThrows() {
        Map<String, Serializable> params = Map.of(Constants.RUN_AS_LDAP_KEY, "oops");
        assertThrows(RuntimeException.class, () -> FormUtils.getRunAsLdapGroups("f", params));
    }

    @Test
    public void testConvertNullMapReturnsEmpty() throws Exception {
        var f = form("f", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        assertTrue(FormUtils.convert(LOCALE, f, null).isEmpty());
    }

    @Test
    public void testConvertReturnsFormFilesEntry() throws Exception {
        var f = form("f", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var result = FormUtils.convert(LOCALE, f, Map.of("name", "hi"));

        assertTrue(result.containsKey(Constants.FORM_FILES));
        assertEquals("hi", result.get("name"));
    }

    @Test
    public void testConvertEmptyStringStringFieldBecomesNull() throws Exception {
        var f = form("f", field("name", FormFields.StringField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var result = FormUtils.convert(LOCALE, f, Map.of("name", ""));
        // optional empty string becomes a null value
        assertTrue(result.containsKey("name"));
        assertNull(result.get("name"));
    }

    @Test
    public void testConvertIntegerStringIsParsed() throws Exception {
        var f = form("f", field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var result = FormUtils.convert(LOCALE, f, Map.of("age", "42"));
        assertEquals(42L, result.get("age"));
    }

    @Test
    public void testConvertIntegerInvalidThrows() {
        var f = form("f", field("age", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(LOCALE, f, Map.of("age", "abc")));
    }

    @Test
    public void testConvertDecimalIsParsed() throws Exception {
        var f = form("f", field("ratio", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var result = FormUtils.convert(LOCALE, f, Map.of("ratio", "1.5"));
        assertEquals(1.5d, result.get("ratio"));
    }

    @Test
    public void testConvertDecimalInvalidThrows() {
        var f = form("f", field("ratio", FormFields.DecimalField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        assertThrows(FormUtils.ValidationException.class,
                () -> FormUtils.convert(LOCALE, f, Map.of("ratio", "x")));
    }

    @Test
    public void testConvertBooleanEmptyStringDefaultsToTrue() throws Exception {
        var f = form("f", field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var result = FormUtils.convert(LOCALE, f, Map.of("flag", ""));
        assertEquals(true, result.get("flag"));
    }

    @Test
    public void testConvertBooleanParsesString() throws Exception {
        var f = form("f", field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var result = FormUtils.convert(LOCALE, f, Map.of("flag", "true"));
        assertEquals(true, result.get("flag"));
    }

    @Test
    public void testConvertBooleanNullDefaultsToFalse() throws Exception {
        var f = form("f", field("flag", FormFields.BooleanField.TYPE, FormField.Cardinality.ONE_OR_NONE));
        var result = FormUtils.convert(LOCALE, f, new HashMap<>() {{
            put("flag", null);
        }});
        assertEquals(false, result.get("flag"));
    }

    @Test
    public void testConvertCardinalityAnyWrapsString() throws Exception {
        var f = form("f", field("tags", FormFields.StringField.TYPE, FormField.Cardinality.ANY));
        var result = FormUtils.convert(LOCALE, f, Map.of("tags", "alpha"));
        assertEquals(List.of("alpha"), result.get("tags"));
    }

    @Test
    public void testConvertEmptyListReturnsNull() throws Exception {
        var f = form("f", field("tags", FormFields.StringField.TYPE, FormField.Cardinality.ANY));
        var result = FormUtils.convert(LOCALE, f, Map.of("tags", new java.util.ArrayList<>()));
        // empty list is treated as null for optional fields
        assertTrue(result.containsKey("tags"));
        assertNull(result.get("tags"));
    }

    @Test
    public void testConvertList() throws Exception {
        var f = form("f", field("nums", FormFields.IntegerField.TYPE, FormField.Cardinality.ANY));
        var result = FormUtils.convert(LOCALE, f, Map.of("nums", new java.util.ArrayList<>(List.of("1", "2"))));
        assertEquals(List.of(1L, 2L), result.get("nums"));
    }

    @Test
    public void testConvertReadOnlyUsesDefaultValue() throws Exception {
        var f = FormField.builder()
                .name("flag")
                .type(FormFields.StringField.TYPE)
                .cardinality(FormField.Cardinality.ONE_OR_NONE)
                .defaultValue("locked")
                .options(Map.of(FormFields.CommonFieldOptions.READ_ONLY.name(), (Serializable) Boolean.TRUE))
                .build();
        var form = form("f", f);

        var result = FormUtils.convert(LOCALE, form, Map.of("flag", "ignored"));
        assertEquals("locked", result.get("flag"));
    }

    @Test
    public void testValidationExceptionGetters() {
        var f = field("a", FormFields.IntegerField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        try {
            FormUtils.convert(LOCALE, "form", f, null, "bad");
            fail();
        } catch (FormUtils.ValidationException e) {
            assertEquals(f, e.getField());
            assertEquals("bad", e.getInput());
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testConvertInputStreamForFileFieldWritesFile() throws Exception {
        var f = field("attachment", FormFields.FileField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        var is = new ByteArrayInputStream("hello".getBytes());

        var result = FormUtils.convert(LOCALE, "form", f, null, is);

        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(java.nio.file.Files.exists(java.nio.file.Path.of((String) result)));
    }

    @Test
    public void testConvertEmptyDateStringReturnsNull() throws Exception {
        var f = field("date", FormFields.DateField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        assertNull(FormUtils.convert(LOCALE, "form", f, null, ""));
    }

    @Test
    public void testConvertDateStringIsFormatted() throws Exception {
        var f = field("date", FormFields.DateField.TYPE, FormField.Cardinality.ONE_OR_NONE);
        var result = (String) FormUtils.convert(LOCALE, "form", f, null, "2025-01-02T03:04:05Z");
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{4}"),
                "Unexpected date format: " + result);
    }
}
