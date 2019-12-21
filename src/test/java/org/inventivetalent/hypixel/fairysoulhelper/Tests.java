package org.inventivetalent.hypixel.fairysoulhelper;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Tests {

	@Test
	public void serverRegexTest1() {
		Matcher matcher = Util.SERVER_REGEX.matcher("08/15/19 mini5R");
		assertTrue(matcher.matches());

		assertEquals("08/15/19", matcher.group(1));
		assertEquals("mini5R", matcher.group(2));
	}

	@Test
	public void serverRegexTest2() {
		Matcher matcher = Util.SERVER_REGEX.matcher("01/15/19 mini12C");
		assertTrue(matcher.matches());

		assertEquals("01/15/19", matcher.group(1));
		assertEquals("mini12C", matcher.group(2));
	}

	@Test
	public void serverRegexTest3() {
		Matcher matcher = Util.SERVER_REGEX.matcher("11/19/19 mini512X");
		assertTrue(matcher.matches());

		assertEquals("11/19/19", matcher.group(1));
		assertEquals("mini512X", matcher.group(2));
	}


}
