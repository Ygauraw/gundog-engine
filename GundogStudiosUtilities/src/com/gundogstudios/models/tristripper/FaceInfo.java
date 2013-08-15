/**
 * Copyright (C) 2013 Gundog Studios LLC.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gundogstudios.models.tristripper;

class FaceInfo {

	int m_v0, m_v1, m_v2;
	int m_stripId; // real strip Id
	int m_testStripId; // strip Id in an experiment
	int m_experimentId; // in what experiment was it given an experiment Id?

	public FaceInfo(int v0, int v1, int v2) {
		m_v0 = v0;
		m_v1 = v1;
		m_v2 = v2;
		m_stripId = -1;
		m_testStripId = -1;
		m_experimentId = -1;
	}

	public void set(FaceInfo o) {
		m_v0 = o.m_v0;
		m_v1 = o.m_v1;
		m_v2 = o.m_v2;

		m_stripId = o.m_stripId;
		m_testStripId = o.m_testStripId;
		m_experimentId = o.m_experimentId;
	}
}
