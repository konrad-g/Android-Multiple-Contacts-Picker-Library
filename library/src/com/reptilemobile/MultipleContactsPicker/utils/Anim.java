package com.reptilemobile.MultipleContactsPicker.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

/**
 * Author Konrad Gadzinowski
 * kgadzinowski@gmail.com
 */
public class Anim {
	public static void setLayoutAnim_slidedown(final ViewGroup panel, Context ctx) {
		
		AnimationSet set = new AnimationSet(true);
 
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// MapContacts.this.mapviewgroup.setVisibility(View.VISIBLE);
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
			}
		});
		set.addAnimation(animation);
 
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);
		panel.setVisibility(0);
		panel.setLayoutAnimation(controller);
		
		//need to refresh layout
		panel.invalidate();
 
	}
 
	public static void setLayoutAnim_slideup(final ViewGroup panel, Context ctx) {
 
		AnimationSet set = new AnimationSet(true);
 
		/*
		 * Animation animation = new AlphaAnimation(1.0f, 0.0f);
		 * animation.setDuration(200); set.addAnimation(animation
		 */
 
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				 //MapContacts.this.mapviewgroup.setVisibility(View.INVISIBLE);
				// TODO Auto-generated method stub
				panel.setVisibility(8);
			}
		});
		set.addAnimation(animation);
 
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);
		panel.setLayoutAnimation(controller);
		
		//need to refresh layout
		panel.invalidate();
	}
	
}
