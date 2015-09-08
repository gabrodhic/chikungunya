package com.protogab.chikungunya.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.protogab.footballme.FootballMe;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;


/* This class DOES NOT belong to project it self, its just for development purposes
 * https://github.com/libgdx/libgdx/wiki/Texture-packer
 * */
 
public class PackerTextureDesktop {
	public static void main (String[] arg) {
		
		TexturePacker.process("C:/mydrive/temp/assets_inCC", "C:/mydrive/temp/assets_outCC", "mypack");
	}
}
