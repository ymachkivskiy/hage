/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @author Tomasz Sławek & Marcin Świątek
 */
package org.jage.random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test of GaussianGenerator.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class GaussianGeneratorTest {

	@Mock
	private INormalizedDoubleRandomGenerator rand;

	@InjectMocks
	private GaussianGenerator underTest = new GaussianGenerator();

	@Test
	public void testBounds() {
		assertThat(underTest.getLowerDouble(), is(equalTo(Double.MIN_VALUE)));
		assertThat(underTest.getUpperDouble(), is(equalTo(Double.MAX_VALUE)));
	}

	@Test
	public void testNextDouble() {
		// given
		given(rand.nextDouble()).willReturn(0.5);

		// then
		assertThat(underTest.nextDouble(), is(equalTo(0.0)));
	}
}
