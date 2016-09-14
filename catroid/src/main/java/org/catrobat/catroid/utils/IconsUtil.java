/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.TextView;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.BrickLayout;

public final class IconsUtil {

    private static boolean activated = true;
    private static boolean positive = true;
    private static boolean largeSize = true;

    private IconsUtil() {
    }

    public static void addIcon(Context context, TextView textView, String category) {
        if (isActivated()) {
            Drawable drawable = null;

            if(isPositive()) {
                if (category.equals(context.getString(R.string.category_control))) {
                    drawable = context.getResources().getDrawable(R.drawable.control_positiv);
                } else if (category.equals(context.getString(R.string.category_motion))) {
                    drawable = context.getResources().getDrawable(R.drawable.motion_pos);
                } else if (category.equals(context.getString(R.string.category_sound))) {
                    drawable = context.getResources().getDrawable(R.drawable.sound_positiv);
                } else if (category.equals(context.getString(R.string.category_looks))) {
                    drawable = context.getResources().getDrawable(R.drawable.look_positiv);
                } else if (category.equals(context.getString(R.string.category_user_bricks))) {
                    drawable = context.getResources().getDrawable(R.drawable.userbricks_positiv);
                } else if (category.equals(context.getString(R.string.category_data))) {
                    drawable = context.getResources().getDrawable(R.drawable.data_positiv);
                } else if (category.equals(context.getString(R.string.category_lego_nxt))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_arduino))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_drone))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_phiro))) {
                    drawable = context.getResources().getDrawable(R.drawable.phiro_positiv);
                } else if (category.equals(context.getString(R.string.category_raspi))) {
                    drawable = context.getResources().getDrawable(R.drawable.rasberry_positiv);
                } else if (category.equals(context.getString(R.string.category_event))) {
                    drawable = context.getResources().getDrawable(R.drawable.event_positiv);
                } else if (category.equals(context.getString(R.string.category_pen))) {
                    drawable = context.getResources().getDrawable(R.drawable.pen_positiv);
                }
            }
            else {
                if (category.equals(context.getString(R.string.category_control))) {
                    drawable = context.getResources().getDrawable(R.drawable.control_negativ);
                } else if (category.equals(context.getString(R.string.category_motion))) {
                    //drawable = context.getResources().getDrawable(R.drawable.motion_pos);
                } else if (category.equals(context.getString(R.string.category_sound))) {
                    drawable = context.getResources().getDrawable(R.drawable.sound_negativ);
                } else if (category.equals(context.getString(R.string.category_looks))) {
                    drawable = context.getResources().getDrawable(R.drawable.look_negativ);
                } else if (category.equals(context.getString(R.string.category_user_bricks))) {
                    drawable = context.getResources().getDrawable(R.drawable.userbricks_negativ);
                } else if (category.equals(context.getString(R.string.category_data))) {
                    drawable = context.getResources().getDrawable(R.drawable.data_negativ);
                } else if (category.equals(context.getString(R.string.category_lego_nxt))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_arduino))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_drone))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_phiro))) {
                    drawable = context.getResources().getDrawable(R.drawable.phiro_negativ);
                } else if (category.equals(context.getString(R.string.category_raspi))) {
                    drawable = context.getResources().getDrawable(R.drawable.rasberry_negativ);
                } else if (category.equals(context.getString(R.string.category_event))) {
                    drawable = context.getResources().getDrawable(R.drawable.event_negativ);
                } else if (category.equals(context.getString(R.string.category_pen))) {
                    drawable = context.getResources().getDrawable(R.drawable.pen_negativ);
                }
            }

            if (drawable != null) {
                drawable.setBounds(0, 0, 64, 64);
                if (isLargeSize()) {
                    drawable.setBounds(0, 0, 128, 128);
                }
                textView.setCompoundDrawables(null, drawable, null, null);
            }
        }
    }

    public static void addIconCategory(Context context, TextView textView, String category) {
        if (isActivated()) {
            Drawable drawable = null;

            if (isPositive()) {
                if (category.equals(context.getString(R.string.category_control))) {
                    drawable = context.getResources().getDrawable(R.drawable.control_positiv);
                } else if (category.equals(context.getString(R.string.category_motion))) {
                    drawable = context.getResources().getDrawable(R.drawable.motion_pos);
                } else if (category.equals(context.getString(R.string.category_sound))) {
                    drawable = context.getResources().getDrawable(R.drawable.sound_positiv);
                } else if (category.equals(context.getString(R.string.category_looks))) {
                    drawable = context.getResources().getDrawable(R.drawable.look_positiv);
                } else if (category.equals(context.getString(R.string.category_user_bricks))) {
                    drawable = context.getResources().getDrawable(R.drawable.userbricks_positiv);
                } else if (category.equals(context.getString(R.string.category_data))) {
                    drawable = context.getResources().getDrawable(R.drawable.data_positiv);
                } else if (category.equals(context.getString(R.string.category_lego_nxt))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_arduino))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_drone))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_phiro))) {
                    drawable = context.getResources().getDrawable(R.drawable.phiro_positiv);
                } else if (category.equals(context.getString(R.string.category_raspi))) {
                    drawable = context.getResources().getDrawable(R.drawable.rasberry_positiv);
                } else if (category.equals(context.getString(R.string.category_event))) {
                    drawable = context.getResources().getDrawable(R.drawable.event_positiv);
                } else if (category.equals(context.getString(R.string.category_pen))) {
                    drawable = context.getResources().getDrawable(R.drawable.pen_positiv);
                }
            } else {
                if (category.equals(context.getString(R.string.category_control))) {
                    drawable = context.getResources().getDrawable(R.drawable.control_negativ);
                } else if (category.equals(context.getString(R.string.category_motion))) {
                    //drawable = context.getResources().getDrawable(R.drawable.motion_pos);
                } else if (category.equals(context.getString(R.string.category_sound))) {
                    drawable = context.getResources().getDrawable(R.drawable.sound_negativ);
                } else if (category.equals(context.getString(R.string.category_looks))) {
                    drawable = context.getResources().getDrawable(R.drawable.look_negativ);
                } else if (category.equals(context.getString(R.string.category_user_bricks))) {
                    drawable = context.getResources().getDrawable(R.drawable.userbricks_negativ);
                } else if (category.equals(context.getString(R.string.category_data))) {
                    drawable = context.getResources().getDrawable(R.drawable.data_negativ);
                } else if (category.equals(context.getString(R.string.category_lego_nxt))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_arduino))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_drone))) {
                    //drawable = context.getResources().getDrawable(R.drawable.);
                } else if (category.equals(context.getString(R.string.category_phiro))) {
                    drawable = context.getResources().getDrawable(R.drawable.phiro_negativ);
                } else if (category.equals(context.getString(R.string.category_raspi))) {
                    drawable = context.getResources().getDrawable(R.drawable.rasberry_negativ);
                } else if (category.equals(context.getString(R.string.category_event))) {
                    drawable = context.getResources().getDrawable(R.drawable.event_negativ);
                } else if (category.equals(context.getString(R.string.category_pen))) {
                    drawable = context.getResources().getDrawable(R.drawable.pen_negativ);
                }
            }

            if (drawable != null) {
                drawable.setBounds(-20, 0, 96, 96);
                if (isLargeSize()) {
                    drawable.setBounds(-20, 0, 256, 256);
                }
                textView.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    public static void addIcons(ViewGroup viewGroup, String category) {
        if (isActivated()) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof BrickLayout) {
                    BrickLayout l = (BrickLayout) viewGroup.getChildAt(i);
                    if (l.getChildAt(0) instanceof TextView) {
                        TextView textView = (TextView) l.getChildAt(0);
                        addIcon(viewGroup.getContext(), textView, category);
                    }
                }
            }
        }
    }

    public static boolean isActivated() {
        return activated;
    }

    public static void setActivated(boolean activated_) {
        activated = activated_;
    }

    public static boolean isPositive() {
        return positive;
    }

    public static void setPositive(boolean positive_) {
        positive = positive_;
    }

    public static boolean isLargeSize() {
        return largeSize;
    }

    public static void setLargeSize(boolean largeSize_) {
        largeSize = largeSize_;
    }
}


