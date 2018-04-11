// This file is part of MicropolisJ.
// Copyright (C) 2013 Jason Long
// Portions Copyright (C) 1989-2007 Electronic Arts Inc.
//
// MicropolisJ is free software; you can redistribute it and/or modify
// it under the terms of the GNU GPLv3, with additional terms.
// See the README file, included in this distribution, for details.

package micropolisj.engine;

/**
 * Enumeration of the various kinds of sprites that may appear in the city.
 */
public enum SpriteKind
{
	//is it possible that only 8 are allowed?
	// Replaced airplane with bomber images for now. Will need to disable airplane.
	TRA(1,5),
	COP(2,8),
	AIR(3,11),
	SHI(4,8),
	GOD(5,16),
	TOR(6,3),
	EXP(7,6),
	BUS(8,4),
	BOM(3,8);
	// first number follows "obj" in filename, second number is quantity of frames
	public final int objectId;
	public final int numFrames;

	private SpriteKind(int objectId, int numFrames)
	{
		this.objectId = objectId;
		this.numFrames = numFrames;
	}
}
