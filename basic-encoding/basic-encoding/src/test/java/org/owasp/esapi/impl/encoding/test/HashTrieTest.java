package org.owasp.esapi.impl.encoding.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.owasp.esapi.impl.encoding.util.HashTrie;


public class HashTrieTest {
	private static final Class<HashTrieTest> CLASS = HashTrieTest.class;

	@Test
	public void testSingleInsertLookup()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();

		trie.put("true", Boolean.TRUE);
		Assert.assertEquals(Boolean.TRUE, trie.get("true"));
		Assert.assertNull(trie.get("not there"));
		Assert.assertNull(trie.get("tru"));
		Assert.assertNull(trie.get("trueX"));
		Assert.assertEquals("true".length(), trie.getMaxKeyLength());
	}

	@Test
	public void testEmpty()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();
		Assert.assertNull(trie.get("true"));
		Assert.assertNull(trie.get("false"));
		Assert.assertNull(trie.get(""));
		Assert.assertTrue(trie.getMaxKeyLength()<0);
	}

	@Test
	public void testTwoInsertLookup()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();

		trie.put("true", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);
		Assert.assertEquals(Boolean.TRUE, trie.get("true"));
		Assert.assertEquals(Boolean.FALSE, trie.get("false"));
		Assert.assertEquals("false".length(),trie.getMaxKeyLength());
	}

	@Test
	public void testMatchingPrefix()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();

		trie.put("pretrue", Boolean.TRUE);
		trie.put("prefalse", Boolean.FALSE);
		Assert.assertEquals(Boolean.TRUE, trie.get("pretrue"));
		Assert.assertEquals(Boolean.FALSE, trie.get("prefalse"));
	}

	@Test
	public void testPrefixIsValidKey()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();

		trie.put("pre", Boolean.TRUE);
		trie.put("prefalse", Boolean.FALSE);
		Assert.assertEquals(Boolean.TRUE, trie.get("pre"));
		Assert.assertEquals(Boolean.FALSE, trie.get("prefalse"));
	}

	@Test
	public void testDuplicateAdd()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();

		Assert.assertNull(trie.put("dup", Boolean.TRUE));
		Assert.assertTrue(trie.put("dup", Boolean.FALSE));
		Assert.assertFalse(trie.get("dup"));
	}

	@Test
	public void testTwoInsertLongestLookup()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();
		Entry<CharSequence,Boolean> entry;

		trie.put("true", Boolean.TRUE);
		trie.put("true idea", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);

		Assert.assertNotNull((entry = trie.getLongestMatch("true")));
		Assert.assertEquals("true", entry.getKey());
		Assert.assertTrue(entry.getValue());

		Assert.assertNotNull((entry = trie.getLongestMatch("false")));
		Assert.assertEquals("false", entry.getKey());
		Assert.assertFalse(entry.getValue());

		Assert.assertNotNull((entry = trie.getLongestMatch("truer")));
		Assert.assertEquals("true", entry.getKey());
		Assert.assertTrue(entry.getValue());

		Assert.assertNotNull((entry = trie.getLongestMatch("true to form")));
		Assert.assertEquals("true", entry.getKey());
		Assert.assertTrue(entry.getValue());

		Assert.assertNotNull((entry = trie.getLongestMatch("false result")));
		Assert.assertEquals("false", entry.getKey());
		Assert.assertFalse(entry.getValue());

		Assert.assertNull(trie.getLongestMatch("not there"));
		Assert.assertNull(trie.getLongestMatch("tru"));
		Assert.assertNull(trie.getLongestMatch("fals"));
	}

	@Test
	public void testContainsKey()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();

		trie.put("true", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);
		Assert.assertTrue(trie.containsKey("true"));
		Assert.assertTrue(trie.containsKey("false"));
		Assert.assertFalse(trie.containsKey("not there"));
	}

	@Test
	public void testContainsValue()
	{
		HashTrie<Integer> trie = new HashTrie<Integer>();

		trie.put("one", 1);
		trie.put("two", 2);
		Assert.assertTrue(trie.containsValue(1));
		Assert.assertTrue(trie.containsValue(2));
		Assert.assertFalse(trie.containsValue(3));
	}

	@Test
	public void testKeySet()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();
		HashSet<CharSequence> expected = new HashSet<CharSequence>(2);

		expected.add("true");
		expected.add("false");
		trie.put("true", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);
		Assert.assertEquals(expected,trie.keySet());
	}

	@Test
	public void testValues()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();
		ArrayList<Boolean> actual;
		ArrayList<Boolean> expected = new ArrayList<Boolean>(2);

		expected.add(Boolean.TRUE);
		expected.add(Boolean.FALSE);
		trie.put("true", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);
		actual = new ArrayList<Boolean>(trie.values());
		Collections.sort(actual);
		Collections.sort(expected);
		Assert.assertEquals(expected,actual);
	}

	@Test
	public void testEntrySet()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();
		HashMap<CharSequence,Boolean> equivMap = new HashMap<CharSequence,Boolean>(2);

		equivMap.put("true",Boolean.TRUE);
		equivMap.put("false",Boolean.FALSE);
		trie.put("true", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);
		Assert.assertEquals(equivMap.entrySet(),trie.entrySet());
	}

	@Test
	public void testEquals()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();
		HashMap<CharSequence,Boolean> equivMap = new HashMap<CharSequence,Boolean>(2);

		equivMap.put("true",Boolean.TRUE);
		equivMap.put("false",Boolean.FALSE);
		trie.put("true", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);
		Assert.assertTrue(trie.equals(equivMap));
	}

	@Test
	public void testHashCode()
	{
		HashTrie<Boolean> trie = new HashTrie<Boolean>();
		HashMap<CharSequence,Boolean> equivMap = new HashMap<CharSequence,Boolean>(2);

		equivMap.put("true",Boolean.TRUE);
		equivMap.put("false",Boolean.FALSE);
		trie.put("true", Boolean.TRUE);
		trie.put("false", Boolean.FALSE);
		Assert.assertEquals(equivMap.hashCode(),trie.hashCode());
	}
}
