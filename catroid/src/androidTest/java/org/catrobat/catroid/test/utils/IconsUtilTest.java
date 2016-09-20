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

package org.catrobat.catroid.test.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import org.catrobat.catroid.R;
import org.catrobat.catroid.utils.IconsUtil;

public final class IconsUtilTest extends AndroidTestCase {
    private TextView textView;

    public void setUp() {
        View view = View.inflate(getContext(), R.layout.brick_when, null);
        textView = (TextView) view.findViewById(R.id.brick_when_label);
        IconsUtil.setActivated(true);
        IconsUtil.setContrast(false);
        IconsUtil.setLargeSize(false);
    }

    public void testAddIcons() {
        setUp();
        assertNotNull(textView);

        IconsUtil.addIcon(getContext(), textView, getContext().getString(R.string.category_control));

        Drawable[] drawables = textView.getCompoundDrawables();
        assertNotNull(drawables);

        Drawable correctDrawable = getContext().getResources().getDrawable(R.drawable.control_pos);
        assertNotNull(correctDrawable);

        Bitmap bitmap = ((BitmapDrawable) drawables[0]).getBitmap();
        assertEquals(IconsUtil.smallIconSize, drawables[0].getBounds());
        assertNotNull(bitmap);

        Bitmap correctBitmap = ((BitmapDrawable) correctDrawable).getBitmap();
        assertNotNull(correctBitmap);

        assertEquals(correctBitmap, bitmap);
    }

    public void testAddIconsLarge() {
        setUp();
        assertNotNull(textView);

        IconsUtil.setLargeSize(true);
        IconsUtil.addIcon(getContext(), textView, getContext().getString(R.string.category_control));

        Drawable[] drawables = textView.getCompoundDrawables();
        assertNotNull(drawables);

        Drawable correctDrawable = getContext().getResources().getDrawable(R.drawable.control_pos);
        assertNotNull(correctDrawable);

        Bitmap bitmap = ((BitmapDrawable) drawables[0]).getBitmap();
        assertEquals(IconsUtil.largeIconSize, drawables[0].getBounds());
        assertNotNull(bitmap);

        Bitmap correctBitmap = ((BitmapDrawable) correctDrawable).getBitmap();
        assertNotNull(correctBitmap);

        assertEquals(correctBitmap, bitmap);
    }

    public void testAddIconsContrast() {
        setUp();
        assertNotNull(textView);

        IconsUtil.setContrast(true);
        IconsUtil.addIcon(getContext(), textView, getContext().getString(R.string.category_control));

        Drawable[] drawables = textView.getCompoundDrawables();
        assertNotNull(drawables);

        Drawable correctDrawable = getContext().getResources().getDrawable(R.drawable.control_neg);
        assertNotNull(correctDrawable);

        Bitmap bitmap = ((BitmapDrawable) drawables[0]).getBitmap();
        assertEquals(IconsUtil.smallIconSize, drawables[0].getBounds());
        assertNotNull(bitmap);

        Bitmap correctBitmap = ((BitmapDrawable) correctDrawable).getBitmap();
        assertNotNull(correctBitmap);

        assertEquals(bitmap, correctBitmap);
    }
}
