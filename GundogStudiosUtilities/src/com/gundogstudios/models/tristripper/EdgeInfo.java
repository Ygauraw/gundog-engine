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

class EdgeInfo {

	FaceInfo m_face0, m_face1;
	int m_v0, m_v1;
	EdgeInfo m_nextV0, m_nextV1;

	public EdgeInfo(int v0, int v1) {
		m_v0 = v0;
		m_v1 = v1;
		m_face0 = null;
		m_face1 = null;
		m_nextV0 = null;
		m_nextV1 = null;

	}
}
