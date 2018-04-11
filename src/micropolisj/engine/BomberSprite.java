// This file is part of MicropolisJ.
// Copyright (C) 2013 Jason Long
// Portions Copyright (C) 1989-2007 Electronic Arts Inc.
//
// MicropolisJ is free software; you can redistribute it and/or modify
// it under the terms of the GNU GPLv3, with additional terms.
// See the README file, included in this distribution, for details.

package micropolisj.engine;

import static micropolisj.engine.TileConstants.*;

/**
 * Implements a bomber (a new disaster) created for Programming class. Most of this code comes from MonsterSprite and HelicopterSprite.
 */
public class BomberSprite extends Sprite
{
	int count;
	int soundCount;
	int destX;
	int destY;
	int origX;
	int origY;
	boolean flag; //true if the bomber wants to return home

	//BOMBER FRAMES
	//   1 : northeast
	//   3 : southeast
	//   5 : southwest
	//   7 : northwest
	//   0 : north
	//   2 : east
	//   4 : south
	//   6 : west

	//What does this do?
	static int [] CDx = { 0,  0,  3,  5,  3,  0, -3, -5, -3 };
	static int [] CDy = { 0, -5, -3,  0,  3,  5,  3,  0, -3 };
	static final int SOUND_FREQ = 500;

	//Details for size and display. Bomber is now the biggest sprite in game.
	public BomberSprite(Micropolis engine, int xpos, int ypos)
	{
		super(engine, SpriteKind.BOM);
		this.x = xpos * 16 + 8;
		this.y = ypos * 16 + 8;
		this.width = 64;
		this.height = 64;
		this.offx = -32;
		this.offy = -32;
		
		//Setting origin and destination (pollution for now)
		this.origX = x;
		this.origY = y;
		//this.count usually used for disasters that time out. Check value in Micropolis if changing.
		this.count = 1000;
		CityLocation p = city.getLocationOfMaxPollution();
		this.destX = p.x * 16 + 8;
		this.destY = p.y * 16 + 8;
		this.flag = false;
		this.frame = 5;
	}

	@Override
	public void moveImpl()
	{
		if (this.frame == 0) {
			return;
		}
		// countdown for sound
		if (soundCount > 0) {
			soundCount--;
		}
		// countdown for disaster. set in Micropolis.java. usually for self-terminating disasters
		if (this.count > 0) {
			this.count--;
		}
		if (getDis(x, y, destX, destY) < 60) {

			// reached destination
			if (!flag) {
				// destination was the pollution center;
				// now head for home
				flag = true;
				destX = origX;
				destY = origY;
			}
			else {
				// destination was origX, origY;
				// removes the sprite
				this.frame = 0;
				return;
			}
		}

		//holding pattern during timer
        int z = this.frame;
        if (city.acycle % 3 == 0) {
            if (this.count > 0) { //countdown still happening
                z--;
                if (z < 2) { z=8; }
                this.frame = z;
            }
            else { // go to destination
            int d = getDir(x, y, destX, destY);
            z = turnTo(z, d);
            this.frame = z;
            }
        }
		
		// Make the noise! Also drop the bomb
		if (soundCount == 0 && this.count == 0) {
			city.makeSound(x/16, y/16, Sound.EXPLOSION_HIGH);
			city.makeExplosionAt(x, y);
			soundCount = 20;
		}
		
		// What does this do? something about sprite movement
		x += CDx[z];
		y += CDy[z];
	
		// Kills whatever sprite the bomber collides with.
		for (Sprite s : city.allSprites())
		{
			if (checkSpriteCollision(s) &&
				(s.kind == SpriteKind.AIR ||
				 s.kind == SpriteKind.COP ||
				 s.kind == SpriteKind.SHI ||
				 s.kind == SpriteKind.TRA)
				) {
				s.explodeSprite();
			}
		}
		// This is how the monster makes rubble. Removed in favor of explosions.
		//destroyTile(x / 16, y / 16);
	}
}
